package com.example.demo.entity;

import com.example.demo.matching.dto.MatchingDetailRequestDto;
import com.example.demo.type.AgeGroup;
import com.example.demo.type.MatchingType;
import com.example.demo.type.Ntrp;
import com.example.demo.type.RecruitStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Table(name = "MATCHING")
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SITE_USER_ID", nullable = false)
    private SiteUser siteUser;

    @Column(name = "TITLE", length = 50, nullable = false)
    private String title;

    @Column(name = "CONTENT", length = 1023)
    private String content;

    @Column(name = "LOCATION", nullable = false)
    private String location;

    @Column(name = "LAT", nullable = false)
    private Double lat;

    @Column(name = "LON", nullable = false)
    private Double lon;

    @Column(name = "LOCATION_IMG", length = 1023)
    private String locationImg;

    @Column(name = "DATE", nullable = false) // yyyy-MM-dd
    private LocalDate date;

    @Column(name = "START_TIME", nullable = false) // HH:mm
    private LocalTime startTime;

    @Column(name = "END_TIME", nullable = false) // HH:mm
    private LocalTime endTime;

    @Column(name = "RECRUIT_DUE_DATE_TIME", nullable = false) // yyyy-MM-dd HH:mm
    private LocalDateTime recruitDueDateTime;

    @Column(name = "RECRUIT_NUM", nullable = false)
    private Integer recruitNum;

    @Column(name = "COST", nullable = false)
    private Integer cost;

    @Column(name = "IS_RESERVED")
    private Boolean isReserved;

    @Enumerated(EnumType.STRING)
    @Column(name = "NTRP")
    private Ntrp ntrp;

    @Enumerated(EnumType.STRING)
    @Column(name = "AGE")
    private AgeGroup age;

    @Enumerated(EnumType.STRING)
    @Column(name = "RECRUIT_STATUS")
    private RecruitStatus recruitStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "MATCHING_TYPE")
    private MatchingType matchingType;

    public Integer getConfirmedNum() {
        return confirmedNum;
    }

    @Column(name = "CONFIRMED_NUM")
    private Integer confirmedNum;

    @CreatedDate
    @Column(name = "CREATE_TIME") // yyyy-MM-dd HH:mm
    private LocalDateTime createTime;

    public static Matching fromDto(MatchingDetailRequestDto matchingDetailRequestDto, SiteUser siteUser) {
        // 시간 파싱
        DateTimeFormatter formForDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        DateTimeFormatter formForTime = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter formForDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String recruitDueDateTimeFromDto = matchingDetailRequestDto.getRecruitDueDate()
                + " " + matchingDetailRequestDto.getRecruitDueTime() + ":00";

        LocalDate date = LocalDate.parse(matchingDetailRequestDto.getDate(), formForDate);
        LocalTime startTime = LocalTime.parse(matchingDetailRequestDto.getStartTime(), formForTime);
        LocalTime endTime = LocalTime.parse(matchingDetailRequestDto.getEndTime(), formForTime);
        LocalDateTime recruitDueDateTime = LocalDateTime
                .parse(recruitDueDateTimeFromDto, formForDateTime);

        return Matching.builder()
                .siteUser(siteUser)
                .title(matchingDetailRequestDto.getTitle())
                .content(matchingDetailRequestDto.getContent())
                .location(matchingDetailRequestDto.getLocation())
                .lat(matchingDetailRequestDto.getLat())
                .lon(matchingDetailRequestDto.getLon())
                .locationImg(matchingDetailRequestDto.getLocationImg())
                .date(date)
                .startTime(startTime)
                .endTime(endTime)
                .recruitDueDateTime(recruitDueDateTime)
                .recruitNum(matchingDetailRequestDto.getRecruitNum())
                .cost(matchingDetailRequestDto.getCost())
                .isReserved(matchingDetailRequestDto.getIsReserved())
                .ntrp(matchingDetailRequestDto.getNtrp())
                .age(matchingDetailRequestDto.getAgeGroup())
                .recruitStatus(matchingDetailRequestDto.getRecruitStatus())
                .matchingType(matchingDetailRequestDto.getMatchingType())
                .build();
    }

    // setter 없이 Matching 수정하기 위한 메서드
    public void update(Matching matching){
        this.title = matching.getTitle();
        this.content = matching.getContent();
        this.location = matching.getLocation();
        this.lat = matching.getLat();
        this.lon = matching.getLon();
        this.locationImg = matching.getLocationImg();
        this.date = matching.getDate();
        this.startTime = matching.getStartTime();
        this.endTime = matching.getEndTime();
        this.recruitDueDateTime = matching.getRecruitDueDateTime();
        this.recruitNum = matching.getRecruitNum();
        this.cost = matching.getCost();
        this.isReserved = matching.getIsReserved();
        this.ntrp = matching.getNtrp();
        this.age = matching.getAge();
        this.matchingType = matching.getMatchingType();
    }

    public void updateConfirmedNum(int confirmedNum) {
        this.confirmedNum = confirmedNum;
    }

    public void changeRecruitStatus(RecruitStatus recruitStatus) {
        this.recruitStatus = recruitStatus;
    }

    public void changeConfirmedNum(int confirmedNum) {
        this.confirmedNum = confirmedNum;
    }
}