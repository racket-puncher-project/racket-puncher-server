package com.example.demo.matching.dto;

import com.example.demo.matching.serializer.ApplyContentsSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = ApplyContentsSerializer.class)
public class ApplyContents {
    private boolean isApplied;
    private int applyNum;
    private int recruitNum;
    private int acceptedNum;
    private List<ApplyMember> appliedMembers;
    private List<ApplyMember> acceptedMembers;
}
