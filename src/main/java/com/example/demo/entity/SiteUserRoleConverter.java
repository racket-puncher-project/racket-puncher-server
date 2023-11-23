package com.example.demo.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class SiteUserRoleConverter implements AttributeConverter<List<String>, String> {

    private static final String SPLIT_CHAR = ",";
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null) {
            return null; // 또는 적절한 기본값 반환
        }
        return attribute.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Arrays.stream(dbData.split(SPLIT_CHAR))
                .collect(Collectors.toList());
    }
}
