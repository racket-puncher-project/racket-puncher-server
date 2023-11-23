package com.example.demo.openfeign.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Body {
    private String dataType;
    private Items items;
    private Integer pageNo;
    private Integer numOfRows;
    private Integer totalCount;

}
