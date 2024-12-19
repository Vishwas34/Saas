package com.saas.service;



import org.springframework.beans.factory.annotation.Autowired;

import com.saas.entity.Service;
import com.saas.entity.User;
import com.saas.repo.ServiceRepository;
import com.saas.repo.UserRepository;

import java.util.List;

@org.springframework.stereotype.Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<Service> getAllServices() {
        return serviceRepository.findAll();
    }

    public void addService(Service service) {
        serviceRepository.save(service);
    }

    public void updateService(Service service) {
        serviceRepository.save(service);
    }

    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }
    public void addUser(User user) {
        userRepository.save(user); // Saves the user to the database
    }
}
