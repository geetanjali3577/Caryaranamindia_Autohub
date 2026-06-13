package com.autohub.enums;
public enum SubscriptionPlan {

    BASIC(999.0, 10, 3),
    STANDARD(2999.0, 20, 3),
    PREMIUM(4999.0, 100, 3);

    private final Double amount;
    private final Integer vehicleLimit;
    private final Integer validityMonths;

    SubscriptionPlan(Double amount, Integer vehicleLimit, Integer validityMonths) {
        this.amount = amount;
        this.vehicleLimit = vehicleLimit;
        this.validityMonths = validityMonths;
    }

    public Double getAmount() {
        return amount;
    }

    public Integer getVehicleLimit() {
        return vehicleLimit;
    }

    public Integer getValidityMonths() {
        return validityMonths;
    }
}