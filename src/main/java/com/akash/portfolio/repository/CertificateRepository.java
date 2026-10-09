package com.akash.portfolio.repository;

import com.akash.portfolio.entity.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findByActiveTrueOrderByDisplayOrderAsc();

    List<Certificate> findByFeaturedTrueOrderByDisplayOrderAsc();

    long countByActiveTrue();

}