package com.jabar.app.fitnesscenter.feature.subscription.service;

import com.jabar.app.fitnesscenter.app.api.PaymentMessage;
import com.jabar.app.fitnesscenter.app.api.request.CreditCardRequest;
import com.jabar.app.fitnesscenter.app.common.BaseResponse;
import com.jabar.app.fitnesscenter.feature.fitness.entity.model.HealthProgram;
import com.jabar.app.fitnesscenter.feature.fitness.service.HealthyProgramService;
import com.jabar.app.fitnesscenter.feature.membership.entity.mapper.CreditCardMapper;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.CreditCard;
import com.jabar.app.fitnesscenter.feature.membership.entity.model.Member;
import com.jabar.app.fitnesscenter.feature.membership.service.MembershipService;
import com.jabar.app.fitnesscenter.feature.payment.entity.PaymentStatus;
import com.jabar.app.fitnesscenter.feature.subscription.entity.StatusSubscribe;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.CanceledSubscriptionRequest;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.SubscriptionRequest;
import com.jabar.app.fitnesscenter.feature.subscription.entity.dto.SubscriptionResponse;
import com.jabar.app.fitnesscenter.feature.subscription.entity.model.Subscription;
import com.jabar.app.fitnesscenter.feature.subscription.entity.repositories.SubscriptionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.concatenate;
import static com.jabar.app.fitnesscenter.app.constant.FitnessAppConstants.messageFormat;
import static com.jabar.app.fitnesscenter.feature.membership.util.Encryptor.encryptToAES;
import static com.jabar.app.fitnesscenter.feature.payment.entity.PaymentStatus.*;

@RequiredArgsConstructor
@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository repository;
    private final MembershipService membershipService;
    private final HealthyProgramService healthyProgramService;
    private final PaymentMessage paymentMessage;
    private final CreditCardMapper creditCardMapper;

    @Override
    public BaseResponse<List<SubscriptionResponse>> findAllSubscription() {
        List<SubscriptionResponse> responseList = repository.findAll()
                .stream().map(subscription -> {
                    var healthPrograms = mappingToHealthProgramSubscription(subscription);

                    Member member = subscription.getMember();
                    return SubscriptionResponse.builder()
                            .subscriptionId(subscription.getId())
                            .healthPrograms(healthPrograms)
                            .memberId(member.getId())
                            .name(member.getName())
                            .phoneNumber(member.getPhoneNumber())
                            .statusSubscribe(subscription.getStatusSubscribe().name())
                            .remainingMeeting(subscription.getRemainingMeeting())
                            .paymentStatus(subscription.getPaymentStatus().getName())
                            .build();
                })
                .toList();

        return new BaseResponse<>(responseList);
    }

    @Transactional
    @Override
    public BaseResponse<SubscriptionResponse> createSubscriptionMember(SubscriptionRequest request) {
        assertNotAlreadySubscriptionProgram(request.healthProgramId(), request.memberId());
        HealthProgram healthProgram = healthyProgramService.getHealthyProgramById(request.healthProgramId());
        Member member = membershipService.getMemberById(request.memberId());

        Subscription entity = new Subscription();
        entity.addHealthPrograms(healthProgram);
        entity.setCreatedAt(new Date());
        entity.setStatusSubscribe(StatusSubscribe.PENDING);
        entity.setMember(member);
        entity.setRemainingMeeting(healthProgram.getTotalMeetings());
        entity.setPaymentStatus(UNPAID);
        repository.save(entity);

        healthProgram.addSubscription(entity);
        healthyProgramService.updatePersistentProgramHealth(healthProgram);

        CreditCard creditCard = member.getCreditCard();

        String buildConcatCreditCard = concatenate("|",
                healthProgram.getPrice(),
                creditCard.getPlaceholder(),
                creditCard.getCardNumber(),
                creditCard.getExpiredDate(),
                creditCard.getCvv());

        String encryptedCreditCard = encryptToAES(buildConcatCreditCard);

        paymentMessage.sendPayment(new CreditCardRequest(encryptedCreditCard));


        return buildSubscriptionBaseResponse(request, member, entity);
    }

    @Override
    public BaseResponse<List<SubscriptionResponse>> findSubscriptionsMember(String memberId) {
        List<SubscriptionResponse> subscriptionList = repository.findByMemberId(memberId)
                .stream().map(subscription -> {
                    var healthPrograms = mappingToHealthProgramSubscription(subscription);
                    Member member = subscription.getMember();
                    return SubscriptionResponse.builder()
                            .subscriptionId(subscription.getId())
                            .healthPrograms(healthPrograms)
                            .memberId(member.getId())
                            .name(member.getName())
                            .phoneNumber(member.getPhoneNumber())
                            .statusSubscribe(subscription.getStatusSubscribe().name())
                            .remainingMeeting(subscription.getRemainingMeeting())
                            .paymentStatus(subscription.getPaymentStatus().getName())
                            .build();

                }).toList();
        return new BaseResponse<>(subscriptionList);
    }

    @Override
    public BaseResponse<SubscriptionResponse> reSubscribeMember(String memberId, String healthProgramId) {
        List<Subscription> persisted = repository.findSubscriptionHealthProgram(memberId, healthProgramId);

        return null;
    }

    @Override
    public BaseResponse<Void> memberSubscribed(String paymentStatus, String subscriptionId) {
        PaymentStatus status = valueOf(paymentStatus.toUpperCase());
        Subscription persisted = getSubscriptionById(subscriptionId);

        String paymentMessage = "";
        switch (status) {
            case PAYOFF -> {
                persisted.setPaymentStatus(PAYOFF);
                paymentMessage = messageFormat("Your ready to join with Us!");
            }
            case PENDING -> {
                persisted.setPaymentStatus(PENDING);
                paymentMessage = messageFormat("Your payment is {}", PENDING.getName());
            }
            case UNPAID -> {
                persisted.setPaymentStatus(UNPAID);
                paymentMessage = messageFormat("your subscription has not been paid");
            }
            case FAILED -> {
                persisted.setPaymentStatus(FAILED);
                paymentMessage = messageFormat("Payment {}", FAILED.getName());
            }
        }

        persisted.setStatusSubscribe(StatusSubscribe.SUBSCRIBE);
        repository.save(persisted);

        return new BaseResponse<>(
                HttpStatus.OK.value(),
                paymentMessage
        );
    }

    @Override
    public BaseResponse<String> memberCancelSubscribed(String memberId, CanceledSubscriptionRequest request) {
//        AppUser authenticatedUserDetails = SecurityUtils.getAuthenticatedUserDetails();
//        if (!authenticatedUserDetails.getMember().getId().equals(memberId)) {
//            throw new AppRuntimeException("Invalid! Not your session");
//        }

        Subscription persisted = getSubscriptionById(request.subscriptionId());
        persisted.getHealthPrograms()
                .removeIf(program -> program.getId().equals(request.healthProgramId()));
        persisted.setStatusSubscribe(StatusSubscribe.CANCELED);
        persisted.setRemainingMeeting(0);
        repository.save(persisted);

        return new BaseResponse<>("Success to cancel your subscription in program health " + request.healthProgramId());
    }

    private BaseResponse<SubscriptionResponse> buildSubscriptionBaseResponse(SubscriptionRequest request, Member member, Subscription entity) {
        var healthPrograms = mappingToHealthProgramSubscription(entity);

        var body = SubscriptionResponse.builder()
                .subscriptionId(entity.getId())
                .healthPrograms(healthPrograms)
                .memberId(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .statusSubscribe(entity.getStatusSubscribe().name())
                .remainingMeeting(entity.getRemainingMeeting())
                .paymentStatus(entity.getPaymentStatus().getName())
                .build();

        return new BaseResponse<>(
                HttpStatus.CREATED.value(),
                messageFormat("Successfully to subscribe new health program {}", request.healthProgramId()),
                body
        );
    }

    private Set<SubscriptionResponse.HealthProgramSubscription> mappingToHealthProgramSubscription(Subscription subscription) {
        return subscription.getHealthPrograms()
                .stream()
                .map(program -> {
                    var exercises = program.getProgramDetails()
                            .stream()
                            .map(exercise -> SubscriptionResponse.HealthProgramSubscription.Exercise.builder()
                                    .exercise(exercise.getExercise())
                                    .description(exercise.getDescription())
                                    .duration(concatenate(" ", exercise.getDuration(), "minutes"))
                                    .build())
                            .collect(Collectors.toSet());

                    return
                            SubscriptionResponse.HealthProgramSubscription.builder()
                                    .programId(program.getId())
                                    .programName(program.getProgramName())
                                    .price(program.getPrice())
                                    .schedule(program.getSchedule())
                                    .exercises(exercises)
                                    .build();

                })
                .collect(Collectors.toSet());
    }

    private Subscription getSubscriptionById(String subscriptionId) {
        return repository.findById(subscriptionId)
                .orElseThrow();
    }

    private void assertNotAlreadySubscriptionProgram(String healthProgramId, String memberId) {
        boolean existed = repository.existHealthPrograms(healthProgramId, memberId);
        if (existed) {
            throw new DataIntegrityViolationException("Already subscribed to health program " + healthProgramId);
        }
    }
}
