package com.digdes.school.logic;

import com.digdes.school.exceptions.RequestDataException;

import java.util.*;

public class ValuesHandler extends GenericClass {

    protected Map<String, Object> parseValues(String request, int startIdx, int endIdx) throws Exception {
        if (endIdx == -1) endIdx = request.length();
        return prepareListOfValues(request
                .toLowerCase()
                .substring(startIdx, endIdx)
                .trim()
        );
    }

    protected Map<String, Object> prepareListOfValues(String values) throws Exception {
        List<String> listOfValues = Arrays
                .stream(values.split(","))
                .map(String::trim)
                .toList();
        if (listOfValues.stream().allMatch(String::isBlank)) {
            throw new RequestDataException("Incorrect data to insert. All values in one row cannot be empty");
        }
        Map<String, Object> rowDataToInsert = new HashMap<>();
        for (String s : listOfValues) {
            Map.Entry<String, Object> entry = prepareEachValue(s);
            if (!Objects.isNull(entry)) {
                rowDataToInsert.put(entry.getKey(), entry.getValue());
            }
        }
        return rowDataToInsert;
    }

    protected Map.Entry<String, Object> prepareEachValue(String value) throws Exception {
        List<String> valueToInsert = Arrays
                .stream(value.split("="))
                .map(String::trim)
                .toList();
        if (valueToInsert.size() != 2) {
            throw new RequestDataException("Incorrect data. " +
                    "Values should have format like: 'column_name' = 'data'");
        }
        checkPresenceOfQuotes(valueToInsert.get(0));
        String columnName = deleteQuotes(valueToInsert.get(0)).toLowerCase();
        if (valueToInsert.get(1).equals("null")) return new AbstractMap.SimpleEntry<>(columnName, null);
        Object preparedValue = convertValueManager(valueToInsert);
        return new AbstractMap.SimpleEntry<>(columnName, preparedValue);
    }

    protected void deleteNullValues(Map<String, Object> row) throws RequestDataException {
        row.values().removeIf(Objects::isNull);
        if (row.isEmpty()) {
            throw new RequestDataException("Incorrect data to insert. All values in one row cannot be empty");
        }
    }
}
