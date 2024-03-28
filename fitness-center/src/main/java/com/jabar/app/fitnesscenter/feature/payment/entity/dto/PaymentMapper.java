package com.jabar.app.fitnesscenter.feature.payment.entity.dto;

import com.jabar.app.fitnesscenter.app.util.DateConverter;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.payment.entity.model.Payment;
import com.jabar.app.fitnesscenter.feature.subscription.entity.model.Subscription;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.jabar.app.fitnesscenter.app.util.DateConverter.DATETIME_FORMAT;

@Component
public class PaymentMapper {
    public PaymentDto toDto(Payment payment) {
        LocalDateTime dateTime = DateConverter.toLocalDateTime(payment.getPaymentLogs().getPaymentDate());
        String date = DateConverter.formatDate(DATETIME_FORMAT, dateTime);

        Subscription subscription = payment.getSubscription();
        Member member = subscription.getMember();
        return PaymentDto.builder()
                .paymentId(payment.getId())
                .subscriptionId(subscription.getId())
                .memberId(member.getId())
                .memberName(member.getName())
                .paymentStatus(payment.getPaymentStatus().name())
                .paymentDate(date)
                .paymentAmount(payment.getAmount())
                .build();
    }
}
