package com.digdes.school.utils;

import java.util.List;
import java.util.Map;

// TODO проработать форматированный вывод
public class Printer {
    public static void printData(List<Map<String, Object>> data) {
        data.forEach(stringObjectMap -> {
            for (Map.Entry<String, Object> entry : stringObjectMap.entrySet()) {
                System.out.print(entry.getKey() + " ");
                System.out.print(entry.getValue() + " ");
            }
            System.out.println();
        });
    }
}
