package com.jabar.app.fitnesscenter.feature.payment.service;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.app.common.exceptions.AppRuntimeException;
import com.jabar.app.fitnesscenter.app.common.exceptions.ResourceNotFoundException;
import com.jabar.app.fitnesscenter.feature.payment.entity.PaymentStatus;
import com.jabar.app.fitnesscenter.feature.payment.entity.dto.PaymentDto;
import com.jabar.app.fitnesscenter.feature.payment.entity.dto.PaymentMapper;
import com.jabar.app.fitnesscenter.feature.payment.entity.dto.PaymentRequest;
import com.jabar.app.fitnesscenter.feature.payment.entity.model.Payment;
import com.jabar.app.fitnesscenter.feature.payment.entity.model.PaymentLogs;
import com.jabar.app.fitnesscenter.feature.payment.entity.repositories.PaymentsRepository;
import com.jabar.app.fitnesscenter.feature.subscription.entity.model.Subscription;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.NOT_FOUND_MSG;
import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.messageFormat;


@RequiredArgsConstructor
@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentsRepository repository;
    private final PaymentMapper paymentMapper;


    @Override
    public Payment findPaymentId(String id) {
        String message = messageFormat(NOT_FOUND_MSG, id);
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(message));
    }

    @Override
    public Payment addPayment(BigDecimal amount, Subscription subscription) {
        Payment entity = new Payment();
        PaymentLogs paymentLogs = new PaymentLogs();

        entity.setSubscription(subscription);
        entity.setPaymentDate(new Date(System.currentTimeMillis()));
        entity.setAmount(amount);

        if (amount.compareTo(BigDecimal.ZERO) > 0) {
            paymentLogs.setPaymentAmount(amount);
            entity.setPaymentLogs(paymentLogs);
            paymentLogs.setPaymentDate(new Date(System.currentTimeMillis()));
        }

        return repository.saveAndFlush(entity);
    }

    @Override
    public BaseResponse<PaymentDto> creditPayment(PaymentRequest request) {
        PaymentLogs paymentLogs = new PaymentLogs();
        Payment entity = Optional.ofNullable(repository.findPaymentByHealthyProgramId(request.healthyId()))
                .orElseThrow(() -> new ResourceNotFoundException(messageFormat(NOT_FOUND_MSG, request.healthyId())));

        PaymentStatus paymentStatus = entity.getPaymentStatus();

        if (paymentStatus.equals(PaymentStatus.PAYOFF)) {
            throw new AppRuntimeException(messageFormat("Order with id %s there is no bill", request.healthyId()));
        }

        BigDecimal result = request.paymentAmount().subtract(entity.getAmount());

        if (result.compareTo(BigDecimal.ZERO) == 0) {
            entity.setPaymentStatus(PaymentStatus.PAYOFF);
            entity.setAmount(request.paymentAmount());

            paymentLogs.setPaymentAmount(entity.getAmount());
            paymentLogs.setPaymentDate(new Date(System.currentTimeMillis()));

            entity.setPaymentLogs(paymentLogs);
        }

        repository.save(entity);
        PaymentDto mapper = paymentMapper.toDto(entity);
        return new BaseResponse<>(
                HttpStatus.CREATED.value(),
                String.format("Payment in order %s is successfully", request.healthyId()),
                mapper);
    }
}
