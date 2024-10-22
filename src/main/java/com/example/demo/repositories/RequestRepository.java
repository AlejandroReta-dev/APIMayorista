package com.example.demo.repositories;

import com.example.demo.models.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRetailerIdAndWholesalerId(Long retailerId, Long wholesalerId);
}

