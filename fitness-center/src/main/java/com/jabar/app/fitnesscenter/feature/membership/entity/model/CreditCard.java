package com.jabar.app.fitnesscenter.feature.membership.entity.model;

import com.jabar.app.fitnesscenter.feature.util.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "credit_card")
public class CreditCard extends BaseEntity<String> {
    private String placeholder;
    private String cardNumber;
    private Date expiredDate;
    private Integer cvv;

    @OneToOne
    @MapsId
    private Member member;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CreditCard that = (CreditCard) o;
        return Objects.equals(placeholder, that.placeholder)
               && Objects.equals(cardNumber, that.cardNumber)
               && Objects.equals(expiredDate, that.expiredDate)
               && Objects.equals(cvv, that.cvv);
    }

    @Override
    public void initialize() {
        if (this.getId() == null) {
            setId(UUID.randomUUID().toString());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), placeholder, cardNumber, expiredDate, cvv);
    }
}