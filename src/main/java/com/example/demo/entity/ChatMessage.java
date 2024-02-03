package com.example.demo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;

@DynamoDBTable(tableName = "ChatMessages")
public class ChatMessage {
    @DynamoDBHashKey(attributeName = "ChatRoomId") // 파티션키
    private String chatRoomId;

    @DynamoDBRangeKey(attributeName = "Time") // 정렬키
    private Date time;

    @DynamoDBAttribute(attributeName = "SenderId")
    private String senderId;

    @DynamoDBAttribute(attributeName = "Content")
    private String content;
}