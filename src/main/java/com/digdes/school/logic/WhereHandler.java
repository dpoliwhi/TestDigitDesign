package com.digdes.school.logic;

import com.digdes.school.exceptions.IncorrectOperatorException;
import com.digdes.school.exceptions.RequestDataException;
import com.digdes.school.utils.ConditionsWrapper;
import com.digdes.school.utils.Operators;

import java.util.*;

public class WhereHandler extends GenericClass {

    protected ConditionsWrapper prepareListOfConditions(String conditions) throws Exception {
        List<String> listOfORConditions = Arrays
                .stream(conditions.split("or|OR"))
                .map(String::trim)
                .toList();

        List<List<String>> dividedConditions = new ArrayList<>();
        for (String listOfORCondition : listOfORConditions) {
            List<String> listOfANDConditions = Arrays
                    .stream(listOfORCondition.split("and|AND"))
                    .map(String::trim)
                    .toList();
            dividedConditions.add(listOfANDConditions);
        }

        List<List<Operators>> operators = findListOfOperators(dividedConditions);
        List<Map<String, Object>> allConditions = new ArrayList<>();
        for (List<String> dividedCondition : dividedConditions) {
            Map<String, Object> conditionsData = new LinkedHashMap<>();
            for (String s : dividedCondition) {
                Map.Entry<String, Object> entry = prepareEachCondition(s);
                conditionsData.put(entry.getKey(), entry.getValue());
            }
            allConditions.add(conditionsData);
        }
        return new ConditionsWrapper(operators, allConditions);
    }

    protected Map.Entry<String, Object> prepareEachCondition(String value) throws Exception {
        List<String> valueToInsert = Arrays
                .stream(value.split("!=|>=|<=|=|>|<|ilike|like"))
                .map(String::trim)
                .toList();
        if (valueToInsert.size() != 2) {
            throw new RequestDataException("Incorrect condition. " +
                    "Conditions should have format like: 'column_name' >= 'condition'");
        }
        checkPresenceOfQuotes(valueToInsert.get(0));
        String columnName = deleteQuotes(valueToInsert.get(0));
        Object preparedValue = convertValueManager(valueToInsert);
        return new AbstractMap.SimpleEntry<>(columnName, preparedValue);
    }


    private List<List<Operators>> findListOfOperators(List<List<String>> dividedConditions) throws IncorrectOperatorException {
        List<List<Operators>> operators = new ArrayList<>();
        for (List<String> dividedCondition : dividedConditions) {
            List<Operators> operatorsInANDLine = new ArrayList<>();
            for (String s : dividedCondition) {
                operatorsInANDLine.add(operatorsHandler(s));
            }
            operators.add(operatorsInANDLine);
        }
        return operators;
    }

    private Operators operatorsHandler(String expression) throws IncorrectOperatorException {
        if (expression.contains(Operators.NOT_EQUALS.getName())) return Operators.NOT_EQUALS;
        if (expression.contains(Operators.MORE_EQUALS.getName())) return Operators.MORE_EQUALS;
        if (expression.contains(Operators.LESS_EQUALS.getName())) return Operators.LESS_EQUALS;
        if (expression.contains(Operators.EQUALS.getName())) return Operators.EQUALS;
        if (expression.contains(Operators.LESS.getName())) return Operators.LESS;
        if (expression.contains(Operators.MORE.getName())) return Operators.MORE;
        if (expression.contains(Operators.I_LIKE.getName())) return Operators.I_LIKE;
        if (expression.contains(Operators.LIKE.getName())) return Operators.LIKE;
        else {
            throw new IncorrectOperatorException("Incorrect operator in condition WHERE");
        }
    }
}
