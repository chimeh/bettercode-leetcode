package com.bld.leetcode.a0链表操作.q160_相交链表;
//https://leetcode-cn.com/problems/intersection-of-two-linked-lists/
//编写一个程序，找到两个单链表相交的起始节点。
//
// 如下面的两个链表： 
//
// 
//
// 在节点 c1 开始相交。 
//
// 
//
// 示例 1： 
//
// 
//
// 输入：intersectVal = 8, listA = [4,1,8,4,5], listB = [5,0,1,8,4,5], skipA = 2, s
//kipB = 3
//输出：Reference of the node with value = 8
//输入解释：相交节点的值为 8 （注意，如果两个链表相交则不能为 0）。从各自的表头开始算起，链表 A 为 [4,1,8,4,5]，链表 B 为 [5,0,1
//,8,4,5]。在 A 中，相交节点前有 2 个节点；在 B 中，相交节点前有 3 个节点。
// 
//
// 
//
// 示例 2： 
//
// 
//
// 输入：intersectVal = 2, listA = [0,9,1,2,4], listB = [3,2,4], skipA = 3, skipB =
// 1
//输出：Reference of the node with value = 2
//输入解释：相交节点的值为 2 （注意，如果两个链表相交则不能为 0）。从各自的表头开始算起，链表 A 为 [0,9,1,2,4]，链表 B 为 [3,2,4
//]。在 A 中，相交节点前有 3 个节点；在 B 中，相交节点前有 1 个节点。
// 
//
// 
//
// 示例 3： 
//
// 
//
// 输入：intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
//输出：null
//输入解释：从各自的表头开始算起，链表 A 为 [2,6,4]，链表 B 为 [1,5]。由于这两个链表不相交，所以 intersectVal 必须为 0，而
// skipA 和 skipB 可以是任意值。
//解释：这两个链表不相交，因此返回 null。
// 
//
// 
//
// 注意： 
//
// 
// 如果两个链表没有交点，返回 null. 
// 在返回结果后，两个链表仍须保持原有的结构。 
// 可假定整个链表结构中没有循环。 
// 程序尽量满足 O(n) 时间复杂度，且仅用 O(1) 内存。 
// 
// Related Topics 链表 
// 👍 865 👎 0


//leetcode submit region begin(Prohibit modification and deletion)

import java.util.HashSet;
import java.util.Set;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        return getIntersectionNodebyLoop(headA, headB);
    }
    public ListNode getIntersectionNodebyLoop(ListNode headA, ListNode headB) {
        ListNode rv = null;
        ListNode p;
        ListNode q;
        for (p = headA; p != null && rv == null;p = p.next) {
            for(q = headB; q != null && rv == null; q = q.next) {
                if (p == q) {
                    rv = p;
                    /* 注意 break 只跳出一层 for 循环 */
                    break;
                }
            }
        }
        return rv;
    }
    public ListNode getIntersectionNodebySet(ListNode headA, ListNode headB) {
        Set<ListNode> set = new HashSet<>();
        ListNode rv = null;
        if (headA == null || headB == null) {
            return null;
        }
        while (headA != null) {
            set.add(headA);
            headA = headA.next;
        }
        while (headB != null) {
            if (set.contains(headB)) {
                rv = headB;
                break;
            }
            headB = headB.next;
        }
        return rv;
    }
    public static void main (String[] args) {
        ListNode a0 = new ListNode(0);
        ListNode a1 = new ListNode(1);
        ListNode a2 = new ListNode(2);
        ListNode a3 = new ListNode(3);

        ListNode b0 = new ListNode(10);
        ListNode b1 = new ListNode(11);
        ListNode b2 = new ListNode(12);

        a0.next = a1;
        a1.next = a2;
        a2.next = a3;
        a3.next = null;

        b0.next = b1;
        b1.next = b2;
        b2.next = a2;

        Solution solve;
        ListNode rv;
        solve = new Solution();
        rv = solve.getIntersectionNode(a0, b0);
        System.out.println(rv.toString() + " " + rv.val);
        rv = solve.getIntersectionNodebySet(a0, b0);
        System.out.println(rv.toString() + " " + rv.val);

    }
}
//leetcode submit region end(Prohibit modification and deletion)
