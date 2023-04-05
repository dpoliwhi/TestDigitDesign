package com.digdes.school.logic;

import com.digdes.school.exceptions.RequestTaskException;
import com.digdes.school.utils.ConditionsWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {

    private final String INSERT = "insert ";
    private final String UPDATE = "update ";
    private final String SELECT = "select";
    private final String DELETE = "delete";
    private final String VALUES = "values";
    private final String WHERE = "where ";

    private final ValuesHandler valuesHandler;
    private final WhereHandler whereHandler;

    private final List<Map<String, Object>> data;

    public JavaSchoolStarter() {
        data = new ArrayList<>();
        valuesHandler = new ValuesHandler();
        whereHandler = new WhereHandler();
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public List<Map<String, Object>> execute(String request) throws Exception {
        return choiceTask(request);
    }

    private List<Map<String, Object>> choiceTask(String request) throws Exception {
        request = request.trim();
        if (request.toLowerCase().startsWith(INSERT)) {
            insertData(request.substring(INSERT.length()).trim());
        } else if (request.toLowerCase().startsWith(UPDATE)) {
            updateData(request.substring(UPDATE.length()).trim());
        } else if (request.toLowerCase().startsWith(SELECT)) {
            return selectData(request.substring(SELECT.length()).trim());
        } else if (request.toLowerCase().startsWith(DELETE)) {
            deleteData(request.substring(DELETE.length()).trim());
        } else {
            throw new RequestTaskException("Incorrect task request. " +
                    "Should start with \"INSERT\", \"UPDATE\"', \"SELECT\" or \"DELETE\"");
        }
        return data;
    }

    private void insertData(String request) throws Exception {
        if (!request.toLowerCase().startsWith(VALUES)) {
            throw new RequestTaskException("Incorrect task request. " +
                    "Enumeration of values to insert should start with \"VALUES\"");
        }
        Map<String, Object> oneRowToAdd = valuesHandler
                .prepareListOfValues(request.substring(VALUES.length()).trim());
        valuesHandler.deleteNullValues(oneRowToAdd);
        data.add(oneRowToAdd);
    }

    private void updateData(String request) throws Exception {
        if (!request.toLowerCase().startsWith(VALUES)) {
            throw new RequestTaskException("Incorrect task request. " +
                    "Enumeration of values to update should start with \"VALUES\"");
        }
        int whereIndex = request.toLowerCase().indexOf(WHERE);
        Map<String, Object> dataToUpdate = valuesHandler.parseValues(request, VALUES.length(), whereIndex);
        if (whereIndex != -1) {
            ConditionsWrapper conditionsWrapper = whereHandler
                    .prepareListOfConditions(request
                            .substring(whereIndex + WHERE.length())
                            .trim());

            Filter filter = new Filter(conditionsWrapper, data);
            List<Map<String, Object>> filteredData = filter.filterRunner();
            data.removeAll(filteredData);
            for (Map<String, Object> oneRow : filteredData) {
                oneRow.putAll(dataToUpdate);
                valuesHandler.deleteNullValues(oneRow);
            }
            data.addAll(filteredData);
        } else {
            for (Map<String, Object> oneRow : data) {
                oneRow.putAll(dataToUpdate);
            }
        }
    }

    private void deleteData(String request) throws Exception {
        if (request.toLowerCase().startsWith(VALUES)) {
            throw new RequestTaskException("Incorrect task request. DELETE task don't use with \"VALUES\"");
        }
        if (request.toLowerCase().startsWith(WHERE)) {
            ConditionsWrapper conditionsWrapper = whereHandler
                    .prepareListOfConditions(request
                            .substring(WHERE.length())
                            .trim());
            Filter filter = new Filter(conditionsWrapper, data);
            List<Map<String, Object>> filteredData = filter.filterRunner();
            data.removeAll(filteredData);
        } else {
            data.clear();
        }
    }

    private List<Map<String, Object>> selectData(String request) throws Exception {
        if (request.toLowerCase().startsWith(VALUES)) {
            throw new RequestTaskException("Incorrect task request. SELECT task don't use with \"VALUES\"");
        }
        if (request.toLowerCase().startsWith(WHERE)) {
            ConditionsWrapper conditionsWrapper = whereHandler
                    .prepareListOfConditions(request
                            .substring(WHERE.length())
                            .trim());

            Filter filter = new Filter(conditionsWrapper, data);
            return filter.filterRunner();
        } else {
            return data;
        }
    }
}
