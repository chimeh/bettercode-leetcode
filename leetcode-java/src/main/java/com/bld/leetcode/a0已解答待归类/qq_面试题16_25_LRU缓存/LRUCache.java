/*设计和构建一个“最近最少使用”缓存，该缓存会删除最近最少使用的项目。缓存应该从键映射到值(允许你插入和检索特定键对应的值)，并在初始化时指定最大容量。当缓存被填满时，它应该删除最近最少使用的项目。

        它应该支持以下操作： 获取数据 get 和 写入数据 put 。

        获取数据 get(key) - 如果密钥 (key) 存在于缓存中，则获取密钥的值（总是正数），否则返回 -1。
        写入数据 put(key, value) - 如果密钥不存在，则写入其数据值。当缓存容量达到上限时，它应该在写入新数据之前删除最近最少使用的数据值，从而为新的数据值留出空间。

        示例:

        LRUCache cache = new LRUCache( 2 *//* 缓存容量 *//* );

        cache.put(1, 1);
        cache.put(2, 2);
        cache.get(1);       // 返回  1
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        cache.get(2);       // 返回 -1 (未找到)
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        cache.get(1);       // 返回 -1 (未找到)
        cache.get(3);       // 返回  3
        cache.get(4);       // 返回  4

        来源：力扣（LeetCode）
        链接：https://leetcode-cn.com/problems/lru-cache-lcci
        著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。*/

/*
 存信息： 存储？ 有哪些数据结构？ 数组、XX链表、哈希表、二叉树

 哪些动作，复杂度分别是什么样的
 method : 删除
 method : 插入
 method : 检索



*/

package com.bld.leetcode.a0已解答待归类.面试题16_25_LRU缓存;
import java.util.*;

public class LRUCache {

    public final static int LRUCACHE_DEFAULT_SIZE = 10;
    public final static int FIRST_INDEX = 0;
    public final static int SECOND_INDEX = 1;
    private int capacity;
    private LinkedList<Integer> kList;
    private Map<Integer, ArrayList<Object>> map;

    public LRUCache() {
        this(LRUCACHE_DEFAULT_SIZE);
    }

    public LRUCache(int capacity) {
        this.capacity = capacity;
        kList = new LinkedList<Integer>();
        map = new HashMap<Integer, ArrayList<Object>>();


    }

    public void set(int k, int v) {
        setv2(k, v);
    }

    public int get(Integer k) {
        return getv2(k);
    }

    public void put(int k, int v) {
        setv2(k, v);
    }

    private Integer getKey(ArrayList<Object> Pair) {
        Integer key = (Integer) Pair.get(SECOND_INDEX);
        return key;
    }
    private Integer getValue(ArrayList<Object> Pair) {
        Integer value = (Integer) Pair.get(FIRST_INDEX);
        return value;
    }
    private Integer moveKeyToHead(LinkedList<Integer> LL, ArrayList<Object> Pair) {
        Integer key = getKey(Pair);
        int index = LL.indexOf(key);
        Integer Hit = LL.remove(index);
        LL.addFirst(Hit);
        return Hit;
    }

    private int getv2(Integer k) {
        ArrayList<Object> pair = map.get(k);
        if (pair == null) {
            return -1;
        } else {
            /*  */
            moveKeyToHead(kList, pair);
            return getValue(pair);
        }
    }

    private void setv2(int k, int v) {
        boolean isFull = map.size() == capacity;
        boolean isNewKey = (!map.containsKey(k));
        if (isFull) {
            /* new Key */
            if (isNewKey) {
                /* invalid oldest key */
                Integer invalidKey = kList.getLast();
                kList.removeLast();
                map.remove(invalidKey);
                /* insert new key, and Value */
                ArrayList<Object> pair = new ArrayList<>();
                pair.add(v);
                kList.addFirst(k);
                pair.add(kList.getFirst());
                map.put(k, pair);
            } else { /* old key */
                ArrayList<Object> pair = map.get(k);
                /* update Value */
                pair.set(FIRST_INDEX, v);
                /* update Key index */
                moveKeyToHead(kList, pair);
            }
        } else {
            if (isNewKey) {
                /* insert new key, and Value */
                ArrayList<Object> pair = new ArrayList<>();
                pair.add(v);
                kList.addFirst(k);
                pair.add(kList.getFirst());
                map.put(k, pair);
            } else {
                ArrayList<Object> pair = map.get(k);
                /* update Value */
                pair.set(FIRST_INDEX, v);
                /* update Key index */
                moveKeyToHead(kList, pair);
            }
        }
    }

    public static void main(String[] args) {
        LRUCache cache;
        cache = new LRUCache(2 /* 缓存容量 */);
        int v;
        cache.put(1, 1);
        cache.put(2, 2);
        v = cache.get(1);       // 返回  1
        System.out.println(v);
        cache.put(3, 3);    // 该操作会使得密钥 2 作废
        v = cache.get(2);       // 返回 -1 (未找到)
        System.out.println(v);
        cache.put(4, 4);    // 该操作会使得密钥 1 作废
        v = cache.get(1);       // 返回 -1 (未找到)
        System.out.println(v);
        v = cache.get(3);       // 返回  3
        System.out.println(v);
        v = cache.get(4);       // 返回  4
        System.out.println(v);

    }
}