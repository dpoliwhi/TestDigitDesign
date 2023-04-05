package com.digdes.school.logic;

import com.digdes.school.exceptions.IncorrectOperatorException;
import com.digdes.school.exceptions.RequestDataException;
import com.digdes.school.utils.ConditionsWrapper;
import com.digdes.school.utils.Operators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Filter {

    private final ConditionsWrapper conditionsWrapper;
    private final List<Map<String, Object>> data;

    public Filter(ConditionsWrapper conditionsWrapper, List<Map<String, Object>> data) {
        this.conditionsWrapper = conditionsWrapper;
        this.data = data;
    }

    protected List<Map<String, Object>> filterRunner() throws Exception {
        List<Map<String, Object>> resultBuf;
        List<Map<String, Object>> result = new ArrayList<>();
        for (int i = 0; i < conditionsWrapper.allConditions().size(); i++) {
            resultBuf = filter(conditionsWrapper.allConditions().get(i), conditionsWrapper.operators().get(i));
            if (result.isEmpty()) result.addAll(resultBuf);
            List<Map<String, Object>> temp = new ArrayList<>(resultBuf);
            temp.removeAll(result);
            result.addAll(temp);
        }
        return result;
    }


    private List<Map<String, Object>> filter(Map<String, Object> condition, List<Operators> operator) throws Exception {
        List<Map<String, Object>> resultBuf = new ArrayList<>();
        List<Map<String, Object>> result = new ArrayList<>();
        int indexOfOperator = 0;
        for (Map.Entry<String, Object> oneConditionEntry : condition.entrySet()) {
            resultBuf.clear();
            for (Map<String, Object> oneRow : data) {
                Object valueToCompare = oneRow.get(oneConditionEntry.getKey());
                Object conditionToCompare = oneConditionEntry.getValue();
                boolean comparison = Objects.isNull(valueToCompare) || checkCondition(
                        valueToCompare, conditionToCompare, operator.get(indexOfOperator));
                if (comparison) {
                    resultBuf.add(oneRow);
                }
            }
            if (result.isEmpty()) result.addAll(resultBuf);
            result.retainAll(resultBuf);
            ++indexOfOperator;
        }
        return result;
    }

    private boolean checkCondition(Object value, Object condition, Operators operator) throws Exception {
        if (operator.equals(Operators.EQUALS)) {
            return value.equals(condition);
        } else if (operator.equals(Operators.NOT_EQUALS)) {
            return !value.equals(condition);
        } else if (value.getClass() == Long.class || value.getClass() == Double.class) {
            return compareLongAndDoubleType(value, condition, operator);
        } else if (value.getClass() == String.class) {
            return compareStringLikeOperator((String) value, (String) condition, operator);
        }
        return false;
    }

    private boolean compareLongAndDoubleType(Object value, Object condition, Operators operator) throws IncorrectOperatorException {
        if (operator.equals(Operators.MORE_EQUALS)) {
            return value.getClass() == Long.class ? (Long) value >= (Long) condition : (Double) value >= (Double) condition;
        } else if (operator.equals(Operators.LESS_EQUALS)) {
            return value.getClass() == Long.class ? (Long) value <= (Long) condition : (Double) value <= (Double) condition;
        } else if (operator.equals(Operators.MORE)) {
            return value.getClass() == Long.class ? (Long) value > (Long) condition : (Double) value > (Double) condition;
        } else if (operator.equals(Operators.LESS)) {
            return value.getClass() == Long.class ? (Long) value < (Long) condition : (Double) value < (Double) condition;
        } else {
            throw new IncorrectOperatorException("Incorrect operator. " +
                    "Operators for types Long and Double should be: <, <=, >, >=, =, !=");
        }
    }

    private boolean compareStringLikeOperator(String value, String condition, Operators operator) throws Exception {
        if (operator.equals(Operators.LIKE)) {
            return regexHandler(value, condition);
        } else if (operator.equals(Operators.I_LIKE)) {
            return regexHandler(value.toLowerCase(), condition.toLowerCase());
        } else {
            throw new IncorrectOperatorException("Incorrect operator. " +
                    "Operators for String type should be: =, !=, like, ilike");
        }
    }

    private boolean regexHandler(String value, String condition) throws RequestDataException {
        if (!condition.contains("%")) {
            return value.equals(condition);
        } else if (condition.matches("%[\\-a-zA-Z0-9А-Яа-я]+")) {
            return value.endsWith(condition.substring(1));
        } else if (condition.matches("[\\-a-zA-Z0-9А-Яа-я]+%")) {
            return value.startsWith(condition.substring(0, condition.length() - 1));
        } else if (condition.matches("%[\\-a-zA-Z0-9А-Яа-я]+%")) {
            return value.contains(condition.substring(1, condition.length() - 1));
        } else {
            throw new RequestDataException("Incorrect data to compare. " +
                    "With operator LIKE or ILIKE condition should be: str, %str, str%, %str%");
        }
    }
}
