//剑指 Offer 09. 用两个栈实现队列
//        用两个栈实现一个队列。队列的声明如下，请实现它的两个函数 appendTail 和 deleteHead ，分别完成在队列尾部插入整数和在队列头部删除整数的功能。(若队列中没有元素，deleteHead 操作返回 -1 )
//
//
//
//        示例 1：
//
//        输入：
//        ["CQueue","appendTail","deleteHead","deleteHead"]
//        [[],[3],[],[]]
//        输出：[null,null,3,-1]
//        示例 2：
//
//        输入：
//        ["CQueue","deleteHead","appendTail","appendTail","deleteHead","deleteHead"]
//        [[],[],[5],[2],[],[]]
//        输出：[null,-1,null,null,5,2]
//        提示：
//
//        1 <= values <= 10000
//        最多会对 appendTail、deleteHead 进行 10000 次调用
//
//        https://leetcode-cn.com/problems/yong-liang-ge-zhan-shi-xian-dui-lie-lcof/

package com.bld.leetcode.a0已解答待归类.剑指Offer09_用两个栈实现队列;

import java.util.*;
class CQueue {
    private Stack<Integer> sIn;
    private Stack<Integer> sOut;
    public CQueue() {
       this.sIn = new Stack<Integer>();
       this.sOut = new Stack<Integer>();
    }
    private void stackPopInto(Stack<Integer> from, Stack<Integer> to) {
        Integer i;
        while (!from.isEmpty()) {
            i = from.pop();
            to.push(i);
        }
    }
    public void appendTail(int value) {
        if(!sOut.isEmpty()) {
            stackPopInto(sOut, sIn);
        }
        sIn.push(value);
    }

    public int deleteHead() {
        int i;
        if (!sIn.isEmpty()) {
            stackPopInto(sIn, sOut);
        }
        if (sOut.isEmpty()) {
            i = -1;
        } else {
            i = sOut.pop();
        }
        return i;
    }
}

/**
 * Your CQueue object will be instantiated and called as such:
 * CQueue obj = new CQueue();
 * obj.appendTail(value);
 * int param_2 = obj.deleteHead();
 */