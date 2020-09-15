package com.bld.leetcode.链表操作.q25_k个一组翻转链表;

//给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
//
// k 是一个正整数，它的值小于或等于链表的长度。
//
// 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
//
//
//
// 示例：
//
// 给你这个链表：1->2->3->4->5
//
// 当 k = 2 时，应当返回: 2->1->4->3->5
//
// 当 k = 3 时，应当返回: 3->2->1->4->5
//
//
//
// 说明：
//
//
// 你的算法只能使用常数的额外空间。
// 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
//
// Related Topics 链表
// 👍 729 👎 0
// https://leetcode-cn.com/problems/reverse-nodes-in-k-group/

//leetcode submit region begin(Prohibit modification and deletion)

//给你一个链表，每 k 个节点一组进行翻转，请你返回翻转后的链表。
//
// k 是一个正整数，它的值小于或等于链表的长度。
//
// 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
//
//
//
// 示例：
//
// 给你这个链表：1->2->3->4->5
//
// 当 k = 2 时，应当返回: 2->1->4->3->5
//
// 当 k = 3 时，应当返回: 3->2->1->4->5
//
//
//
// 说明：
//
//
// 你的算法只能使用常数的额外空间。
// 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
//
// Related Topics 链表
// 👍 729 👎 0
// https://leetcode-cn.com/problems/reverse-nodes-in-k-group/

//leetcode submit region begin(Prohibit modification and deletion)
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) { val = x; }
 * }
 */

class Solution {
    public ListNode reverseKGroup(ListNode head, int k) {
        ListNode dummyk0prev = new ListNode(0);
        dummyk0prev.next = head;
        ListNode k0 = head;
        ListNode k0prev = dummyk0prev;
        ListNode node = head;
        int i = 0;
        while(node != null) {
            if (i == k - 1) {
                i = 0;
                reverseKArray(k0prev, k0, node.next, k);
                k0prev =k0;
                node = k0.next;
                k0 = k0.next;
            } else {
                i++;
                node = node.next;
            }
        }
        return dummyk0prev.next;
    }
    private void reverseKArray(ListNode k0prev, ListNode k0, ListNode kn, int k) {
        ListNode[] array = new ListNode[k];
        ListNode node = k0;
        int i;
        for(i = 0; i < k; i++) {
            array[i] = node;
            node = node.next;
        }
        for(i = k-1; i > 0; i--) {
            array[i].next = array[i-1];
        }
        k0prev.next = array[k-1];
        array[0].next = kn;

    }

    private void reverseK(ListNode k0prev, ListNode k0, ListNode kn, int k ) {

        ListNode prev = k0prev;
        ListNode node = k0;
        ListNode next = null;
        while(node != kn) {
            if (node.next == kn ) { /* k-1 th node */
                next = node.next;
                k0prev.next = node;
                node.next = prev;
                prev = node;
                node = next;
                return;
            } else if (node == k0) { /* 0 th node */
                next = node.next;
                node.next = kn;
                prev = node;
                node = next;

            } else {
                next = node.next;
                node.next = prev;
                prev = node;
                node = next;
            }

        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)

