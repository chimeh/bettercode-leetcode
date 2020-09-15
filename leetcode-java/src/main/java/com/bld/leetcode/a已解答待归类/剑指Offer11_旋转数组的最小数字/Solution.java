//剑指 Offer 11. 旋转数组的最小数字
//        把一个数组最开始的若干个元素搬到数组的末尾，我们称之为数组的旋转。
//        输入一个递增排序的数组的一个旋转，输出旋转数组的最小元素。
//        例如，数组 [3,4,5,1,2] 为 [1,2,3,4,5] 的一个旋转，该数组的最小值为1。
//
//        示例 1：
//
//        输入：[3,4,5,1,2]
//        输出：1
//        示例 2：
//
//        输入：[2,2,2,0,1]
//        输出：0
//        注意：本题与主站 154 题相同：https://leetcode-cn.com/problems/find-minimum-in-rotated-sorted-array-ii/
//
//        通过次数96,111提交次数194,088

package com.bld.leetcode.a已解答待归类.剑指Offer11_旋转数组的最小数字;


class Solution {
    public int minArray(int[] numbers) {
        return minArray2(numbers);
    }
    public int minArray1(int[] numbers) {
        if (numbers.length == 0)
            return 0;

        for (int i = 0, j = 1; i < numbers.length &&  j < numbers.length; i++, j++) {
            if (numbers[i] > numbers[j])
                return numbers[j];
        }
        return numbers[0];
    }
    public  int minArray2(int[] numbers) {
        int mid;
        int low = 0;
        int high = numbers.length -1;
        int midVal;
        while (low <= high) {
            mid = (low + high) >>> 1;
            midVal = numbers[mid];
            if(midVal > numbers[high])
                low = mid + 1; // on the righ
            else if (midVal < numbers[high])
                high = mid; // on the left
            else
                high = high - 1;
        }
        return numbers[low];
    }
}