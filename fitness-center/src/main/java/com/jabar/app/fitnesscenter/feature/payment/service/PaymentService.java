package com.jabar.app.fitnesscenter.feature.payment.service;


import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.payment.entity.dto.PaymentDto;
import com.jabar.app.fitnesscenter.feature.payment.entity.dto.PaymentRequest;
import com.jabar.app.fitnesscenter.feature.payment.entity.model.Payment;
import com.jabar.app.fitnesscenter.feature.subscription.entity.model.Subscription;

import java.math.BigDecimal;

public interface PaymentService {

    Payment findPaymentId(String id);
    Payment addPayment(BigDecimal amount, Subscription subscription);
    BaseResponse<PaymentDto> creditPayment(PaymentRequest request);
}
