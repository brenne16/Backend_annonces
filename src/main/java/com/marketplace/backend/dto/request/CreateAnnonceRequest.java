package com.marketplace.backend.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class CreateAnnonceRequest {

    @NotBlank @Size(min = 5, max = 150)
    private String titre;

    @NotBlank @Size(min = 20)
    private String description;

    @DecimalMin("0.0")
    private BigDecimal prix;

    @NotBlank
    private String ville;

    @NotNull
    private UUID categorieId;

    private List<String> tags;

    public String getTitre() { return titre; }
    public String getDescription() { return description; }
    public BigDecimal getPrix() { return prix; }
    public String getVille() { return ville; }
    public UUID getCategorieId() { return categorieId; }
    public List<String> getTags() { return tags; }

    public void setTitre(String v) { this.titre = v; }
    public void setDescription(String v) { this.description = v; }
    public void setPrix(BigDecimal v) { this.prix = v; }
    public void setVille(String v) { this.ville = v; }
    public void setCategorieId(UUID v) { this.categorieId = v; }
    public void setTags(List<String> v) { this.tags = v; }
}