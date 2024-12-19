package com.saas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.saas.entity.Service;
import com.saas.entity.User;
import com.saas.service.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return adminService.getAllUsers();
    }

    @DeleteMapping("/user/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
    }
  
    @PostMapping("/user")
    public void addUser(@RequestBody User user) {
        adminService.addUser(user);
    }
 
    @GetMapping("/services")
    public List<Service> getAllServices() {
        return adminService.getAllServices();
    }

    @PostMapping("/service")
    public void addService(@RequestBody Service service) {
        adminService.addService(service);
    }

    @PutMapping("/service")
    public void updateService(@RequestBody Service service) {
        adminService.updateService(service);
    }

    @DeleteMapping("/service/{serviceId}")
    public void deleteService(@PathVariable Long serviceId) {
        adminService.deleteService(serviceId);
    }
}
