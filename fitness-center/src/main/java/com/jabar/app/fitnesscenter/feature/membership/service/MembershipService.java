package com.jabar.app.fitnesscenter.feature.membership.service;

import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MemberDto;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MembershipDetailsRequest;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.UpdateMemberDetailRequest;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.subscription.entity.model.Subscription;
import com.jabar.app.fitnesscenter.feature.user.entity.dto.PasswordRequest;

import java.util.List;

public interface MembershipService {
    BaseResponse<List<MemberDto>> findAllMember();

    Member getMemberById(String id);

    Subscription findSubscriptionProgram(String memberId, String programId);

    BaseResponse<MemberDto> registration(MembershipDetailsRequest request);

    BaseResponse<MemberDto> updateMember(String id, UpdateMemberDetailRequest request);

    BaseResponse<String> checkStatusMemberRegistration(String email);

    BaseResponse<Void> changePassword(String id, PasswordRequest request);
}
