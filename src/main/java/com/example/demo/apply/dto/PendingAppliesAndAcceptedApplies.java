package com.example.demo.apply.dto;

import java.util.List;
import lombok.Data;

@Data
public class PendingAppliesAndAcceptedApplies {
    List<Long> pendingApplies;
    List<Long> acceptedApplies;
}
