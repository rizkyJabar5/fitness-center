package com.jabar.app.fitnesscenter.feature.subscription.entity.mapper;

import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.SubscriptionResponse;
import com.jabar.app.fitnesscenter.feature.subscription.entity.model.Subscription;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class SubscriptionMapper implements Function<Subscription, SubscriptionResponse> {

    @Override
    public SubscriptionResponse apply(Subscription subscription) {
//        Set<SubscriptionResponse.HealthProgramSubscription> healthPrograms = new HashSet<>();
//        subscription.getHealthPrograms()
//                .forEach(program -> {
//
//                    healthPrograms.add()
//                });
        return SubscriptionResponse.builder()
                .healthPrograms(null)

                .build();
    }
}
