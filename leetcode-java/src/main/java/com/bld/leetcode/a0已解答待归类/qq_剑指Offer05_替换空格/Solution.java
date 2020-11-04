//剑指 Offer 05. 替换空格
//        请实现一个函数，把字符串 s 中的每个空格替换成"%20"。
//
//
//
//        示例 1：
//
//        输入：s = "We are happy."
//        输出："We%20are%20happy."
//
//
//        限制：
//
//        0 <= s 的长度 <= 10000
//
//        通过次数83,025提交次数109,133
// https://leetcode-cn.com/problems/ti-huan-kong-ge-lcof/
package com.bld.leetcode.a0已解答待归类.剑指Offer05_替换空格;

public class Solution {
    public String replaceSpace(String s) {
        return s;
    }
    private String rsReplace (String s) {
        return s.replaceAll(" ", "%20");
    }
    private  String rsStringBuilder( String s) {
        StringBuilder sb = new StringBuilder();
        return null;
    }
}
