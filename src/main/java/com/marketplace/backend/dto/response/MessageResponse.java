package com.marketplace.backend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class MessageResponse {

    private UUID id;
    private String contenu;
    private boolean lu;
    private LocalDateTime createdAt;
    private UUID expediteurId;
    private String expediteurNom;
    private String expediteurPrenom;
    private UUID destinataireId;
    private String destinataireNom;
    private String destinatairePrenom;
    private UUID annonceId;
    private String annonceTitre;

    public MessageResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String contenu;
        private boolean lu;
        private LocalDateTime createdAt;
        private UUID expediteurId;
        private String expediteurNom;
        private String expediteurPrenom;
        private UUID destinataireId;
        private String destinataireNom;
        private String destinatairePrenom;
        private UUID annonceId;
        private String annonceTitre;

        public Builder id(UUID v) { this.id = v; return this; }
        public Builder contenu(String v) { this.contenu = v; return this; }
        public Builder lu(boolean v) { this.lu = v; return this; }
        public Builder createdAt(LocalDateTime v) { this.createdAt = v; return this; }
        public Builder expediteurId(UUID v) { this.expediteurId = v; return this; }
        public Builder expediteurNom(String v) { this.expediteurNom = v; return this; }
        public Builder expediteurPrenom(String v) { this.expediteurPrenom = v; return this; }
        public Builder destinataireId(UUID v) { this.destinataireId = v; return this; }
        public Builder destinataireNom(String v) { this.destinataireNom = v; return this; }
        public Builder destinatairePrenom(String v) { this.destinatairePrenom = v; return this; }
        public Builder annonceId(UUID v) { this.annonceId = v; return this; }
        public Builder annonceTitre(String v) { this.annonceTitre = v; return this; }

        public MessageResponse build() {
            MessageResponse r = new MessageResponse();
            r.id = this.id; r.contenu = this.contenu; r.lu = this.lu;
            r.createdAt = this.createdAt; r.expediteurId = this.expediteurId;
            r.expediteurNom = this.expediteurNom; r.expediteurPrenom = this.expediteurPrenom;
            r.destinataireId = this.destinataireId; r.destinataireNom = this.destinataireNom;
            r.destinatairePrenom = this.destinatairePrenom;
            r.annonceId = this.annonceId; r.annonceTitre = this.annonceTitre;
            return r;
        }
    }

    public UUID getId() { return id; }
    public String getContenu() { return contenu; }
    public boolean isLu() { return lu; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UUID getExpediteurId() { return expediteurId; }
    public String getExpediteurNom() { return expediteurNom; }
    public String getExpediteurPrenom() { return expediteurPrenom; }
    public UUID getDestinataireId() { return destinataireId; }
    public String getDestinataireNom() { return destinataireNom; }
    public String getDestinatairePrenom() { return destinatairePrenom; }
    public UUID getAnnonceId() { return annonceId; }
    public String getAnnonceTitre() { return annonceTitre; }
}