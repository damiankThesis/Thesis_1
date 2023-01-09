package com.praca.komis.project.service.payment;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentPrzelewy24DTO {
    private Integer merchantId;
    private Integer posId;
    private String sessionId;
    private Integer amount;
    private String currency;
    private String description;
    private String email;
    private String country;
    private String language;
    private String urlReturn;
    private String urlStatus;
    private String sign;
    private String client;
    private String encoding;
}
