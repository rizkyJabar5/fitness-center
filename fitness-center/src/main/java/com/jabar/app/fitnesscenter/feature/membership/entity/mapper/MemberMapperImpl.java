package com.jabar.app.fitnesscenter.feature.membership.entity.mapper;

import com.jabar.app.fitnesscenter.feature.membership.entity.model.CreditCard;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.StatusMembership;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MemberDto;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MembershipDetailsRequest;
import com.jabar.app.fitnesscenter.feature.membership.util.Encryptor;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.concatenate;


@Component
public class MemberMapperImpl implements MemberMapper {
    private final CreditCardMapper creditCardMapper;

    public MemberMapperImpl(CreditCardMapper creditCardMapper) {
        this.creditCardMapper = creditCardMapper;
    }

    @Override
    public Member toEntity(MembershipDetailsRequest membershipDetailsRequest) {
        Member member = new Member();
        member.setName(membershipDetailsRequest.name());
        member.setEmail(membershipDetailsRequest.email());
        member.setPhoneNumber(membershipDetailsRequest.phoneNumber());
        member.setCreatedAt(new Date(System.currentTimeMillis()));
        CreditCard creditCard = creditCardMapper.toEntity(membershipDetailsRequest.creditCard(), member);
        member.setCreditCard(creditCard);
        member.setStatusMembership(StatusMembership.NOT_VALIDATED);

        return member;
    }

    @Override
    public MemberDto toDto(Member member) {
        CreditCard creditCard = member.getCreditCard();

        String buildConcatCreditCard = concatenate("|",
                creditCard.getPlaceholder(),
                creditCard.getCardNumber(),
                creditCard.getExpiredDate(),
                creditCard.getCvv());

        String encryptCreditCard = Encryptor.encryptToAES(buildConcatCreditCard);

        return MemberDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .creditCard(encryptCreditCard)
                .build();
    }
}
