package com.jabar.app.fitnesscenter.feature.user.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MembershipDetailsRequest;
import com.jabar.app.fitnesscenter.feature.membership.entity.repositories.MemberRepository;
import com.jabar.app.fitnesscenter.feature.user.entity.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.jabar.app.fitnesscenter.app.constant.ApiUrlConstant.AUTHENTICATION_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        userRepository.deleteAll();
//        MembershipDetailsRequest.CreditCardDto creditCardRequest = MembershipDetailsRequest.CreditCardDto
//                .builder()
//                .placeholder("Amirah Nicole")
//                .cardNumber("8796045672842245")
//                .expiredDate("12/28")
//                .cvv("877")
//                .build();
//        var request = MembershipDetailsRequest.builder()
//                .name("Testing")
//                .email("test@email.com")
//                .phoneNumber("0989385894")
//                .password("password")
//                .creditCard(creditCardRequest)
//                .build();
//        this.member = memberMapper.toEntity(request);
//
//        AppUser appUser = new AppUser();
    }
//
//    @AfterEach
//    void tearDown() {
//        memberRepository.deleteAll();
//        userRepository.deleteAll();
//    }

    @Test
    void successRegistration() throws Exception {
        var creditCardRequest = MembershipDetailsRequest.CreditCardDto
                .builder()
                .placeholder("Amirah Nicole")
                .cardNumber("8796045672842245")
                .expiredDate("12/28")
                .cvv("877")
                .build();
        var request = MembershipDetailsRequest.builder()
                .name("Testing")
                .email("test@email.com")
                .phoneNumber("0989385894")
                .password("password")
                .creditCard(creditCardRequest)
                .build();
//        BaseResponse<MemberDto> registration = membershipService.registration(request);
//        var responseEntity = authenticationController.registerMembership(request);
        mockMvc.perform(
                post(AUTHENTICATION_URL + "/signup")
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            var reference = new TypeReference<BaseResponse<Void>>() {};
            var baseResponse = objectMapper.readValue(result.getResponse().getContentAsString(), reference);
            assertThat(baseResponse)
                    .isNotNull();
//            assertThat(baseResponse).isEqualTo(registration);
        });
    }

    @Test
    void verifyEmail() {
    }

    @Test
    void forgetPassword() {
    }

    @Test
    void resetPassword() {
    }
}