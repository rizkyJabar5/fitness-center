package com.jabar.app.fitnesscenter.feature.membership.entity.repositories;

import com.jabar.app.fitnesscenter.feature.membership.entity.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardRepository extends JpaRepository<CreditCard, String> {
}