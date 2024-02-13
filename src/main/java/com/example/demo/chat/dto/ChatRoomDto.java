package com.example.demo.chat.dto;

import com.example.demo.entity.Matching;
import com.example.demo.siteuser.dto.SiteUserInfoForListDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {
    private long matchingId;
    private String title;
    private String locationImg;
    private long newMessageNum;
    private List<SiteUserInfoForListDto> participants;

    public static ChatRoomDto makeChatRoomDto(Matching matching, long newMessageNum, List<SiteUserInfoForListDto> participants){
        return ChatRoomDto.builder()
                .matchingId(matching.getId())
                .title(matching.getTitle())
                .locationImg(matching.getLocationImg())
                .newMessageNum(newMessageNum)
                .participants(participants)
                .build();
    }
}
