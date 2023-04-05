package com.digdes.school.logic;

import com.digdes.school.exceptions.ConvertValueException;
import com.digdes.school.exceptions.RequestDataException;

import java.util.List;

public abstract class GenericClass {
    protected Object convertValueManager(List<String> value) throws Exception {
        switch (value.get(0).toLowerCase()) {
            case "'lastname'" -> {
                checkPresenceOfQuotes(value.get(1));
                return deleteQuotes(value.get(1));
            }
            case "'id'", "'age'" -> {
                return convertCurrentValueGeneric(value, Long.class);
            }
            case "'cost'" -> {
                return convertCurrentValueGeneric(value, Double.class);
            }
            case "'active'" -> {
                return convertBooleanValue(value);
            }
            default -> throw new RequestDataException("Incorrect column name");
        }
    }

    private <T> Object convertCurrentValueGeneric(List<String> value, Class<T> typeToConvert) throws Exception {
        checkAbsenceOfQuotes(value.get(1));
        try {
            return typeToConvert.getConstructor(String.class).newInstance(value.get(1));
        } catch (Exception e) {
            throw new ConvertValueException("Incorrect value for " + value.get(0) + " column. " +
                    "Should be " + typeToConvert.getSimpleName() + " type");
        }
    }

    private Object convertBooleanValue(List<String> value) throws Exception {
        checkAbsenceOfQuotes(value.get(1));
        if (value.get(1).equals("true")) {
            return Boolean.TRUE;
        } else if (value.get(1).equals("false")) {
            return Boolean.FALSE;
        } else {
            throw new ConvertValueException("Incorrect value for " + value.get(0) + " column. " +
                    "Should be true or false");
        }
    }

    protected String deleteQuotes(String str) {
        return str.replace("'", "");
    }

    protected void checkPresenceOfQuotes(String value) throws RequestDataException {
        if (!(value.startsWith("'") && value.endsWith("'"))) {
            throw new RequestDataException("Incorrect data. " +
                    "Column names and value with type Srting should be in quotes");
        }
    }

    private void checkAbsenceOfQuotes(String value) throws RequestDataException {
        if (value.startsWith("'") || value.endsWith("'")) {
            throw new RequestDataException("Incorrect data. " +
                    "Numbers and Boolean type should be without quotes");
        }
    }
}
