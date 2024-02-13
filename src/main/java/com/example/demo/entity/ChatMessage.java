package com.example.demo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.example.demo.chat.repository.ChatMessageId;
import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@DynamoDBTable(tableName = "chat-history")
public class ChatMessage {
    @Id
    private ChatMessageId id;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute(attributeName = "senderId")
    private String senderId;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute(attributeName = "content")
    private String content;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBHashKey(attributeName = "matchingId")
    public String getMatchingId(){
        return id != null ? id.getMatchingId() : null;
    }

    public void setMatchingId(String matchingId) {
        if (this.id == null) {
            this.id = new ChatMessageId();
        }
        this.id.setMatchingId(matchingId);
    }

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBRangeKey(attributeName = "time")
    public String getTime(){
        return id != null ? id.getTime() : null;
    }

    public void setTime(String time) {
        if (this.id == null) {
            this.id = new ChatMessageId();
        }
        this.id.setTime(time);
    }
}