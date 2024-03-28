package com.jabar.app.fitnesscenter.feature.membership.entity.mapper;

import com.jabar.app.fitnesscenter.app.util.DateConverter;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.CreditCard;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.membership.entity.dto.MembershipDetailsRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

import static com.jabar.app.fitnesscenter.app.util.DateConverter.*;

@Component
public class CreditCardMapperImpl implements CreditCardMapper {
    @Override
    public CreditCard toEntity(MembershipDetailsRequest.CreditCardDto creditCardDto, Member member) {
        Date date = parseToDate(creditCardDto.expiredDate(), MONTH_YEARS_FORMAT);

        CreditCard creditCard = new CreditCard();
        creditCard.setCreatedAt(new Date(System.currentTimeMillis()));
        creditCard.setPlaceholder(creditCardDto.placeholder());
        creditCard.setCardNumber(creditCardDto.cardNumber());
        creditCard.setExpiredDate(date);
        creditCard.setCvv(Integer.parseInt(creditCardDto.cvv()));
        creditCard.setMember(member);

        return creditCard;
    }

    @Override
    public MembershipDetailsRequest.CreditCardDto toDto(CreditCard creditCard) {
        LocalDateTime dateTime = DateConverter.toLocalDateTime(creditCard.getExpiredDate());
        String timeFormatter = DateConverter.formatDate(MONTH_YEARS_FORMAT, dateTime);

        return MembershipDetailsRequest.CreditCardDto.builder()
                .placeholder(creditCard.getPlaceholder())
                .cardNumber(creditCard.getCardNumber())
                .expiredDate(timeFormatter)
                .cvv(String.valueOf(creditCard.getCvv()))
                .build();
    }
}
