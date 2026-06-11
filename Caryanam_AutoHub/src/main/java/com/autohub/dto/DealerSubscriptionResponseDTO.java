package com.autohub.dto;

import com.autohub.enums.SubscriptionPlan;
import lombok.Data;

@Data
public class DealerSubscriptionResponseDTO {

    private Long Id;

    private String dealerName;

    private String dealerId;

    private Boolean subscriptionActive;

    private SubscriptionPlan subscriptionPlan;


}
