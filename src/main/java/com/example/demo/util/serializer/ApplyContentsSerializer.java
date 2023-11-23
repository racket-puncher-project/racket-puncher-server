package com.example.demo.util.serializer;

import com.example.demo.matching.dto.ApplyContents;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class ApplyContentsSerializer extends JsonSerializer<ApplyContents> {

    @Override
    public void serialize(ApplyContents applyContents, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        if (applyContents.getApplyNum() != 0) {
            gen.writeNumberField("applyNum", applyContents.getApplyNum());
        }
        gen.writeNumberField("recruitNum", applyContents.getRecruitNum());
        gen.writeNumberField("confirmedNum", applyContents.getConfirmedNum());
        if (applyContents.getAppliedMembers() != null) {
            gen.writeObjectField("appliedMembers", applyContents.getAppliedMembers());
        }
        gen.writeObjectField("confirmedMembers", applyContents.getConfirmedMembers());

        gen.writeEndObject();
    }
}
