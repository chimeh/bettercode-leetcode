package com.bld.leetcode.a0链表操作.q_206_反转链表;
//https://leetcode-cn.com/problems/reverse-linked-list/
//反转一个单链表。
//
// 示例: 
//
// 输入: 1->2->3->4->5->NULL
//输出: 5->4->3->2->1->NULL 
//
// 进阶: 
//你可以迭代或递归地反转链表。你能否用两种方法解决这道题？ 
// Related Topics 链表 
// 👍 1318 👎 0


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
    public ListNode reverseList(ListNode head) {
        //return iterReverseList(head);
        return recurseReverseList(head);

    }

    private ListNode iterReverseList(ListNode head) {
        ListNode rv = new ListNode(0);
        ListNode node;
        while (head != null) {
            node = head;
            head = node.next;
            node.next = rv.next;
            rv.next = node;
        }
        return rv.next;
    }
    private ListNode recurseReverseList(ListNode head) {

        if (head.next == null) {
            return head;
        }

        /* 分割 输入 */
        ListNode node = head;
        ListNode sub = head.next;
        ListNode rvsub;
        /* 独立处理 */
        rvsub = recurseReverseList(sub);

        /* 合并输出 */
        sub.next = node;
        node.next = null;
        return rvsub;

    }

    public static void main(String[] args) {
        ListNode a1 = new ListNode(1);
        ListNode a2 = new ListNode(2);
        ListNode a3 = new ListNode(3);
        ListNode a4 = new ListNode(4);
        ListNode a5 = new ListNode(5);
        a1.next = a2;
        a2.next = a3;
        a3.next = a4;
        a4.next = a5;
        a5.next = null;
        Solution solve;
        ListNode node;
        ListNode rv;
        node = a1;
        System.out.println("输入：");
        while (node != null) {
            System.out.format(" %s", node.val);
            node = node.next;
        }
        solve = new Solution();
        rv = solve.reverseList(a1);
        node = rv;
        System.out.println("输出，反转后：");
        while (node != null) {
            System.out.format(" %s", node.val);
            node =node.next;
        }
        return;
    }

}
//leetcode submit region end(Prohibit modification and deletion)
