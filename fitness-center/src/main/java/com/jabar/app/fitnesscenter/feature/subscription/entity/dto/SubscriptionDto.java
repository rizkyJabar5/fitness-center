package com.jabar.app.fitnesscenter.feature.subscription.entity.dto;

import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MemberDto;
import com.jabar.app.fitnesscenter.feature.subscription.entity.model.Subscription;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for {@link Subscription}
 */
public record SubscriptionDto(
        String id,
        Date createdAt,
        Date lastModifiedDate,
        MemberDto member,
        Boolean isSubscribe,
        Integer remainingMeeting) implements Serializable {
}