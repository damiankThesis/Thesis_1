package com.praca.komis.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.praca.komis.project.dto.BuyDTO;
import com.praca.komis.project.model.Buy;
import com.praca.komis.project.model.Car;
import com.praca.komis.project.model.User;
import com.praca.komis.project.repository.BuyRepository;
import com.praca.komis.project.security.service.CarUserDetailsService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BuyControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BuyRepository buyRepository;

    @InjectMocks
    private CarUserDetailsService carUserDetailsService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    @SneakyThrows
    void BuyController_findAllBuy_ReturnOK()  {
        given(buyRepository.findAll()).willReturn(List.of(Buy.builder().build()));
        mockMvc.perform(get("/api/v1/buy").accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    void BuyController_findBuyById_ReturnOK() throws Exception {
        Buy buy = initBuy();
        given(buyRepository.findById(any())).willReturn(Optional.of(buy));
        mockMvc.perform(RestDocumentationRequestBuilders
                        .get("/api/v1/buy/{id}", Math.abs(new Random().nextLong()))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(buy.getId().intValue())))
                .andExpect(jsonPath("$.description", is(buy.getDescription())))
                .andExpect(jsonPath("$.totalBuyCost", is(buy.getTotalBuyCost().intValue())))
                .andExpect(jsonPath("$.user.id", is(buy.getUser().getId().intValue())))
                .andExpect(jsonPath("$.user.username", is(buy.getUser().getUsername())))
                .andExpect(jsonPath("$.user.name", is(buy.getUser().getName())))
                .andExpect(jsonPath("$.user.surname", is(buy.getUser().getSurname())))
                .andExpect(jsonPath("$.car.id", is(buy.getCar().getId().intValue())))
                .andExpect(jsonPath("$.car.brand", is(buy.getCar().getBrand())))
                .andExpect(jsonPath("$.car.model", is(buy.getCar().getModel())));
    }

    @Test
    @SneakyThrows
    void BuyController_noAddBuy_ReturnBadRequest() {
        given(buyRepository.save(any())).willReturn(Buy.builder().build());
        mockMvc.perform(post("/api/v1/buy")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(null)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(value = "admin", roles = {"ADMIN"})
    @SneakyThrows
    void BuyController_deleteExistingBuy_ReturnNotFound()  {
        given(buyRepository.existsById(any())).willReturn(true);
        mockMvc.perform(delete("/api/v1/buy/{id}", Math.abs(new Random().nextInt()))
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
    }

    private Buy initBuy() {
        return Buy.builder()
                .id(1L)
                .meetDate(LocalDateTime.now().plusDays(1))
                .reqDate(LocalDateTime.now())
                .description("description")
                .totalBuyCost(BigDecimal.valueOf(123000))
                .user(User.builder()
                        .id(2L)
                        .password("passw0rd!")
                        .username("username@username.com")
                        .name("name")
                        .surname("surname")
                        .enabled(true)
                        .build())
                .car(Car.builder()
                        .id(1L)
                        .brand("brand")
                        .model("model")
                        .build())
                .build();
    }

    private BuyDTO initBuyDTO() {
        return BuyDTO.builder()
                .meetDate(LocalDateTime.now().plusDays(1))
                .description("description")
                .totalBuyCost(BigDecimal.valueOf(123000))
                .user(1L)
                .car(1L)
                .build();
    }

}
