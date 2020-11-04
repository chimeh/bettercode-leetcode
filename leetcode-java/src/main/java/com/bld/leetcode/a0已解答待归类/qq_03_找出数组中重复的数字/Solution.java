//找出数组中重复的数字。
//
//
//        在一个长度为 n 的数组 nums 里的所有数字都在 0～n-1 的范围内。数组中某些数字是重复的，但不知道有几个数字重复了，也不知道每个数字重复了几次。请找出数组中任意一个重复的数字。
//
//        示例 1：
//
//        输入：
//        [2, 3, 1, 0, 2, 5, 3]
//        输出：2 或 3
//         
//
//        限制：
//
//        2 <= n <= 100000
//
//        通过次数124,015提交次数183,539
//        在真实的面试中遇到过这道题？
//
//        来源：力扣（LeetCode）
//        链接：https://leetcode-cn.com/problems/shu-zu-zhong-zhong-fu-de-shu-zi-lcof
//        著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
package com.bld.leetcode.a0已解答待归类.q03_找出数组中重复的数字;

import java.util.*;
public class Solution {
    public int findRepeatNumber(int[] nums) {
        return findRepeatNumber_Set(nums);
    }
    private int findRepeatNumber_Set(int [] nums) {
        Set<Integer> set = new HashSet<Integer>();
        int repeat = -1;
        for (int i = 0; i < nums.length; i++) {
            boolean rv = set.add(nums[i]);
            if (rv == false) {
                repeat = nums[i];
                break;
            }
        }
        return repeat;
    }
    private int findRepeatNumber_bucket(int [] nums) {
        int repeat = -1;
        int [] count = new int[nums.length];

        for (int i = 0; i < nums.length; i++) {
            int v = nums[i];
            int index = v % nums.length;
            count[index]++;
            if(count[index] >1) {
                repeat = index;
                break;
            }
        }
        return repeat;
    }
    private int findRepeatNumber_violence(int [] nums) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i+1; j < nums.length ; j++) {
                if (nums[i] == nums[j]) {
                    return nums[i];
                }
            }
        }
        return -1;
    }
    private int findRepeatNumber_violence1(int [] nums) {
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            for (int j = i+1; j < nums.length ; j++) {
                if (nums[i] == nums[j]) {
                    return nums[i];
                }
            }
        }
        return -1;
    }
}
