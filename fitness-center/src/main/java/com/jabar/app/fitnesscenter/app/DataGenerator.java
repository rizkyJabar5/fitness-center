package com.jabar.app.fitnesscenter.app;

import com.jabar.app.fitnesscenter.feature.membership.entity.model.CreditCard;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.StatusMembership;
import com.jabar.app.fitnesscenter.feature.membership.entity.repositories.MemberRepository;
import com.jabar.app.fitnesscenter.feature.user.entity.model.AppUser;
import com.jabar.app.fitnesscenter.feature.user.entity.model.ERole;
import com.jabar.app.fitnesscenter.feature.user.entity.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class DataGenerator {

    @Bean
    @Transactional
    public CommandLineRunner loadData(UserRepository userRepository,
                                      MemberRepository memberRepository,
                                      PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("Loading data...");
            log.info("Generate users");
            addDefaultUser(userRepository, memberRepository, passwordEncoder);
        };
    }

    /**
     * For creating a default user with role super admin
     */
    private void addDefaultUser(UserRepository userRepository,
                                MemberRepository memberRepository,
                                PasswordEncoder passwordEncoder) {
        try {
            String email = "user@email.com";
            boolean exists = memberRepository.existsByEmail(email);
            if (!exists) {
                Member member = new Member();
                AppUser user = new AppUser();
                member.setId(UUID.randomUUID().toString());
                member.setCreatedAt(new Date());
                member.setEmail(email);
                member.setName("Admin");
                member.setStatusMembership(StatusMembership.REGISTERED);
                member.setPhoneNumber("08934823432");
                user.setERole(ERole.USER);
                user.setIsCredentialsNonExpired(true);
                user.setUserId(UUID.randomUUID().toString());
                user.setPassword(passwordEncoder.encode("admin"));
                user.setIsEnabled(true);
                user.setMember(member);
                userRepository.save(user);
                member.setAppUser(user);
                CreditCard creditCard = new CreditCard();
                creditCard.setPlaceholder("Admin");
                creditCard.setExpiredDate(new Date());
                creditCard.setCardNumber("9090897367549087");
                creditCard.setCvv(920);
                creditCard.setMember(member);
                member.setCreditCard(creditCard);
                memberRepository.save(member);
                log.info("Successfully created default user {}", user.getUsername());
            }
            log.info("{} user found.", email);
        } catch (Exception e) {
            throw new DataIntegrityViolationException("Cannot create user " + e.getMessage());
        }
    }
}