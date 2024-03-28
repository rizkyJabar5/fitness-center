package com.jabar.app.fitnesscenter.feature.subscription.controller;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.CanceledSubscriptionRequest;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.MemberSubscribedRequest;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.SubscriptionRequest;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.SubscriptionResponse;
import com.jabar.app.fitnesscenter.feature.subscription.service.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jabar.app.fitnesscenter.app.constant.ApiUrlConstant.SUBSCRIPTION_URL;

@RequestMapping(SUBSCRIPTION_URL)
@RequiredArgsConstructor
@RestController
public class SubscriptionController {

    private final SubscriptionService service;

    @GetMapping
    public BaseResponse<List<SubscriptionResponse>> getAllSubscription() {
        return service.findAllSubscription();
    }

    @PostMapping
    public BaseResponse<SubscriptionResponse> createSubscriptionMember(
            @Valid @RequestBody SubscriptionRequest request) {
        return service.createSubscriptionMember(request);
    }

    @GetMapping("/{subscriptionId}/payment-status")
    public BaseResponse<Void> memberSubscribed(@PathVariable String subscriptionId,
                                               @RequestBody MemberSubscribedRequest request) {
        return service.memberSubscribed(request.paymentStatus(), subscriptionId);
    }

    @PostMapping("member/{memberId}/cancel")
    public BaseResponse<String> memberCancelSubscribed(@PathVariable String memberId,
                                                       @RequestBody CanceledSubscriptionRequest request) {
        return service.memberCancelSubscribed(memberId, request);
    }
}
