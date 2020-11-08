package com.bld.leetcode.a0链表操作.q86_分隔链表;
//https://leetcode-cn.com/problems/partition-list/
//给定一个链表和一个特定值 x，对链表进行分隔，使得所有小于 x 的节点都在大于或等于 x 的节点之前。
//
// 你应当保留两个分区中每个节点的初始相对位置。 
//
// 
//
// 示例: 
//
// 输入: head = 1->4->3->2->5->2, x = 3
//输出: 1->2->2->4->3->5
// 
// Related Topics 链表 双指针 
// 👍 278 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

import java.util.List;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */
/*
* #.
* #. 测试用例用最简单笨的办法，构造测试数据
* */
class Solution {
    public ListNode partition(ListNode head, int x) {
        ListNode left = new ListNode(0);
        ListNode lnode = left;
        ListNode right= new ListNode(0);
        ListNode rnode = right;

        while (head != null) {
            if (head.val < x) {
                lnode.next = head;
                lnode = lnode.next;
                head = head.next;
                lnode.next = null;
            } else {
                rnode.next = head;
                rnode = rnode.next;
                head = head.next;
                rnode.next = null;
            }
        }
        lnode.next = right.next;
        return left.next;
    }

    public static void main(String[] args) {
        ListNode a1 = new ListNode(1);
        ListNode a2 = new ListNode(4);
        ListNode a3 = new ListNode(3);
        ListNode a4 = new ListNode(2);
        ListNode a5 = new ListNode(5);
        ListNode a6 = new ListNode(2);
        a1.next = a2;
        a2.next = a3;
        a3.next = a4;
        a4.next = a5;
        a5.next = a6;
        a6.next = null;
        Solution solve;
        ListNode rv;
        solve = new Solution();
        rv = solve.partition(a1, 3);
        ListNode n = rv;
        while (n != null) {
            System.out.println(n.val);
            n = n.next;
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
