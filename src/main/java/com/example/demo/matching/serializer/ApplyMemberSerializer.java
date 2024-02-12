package com.example.demo.matching.serializer;

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
        gen.writeStringField("siteUsername", applyMember.getSiteUsername());
        gen.writeStringField("email", applyMember.getEmail());
        gen.writeNumberField("mannerScore", applyMember.getMannerScore());
        gen.writeStringField("genderType", String.valueOf(applyMember.getGenderType()));
        gen.writeStringField("ntrp", String.valueOf(applyMember.getNtrp()));
        gen.writeStringField("ageGroup", String.valueOf(applyMember.getAgeGroup()));
        gen.writeStringField("address", applyMember.getAddress());
        gen.writeStringField("zipCode", applyMember.getZipCode());
        gen.writeStringField("profileImg", applyMember.getProfileImg());

        gen.writeEndObject();
    }
}
