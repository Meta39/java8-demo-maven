package com.fu.basedemo.algorithm;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * 排序算法
 */
public class SortTest {

    //需要排序的数据
    private static int[] arr = new int[]{1, 10, 2, 9, 8, 3, 7, 5, 6, 4};

    /**
     * 冒泡排序（稳定）
     * 每次比较2个数，大的就交换位置
     */
    @Test
    public void bubbleSort() {
        boolean finish;//结束标识
        int i, j, temp;//初始化变量
        for (i = 0; i < arr.length - 1; i++) {
            finish = true;
            for (j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    finish = false;
                }
            }
            if (finish) {
                break;
            }
            System.out.println("第" + (i + 1) + "次排序：" + Arrays.toString(arr));
        }
    }

    /**
     * 快速排序（不稳定）
     */
    @Test
    public void quicksort() {
        quickSort(arr, 0, arr.length - 1);//数组下标是从0开始的，所以长度减1才是最后一个元素的下标
        System.out.println(Arrays.toString(arr));
    }

    /**
     * 快速排序（推荐）
     *
     * @param arr  需要排序的数组
     * @param low  起始元素下标
     * @param high 结束元素下标
     */
    private static void quickSort(int[] arr, int low, int high) {
        int i, j, temp, t;
        if (low > high) {
            return;
        }
        i = low;
        j = high;
        //temp就是基准位
        temp = arr[low];

        while (i < j) {
            //先看右边，依次往左递减
            while (temp <= arr[j] && i < j) {
                j--;
            }
            //再看左边，依次往右递增
            while (temp >= arr[i] && i < j) {
                i++;
            }
            //如果满足条件则交换
            if (i < j) {
                t = arr[j];
                arr[j] = arr[i];
                arr[i] = t;
            }

        }
        //最后将基准为与i和j相等位置的数字交换
        arr[low] = arr[i];
        arr[i] = temp;
        //递归调用左半数组
        quickSort(arr, low, j - 1);
        //递归调用右半数组
        quickSort(arr, j + 1, high);
    }

}
