package com.marketplace.backend.repository;

import com.marketplace.backend.entity.Image;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    List<Image> findByAnnonceId(UUID annonceId);
    @Query("SELECT i FROM Image i JOIN FETCH i.annonce a JOIN FETCH a.user WHERE i.id = :id")
    Optional<Image> findByIdAvecAnnonce(@Param("id") UUID id);
}