package com.example.demo.matching.repository.filtering;

import com.example.demo.entity.Matching;
import com.example.demo.matching.dto.FilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomRepositoryForFiltering {
    Page<Matching> findAllByFilter(FilterDto filterDto, Pageable pageable);
}