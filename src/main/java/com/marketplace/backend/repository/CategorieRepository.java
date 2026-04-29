package com.marketplace.backend.repository;

import com.marketplace.backend.entity.Categori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategorieRepository extends JpaRepository<Categori, UUID> {
    Optional<Categori> findBySlug(String slug);
    List<Categori> findByParentIsNull();
    List<Categori> findByActifTrue();
}