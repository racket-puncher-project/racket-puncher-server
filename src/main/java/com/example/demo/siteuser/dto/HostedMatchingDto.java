package com.example.demo.siteuser.dto;

import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.MatchingType;
import com.example.demo.type.RecruitStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HostedMatchingDto {
    private Long id;
    private String title;
    private String location;
    private String date;
    private MatchingType matchingType;
    private List<SiteUserInfoForListDto> otherUsers;
    private RecruitStatus recruitStatus;
    private Boolean evaluated;

    public static HostedMatchingDto makeHostedMatchingDto(Matching matching, ApplyStatus applyStatus, List<SiteUserInfoForListDto> otherUsers) {
        String[] words = matching.getLocation().split(" ");
        String abstractAddress = words[0] + " " + words[1]; // ㅇㅇ시 ㅇㅇ구

        boolean evaluated = applyStatus.equals(ApplyStatus.EVALUATION_COMPLETED);

        return HostedMatchingDto.builder()
                .id(matching.getId())
                .title(matching.getTitle())
                .location(abstractAddress)
                .date(matching.getDate().toString())
                .matchingType(matching.getMatchingType())
                .otherUsers(otherUsers)
                .recruitStatus(matching.getRecruitStatus())
                .evaluated(evaluated)
                .build();
    }
}