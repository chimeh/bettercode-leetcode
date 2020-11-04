//剑指 Offer 06. 从尾到头打印链表
//        输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
//
//
//
//        示例 1：
//
//        输入：head = [1,3,2]
//        输出：[2,3,1]
//
//
//        限制：
//
//        0 <= 链表长度 <= 10000
// https://leetcode-cn.com/problems/cong-wei-dao-tou-da-yin-lian-biao-lcof/solution/mian-shi-ti-06-cong-wei-dao-tou-da-yin-lian-biao-b/

package com.bld.leetcode.a0已解答待归类.剑指Offer06_从尾到头打印链表;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
class Solution {
      public class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
  }
    public int[] reversePrint(ListNode head) {
        int count = 0;
        ListNode n = head;
        while(n != null) {
            count++;
            n = n.next;
        }
        int [] array = new int [count];
        n = head;
        for (int i = 0; i < count; i++) {
            array[count-1-i] = n.val;
            n = n.next;
        }
        return array;
    }
}
