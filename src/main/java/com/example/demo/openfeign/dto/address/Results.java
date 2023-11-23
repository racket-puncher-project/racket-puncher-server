package com.example.demo.openfeign.dto.address;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Results {
    private Common common;
    private List<Juso> juso;
}
