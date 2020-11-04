//剑指 Offer 04. 二维数组中的查找
//        在一个 n * m 的二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
//
//
//
//        示例:
//
//        现有矩阵 matrix 如下：
//
//        [
//        [1,   4,  7, 11, 15],
//        [2,   5,  8, 12, 19],
//        [3,   6,  9, 16, 22],
//        [10, 13, 14, 17, 24],
//        [18, 21, 23, 26, 30]
//        ]
//        给定 target = 5，返回 true。
//
//        给定 target = 20，返回 false。
//
//
//
//        限制：
//
//        0 <= n <= 1000
//
//        0 <= m <= 1000
//
//
//
//        注意：本题与主站 240 题相同：https://leetcode-cn.com/problems/search-a-2d-matrix-ii/
package com.bld.leetcode.a0已解答待归类.剑指Offer04_二维数组中的查找;

// matrix[n-1][m-1], matric[n-1][m-1], matric[0][m-1]
public class Solution {
    public boolean findNumberIn2DArray(int[][] matrix, int target) {
        final int rowSize = matrix.length;
        final int columnSize = matrix.length > 0 ? matrix[0].length: 0;

        boolean hit = false;
        for (int i = rowSize-1; i >= 0 ; i--) {
            hit = row_find(matrix, i, target);
            if(hit) {
                hit = true;
                break;
            }
        }
        return hit;


    }
    int ArrayBinarySearch(int a[], int k) {
        int mid;
        int low=0;
        int high=a.length -1;
        int midValue;

        while(low <= high) {
            mid = (low+high) >>> 1;
            midValue = a[mid];
            if (midValue < k) {
                low = mid + 1;
            } else if (midValue > k) {
                high = mid - 1;
            } else {
                /* found */
                return mid;
            }
        }
        return -(low+1);

    }

    boolean row_find(int[][] matrix, int row, int target) {
        int rv;
        boolean hit;
        rv = ArrayBinarySearch(matrix[row], target);
        if (rv >= 0) {
            /* finded */
            hit = true;
        } else {
            hit = false;
        }
        return hit;
    }

}