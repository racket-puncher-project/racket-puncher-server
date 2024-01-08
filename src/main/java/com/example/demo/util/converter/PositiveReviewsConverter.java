package com.example.demo.util.converter;

import com.example.demo.exception.RacketPuncherException;
import com.example.demo.exception.type.ErrorCode;
import com.example.demo.type.PositiveReviewType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;
import java.util.List;

@Converter(autoApply = true)
public class PositiveReviewsConverter implements AttributeConverter<List<PositiveReviewType>, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<PositiveReviewType> meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException e) {
            throw new RacketPuncherException(ErrorCode.JSON_PARSING_FAILED);
        }
    }

    @Override
    public List<PositiveReviewType> convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, List.class);
        } catch (IOException e) {
            throw new RacketPuncherException(ErrorCode.JSON_PARSING_FAILED);
        }
    }
}
