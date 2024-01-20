package com.example.demo.matching.repository.boundary;

import com.example.demo.entity.Matching;
import com.example.demo.matching.dto.LocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomRepositoryForBoundary {
    Page<Matching> findAllWithinBoundary(LocationDto center, LocationDto northEastBound, LocationDto southWestBound, Pageable pageable);
}
