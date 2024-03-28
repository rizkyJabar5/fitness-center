package com.jabar.app.fitnesscenter.feature.payment.controller;


import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.payment.entity.dto.PaymentDto;
import com.jabar.app.fitnesscenter.feature.payment.entity.dto.PaymentRequest;
import com.jabar.app.fitnesscenter.feature.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jabar.app.fitnesscenter.app.constant.ApiUrlConstant.PAYMENT_URL;


@Tag(name = "Payment Endpoint",
        description = "Add new Payment for customer order")
@RequiredArgsConstructor
@RestController
@RequestMapping(PAYMENT_URL)
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Add payment",
            description = "Payment customer by order id")
    @PostMapping
    public ResponseEntity<BaseResponse<PaymentDto>> addPaymentSubscription(@Valid @RequestBody PaymentRequest request) {
        BaseResponse<PaymentDto> response = paymentService.creditPayment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
