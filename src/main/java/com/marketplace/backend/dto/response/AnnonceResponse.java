package com.marketplace.backend.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class AnnonceResponse {
    private UUID id;
    private String titre;
    private String description;
    private BigDecimal prix;
    private String ville;
    private String statut;
    private String[] tags;
    private Integer vues;
    private LocalDateTime createdAt;
    private String categorieNom;
    private UUID categorieId;
    private String vendeurNom;
    private String vendeurPrenom;
    private UUID vendeurId;
    private List<String> imageUrls;
    private String imagePrincipaleUrl;

    public AnnonceResponse() {}

    // Builder manuel
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private String titre, description, ville, statut, categorieNom, vendeurNom, vendeurPrenom, imagePrincipaleUrl;
        private BigDecimal prix;
        private String[] tags;
        private Integer vues;
        private LocalDateTime createdAt;
        private UUID categorieId, vendeurId;
        private List<String> imageUrls;

        public Builder id(UUID v) { this.id = v; return this; }
        public Builder titre(String v) { this.titre = v; return this; }
        public Builder description(String v) { this.description = v; return this; }
        public Builder prix(BigDecimal v) { this.prix = v; return this; }
        public Builder ville(String v) { this.ville = v; return this; }
        public Builder statut(String v) { this.statut = v; return this; }
        public Builder tags(String[] v) { this.tags = v; return this; }
        public Builder vues(Integer v) { this.vues = v; return this; }
        public Builder createdAt(LocalDateTime v) { this.createdAt = v; return this; }
        public Builder categorieNom(String v) { this.categorieNom = v; return this; }
        public Builder categorieId(UUID v) { this.categorieId = v; return this; }
        public Builder vendeurNom(String v) { this.vendeurNom = v; return this; }
        public Builder vendeurPrenom(String v) { this.vendeurPrenom = v; return this; }
        public Builder vendeurId(UUID v) { this.vendeurId = v; return this; }
        public Builder imageUrls(List<String> v) { this.imageUrls = v; return this; }
        public Builder imagePrincipaleUrl(String v) { this.imagePrincipaleUrl = v; return this; }

        public AnnonceResponse build() {
            AnnonceResponse r = new AnnonceResponse();
            r.id = this.id; r.titre = this.titre; r.description = this.description;
            r.prix = this.prix; r.ville = this.ville; r.statut = this.statut;
            r.tags = this.tags; r.vues = this.vues; r.createdAt = this.createdAt;
            r.categorieNom = this.categorieNom; r.categorieId = this.categorieId;
            r.vendeurNom = this.vendeurNom; r.vendeurPrenom = this.vendeurPrenom;
            r.vendeurId = this.vendeurId; r.imageUrls = this.imageUrls;
            r.imagePrincipaleUrl = this.imagePrincipaleUrl;
            return r;
        }
    }

    // Getters
    public UUID getId() { return id; }
    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public BigDecimal getPrix() { return prix; }
    public String getVille() { return ville; }
    public String getStatut() { return statut; }
    public String[] getTags() { return tags; }
    public Integer getVues() { return vues; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getCategorieNom() { return categorieNom; }
    public UUID getCategorieId() { return categorieId; }
    public String getVendeurNom() { return vendeurNom; }
    public String getVendeurPrenom() { return vendeurPrenom; }
    public UUID getVendeurId() { return vendeurId; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getImagePrincipaleUrl() { return imagePrincipaleUrl; }
}