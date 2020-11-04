//217. 存在重复元素
//        给定一个整数数组，判断是否存在重复元素。
//
//        如果任意一值在数组中出现至少两次，函数返回 true 。如果数组中每个元素都不相同，则返回 false 。
//
//
//
//        示例 1:
//
//        输入: [1,2,3,1]
//        输出: true
//        示例 2:
//
//        输入: [1,2,3,4]
//        输出: false
//        示例 3:
//
//        输入: [1,1,1,3,3,4,3,2,4,2]
//        输出: true
// https://leetcode-cn.com/problems/contains-duplicate/

        package com.bld.leetcode.a0已解答待归类.q217_存在重复元素;

import java.util.*;
public class containsDuplicate {
    public boolean containsDuplicate(int[] nums) {

        Map<Integer, Boolean> map = new HashMap<>();
        boolean rv;
        for (int i=0; i < nums.length; i++) {
            int k = nums[i];
            if (null != map.get(k)) {
                return true;
            } else {
                map.put(k, true);
            }
        }
        return false;
    }
    public boolean containsDuplicate2(int[] nums) {
        boolean rv;
        int p=0;
        int postion;
        for (p=0;p < nums.length;p++) {
            for (postion=p+1;postion < nums.length;postion++) {
                rv = nums[p] == nums[postion];
                if (rv == true) {
                    return true;
                }
            }
        }

        return false;
    }
    public boolean containsDuplicate3(int[] nums) {
        Set<Integer> set = new HashSet<Integer>(nums.length);
        for(int x: nums) {
            if (set.contains(x)) {
                return  true;
            } else {
                set.add(x);
            }
        }
        return false;
    }
}

