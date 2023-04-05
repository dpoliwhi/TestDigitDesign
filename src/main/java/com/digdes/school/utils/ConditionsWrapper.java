package com.digdes.school.utils;

import java.util.List;
import java.util.Map;

public record ConditionsWrapper(List<List<Operators>> operators, List<Map<String, Object>> allConditions) {
}
