package com.jabar.app.fitnesscenter.feature.payment.service;

import com.jabar.app.fitnesscenter.feature.payment.entity.repositories.PaymentLogsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class PaymentLogServiceImpl implements PaymentLogService {

    private final PaymentLogsRepository repository;
    @Override
    public BigDecimal sumTotalAmountPayment() {

        return repository.sumAllPaymentAmount();
    }
}
