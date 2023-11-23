package com.example.demo.util.serializer;

import com.example.demo.matching.dto.ApplyMember;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class ApplyMemberSerializer extends JsonSerializer<ApplyMember> {

    @Override
    public void serialize(ApplyMember applyMember, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();

        gen.writeNumberField("applyId", applyMember.getApplyId());
        gen.writeNumberField("siteUserId", applyMember.getSiteUserId());
        gen.writeStringField("nickname", applyMember.getNickname());

        gen.writeEndObject();
    }
}
