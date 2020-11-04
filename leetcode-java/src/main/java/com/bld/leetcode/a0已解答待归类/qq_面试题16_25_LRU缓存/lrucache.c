#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <assert.h>
#include <string.h>

#define XMALLOC(T, SZ) memset(malloc((SZ)),0,(SZ))
#define XCALLOC(T, SZ) memset(malloc((SZ)),0,(SZ))
#define XFREE(T, PTR)  free((PTR))
//#define true 1U
//#define false 0U

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

  /* apply function if key and cmp both same */
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

        for (backet = hash->index[index]; backet != NULL; backet = backet->next) {
            if (backet->key == key && (*hash->hash_cmp) (backet->data, data)) {
                    return backet->data;
            }
        }

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


/////////////////////////////////////////////////////////////////////////////////
#ifdef __cplusplus
#define static_cast(l, r) static_cast<decltype(l)>((r))
#else
#define static_cast(l, r) (r)
#endif


#ifndef _ZEBRA_LINKLIST_H
#define _ZEBRA_LINKLIST_H

#ifdef __cplusplus
extern "C" {
#endif

/* listnodes must always contain data to be valid. Adding an empty node
 * to a list is invalid
 */
struct listnode {
        struct listnode *next;
        struct listnode *prev;

        /* private member, use getdata() to retrieve, do not access directly */
        void *data;
};

struct list {
        struct listnode *head;
        struct listnode *tail;

        /* invariant: count is the number of listnodes in the list */
        unsigned int count;

        unsigned char flags;
/* Indicates that listnode memory is managed by the application and
 * doesn't need to be freed by this library via listnode_delete etc.
 */
#define LINKLIST_FLAG_NODE_MEM_BY_APP (1 << 0)

        /*
         * Returns -1 if val1 < val2, 0 if equal?, 1 if val1 > val2.
         * Used as definition of sorted for listnode_add_sort
         */
        int (*cmp)(void *val1, void *val2);

        /* callback to free user-owned data when listnode is deleted. supplying
         * this callback is very much encouraged!
         */
        void (*del)(void *val);
};

#define listnextnode(X) ((X) ? ((X)->next) : NULL)
#define listnextnode_unchecked(X) ((X)->next)
#define listhead(X) ((X) ? ((X)->head) : NULL)
#define listhead_unchecked(X) ((X)->head)
#define listtail(X) ((X) ? ((X)->tail) : NULL)
#define listtail_unchecked(X) ((X)->tail)
#define listcount(X) ((X)->count)
#define list_isempty(X) ((X)->head == NULL && (X)->tail == NULL)
/* return X->data only if X and X->data are not NULL */
#define listgetdata(X) (assert(X), assert((X)->data != NULL), (X)->data)
/* App is going to manage listnode memory */
#define listset_app_node_mem(X) ((X)->flags |= LINKLIST_FLAG_NODE_MEM_BY_APP)
#define listnode_init(X, val) ((X)->data = (val))

/*
 * Create a new linked list.
 *
 * Returns:
 *    the created linked list
 */
extern struct list *list_new(void);

/*
 * Add a new element to the tail of a list.
 *
 * Runtime is O(1).
 *
 * list
 *    list to operate on
 *
 * data
 *    element to add
 */
extern struct listnode *listnode_add(struct list *list, void *data);

/*
 * Add a new element to the beginning of a list.
 *
 * Runtime is O(1).
 *
 * list
 *    list to operate on
 *
 * data
 *    If MEM_BY_APP is set this is listnode. Otherwise it is element to add.
 */
extern void listnode_add_head(struct list *list, void *data);

/*
 * Insert a new element into a list with insertion sort.
 *
 * If list->cmp is set, this function is used to determine the position to
 * insert the new element. If it is not set, this function is equivalent to
 * listnode_add.
 *
 * Runtime is O(N).
 *
 * list
 *    list to operate on
 *
 * val
 *    If MEM_BY_APP is set this is listnode. Otherwise it is element to add.
 */
extern void listnode_add_sort(struct list *list, void *val);

/*
 * Insert a new element into a list after another element.
 *
 * Runtime is O(1).
 *
 * list
 *    list to operate on
 *
 * pp
 *    listnode to insert after
 *
 * data
 *    If MEM_BY_APP is set this is listnode. Otherwise it is element to add.
 *
 * Returns:
 *    pointer to newly created listnode that contains the inserted data
 */
extern struct listnode *listnode_add_after(struct list *list,
                                           struct listnode *pp, void *data);

/*
 * Insert a new element into a list before another element.
 *
 * Runtime is O(1).
 *
 * list
 *    list to operate on
 *
 * pp
 *    listnode to insert before
 *
 * data
 *    If MEM_BY_APP is set this is listnode. Otherwise it is element to add.
 *
 * Returns:
 *    pointer to newly created listnode that contains the inserted data
 */
extern struct listnode *listnode_add_before(struct list *list,
                                            struct listnode *pp, void *data);

/*
 * Move a node to the tail of a list.
 *
 * Runtime is O(1).
 *
 * list
 *    list to operate on
 *
 * node
 *    node to move to tail
 */
extern void listnode_move_to_tail(struct list *list, struct listnode *node);

/*
 * Delete an element from a list.
 *
 * Runtime is O(N).
 *
 * list
 *    list to operate on
 *
 * data
 *    data to insert into list
 */
extern void listnode_delete(struct list *list, const void *data);

/*
 * Find the listnode corresponding to an element in a list.
 *
 * list
 *    list to operate on
 *
 * data
 *    data to search for
 *
 * Returns:
 *    pointer to listnode storing the given data if found, NULL otherwise
 */
extern struct listnode *listnode_lookup(struct list *list, const void *data);

/*
 * Retrieve the element at the head of a list.
 *
 * list
 *    list to operate on
 *
 * Returns:
 *    data at head of list, or NULL if list is empty
 */
extern void *listnode_head(struct list *list);

/*
 * Sort a list in place.
 *
 * The sorting algorithm used is quicksort. Runtimes are equivalent to those of
 * quicksort plus N. The sort is not stable.
 *
 * For portability reasons, the comparison function takes a pointer to pointer
 * to void. This pointer should be dereferenced to get the actual data pointer.
 * It is always safe to do this.
 *
 * list
 *    list to sort
 *
 * cmp
 *    comparison function for quicksort. Should return less than, equal to or
 *    greater than zero if the first argument is less than, equal to or greater
 *    than the second argument.
 */
extern void list_sort(struct list *list,
                      int (*cmp)(const void **, const void **));

/*
 * Convert a list to an array of void pointers.
 *
 * Starts from the list head and ends either on the last node of the list or
 * when the provided array cannot store any more elements.
 *
 * list
 *    list to convert
 *
 * arr
 *    Pre-allocated array of void *
 *
 * arrlen
 *    Number of elements in arr
 *
 * Returns:
 *    arr
 */
void **list_to_array(struct list *list, void **arr, size_t arrlen);

/*
 * Delete a list and NULL its pointer.
 *
 * If non-null, list->del is called with each data element.
 *
 * plist
 *    pointer to list pointer; this will be set to NULL after the list has been
 *    deleted
 */
extern void list_delete(struct list **plist);

/*
 * Delete all nodes from a list without deleting the list itself.
 *
 * If non-null, list->del is called with each data element.
 *
 * list
 *    list to operate on
 */
extern void list_delete_all_node(struct list *list);

/*
 * Delete a node from a list.
 *
 * list->del is not called with the data associated with the node.
 *
 * Runtime is O(1).
 *
 * list
 *    list to operate on
 *
 * node
 *    the node to delete
 */
extern void list_delete_node(struct list *list, struct listnode *node);

/*
 * Delete all nodes which satisfy a condition from a list.
 * Deletes the node if cond function returns true for the node.
 * If function ptr passed is NULL, it deletes all nodes
 *
 * list
 *    list to operate on
 * cond
 *    function pointer which takes node data as input and return true or false
 */

extern void list_filter_out_nodes(struct list *list, bool (*cond)(void *data));

/*
 * Insert a new element into a list with insertion sort if there is no
 * duplicate element present in the list. This assumes the input list is
 * sorted. If unsorted, it will check for duplicate until it finds out
 * the position to do insertion sort with the unsorted list.
 *
 * If list->cmp is set, this function is used to determine the position to
 * insert the new element. If it is not set, this function is equivalent to
 * listnode_add. duplicate element is determined by cmp function returning 0.
 *
 * Runtime is O(N).
 *
 * list
 *    list to operate on
 *
 * val
 *    If MEM_BY_APP is set this is listnode. Otherwise it is element to add.
 */

extern bool listnode_add_sort_nodup(struct list *list, void *val);

/*
 * Duplicate the specified list, creating a shallow copy of each of its
 * elements.
 *
 * list
 *    list to duplicate
 *
 * Returns:
 *    the duplicated list
 */
extern struct list *list_dup(struct list *list);

/* List iteration macro.
 * Usage: for (ALL_LIST_ELEMENTS (...) { ... }
 * It is safe to delete the listnode using this macro.
 */
#define ALL_LIST_ELEMENTS(list, node, nextnode, data)                          \
        (node) = listhead(list), ((data) = NULL);                              \
        (node) != NULL                                                         \
                && ((data) = static_cast(data, listgetdata(node)),             \
                    (nextnode) = node->next, 1);                               \
        (node) = (nextnode), ((data) = NULL)

/* read-only list iteration macro.
 * Usage: as per ALL_LIST_ELEMENTS, but not safe to delete the listnode Only
 * use this macro when it is *immediately obvious* the listnode is not
 * deleted in the body of the loop. Does not have forward-reference overhead
 * of previous macro.
 */
#define ALL_LIST_ELEMENTS_RO(list, node, data)                                 \
        (node) = listhead(list), ((data) = NULL);                              \
        (node) != NULL && ((data) = static_cast(data, listgetdata(node)), 1);  \
        (node) = listnextnode(node), ((data) = NULL)

/* these *do not* cleanup list nodes and referenced data, as the functions
 * do - these macros simply {de,at}tach a listnode from/to a list.
 */

/* List node attach macro.  */
#define LISTNODE_ATTACH(L, N)                                                  \
        do {                                                                   \
                (N)->prev = (L)->tail;                                         \
                (N)->next = NULL;                                              \
                if ((L)->head == NULL)                                         \
                        (L)->head = (N);                                       \
                else                                                           \
                        (L)->tail->next = (N);                                 \
                (L)->tail = (N);                                               \
                (L)->count++;                                                  \
        } while (0)

/* List node detach macro.  */
#define LISTNODE_DETACH(L, N)                                                  \
        do {                                                                   \
                if ((N)->prev)                                                 \
                        (N)->prev->next = (N)->next;                           \
                else                                                           \
                        (L)->head = (N)->next;                                 \
                if ((N)->next)                                                 \
                        (N)->next->prev = (N)->prev;                           \
                else                                                           \
                        (L)->tail = (N)->prev;                                 \
                (L)->count--;                                                  \
        } while (0)

extern struct listnode *listnode_lookup_nocheck(struct list *list, void *data);

/*
 * Add a node to *list, if non-NULL. Otherwise, allocate a new list, mail
 * it back in *list, and add a new node.
 *
 * Return: the new node.
 */
extern struct listnode *listnode_add_force(struct list **list, void *val);

#ifdef __cplusplus
}
#endif

#endif /* _ZEBRA_LINKLIST_H */

/* Generic linked list routine.
 * Copyright (C) 1997, 2000 Kunihiro Ishiguro
 *
 * This file is part of GNU Zebra.
 *
 * GNU Zebra is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2, or (at your option) any
 * later version.
 *
 * GNU Zebra is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; see the file COPYING; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA
 */



//DEFINE_MTYPE_STATIC(LIB, LINK_LIST, "Link List")
//DEFINE_MTYPE_STATIC(LIB, LINK_NODE, "Link Node")

struct list *list_new(void)
{
        return XCALLOC(MTYPE_LINK_LIST, sizeof(struct list));
}

/* Free list. */
static void list_free_internal(struct list *l)
{
        XFREE(MTYPE_LINK_LIST, l);
}


/* Allocate new listnode.  Internal use only. */
static struct listnode *listnode_new(struct list *list, void *val)
{
        struct listnode *node;

        /* if listnode memory is managed by the app then the val
         * passed in is the listnode
         */
        if (list->flags & LINKLIST_FLAG_NODE_MEM_BY_APP) {
                node = val;
                node->prev = node->next = NULL;
        } else {
                node = XCALLOC(MTYPE_LINK_NODE, sizeof(struct listnode));
                node->data = val;
        }
        return node;
}

/* Free listnode. */
static void listnode_free(struct list *list, struct listnode *node)
{
        if (!(list->flags & LINKLIST_FLAG_NODE_MEM_BY_APP))
                XFREE(MTYPE_LINK_NODE, node);
}

struct listnode *listnode_add(struct list *list, void *val)
{
        struct listnode *node;

        assert(val != NULL);

        node = listnode_new(list, val);

        node->prev = list->tail;
        if (list->head == NULL)
                list->head = node;
        else
                list->tail->next = node;
        list->tail = node;

        list->count++;

        return node;
}

void listnode_add_head(struct list *list, void *val)
{
        struct listnode *node;

        assert(val != NULL);

        node = listnode_new(list, val);

        node->next = list->head;

        if (list->head == NULL)
                list->head = node;
        else
                list->head->prev = node;
        list->head = node;

        list->count++;
}

bool listnode_add_sort_nodup(struct list *list, void *val)
{
        struct listnode *n;
        struct listnode *new;
        int ret;
        void *data;

        assert(val != NULL);

        if (list->flags & LINKLIST_FLAG_NODE_MEM_BY_APP) {
                n = val;
                data = n->data;
        } else {
                data = val;
        }

        if (list->cmp) {
                for (n = list->head; n; n = n->next) {
                        ret = (*list->cmp)(data, n->data);
                        if (ret < 0) {
                                new = listnode_new(list, val);

                                new->next = n;
                                new->prev = n->prev;

                                if (n->prev)
                                        n->prev->next = new;
                                else
                                        list->head = new;
                                n->prev = new;
                                list->count++;
                                return true;
                        }
                        /* found duplicate return false */
                        if (ret == 0)
                                return false;
                }
        }

        new = listnode_new(list, val);

        LISTNODE_ATTACH(list, new);

        return true;
}

struct list *list_dup(struct list *list)
{
        struct list *dup;
        struct listnode *node;
        void *data;

        assert(list);

        dup = list_new();
        dup->cmp = list->cmp;
        dup->del = list->del;
        for (ALL_LIST_ELEMENTS_RO(list, node, data))
                listnode_add(dup, data);

        return dup;
}

void listnode_add_sort(struct list *list, void *val)
{
        struct listnode *n;
        struct listnode *new;

        assert(val != NULL);

        new = listnode_new(list, val);
        val = new->data;

        if (list->cmp) {
                for (n = list->head; n; n = n->next) {
                        if ((*list->cmp)(val, n->data) < 0) {
                                new->next = n;
                                new->prev = n->prev;

                                if (n->prev)
                                        n->prev->next = new;
                                else
                                        list->head = new;
                                n->prev = new;
                                list->count++;
                                return;
                        }
                }
        }

        new->prev = list->tail;

        if (list->tail)
                list->tail->next = new;
        else
                list->head = new;

        list->tail = new;
        list->count++;
}

struct listnode *listnode_add_after(struct list *list, struct listnode *pp,
                                    void *val)
{
        struct listnode *nn;

        assert(val != NULL);

        nn = listnode_new(list, val);

        if (pp == NULL) {
                if (list->head)
                        list->head->prev = nn;
                else
                        list->tail = nn;

                nn->next = list->head;
                nn->prev = pp;

                list->head = nn;
        } else {
                if (pp->next)
                        pp->next->prev = nn;
                else
                        list->tail = nn;

                nn->next = pp->next;
                nn->prev = pp;

                pp->next = nn;
        }
        list->count++;
        return nn;
}

struct listnode *listnode_add_before(struct list *list, struct listnode *pp,
                                     void *val)
{
        struct listnode *nn;

        assert(val != NULL);

        nn = listnode_new(list, val);

        if (pp == NULL) {
                if (list->tail)
                        list->tail->next = nn;
                else
                        list->head = nn;

                nn->prev = list->tail;
                nn->next = pp;

                list->tail = nn;
        } else {
                if (pp->prev)
                        pp->prev->next = nn;
                else
                        list->head = nn;

                nn->prev = pp->prev;
                nn->next = pp;

                pp->prev = nn;
        }
        list->count++;
        return nn;
}

void listnode_move_to_tail(struct list *l, struct listnode *n)
{
        LISTNODE_DETACH(l, n);
        LISTNODE_ATTACH(l, n);
}

void listnode_delete(struct list *list, const void *val)
{
        struct listnode *node = listnode_lookup(list, val);

        if (node)
                list_delete_node(list, node);
}

void *listnode_head(struct list *list)
{
        struct listnode *node;

        assert(list);
        node = list->head;

        if (node)
                return node->data;
        return NULL;
}

void list_delete_all_node(struct list *list)
{
        struct listnode *node;
        struct listnode *next;

        assert(list);
        for (node = list->head; node; node = next) {
                next = node->next;
                if (*list->del)
                        (*list->del)(node->data);
                listnode_free(list, node);
        }
        list->head = list->tail = NULL;
        list->count = 0;
}

void list_filter_out_nodes(struct list *list, bool (*cond)(void *data))
{
        struct listnode *node;
        struct listnode *next;
        void *data;

        assert(list);

        for (ALL_LIST_ELEMENTS(list, node, next, data)) {
                if ((cond && cond(data)) || (!cond)) {
                        if (*list->del)
                                (*list->del)(data);
                        list_delete_node(list, node);
                }
        }
}

void list_delete(struct list **list)
{
        assert(*list);
        list_delete_all_node(*list);
        list_free_internal(*list);
        *list = NULL;
}

struct listnode *listnode_lookup(struct list *list, const void *data)
{
        struct listnode *node;

        assert(list);
        for (node = listhead(list); node; node = listnextnode(node))
                if (data == listgetdata(node))
                        return node;
        return NULL;
}

struct listnode *listnode_lookup_nocheck(struct list *list, void *data)
{
        if (!list)
                return NULL;
        return listnode_lookup(list, data);
}

void list_delete_node(struct list *list, struct listnode *node)
{
        if (node->prev)
                node->prev->next = node->next;
        else
                list->head = node->next;
        if (node->next)
                node->next->prev = node->prev;
        else
                list->tail = node->prev;
        list->count--;
        listnode_free(list, node);
}

void list_sort(struct list *list, int (*cmp)(const void **, const void **))
{
        struct listnode *ln, *nn;
        int i = -1;
        void *data;
        size_t n = list->count;
        void **items = XCALLOC(MTYPE_TMP, (sizeof(void *)) * n);
        int (*realcmp)(const void *, const void *) =
                (int (*)(const void *, const void *))cmp;

        for (ALL_LIST_ELEMENTS(list, ln, nn, data)) {
                items[++i] = data;
                list_delete_node(list, ln);
        }

        qsort(items, n, sizeof(void *), realcmp);
        unsigned int j;
        for (j = 0; j < n; ++j)
                listnode_add(list, items[j]);

        XFREE(MTYPE_TMP, items);
}

struct listnode *listnode_add_force(struct list **list, void *val)
{
        if (*list == NULL)
                *list = list_new();
        return listnode_add(*list, val);
}

void **list_to_array(struct list *list, void **arr, size_t arrlen)
{
        struct listnode *ln;
        void *vp;
        size_t idx = 0;

        for (ALL_LIST_ELEMENTS_RO(list, ln, vp)) {
                arr[idx++] = vp;
                if (idx == arrlen)
                        break;
        }

        return arr;
}


/////////////////////////////////////////////////////////////////////////////////
typedef struct {
    struct hash *hash;
    struct list *list;
    unsigned int capacity;
} LRUCache;

typedef struct {
    struct listnode node;
    int key;
    int value;
    int visit;
} KVPair;

unsigned int int_hash_key (KVPair *pair) {
    return pair->key;
}
int int_hash_cmp(const KVPair *data1, const KVPair *data2) {
    return (data1->key) == (data2->key);
}

void* hash_alloc_func_int (KVPair *arg)
{
  return arg;
}

/* void listnode_move_to_head(struct list *list, struct listnode *node) {
    node->prev->next = node->next;
    node->next->prev = node->prev;
    if (node->next == NULL) {
        list->tail = node->prev;
    }
    list->head->prev = node;
    node->next = list->head;
    list->head = node;
} */

LRUCache* lRUCacheCreate(int capacity) {
    LRUCache *obj = malloc(sizeof(LRUCache));
    obj->hash = hash_create_size(capacity, int_hash_key, int_hash_cmp);
    obj->list = list_new();
    listset_app_node_mem(obj->list);
    obj->capacity = capacity;
    return obj;
}

int lRUCacheGet(LRUCache* obj, int key) {
    KVPair keystore;
    keystore.key = key;
    KVPair *pair = hash_lookup(obj->hash, &keystore);
    struct listnode *node = (struct listnode *)pair;
    if (pair) {
        /*  */
        listnode_move_to_tail(obj->list, node);
        assert(key == pair->key);
        pair->visit++;
        return pair->value;
    } else {
    return -1;
    }
}



void lRUCachePut(LRUCache* obj, int key, int value) {
    int isFull = (obj->hash->count == obj->capacity);
    KVPair keystore;
    keystore.key = key;
    KVPair *pair = hash_lookup(obj->hash, &keystore);
    int isNewKey = (NULL == pair);
    struct listnode *node;
    if (isFull) {
        if (isNewKey) {
            /* reuse */
            node = listhead(obj->list);
            pair = (KVPair*) node;
            listnode_move_to_tail(obj->list, node);
            hash_release(obj->hash, pair);
            /* , reuse node */
            pair->key = key;
            pair->value = value;
            pair->visit=1;
            hash_get(obj->hash, pair, hash_alloc_func_int);
        } else {
            node = (struct listnode *)pair;
            listnode_move_to_tail(obj->list, node);
            pair->key = key;
            pair->value = value;
            pair->visit++;
        }
    } else {
         if (isNewKey) {
            pair = XCALLOC(MTYPE_LRUCACHE, sizeof(KVPair));
            node = (struct listnode *)pair;
            listnode_add(obj->list, node);
            pair->key = key;
            pair->value = value;
            pair->visit=1;
            hash_get(obj->hash, pair, hash_alloc_func_int);
         } else {
            node = (struct listnode *)pair;
            listnode_move_to_tail(obj->list, node);
            pair->key = key;
            pair->value = value;
            pair->visit++;

         }
    }
}


void lRUCacheFree(LRUCache* obj) {
    hash_clean(obj->hash, free);
    //list_delete(&obj->list);
    hash_free(obj->hash);
}

void dump_pair(KVPair *pair, char *from) {
    if (!from) {
       from = "unkonwn";
    }
    printf("%s: K: %d, V: %d, visit: %d, prev %#p, node %#p, next %#p\n", from, pair->key, pair->value, pair->visit, pair->node.prev, pair, pair->node.next);
}
void list_dump(struct list *list) {
    struct listnode *node;
    struct listnode *nextnode;
    KVPair *pair;
    for (node = listhead(list), pair = NULL; node != NULL && (pair = node, nextnode = node->next, 1); node = nextnode, pair=NULL) {
        dump_pair(pair, "list");
    }
    printf("list count: %d\n", list->count);
}

int cond_true(void *arg1, void *arg2) {
    return true;
}

void hash_element_dump(KVPair *pair, void *action_arg) {
    dump_pair(pair, "hash");
}
void hash_dump(struct hash *hash) {
    hash_iterate_cond(hash,
                cond_true,
                NULL,
                hash_element_dump,
                NULL);
    printf("hash count: %d\n", hash->count);

}

/**
 * Your LRUCache struct will be instantiated and called as such:
 * LRUCache* obj = lRUCacheCreate(capacity);
 * int param_1 = lRUCacheGet(obj, key);

 * lRUCachePut(obj, key, value);

 * lRUCacheFree(obj);
*/

void mainx(int argc, char* argv[])
{
    LRUCache *obj = lRUCacheCreate( 2 /* 缓存容量 */ );

    int rv;
    lRUCachePut(obj, 1, 1);
    lRUCachePut(obj, 2, 2);
    rv=lRUCacheGet(obj, 1); printf("%d\n", rv);       // 返回  1
    lRUCachePut(obj, 3, 3);    // 该操作会使得密钥 2 作废
    rv=lRUCacheGet(obj, 2); printf("%d\n", rv);       // 返回 -1 (未找到)
    list_dump(obj->list);
    hash_dump(obj->hash);
    lRUCachePut(obj, 4, 4);    // 该操作会使得密钥 1 作废
    list_dump(obj->list);
    hash_dump(obj->hash);
    rv=lRUCacheGet(obj, 1); printf("%d\n", rv);       // 返回 -1 (未找到)
    rv=lRUCacheGet(obj, 3); printf("%d\n", rv);       // 返回  3
    rv=lRUCacheGet(obj, 4); printf("%d\n", rv);       // 返回  4
    list_dump(obj->list);
    hash_dump(obj->hash);

    lRUCacheFree(obj);
}
