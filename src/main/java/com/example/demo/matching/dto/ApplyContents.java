package com.example.demo.matching.dto;

import com.example.demo.util.serializer.ApplyContentsSerializer;
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
    private int applyNum;
    private int recruitNum;
    private int confirmedNum;
    private List<ApplyMember> appliedMembers;
    private List<ApplyMember> confirmedMembers;
}
