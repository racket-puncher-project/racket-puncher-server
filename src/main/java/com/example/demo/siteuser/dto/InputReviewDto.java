package com.example.demo.siteuser.dto;

import com.example.demo.type.NegativeReviewType;
import com.example.demo.type.PositiveReviewType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InputReviewDto {
    private long objectUserId;
    private List<PositiveReviewType> positiveReviews;
    private List<NegativeReviewType> negativeReviews;
}
