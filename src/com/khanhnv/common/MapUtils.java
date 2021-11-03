package com.khanhnv.common;

import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class MapUtils {

    public static SortedMap<String, Integer> wordCount(String[] strings) {
        // write your code here
        SortedMap<String, Integer> map = new TreeMap<String, Integer>();
        for (var str : strings) {
            if (!map.containsKey(str)) {
                map.put(str, 1);
            } else {
                map.put(str, map.get(str) + 1);
            }
        }
        return map;
    }

    public static void printMap(Map<String, Integer> map) {
        // write your code here
        for (var entry : map.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] words = scanner.nextLine().split(" ");
        MapUtils.printMap(MapUtils.wordCount(words));
    }

}