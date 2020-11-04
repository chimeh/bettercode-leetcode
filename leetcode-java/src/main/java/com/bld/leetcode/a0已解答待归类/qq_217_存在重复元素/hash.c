#include <stdio.h>
#include <stdbool.h>
#define true 1U
#define false 0U
/* Default hash table size.  */
#define HASHTABSIZE     1024

#define XMALLOC(T, SZ) malloc((SZ))
#define XFREE(T, PTR)  free((PTR))
struct hash_backet
{
  /* Linked list.  */
  struct hash_backet *next;

  /* Hash key. */
  unsigned int key;

  /* Data.  */
  void *data;
};

struct hash
{
  /* Hash backet. */
  struct hash_backet **index;

  /* Hash table size. */
  unsigned int size;

  /* Key make function. */
  unsigned int (*hash_key) ();

  /* Data compare function. */
  int (*hash_cmp) ();

  /* Backet alloc. */
  unsigned long count;
};

struct hash *hash_create (unsigned int (*) (), int (*) ());
struct hash *hash_create_size (unsigned int, unsigned int (*) (), int (*) ());

void *hash_get (struct hash *, void *, void * (*) ());
void *hash_alloc_intern (void *);
void *hash_lookup (struct hash *, void *);
void *hash_release (struct hash *, void *);

void hash_iterate (struct hash *,
                   void (*) (struct hash_backet *, void *), void *);

void hash_clean (struct hash *, void (*) (void *));
void hash_free (struct hash *);
/*
 * condition iterator
 * iterate over the hash, do action over element if the element meet the condition
 * @hash, the hash table
 * @cond, int (*cond)(void *element, void* cond_arg), condition checker, if meet return nonzero, else return 0;
 * @cond_arg, the arg to cond
 * @action, void (*action)(void *element, void* action_arg), do action if meet cond and action !=NULL
 * @action_arg, the arg to cond
 */

void hash_iterate_cond(struct hash* hash,
                int (*cond)(void*, void*),
                void *cond_arg,
                void (*action) (void*, void*),
                void *action_arg);

/*
 * condition cleanup, free bucket and data in bucket from hash if bucket's data meet condition.
 * iterate over the hash, free bucket and data if the data meet the condition
 * @hash, the hash table
 * @data_cond, int (*data_cond)(void *element, void* data_cond_arg), condition checker, if meet return nonzero, else return 0.NULL treat no meet
 * @cond_arg, the arg to cond
 * @free_func, assert(NONULL), void (*free_func) (void*), the free method of data in bucket.
 */

void hash_clean_cond(struct hash* hash,
                    int (*data_cond)(void*, void*),
                    void* data_cond_arg,
                    void (*free_func) (void*));

/* Allocate a new hash.  */
struct hash* hash_create_size(unsigned int size, unsigned int (*hash_key) (),
        int (*hash_cmp) ())
{
        struct hash* hash;

        hash = XMALLOC(MTYPE_HASH, sizeof(struct hash));
        if (NULL == hash)
                return NULL;
        hash->index = XMALLOC(MTYPE_HASH_INDEX,
                                        sizeof(struct hash_backet *) * size);
        if (NULL == hash->index)
        {
                XFREE(MTYPE_HASH, hash);
                return NULL;
        }
        memset(hash->index, 0, sizeof(struct hash_backet *) * size);
        hash->size = size;
        hash->hash_key = hash_key;
        hash->hash_cmp = hash_cmp;
        hash->count = 0;

        return hash;
}

/* Allocate a new hash with default hash size.  */
struct hash* hash_create(unsigned int (*hash_key) (), int (*hash_cmp) ())
{
        return hash_create_size(HASHTABSIZE, hash_key, hash_cmp);
}

/* Utility function for hash_get().  When this function is specified
   as alloc_func, return arugment as it is.  This function is used for
   intern already allocated value.  */
void* hash_alloc_intern(void* arg)
{
        return arg;
}

/* Lookup and return hash backet in hash.  If there is no
   corresponding hash backet and alloc_func is specified, create new
   hash backet.  */
void* hash_get(struct hash* hash, void* data, void * (*alloc_func) ())
{
        unsigned int key;
        unsigned int index;
        void* newdata;
        struct hash_backet* backet;

        key = (*hash->hash_key) (data);
        index = key % hash->size;

        for (backet = hash->index[index]; backet != NULL; backet = backet->next)
                if (backet->key == key && (*hash->hash_cmp) (backet->data, data))
                        return backet->data;

        if (alloc_func)
        {
                newdata = (*alloc_func) (data);
                if (newdata == NULL)
                        return NULL;

                backet = XMALLOC(MTYPE_HASH_BACKET, sizeof(struct hash_backet));
                if (NULL == backet)
                        return NULL;
                backet->data = newdata;
                backet->key = key;
                backet->next = hash->index[index];
                hash->index[index] = backet;
                hash->count++;
                return backet->data;
        }
        return NULL;
}

/* Hash lookup.  */
void* hash_lookup(struct hash* hash, void* data)
{
        return hash_get(hash, data, NULL);
}

/* This function release registered value from specified hash.  When
   release is successfully finished, return the data pointer in the
   hash backet.  */
void* hash_release(struct hash* hash, void* data)
{
        void* ret;
        unsigned int key;
        unsigned int index;
        struct hash_backet* backet;
        struct hash_backet* pp;

        key = (*hash->hash_key) (data);
        index = key % hash->size;

        for (backet = pp = hash->index[index]; backet; backet = backet->next)
        {
                if (backet->key == key && (*hash->hash_cmp) (backet->data, data))
                {
                        if (backet == pp)
                                hash->index[index] = backet->next;
                        else
                                pp->next = backet->next;

                        ret = backet->data;
                        XFREE(MTYPE_HASH_BACKET, backet);
                        hash->count--;
                        return ret;
                }
                pp = backet;
        }
        return NULL;
}

/* Iterator function for hash.  */
void hash_iterate(struct hash* hash,
        void (*func) (struct hash_backet *, void*), void* arg)
{
        int i;
        struct hash_backet* hb;

        for (i = 0; i < hash->size; i++)
                for (hb = hash->index[i]; hb; hb = hb->next)
                        (*func) (hb, arg);
}

/* Clean up hash.  */
void hash_clean(struct hash* hash, void (*free_func) (void*))
{
        int i;
        struct hash_backet* hb;
        struct hash_backet* next;

        for (i = 0; i < hash->size; i++)
        {
                for (hb = hash->index[i]; hb; hb = next)
                {
                        next = hb->next;

                        if (free_func)
                                (*free_func) (hb->data);

                        XFREE(MTYPE_HASH_BACKET, hb);
                        hash->count--;
                }
                hash->index[i] = NULL;
        }
}

/* Free hash memory.  You may call hash_clean before call this
   function.  */
void hash_free(struct hash* hash)
{
        XFREE(MTYPE_HASH_INDEX, hash->index);
        XFREE(MTYPE_HASH, hash);
}

/*
 * condition iterator
 * iterate over the hash, do action over element if the element meet the condition
 * @hash, the hash table
 * @cond, int (*cond)(void *element, void* cond_arg), condition checker, if meet return nonzero, else return 0;
 * @cond_arg, the arg to cond
 * @action, void (*action)(void *element, void* action_arg), do action if meet cond and action !=NULL
 * @action_arg, the arg to cond
 */

void hash_iterate_cond(struct hash* hash,
                int (*cond)(void*, void*),
                void *cond_arg,
                void (*action) (void*, void*),
                void *action_arg)
{
    unsigned int i;
    struct hash_backet* hb;
    //struct hash_backet* next;
    for (i = 0; i < hash->size; i++)
    {
        for (hb = hash->index[i]; hb; hb = hb->next)
        {
            if (cond && (*cond)(hb->data, cond_arg)) {
                if (action) {
                    (*action)(hb->data, action_arg);
                }
            }
        }
    }
}



unsigned int int_hash_key (int *data) {
    return *data;
}
int int_hash_cmp(const int *data1, const int *data2) {
    return (int)(*data1) == (int)(*data2)? true :false;
}

void* hash_alloc_func_int (int *arg)
{
  return arg;
}


inline bool containsDuplicate1(int* nums, int numsSize){
    struct hash *hash = hash_create_size(numsSize, int_hash_key, int_hash_cmp);
    int i;
    for(i = 0; i < numsSize; i++) {
        if (hash_lookup(hash, &nums[i])) {
            return  true;
        } else {
            hash_get(hash, &nums[i], hash_alloc_func_int);
        }
    }
    return false;

}

inline bool containsDuplicate2(int* nums, int numsSize){
    struct hash *hash = hash_create_size(numsSize, int_hash_key, int_hash_cmp);
    int i;
    unsigned int cnt1;
    unsigned int cnt2;
    for(i = 0; i < numsSize; i++) {
        cnt1 = hash->count;
        hash_get(hash, &nums[i], hash_alloc_func_int);
        cnt2 = hash->count;
        if (cnt2 == cnt1) {
            return true;
        }
    }
    return false;

}
bool containsDuplicate(int* nums, int numsSize){
    return containsDuplicate2(nums, numsSize);
}