package com.bld.leetcode.a0链表操作.q142_环形链表II;
//https://leetcode-cn.com/problems/linked-list-cycle-ii/
//给定一个链表，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
//
// 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。注意，po
//s 仅仅是用于标识环的情况，并不会作为参数传递到函数中。 
//
// 说明：不允许修改给定的链表。 
//
// 进阶： 
//
// 
// 你是否可以使用 O(1) 空间解决此题？ 
// 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：head = [3,2,0,-4], pos = 1
//输出：返回索引为 1 的链表节点
//解释：链表中有一个环，其尾部连接到第二个节点。
// 
//
// 示例 2： 
//
// 
//
// 
//输入：head = [1,2], pos = 0
//输出：返回索引为 0 的链表节点
//解释：链表中有一个环，其尾部连接到第一个节点。
// 
//
// 示例 3： 
//
// 
//
// 
//输入：head = [1], pos = -1
//输出：返回 null
//解释：链表中没有环。
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目范围在范围 [0, 104] 内 
// -105 <= Node.val <= 105 
// pos 的值为 -1 或者链表中的一个有效索引 
// 
// Related Topics 链表 双指针 
// 👍 747 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

import java.util.HashSet;

/**
 * Definition for singly-linked list.
 * class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */

//设环入口点距起点移动了 pos步
//        设slow走了n步，fast 走了2n，
//        设相遇于入环点为反向k步，正向j步
//
//        则
//        slow走了 post+k 步
//        fast走了2*(pos+k)，或pos+k+j+k
//
//        post+k+post+k=pos+k+j+k
//        pos=j
//
//        则入口点为，相遇点再往前走
public class Solution {
    public ListNode detectCycle(ListNode head) {
        ListNode meet;
        ListNode slow;
        ListNode fast;

        slow = head;
        fast = head;

        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
            if (fast != null) {
                fast = fast.next;
            } else {
                return null;
            }
            if (fast == slow) {
                meet = slow;
                break;
            }
        }
        slow = head;
        while (fast != null) {
            if (fast == slow) {
                return slow;
            }
            slow = slow.next;
            fast = fast.next;
        }
        return null;
    }
    public ListNode detectCycleBySet(ListNode head) {
        ListNode rv;
        HashSet<ListNode> set = new HashSet<>();
        while (head != null) {
            if (set.contains(head)) {
                rv = head;
                break;
            }
            set.add(head);
            head = head.next;
        }
        rv = head;
        return rv;
    }
    public static void main(String[] args) {
        ListNode a0 = new ListNode(0);
        ListNode a1 = new ListNode(1);
        ListNode a2 = new ListNode(2);
        ListNode a3 = new ListNode(3);
        a0.next = a1;
        a1.next = a2;
        a2.next = a3;
        a3.next = a1;

        ListNode node = a0;
        int i = 0;
        while (i < 4) {
            System.out.println(node.toString() + " " + node.val);
            node = node.next;
            i++;
        }
        Solution solve;
        ListNode rv;
        solve = new Solution();
        rv = solve.detectCycle(a0);
        node = rv;
        System.out.println(node.toString() + " " + node.val);
    }
}
//leetcode submit region end(Prohibit modification and deletion)
