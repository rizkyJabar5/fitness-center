package com.jabar.app.fitnesscenter.feature.membership.controller;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MemberDto;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.UpdateMemberDetailRequest;
import com.jabar.app.fitnesscenter.feature.membership.service.MembershipService;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.SubscriptionResponse;
import com.jabar.app.fitnesscenter.feature.subscription.service.SubscriptionService;
import com.jabar.app.fitnesscenter.feature.user.entity.dto.PasswordRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.jabar.app.fitnesscenter.app.constant.ApiUrlConstant.MEMBERSHIP_URL;

@RequiredArgsConstructor
@RequestMapping(MEMBERSHIP_URL)
@RestController
public class MembershipController {

    private final MembershipService membershipService;
    private final SubscriptionService subscriptionService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<MemberDto>>> getAllMembers() {
        var baseResponse = membershipService.findAllMember();
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/registration-status")
    public ResponseEntity<BaseResponse<String>> checkStatusMemberRegistration(@RequestParam String email) {
        var baseResponse = membershipService.checkStatusMemberRegistration(email);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{memberId}/subscriptions")
    public ResponseEntity<BaseResponse<List<SubscriptionResponse>>> getSubscriptionMember(@PathVariable String memberId) {
        var baseResponse = subscriptionService.findSubscriptionsMember(memberId);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{memberId}/subscriptions/{subscriptionId}")
    public ResponseEntity<BaseResponse<SubscriptionResponse>> reSubscribeMember(@PathVariable String memberId, @PathVariable String subscriptionId) {
        var baseResponse = subscriptionService.reSubscribeMember(memberId, subscriptionId);
        return ResponseEntity.ok(baseResponse);
    }

    @PostMapping("/{id}/change-password")
    public ResponseEntity<BaseResponse<Void>> changePassword(@PathVariable String id,
                                                             @Valid @RequestBody PasswordRequest request) {
        var baseResponse = membershipService.changePassword(id, request);
        return ResponseEntity.ok(baseResponse);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponse<MemberDto>> updateMember(@PathVariable String id,
                                                                @Valid @RequestBody UpdateMemberDetailRequest request) {
        var baseResponse = membershipService.updateMember(id, request);
        return ResponseEntity.ok(baseResponse);
    }
}
