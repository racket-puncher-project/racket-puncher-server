package com.example.demo.entity;

import com.example.demo.siteuser.dto.ProcessedReviewDto;
import com.example.demo.type.NegativeReviewType;
import com.example.demo.type.PositiveReviewType;
import com.example.demo.util.converter.NegativeReviewsConverter;
import com.example.demo.util.converter.PositiveReviewsConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
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
@EntityListeners(AuditingEntityListener.class)
@DynamicInsert
@DynamicUpdate
@Entity
@Table(name = "REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MATCHING_ID", nullable = false)
    private Matching matching;

    @ManyToOne
    @JoinColumn(name = "OBJECT_USER_ID", nullable = false)
    private SiteUser objectUser;

    @ManyToOne
    @JoinColumn(name = "SUBJECT_USER_ID", nullable = false)
    private SiteUser subjectUser;

    @Column(name = "SCORE", nullable = false)
    private Integer score;

    @CreatedDate
    @Column(name = "CREATE_TIME")
    private LocalDateTime createTime;

    @Column(name = "POSITIVE_REVIEWS", columnDefinition = "json")
    @Convert(converter = PositiveReviewsConverter.class)
    private List<PositiveReviewType> positiveReviews;

    @Column(name = "NEGATIVE_REVIEWS", columnDefinition = "json")
    @Convert(converter = NegativeReviewsConverter.class)
    private List<NegativeReviewType> negativeReviews;

    public static Review fromDto(ProcessedReviewDto processedReviewDto) {
        return Review.builder()
                .matching(processedReviewDto.getMatching())
                .objectUser(processedReviewDto.getObjectUser())
                .subjectUser(processedReviewDto.getSubjectUser())
                .positiveReviews(processedReviewDto.getPositiveReviews())
                .negativeReviews(processedReviewDto.getNegativeReviews())
                .score(processedReviewDto.getScore())
                .build();
    }
}