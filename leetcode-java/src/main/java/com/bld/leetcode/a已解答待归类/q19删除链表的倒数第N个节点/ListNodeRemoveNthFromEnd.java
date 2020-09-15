/*
 19. 删除链表的倒数第N个节点
 给定一个链表，删除链表的倒数第 n 个节点，并且返回链表的头结点。

        示例：

        给定一个链表: 1->2->3->4->5, 和 n = 2.

        当删除了倒数第二个节点后，链表变为 1->2->3->5.
        说明：

        给定的 n 保证是有效的。

        进阶：

        你能尝试使用一趟扫描实现吗？

        来源：力扣（LeetCode）
        链接：https://leetcode-cn.com/problems/remove-nth-node-from-end-of-list
        著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */


package com.bld.leetcode.a已解答待归类.q19删除链表的倒数第N个节点;

//
class Solution {
    private class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }
    public ListNode removeNthFromEnd(ListNode head, int n) {
        int length = 0;
        int walkLen_0th = 0;
        int walkLen_behindNth = 0;
        int walkLen_behindN1th = 0;
        ListNode oldhead = head;
        ListNode node_0th = null;
        ListNode node_behindNth = null;
        ListNode node_behindN1th = null;
        for (node_0th = head; node_0th != null; node_0th = node_0th.next) {
            walkLen_0th++;
            if (walkLen_0th == n) {
                walkLen_behindNth++;
                node_behindNth = head;
            } else if (walkLen_0th > n) {
                walkLen_behindNth++;
                walkLen_behindN1th++;
                node_behindN1th = node_behindNth;
                node_behindNth = node_behindNth.next;
            }
        }
        if (walkLen_behindNth == 0) {
            head = oldhead;
        } else if (walkLen_behindNth == 1) {
            head = oldhead.next;
        } else {
            head = oldhead;
            node_behindN1th.next = node_behindNth.next;
        }

        return head;
    }
}