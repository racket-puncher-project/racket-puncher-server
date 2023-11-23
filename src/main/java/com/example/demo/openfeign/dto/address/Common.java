package com.example.demo.openfeign.dto.address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Common {
    private String errorMessage;
    private String countPerPage;
    private String totalCount;
    private String errorCode;
    private String currentPage;
}
