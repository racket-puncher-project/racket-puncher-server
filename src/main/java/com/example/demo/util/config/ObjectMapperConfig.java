package com.example.demo.util.config;

import com.example.demo.matching.dto.ApplyContents;
import com.example.demo.matching.dto.ApplyMember;
import com.example.demo.util.serializer.ApplyContentsSerializer;
import com.example.demo.util.serializer.ApplyMemberSerializer;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper getObjectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(ApplyMember.class, new ApplyMemberSerializer());
        simpleModule.addSerializer(ApplyContents.class, new ApplyContentsSerializer());

        objectMapper.registerModule(simpleModule);

        return objectMapper;
    }
}
