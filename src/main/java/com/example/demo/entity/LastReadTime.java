package com.example.demo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.example.demo.chat.repository.LastReadTimeId;
import lombok.*;
import org.springframework.data.annotation.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@DynamoDBTable(tableName = "last-read-time")
public class LastReadTime {
    @Id
    private LastReadTimeId id;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBAttribute(attributeName = "time")
    private String time;

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBHashKey(attributeName = "matchingId")
    public String getMatchingId(){
        return id != null ? id.getMatchingId() : null;
    }

    public void setMatchingId(String matchingId) {
        if (this.id == null) {
            this.id = new LastReadTimeId();
        }
        this.id.setMatchingId(matchingId);
    }

    @DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
    @DynamoDBRangeKey(attributeName = "siteUserId")
    public String getSiteUserId(){
        return id != null ? id.getSiteUserId() : null;
    }

    public void setSiteUserId(String time) {
        if (this.id == null) {
            this.id = new LastReadTimeId();
        }
        this.id.setSiteUserId(time);
    }
}
