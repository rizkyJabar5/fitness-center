package com.jabar.app.fitnesscenter.feature.membership.entity.mapper;

import com.jabar.app.fitnesscenter.feature.membership.entity.model.CreditCard;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MembershipDetailsRequest;

public interface CreditCardMapper {
    CreditCard toEntity(MembershipDetailsRequest.CreditCardDto creditCardDto, Member member);

    MembershipDetailsRequest.CreditCardDto toDto(CreditCard creditCard);
}