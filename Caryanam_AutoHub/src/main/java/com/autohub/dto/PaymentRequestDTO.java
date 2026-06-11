package com.autohub.dto;

import com.autohub.enums.SubscriptionPlan;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    private Long dealerId;

    private SubscriptionPlan subscriptionPlan;
}
