package com.autohub.enums;

public enum SubscriptionPlan {

    BASIC(999.0, 20),
    STANDARD(2999.0, 100),
    PREMIUM(4999.0, Integer.MAX_VALUE);

    private final Double amount;
    private final Integer vehicleLimit;

    SubscriptionPlan(Double amount,
                     Integer vehicleLimit) {

        this.amount = amount;
        this.vehicleLimit = vehicleLimit;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getVehicleLimit() {
        return vehicleLimit;
    }
}
