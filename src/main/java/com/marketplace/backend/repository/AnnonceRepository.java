package com.marketplace.backend.repository;

import com.marketplace.backend.entity.Annonce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AnnonceRepository extends JpaRepository<Annonce, UUID> {

    @Query(value = """
    SELECT a.* FROM annonces a
    WHERE a.statut = 'ACTIVE'
      AND (:query IS NULL OR a.search_vector @@ plainto_tsquery('french', :query))
      AND (:categorieId IS NULL OR a.categorie_id = CAST(:categorieId AS uuid))
      AND (:ville IS NULL OR LOWER(a.ville) LIKE LOWER(CONCAT('%', :ville, '%')))
      AND (:prixMin IS NULL OR a.prix >= :prixMin)
      AND (:prixMax IS NULL OR a.prix <= :prixMax)
    ORDER BY a.created_at DESC
    """,
            countQuery = """
    SELECT COUNT(*) FROM annonces a
    WHERE a.statut = 'ACTIVE'
      AND (:query IS NULL OR a.search_vector @@ plainto_tsquery('french', :query))
      AND (:categorieId IS NULL OR a.categorie_id = CAST(:categorieId AS uuid))
      AND (:ville IS NULL OR LOWER(a.ville) LIKE LOWER(CONCAT('%', :ville, '%')))
      AND (:prixMin IS NULL OR a.prix >= :prixMin)
      AND (:prixMax IS NULL OR a.prix <= :prixMax)
    """,
            nativeQuery = true)
    Page<Annonce> rechercher(
            @Param("query") String query,
            @Param("categorieId") String categorieId,
            @Param("ville") String ville,
            @Param("prixMin") BigDecimal prixMin,
            @Param("prixMax") BigDecimal prixMax,
            Pageable pageable
    );

    Page<Annonce> findByUserIdAndStatutNot(UUID userId, Annonce.Statut statut, Pageable pageable);

    @Modifying
    @Query("UPDATE Annonce a SET a.vues = a.vues + 1 WHERE a.id = :id")
    void incrementerVues(@Param("id") UUID id);

    @Query("""
        SELECT a FROM Annonce a
        WHERE a.categorie.id = :categorieId
          AND a.id != :annonceId
          AND a.statut = 'ACTIVE'
        ORDER BY a.createdAt DESC
        """)
    List<Annonce> findSimilaires(
            @Param("categorieId") UUID categorieId,
            @Param("annonceId") UUID annonceId,
            Pageable pageable
    );
    @Query("SELECT a FROM Annonce a JOIN FETCH a.user WHERE a.id = :id")
    Optional<Annonce> findByIdAvecUser(@Param("id") UUID id);

    @Query("SELECT COUNT(a) FROM Annonce a WHERE a.user.id = :userId AND a.statut = 'ACTIVE'")
    long countActiveByUserId(@Param("userId") UUID userId);

    @Query("SELECT COALESCE(SUM(a.vues), 0) FROM Annonce a WHERE a.user.id = :userId")
    long totalVuesByUserId(@Param("userId") UUID userId);

}