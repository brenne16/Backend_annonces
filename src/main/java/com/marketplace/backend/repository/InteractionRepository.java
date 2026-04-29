package com.marketplace.backend.repository;

import com.marketplace.backend.entity.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface InteractionRepository extends JpaRepository<Interaction, UUID> {
}