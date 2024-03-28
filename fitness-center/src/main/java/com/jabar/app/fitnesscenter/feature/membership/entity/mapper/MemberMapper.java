package com.jabar.app.fitnesscenter.feature.membership.entity.mapper;

import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MemberDto;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MembershipDetailsRequest;

public interface MemberMapper {
    Member toEntity(MembershipDetailsRequest membershipDetailsRequest);

    MemberDto toDto(Member creditCard);
}