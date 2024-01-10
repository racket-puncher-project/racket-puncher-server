package com.example.demo.matching.service;

import static com.example.demo.exception.type.ErrorCode.APPLY_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.EMAIL_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.JSON_PARSING_FAILED;
import static com.example.demo.exception.type.ErrorCode.MATCHING_NOT_FOUND;
import static com.example.demo.exception.type.ErrorCode.PERMISSION_DENIED_TO_EDIT_AND_DELETE_MATCHING;
import static com.example.demo.exception.type.ErrorCode.USER_NOT_FOUND;

import com.example.demo.apply.dto.ApplyDto;
import com.example.demo.apply.repository.ApplyRepository;
import com.example.demo.common.FindEntity;
import com.example.demo.entity.Apply;
import com.example.demo.entity.Matching;
import com.example.demo.entity.SiteUser;
import com.example.demo.exception.RacketPuncherException;
import com.example.demo.matching.dto.ApplyContents;
import com.example.demo.matching.dto.ApplyMember;
import com.example.demo.matching.dto.FilterRequestDto;
import com.example.demo.matching.dto.LocationDto;
import com.example.demo.matching.dto.MatchingDetailRequestDto;
import com.example.demo.matching.dto.MatchingDetailResponseDto;
import com.example.demo.matching.dto.MatchingPreviewDto;
import com.example.demo.matching.repository.MatchingRepository;
import com.example.demo.notification.service.NotificationService;
import com.example.demo.siteuser.repository.SiteUserRepository;
import com.example.demo.type.ApplyStatus;
import com.example.demo.type.NotificationType;
import com.example.demo.type.PenaltyType;
import com.example.demo.type.RecruitStatus;
import com.example.demo.util.geometry.GeometryUtil;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchingServiceImpl implements MatchingService {

    private final MatchingRepository matchingRepository;
    private final ApplyRepository applyRepository;
    private final FindEntity findEntity;
    private final SiteUserRepository siteUserRepository;
    private final NotificationService notificationService;

    @Value("${kakao.client_id}")
    private String apiKey;


    private static boolean isOrganizer(long userId, Matching matching) {
        return matching.getSiteUser().getId() == userId;
    }

    @Override
    public Matching create(String email, MatchingDetailRequestDto matchingDetailRequestDto) {
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));
        List<Double> latAndLon = getLatAndLon(getUserAddressInfo(matchingDetailRequestDto.getLocation()));
        matchingDetailRequestDto.setLat(latAndLon.get(0));
        matchingDetailRequestDto.setLon(latAndLon.get(1));
        Matching matching = matchingRepository.save(Matching.fromDto(matchingDetailRequestDto, siteUser));
        saveApplyForOrganizer(matching, siteUser);
        return matching;
    }

    private void saveApplyForOrganizer(Matching matching, SiteUser siteUser) {
        var applyDto = ApplyDto.builder()
                .matching(matching)
                .siteUser(siteUser)
                .build();
        Apply apply = applyRepository.save(Apply.fromDto(applyDto));
        apply.changeApplyStatus(ApplyStatus.ACCEPTED);
    }

    @Override
    @Transactional
    public Matching update(String email, Long matchingId, MatchingDetailRequestDto matchingDetailRequestDto) {
        var siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));

        var matching = findEntity.findMatching(matchingId);

        var confirmedApplies
                = applyRepository.findAllByMatching_IdAndApplyStatus(matchingId, ApplyStatus.ACCEPTED).get();

        validateOrganizer(matchingId, siteUser);

        // 주소 다르다면 위경도 업데이트
        updateLatAndLon(matchingDetailRequestDto, matching);

        sendNotificationToApplyUser(matchingId, siteUser, matching, NotificationType.MODIFY_MATCHING);
        penalizeToOrganizer(confirmedApplies, siteUser, PenaltyType.MATCHING_MODIFY);
        confirmedApplies.forEach(apply -> {
            if (!apply.getSiteUser().equals(siteUser)) {
                apply.changeApplyStatus(ApplyStatus.PENDING);
            }
        });

        matching.update(Matching.fromDto(matchingDetailRequestDto, siteUser));
        return matching;
    }

    private void updateLatAndLon(MatchingDetailRequestDto matchingDetailRequestDto, Matching matching) {
        if(!matchingDetailRequestDto.getLocation().equals(matching.getLocation())){
            List<Double> latAndLon = getLatAndLon(getUserAddressInfo(matchingDetailRequestDto.getLocation()));
            matchingDetailRequestDto.setLat(latAndLon.get(0));
            matchingDetailRequestDto.setLon(latAndLon.get(1));
        }
    }

    private void penalizeToOrganizer(List<Apply> confirmedApplies, SiteUser siteUser, PenaltyType penaltyType) {
        if (confirmedApplies.size() >= 2) {
            siteUser.penalize(penaltyType);
        }
    }

    private void sendNotificationToApplyUser(Long matchingId, SiteUser siteUser, Matching matching,
                                             NotificationType notificationType) {
        var applies = applyRepository.findAllByMatching_Id(matchingId).get();

        applies.forEach(apply -> {
            if (!apply.getSiteUser().equals(siteUser)) {
                notificationService.createAndSendNotification(apply.getSiteUser(), matching, notificationType);
            }
        });
    }

    @Override
    public void delete(String email, Long matchingId) {
        SiteUser siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(USER_NOT_FOUND));

        Matching matching = findEntity.findMatching(matchingId);

        var confirmedApplies
                = applyRepository.findAllByMatching_IdAndApplyStatus(matchingId, ApplyStatus.ACCEPTED).get();

        validateOrganizer(matchingId, siteUser);

        if (!matching.getRecruitStatus().equals(RecruitStatus.WEATHER_ISSUE)) { // 우천으로 인한 취소가 아니면 패널티 적용
            penalizeToOrganizer(confirmedApplies, siteUser, PenaltyType.MATCHING_DELETE);
        }

        sendNotificationToApplyUser(matchingId, siteUser, matching, NotificationType.DELETE_MATCHING);
        confirmedApplies.forEach(apply -> applyRepository.delete(apply));

        matchingRepository.delete(matching);
    }

    private void validateOrganizer(Long matchingId, SiteUser siteUser) {
        if (!isUserMadeThisMatching(matchingId, siteUser)) {
            throw new RacketPuncherException(PERMISSION_DENIED_TO_EDIT_AND_DELETE_MATCHING);
        }
    }

    @Override
    public Page<MatchingPreviewDto> findFilteredMatching(FilterRequestDto filterRequestDto, Pageable pageable) {
        // 필터링 없으면 정렬만 하고 반환
        if (checkFilterEmpty(filterRequestDto)) {
            return matchingRepository.findByRecruitStatusAndRecruitDueDateTimeAfter(RecruitStatus.OPEN, LocalDateTime.now(), pageable)
                    .map(MatchingPreviewDto::fromEntity);
        }

        // 필터링 있으면 필터링 후 반환
        return matchingRepository.searchWithFilter(filterRequestDto, pageable)
                .map(MatchingPreviewDto::fromEntity);
    }

    private boolean checkFilterEmpty(FilterRequestDto filterRequestDto) {
        // location
        if (filterRequestDto.getLocation().getLat() == 0
                && filterRequestDto.getLocation().getLon() == 0
                && filterRequestDto.getFilters().getDate().length() == 0
                && filterRequestDto.getFilters().getRegions().size() == 0
                && filterRequestDto.getFilters().getMatchingTypes().size() == 0
                && filterRequestDto.getFilters().getAgeGroups().size() == 0
                && filterRequestDto.getFilters().getNtrps().size() == 0
        ) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Page<MatchingPreviewDto> findCloseMatching(LocationDto locationDto, Double distance, Pageable pageable) {
        Double x = locationDto.getLat();
        Double y = locationDto.getLon();
        LocationDto northEast = GeometryUtil.calculate(x, y, distance / 2, 45.0);
        LocationDto southWest = GeometryUtil.calculate(x, y, distance / 2, 225.0);
        return matchingRepository.searchWithin(locationDto, northEast, southWest, pageable)
                .map(MatchingPreviewDto::fromEntity);
    }

    private String getUserAddressInfo(String address) {
        WebClient webClient = WebClient.builder()
                .baseUrl("https://dapi.kakao.com/v2/local/search/address")
                .defaultHeader("Authorization", apiKey)
                .build();

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", address)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private List<Double> getLatAndLon(String address) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(address);
        } catch (ParseException e) {
            throw new RacketPuncherException(JSON_PARSING_FAILED);
        }

        JSONArray documents = (JSONArray) jsonObject.get("documents");
        JSONObject firstDocument = (JSONObject) documents.get(0);
        double lon = Double.parseDouble((String) firstDocument.get("x")); // 경도
        double lat = Double.parseDouble((String) firstDocument.get("y")); // 위도

        return List.of(lat, lon);
    }

    @Override
    public MatchingDetailResponseDto getDetail(Long matchingId) {
        Matching matching = findEntity.findMatching(matchingId);
        return MatchingDetailResponseDto.fromEntity(matching);
    }

    private boolean isUserMadeThisMatching(Long matchingId, SiteUser siteUser) {
        return matchingRepository.existsByIdAndSiteUser(matchingId, siteUser);
    }

    @Override
    public ApplyContents getApplyContents(String email, long matchingId) {
        var siteUser = siteUserRepository.findByEmail(email)
                .orElseThrow(() -> new RacketPuncherException(EMAIL_NOT_FOUND));
        var matching = findEntity.findMatching(matchingId);
        var recruitNum = matching.getRecruitNum();
        var confirmedNum = matching.getConfirmedNum();
        var applyNum = applyRepository.countByMatching_IdAndApplyStatus(matchingId, ApplyStatus.PENDING).get();

        var appliedMembers = findAppliedMembers(matchingId);
        var confirmedMembers = findConfirmedMembers(matchingId);

        if (isOrganizer(siteUser.getId(), matching)) {
            var applyContentsForOrganizer = ApplyContents.builder()
                    .applyNum(applyNum)
                    .recruitNum(recruitNum)
                    .confirmedNum(confirmedNum)
                    .appliedMembers(appliedMembers)
                    .confirmedMembers(confirmedMembers)
                    .build();

            return applyContentsForOrganizer;
        }

        var applyContentsForUser = ApplyContents.builder()
                .recruitNum(recruitNum)
                .confirmedNum(confirmedNum)
                .confirmedMembers(confirmedMembers)
                .build();

        return applyContentsForUser;
    }

    private List<ApplyMember> findConfirmedMembers(long matchingId) {
        return applyRepository.findAllByMatching_IdAndApplyStatus(matchingId, ApplyStatus.ACCEPTED)
                .orElse(Collections.emptyList())
                .stream().map((apply)
                        -> ApplyMember.builder()
                        .applyId(apply.getId())
                        .siteUserId(apply.getSiteUser().getId())
                        .nickname(apply.getSiteUser().getNickname())
                        .build()).collect(Collectors.toList());
    }

    private List<ApplyMember> findAppliedMembers(long matchingId) {
        return applyRepository.findAllByMatching_IdAndApplyStatus(matchingId, ApplyStatus.PENDING)
                .orElse(Collections.emptyList())
                .stream().map((apply)
                        -> ApplyMember.builder()
                        .applyId(apply.getId())
                        .siteUserId(apply.getSiteUser().getId())
                        .nickname(apply.getSiteUser().getNickname())
                        .build()).collect(Collectors.toList());
    }
}