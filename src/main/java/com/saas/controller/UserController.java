package com.saas.controller;

import com.saas.entity.Service;
import com.saas.entity.User;
import com.saas.entity.UserSubscription;
import com.saas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        User user = userService.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            return ResponseEntity.ok("Login successful. Welcome " + user.getName() + "!");
        } else {
            return ResponseEntity.status(401).body("Invalid email or password. Please try again.");
        }
    }
    /**
     * Register a new user and assign a 2-day demo subscription.
     */
    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    /**
     * Renew user subscription by providing user ID, service ID, and duration.
     */
    @PostMapping("/renew/{userId}/{serviceId}/{durationInDays}")
    public UserSubscription renewSubscription(
            @PathVariable Long userId,
            @PathVariable Long serviceId,
            @PathVariable int durationInDays) {
        return userService.renewSubscription(userId, serviceId, durationInDays);
    }

    /**
     * Get all available services.
     */
    @GetMapping("/services")
    public List<Service> getAllServices() {
        return userService.getAllServices();
    }

    /**
     * Fetch all users.
     */
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /**
     * Delete a specific user by ID.
     */
    @DeleteMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return "User with ID " + userId + " deleted successfully.";
    }

    /**
     * Check and update subscription status.
     */
    @PutMapping("/update-subscription-status")
    public String updateSubscriptionStatus() {
        userService.updateSubscriptionStatus();
        return "Subscription statuses updated successfully.";
    }
    
    /**
     * Fetch subscription status for a specific user.
     */
    @GetMapping("/{userId}/subscription-status")
    public ResponseEntity<?> getUserSubscriptionStatus(@PathVariable Long userId) {
        List<UserSubscription> subscriptions = userService.getAllSubscriptionsByUserId(userId);  // Fetch all subscriptions
        if (subscriptions != null && !subscriptions.isEmpty()) {
            return ResponseEntity.ok(subscriptions);
        } else {
            return ResponseEntity.status(404).body("No subscriptions found for user ID: " + userId);
        }
    }
    
    @GetMapping("/{userId}/subscription-expiration")
    public ResponseEntity<?> checkSubscriptionExpiration(@PathVariable Long userId) {
        UserSubscription subscription = userService.getActiveSubscription(userId);
        if (subscription != null) {
            return ResponseEntity.ok("Subscription expires on: " + subscription.getEndDate());
        } else {
            return ResponseEntity.status(404).body("No active subscription found for user ID: " + userId);
        }
    }
}
