package com.marketplace.backend.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "messages")
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expediteur_id", nullable = false)
    private User expediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private User destinataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "annonce_id")
    private Annonce annonce;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenu;

    @Column(nullable = false)
    private Boolean lu = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Message() {}

    public UUID getId() { return id; }
    public User getExpediteur() { return expediteur; }
    public User getDestinataire() { return destinataire; }
    public Annonce getAnnonce() { return annonce; }
    public String getContenu() { return contenu; }
    public Boolean getLu() { return lu; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setExpediteur(User v) { this.expediteur = v; }
    public void setDestinataire(User v) { this.destinataire = v; }
    public void setAnnonce(Annonce v) { this.annonce = v; }
    public void setContenu(String v) { this.contenu = v; }
    public void setLu(Boolean v) { this.lu = v; }
}