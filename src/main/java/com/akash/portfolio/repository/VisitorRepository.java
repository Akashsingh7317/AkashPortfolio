package com.akash.portfolio.repository;

import com.akash.portfolio.entity.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Long> {

    Optional<Visitor> findBySessionId(String sessionId);

    long countByActiveSessionTrue();

}