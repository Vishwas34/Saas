package com.saas.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saas.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
