package com.cms.contraband.service.model;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContrabandRepository extends JpaRepository<Contraband, Long> {
    Optional<Contraband> findByContrabandCode(String contrabandCode);
}
