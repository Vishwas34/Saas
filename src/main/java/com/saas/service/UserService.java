package com.saas.service;

import com.saas.entity.Service;
import com.saas.entity.User;
import com.saas.entity.UserSubscription;
import com.saas.repo.ServiceRepository;
import com.saas.repo.UserRepository;
import com.saas.repo.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    /**
     * Register a new user and assign a 2-day free demo subscription.
     */
    public User registerUser(User user) {
        userRepository.save(user);
        assignDemoSubscription(user);
        return user;
    }

    /**
     * Assign a demo subscription (2 days) to the registered user.
     */
    @Transactional
    public void assignDemoSubscription(User user) {
        UserSubscription demoSubscription = new UserSubscription();
        demoSubscription.setUser(user);
        demoSubscription.setStartDate(LocalDateTime.now());
        demoSubscription.setEndDate(LocalDateTime.now().plusDays(2));
        demoSubscription.setStatus("ACTIVE");
        userSubscriptionRepository.save(demoSubscription);
    }

    /**
     * Allow users to renew their subscriptions with a selected service and duration.
     */
    @Transactional
    public UserSubscription renewSubscription(Long userId, Long serviceId, int durationInDays) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Service service = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found with ID: " + serviceId));

        // Check for existing active subscription
        Optional<UserSubscription> existingSubscription = userSubscriptionRepository.findActiveSubscription(userId, serviceId);

        if (existingSubscription.isPresent()) {
            throw new RuntimeException("User already has an active subscription for this service.");
        }

        // Create a new subscription
        UserSubscription newSubscription = new UserSubscription();
        newSubscription.setUser(user);
        
        newSubscription.setStartDate(LocalDateTime.now());
        newSubscription.setEndDate(LocalDateTime.now().plusDays(durationInDays));
        newSubscription.setStatus("ACTIVE");

        return userSubscriptionRepository.save(newSubscription);
    }


    /**
     * Fetch all users from the system.
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Fetch all services available for subscription.
     */
    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    /**
     * Add or update a service.
     */
    public Service saveOrUpdateService(Service service) {
        return serviceRepository.save(service);
    }

    /**
     * Delete a specific service by ID.
     */
    public void deleteService(Long serviceId) {
        if (serviceRepository.existsById(serviceId)) {
            serviceRepository.deleteById(serviceId);
        } else {
            throw new RuntimeException("Service not found with ID: " + serviceId);
        }
    }

    /**
     * Delete a user from the system.
     */
    public void deleteUser(Long userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
        } else {
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    /**
     * Fetch all subscriptions for administrative purposes.
     */
    public List<UserSubscription> getAllUserSubscriptions() {
        return userSubscriptionRepository.findAll();
    }

    /**
     * Check for expired subscriptions and update their status to 'INACTIVE'.
     */
    @Transactional
    public void updateSubscriptionStatus() {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (UserSubscription subscription : subscriptions) {
            if (subscription.getEndDate().isBefore(now) && subscription.getStatus().equals("ACTIVE")) {
                subscription.setStatus("INACTIVE");
                userSubscriptionRepository.save(subscription);
            }
        }
    }
    public User findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }
    
    public UserSubscription getSubscriptionStatusByUserId(Long userId) {
        // Fetch user and their subscription status
        return userSubscriptionRepository.findActiveSubscriptionByUserId(userId);
    }
    public List<UserSubscription> getAllSubscriptionsByUserId(Long userId) {
        return userSubscriptionRepository.findByUserId(userId);
    }
    
    public UserSubscription getActiveSubscription(Long userId) {
        List<UserSubscription> subscriptions = userSubscriptionRepository.findByUserId(userId);

        // Find the active subscription based on the expiration date
        for (UserSubscription subscription : subscriptions) {
            // Convert LocalDate to LocalDateTime for comparison
            if (subscription.getEndDate().isAfter(LocalDate.now().atStartOfDay())) {
                return subscription; // Return the first active subscription
            }
        }
        return null; // Return null if no active subscription is found
    } 
}