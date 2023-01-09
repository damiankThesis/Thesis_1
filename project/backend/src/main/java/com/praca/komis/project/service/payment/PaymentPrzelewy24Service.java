package com.praca.komis.project.service.payment;

import com.praca.komis.project.configuration.PaymentPrzelewy24Config;
import com.praca.komis.project.model.Rent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentPrzelewy24Service {

    @Autowired
    private final PaymentPrzelewy24Config config;

    public String initPayment(Rent rent) {
        log.info("Init payment");
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(config.getPosId().toString(),
                        config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey()))
                .baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();

        ResponseEntity<PaymentPrzelewy24Response> res = webClient.post()
                .uri("/transaction/register")
                .bodyValue(PaymentPrzelewy24DTO.builder()
                        .merchantId(config.getMerchantId())
                        .posId(config.getPosId())
                        .sessionId(createSessionId(rent))
                        .amount(rent.getTotalCost().movePointRight(2).intValue())
                        .currency("PLN")
                        .description("Wynajem nr" + rent.getId())
                        .email(rent.getUser().getUsername())
                        .client(rent.getUser().getName() + " " + rent.getUser().getSurname())
                        .country("PL")
                        .language("pl")
                        .urlReturn(config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn())
                        .sign(createSign(rent))
                        .encoding("UTF-8")
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError,
                        clientResponse -> {
                            log.error("Sth gone wrong: " + clientResponse.statusCode().name());
                            return Mono.empty();
                        })
                .toEntity(PaymentPrzelewy24Response.class)
                .block();

        if (res != null && res.getBody() != null && res.getBody().getData() != null)
            return (config.isTestMode()? config.getTestUrl() : config.getUrl()) + "/trnRequest/"
                    + res.getBody().getData().getToken();

        return null;
        //https://sandbox.przelewy24.pl/api/v1/transaction/register
        //https://secure.przelewy24.pl/trnRequest/{TOKEN}
    }

    private String createSign(Rent rent) {
        String val  = "{\"sessionId\":\""+ createSessionId(rent) +
                "\",\"merchantId\":"+ config.getMerchantId() +
                ",\"amount\":"+ rent.getTotalCost().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\""+ (config.isTestMode() ? config.getTestCrc(): config.getCrc())+"\"}";
        return DigestUtils.sha384Hex(val);
    }

    private String createSessionId(Rent rent) {
        return "rent_id_" + rent.getId().toString();
    }
}
