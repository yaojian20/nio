package com.yao.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Created by yaojian on 2022/5/12 15:01
 *
 * @author
 */
public class Test {


    public static void main(String[] args) {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();


    }

    public static void  test1(){
        WorkMonth workMonth = new WorkMonth(1,3);
        WorkMonth workMonth1 = new WorkMonth(6,9);
        List<WorkMonth> workMonths = new ArrayList<>();
        workMonths.add(workMonth);
        workMonths.add(workMonth1);
        WorkMonth newWorkMonth = new WorkMonth(2,5);
        generateWorkMonth(workMonths, newWorkMonth);
    }
    public static void  test2(){
        WorkMonth workMonth = new WorkMonth(1,2);
        WorkMonth workMonth1 = new WorkMonth(3,5);
        WorkMonth workMonth4 = new WorkMonth(6,7);
        WorkMonth workMonth2 = new WorkMonth(8,10);
        WorkMonth workMonth3 = new WorkMonth(12,16);
        List<WorkMonth> workMonths = new ArrayList<>();
        workMonths.add(workMonth);
        workMonths.add(workMonth1);
        workMonths.add(workMonth4);
        workMonths.add(workMonth2);
        workMonths.add(workMonth3);
        WorkMonth newWorkMonth = new WorkMonth(4,8);
        generateWorkMonth(workMonths, newWorkMonth);
    }

    public static void  test3(){

        List<WorkMonth> workMonths = new ArrayList<>();
        WorkMonth newWorkMonth = new WorkMonth(5,7);
        generateWorkMonth(workMonths, newWorkMonth);
    }
    public static void  test4(){
        WorkMonth workMonth = new WorkMonth(1,5);
        List<WorkMonth> workMonths = new ArrayList<>();
        workMonths.add(workMonth);
        WorkMonth newWorkMonth = new WorkMonth(2,3);
        generateWorkMonth(workMonths, newWorkMonth);
    }
    public static void  test5(){
        WorkMonth workMonth = new WorkMonth(1,5);
        List<WorkMonth> workMonths = new ArrayList<>();
        workMonths.add(workMonth);
        WorkMonth newWorkMonth = new WorkMonth(2,7);
        generateWorkMonth(workMonths, newWorkMonth);
    }

    public static void  test6(){
        WorkMonth workMonth = new WorkMonth(1,3);
        WorkMonth workMonth1 = new WorkMonth(8,10);
        List<WorkMonth> workMonths = new ArrayList<>();
        workMonths.add(workMonth);
        workMonths.add(workMonth1);
        WorkMonth newWorkMonth = new WorkMonth(5,7);
        generateWorkMonth(workMonths, newWorkMonth);
    }

    public static void  test7(){
        WorkMonth workMonth = new WorkMonth(1,3);
        WorkMonth workMonth1 = new WorkMonth(8,10);
        List<WorkMonth> workMonths = new ArrayList<>();
        workMonths.add(workMonth);
        workMonths.add(workMonth1);
        WorkMonth newWorkMonth = new WorkMonth(5,9);
        generateWorkMonth(workMonths, newWorkMonth);
    }


    public static List<WorkMonth> generateWorkMonth(List<WorkMonth> oldWorkMonths,WorkMonth workMonth){
        if ((oldWorkMonths == null || oldWorkMonths.size() == 0) && workMonth == null){
            return null;
        }
        if (workMonth == null){
            return oldWorkMonths;
        }
        if (oldWorkMonths == null || oldWorkMonths.size() == 0){
            oldWorkMonths = new ArrayList<>();
            oldWorkMonths.add(workMonth);
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }
        WorkMonth firstWorkMonth = oldWorkMonths.get(0);
        if (firstWorkMonth.getStart() > workMonth.getEnd()){
            //结束月小于原始数组的开始月，那么直接放在集合的最前面，不需要重组
            oldWorkMonths.add(0, workMonth);
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }

        WorkMonth lastWorkMonth = oldWorkMonths.get(oldWorkMonths.size()-1);
        if (lastWorkMonth.getEnd() < workMonth.getStart()){
            //开始月大于于原始数组的结束月，那么直接放在集合的最后面，不需要重组
            oldWorkMonths.add(workMonth);
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }

        //现在有三种种情况，一种是新加的时间间隔刚好在原始的间隔内，那么无需变化，
        // 如果横跨多个，那么多个数组要融合
        // 如果刚好在其中一个到另外一个中间而且跟他们没有交集，那么需要插入到这两个数组中间
        int start = -1;//记录新插入的时间开始位于老数组的哪个下标里面
        int end = -1;//记录新插入的时间结束位于老数组的哪个下标里面
        int startFlag = 0;//记录新插入的时间开始位于老数组的哪个下标前面
        int endFlag = 0;//记录新插入的时间结束位于老数组的哪个下标前面
        for (int i = 0; i < oldWorkMonths.size(); i++){
            if (workMonth.getStart() <= oldWorkMonths.get(i).getEnd() && workMonth.getStart() >= oldWorkMonths.get(i).getStart()){
                start = i;
            }
            if (workMonth.getEnd() <= oldWorkMonths.get(i).getEnd() && workMonth.getEnd() >= oldWorkMonths.get(i).getStart()){
                end = i;
            }
            if (workMonth.getStart() > oldWorkMonths.get(i).getEnd()){
                startFlag = i;
            }
            if (workMonth.getEnd() > oldWorkMonths.get(i).getStart()){
                endFlag = i;
            }
        }

        if (start != -1 && start == end){
            //一种是新加的时间间隔刚好在原始的间隔内，那么无需变化，
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }
        if (start == -1 && start == end){
            //如果刚好在其中一个到另外一个中间而且跟他们没有交集，那么需要插入到这两个数组中间
            oldWorkMonths.add(startFlag+1, workMonth);
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }

        if (start == -1 && start != end){
            WorkMonth workMonth1 = new WorkMonth();
            workMonth1.setStart(workMonth.getStart());
            workMonth1.setEnd(oldWorkMonths.get(end).getEnd());
            List<WorkMonth> remove = oldWorkMonths.subList(startFlag+1, end+1);
            oldWorkMonths.removeAll(remove);
            oldWorkMonths.add(end, workMonth1);
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }

        if (end == -1 && start != end){
            WorkMonth workMonth1 = new WorkMonth();
            workMonth1.setStart(oldWorkMonths.get(start).getStart());
            workMonth1.setEnd(workMonth.getEnd());
            List<WorkMonth> remove = oldWorkMonths.subList(start, endFlag+1);
            oldWorkMonths.removeAll(remove);
            oldWorkMonths.add(start, workMonth1);
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }

        if (start !=-1 && end != -1 && start != end){
            WorkMonth workMonth1 = new WorkMonth();
            workMonth1.setStart(oldWorkMonths.get(start).getStart());
            workMonth1.setEnd(oldWorkMonths.get(end).getEnd());
            List<WorkMonth> remove = oldWorkMonths.subList(start, end+1);
            oldWorkMonths.removeAll(remove);
            oldWorkMonths.add(start, workMonth1);
            System.out.println(oldWorkMonths.toString());
            return oldWorkMonths;
        }

        return null;
    }

    // Example 1:
    // Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
    // Output: [[1,5],[6,9]]

    // Example 2:
    // Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
    // Output: [[1,2],[3,10],[12,16]]
    // Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].

    // Example 3:
    // Input: intervals = [], newInterval = [5,7]
    // Output: [[5,7]]

    // Example 4:
    // Input: intervals = [[1,5]], newInterval = [2,3]
    // Output: [[1,5]]

    // Example 5:
    // Input: intervals = [[1,5]], newInterval = [2,7]
    // Output: [[1,7]]


}
