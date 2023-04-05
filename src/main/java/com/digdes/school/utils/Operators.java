package com.digdes.school.utils;

public enum Operators {

    EQUALS("="),
    NOT_EQUALS("!="),
    LIKE("like"),
    I_LIKE("ilike"),
    MORE_EQUALS(">="),
    LESS_EQUALS("<="),
    MORE(">"),
    LESS("<");

    private final String name;

    Operators(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}