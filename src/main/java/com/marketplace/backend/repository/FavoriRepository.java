package com.marketplace.backend.repository;

import com.marketplace.backend.entity.Favori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FavoriRepository extends JpaRepository<Favori, UUID> {

    @Query("SELECT f FROM Favori f JOIN FETCH f.annonce a JOIN FETCH a.categorie JOIN FETCH a.user WHERE f.user.id = :userId ORDER BY f.createdAt DESC")
    List<Favori> findByUserIdAvecAnnonce(@Param("userId") UUID userId);

    @Query("SELECT f FROM Favori f WHERE f.user.id = :userId AND f.annonce.id = :annonceId")
    Optional<Favori> findByUserIdAndAnnonceId(@Param("userId") UUID userId, @Param("annonceId") UUID annonceId);

    boolean existsByUserIdAndAnnonceId(UUID userId, UUID annonceId);

    @Query("SELECT COUNT(f) FROM Favori f WHERE f.annonce.user.id = :userId")
    long countFavorisByVendeurId(@Param("userId") UUID userId);

    @Query("SELECT COUNT(f) FROM Favori f WHERE f.annonce.id = :annonceId")
    long countByAnnonceId(@Param("annonceId") UUID annonceId);
}