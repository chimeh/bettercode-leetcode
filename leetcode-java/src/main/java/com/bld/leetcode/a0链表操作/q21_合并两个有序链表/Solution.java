package com.bld.leetcode.a0链表操作.q21_合并两个有序链表;
//https://leetcode-cn.com/problems/merge-two-sorted-lists/
//将两个升序链表合并为一个新的 升序 链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
//
// 
//
// 示例： 
//
// 输入：1->2->4, 1->3->4
//输出：1->1->2->3->4->4
// 
// Related Topics 链表 
// 👍 1357 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

import java.util.List;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */

/*
* #. 看输出，设置返回值变量；
* #. 过程看，维护的中间变量，三条链，每条链维护一个指针；
* #. 边界处理，处理完可能存在长度一样的情况。
* */
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        ListNode rv = new ListNode(0, null);
        ListNode node = rv;
        ListNode i = l1;
        ListNode j = l2;

        while(i != null && j != null) {
            if (i.val > j.val) {
                node.next = j;
                j = j.next;
            } else {
                node.next = i;
                i = i.next;
            }
            node = node.next;

        }
        if (i != null) {
            node.next = i;
        } else if (j != null)  {
            node.next = j;
        } else {
            node.next = null;
        }
        return rv.next;
    }

    public static void main(String[] args) {
        ListNode a1 = new ListNode(1);
        ListNode a2 = new ListNode(2);
        ListNode a3 = new ListNode(2);
        ListNode a4 = new ListNode(8);
        a1.next = a2;
        a2.next = a3;
        a3.next = a4;
        a4.next = null;
        ListNode b1 = new ListNode(2);
        ListNode b2 = new ListNode(3);
        ListNode b3 = new ListNode(7);
        ListNode b4 = new ListNode(10);
        b1.next = b2;
        b2.next = b3;
        b3.next = b4;
        b4.next = null;
        Solution solve = new Solution();
        ListNode rv = solve.mergeTwoLists(a1, b1);
        ListNode node = rv;
        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }

        return;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
