package com.bld.leetcode.a0链表操作.qq2_两数相加;
//给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
//
// 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
//
// 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
//
// 示例：
//
// 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
//输出：7 -> 0 -> 8
//原因：342 + 465 = 807
//
// Related Topics 链表 数学
// 👍 4894 👎 0
//https://leetcode-cn.com/problems/add-two-numbers


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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode head = null;
        ListNode tail = null;
        ListNode i = l1;
        ListNode j = l2;
        int carry = 0;
        int ival;
        int jval;
        int sum;
        int val;
        while ( i != null || j != null || carry !=0 ) {
            ival = 0;
            jval = 0;
            if (i != null) {
                ival = i.val;
                i = i.next;
            }
            if (j != null) {
                jval =j.val;
                j = j.next;
            }
            sum = (carry + ival + jval);
            carry = sum / 10;
            val = sum % 10;

            ListNode node = new ListNode(val);
            node.next = null;
            if (tail == null) {
                /* 第一个节点 */
                tail = node;
                head = node;
            } else {
                tail.next = node;
                tail = node;
            }

        }
        return head;

    }

    public static void main(String[] args) {
        return;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
