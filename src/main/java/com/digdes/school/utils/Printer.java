package com.digdes.school.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Printer {
    private static final int COLUMN_WIDTH = 10;
    private static final String[] COLUMNS = {"id", "lastName", "age", "cost", "active"};

    public static void printData(List<Map<String, Object>> data) {
        Arrays.stream(COLUMNS).forEach(col ->
                System.out.printf("%" + COLUMN_WIDTH + "s|", col)
        );
        System.out.println();
        for (int i = 0; i < (COLUMN_WIDTH * COLUMNS.length + COLUMNS.length); i++) {
            System.out.print("_");
        }
        System.out.println();
        data.forEach(row -> {
            Arrays.stream(COLUMNS).forEach(col ->
                System.out.printf("%" + COLUMN_WIDTH + "s|", row.get(col.toLowerCase()))
            );
            System.out.println();
        });
    }
}
