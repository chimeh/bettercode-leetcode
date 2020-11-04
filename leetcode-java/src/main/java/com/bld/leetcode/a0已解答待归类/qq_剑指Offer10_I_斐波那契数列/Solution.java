//剑指 Offer 10- I. 斐波那契数列
//        写一个函数，输入 n ，求斐波那契（Fibonacci）数列的第 n 项。斐波那契数列的定义如下：
//
//        F(0) = 0,   F(1) = 1
//        F(N) = F(N - 1) + F(N - 2), 其中 N > 1.
//        斐波那契数列由 0 和 1 开始，之后的斐波那契数就是由之前的两数相加而得出。
//
//        答案需要取模 1e9+7（1000000007），如计算初始结果为：1000000008，请返回 1。
//
//
//
//        示例 1：
//
//        输入：n = 2
//        输出：1
//        示例 2：
//
//        输入：n = 5
//        输出：5
//
//
//        提示：
//
//        0 <= n <= 100
//        注意：本题与主站 509 题相同：https://leetcode-cn.com/problems/fibonacci-number/
//
//        通过次数68,272提交次数203,771
//https://leetcode-cn.com/problems/fei-bo-na-qi-shu-lie-lcof/

package com.bld.leetcode.a0已解答待归类.剑指Offer10_I_斐波那契数列;


public class Solution {
    public int fib(int n) {
        return fibArray2(n);
    }

    private int fibRecurse(int n) {
        final int mod=1000000007;
        int val;
        val = fibHelper(n);
        return val % mod;
    }
    private int mod(int v) {
        final int mod=1000000007;
        return v % mod;
    }
    private int fibArray(int n) {
        if (n < 0) {
            return 0;
        } else if (n <= 1) {
            return n;
        } else {
            int [] a = new int[n+2];
            a[0] = 0;
            a[1] = 1;
            for (int i = 2; i <= n; i++) {
                a[i] = mod(a[i-1]) + mod(a[i-2]);
                if (i == n) {
                    return mod(a[i]);
                }
            }
        }
        return 0;
    }
    private int fibHelper(int n) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        } else if (n > 1) {
            return mod(fibHelper(n - 1) + fibHelper(n - 2));
        } else {
            return 0;
        }
    }
    public int fibArray2 (int n) {
        if (n < 0) {
            return 0;
        } else if (n <= 1) {
            return n;
        } else {
            int fn_2 = 0;
            int fn_1 = 1;
            int fn;
            int i = 2;
            while (i <= n) {
                fn = mod(fn_2 + fn_1);
                if (i == n) {
                    return fn;
                } else {
                    i++;
                    fn_2 = fn_1;
                    fn_1 = fn;
                }
            }
        }
        return 0;
    }
    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.fib(0));
        System.out.println(s.fib(1));
        System.out.println(s.fib(2));
        System.out.println(s.fib(3));
        System.out.println(s.fib(4));
        System.out.println(s.fib(48));
    }
}
