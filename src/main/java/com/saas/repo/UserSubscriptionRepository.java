package com.saas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saas.entity.UserSubscription;

import java.util.List;
import java.util.Optional;
public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long> {
    
    @Query("SELECT us FROM UserSubscription us WHERE us.user.id = :userId AND us.endDate >= CURRENT_DATE")
    UserSubscription findActiveSubscriptionByUserId(Long userId);
    List<UserSubscription> findByUserId(Long userId);
    @Query("SELECT us FROM UserSubscription us WHERE us.user.id = :userId AND us.service.id = :serviceId AND us.status = 'ACTIVE'")
    Optional<UserSubscription> findActiveSubscription(@Param("userId") Long userId, @Param("serviceId") Long serviceId);
}
    
