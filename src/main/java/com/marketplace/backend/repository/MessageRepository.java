package com.marketplace.backend.repository;

import com.marketplace.backend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Messages entre deux utilisateurs
    @Query("""
        SELECT m FROM Message m
        JOIN FETCH m.expediteur
        JOIN FETCH m.destinataire
        WHERE (m.expediteur.id = :userId1 AND m.destinataire.id = :userId2)
           OR (m.expediteur.id = :userId2 AND m.destinataire.id = :userId1)
        ORDER BY m.createdAt ASC
        """)
    List<Message> findConversation(
            @Param("userId1") UUID userId1,
            @Param("userId2") UUID userId2);

    // Marquer comme lus
    @Modifying
    @Query("""
        UPDATE Message m SET m.lu = true
        WHERE m.destinataire.id = :destinataireId
          AND m.expediteur.id = :expediteurId
          AND m.lu = false
        """)
    void marquerLus(
            @Param("destinataireId") UUID destinataireId,
            @Param("expediteurId") UUID expediteurId);

    // Nombre de messages non lus
    @Query("SELECT COUNT(m) FROM Message m WHERE m.destinataire.id = :userId AND m.lu = false")
    long countNonLus(@Param("userId") UUID userId);

    // Derniers messages par conversation
    @Query(value = """
        SELECT DISTINCT ON (LEAST(m.expediteur_id::text, m.destinataire_id::text) || '-' ||
                            GREATEST(m.expediteur_id::text, m.destinataire_id::text))
               m.*
        FROM messages m
        WHERE m.expediteur_id = :userId OR m.destinataire_id = :userId
        ORDER BY LEAST(m.expediteur_id::text, m.destinataire_id::text) || '-' ||
                 GREATEST(m.expediteur_id::text, m.destinataire_id::text),
                 m.created_at DESC
        """, nativeQuery = true)
    List<Message> findDerniersMessagesParConversation(@Param("userId") UUID userId);
}