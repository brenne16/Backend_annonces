package com.marketplace.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public class MessageRequest {

    @NotNull
    private UUID destinataireId;

    @NotBlank
    private String contenu;

    private UUID annonceId;

    public UUID getDestinataireId() { return destinataireId; }
    public String getContenu() { return contenu; }
    public UUID getAnnonceId() { return annonceId; }
    public void setDestinataireId(UUID v) { this.destinataireId = v; }
    public void setContenu(String v) { this.contenu = v; }
    public void setAnnonceId(UUID v) { this.annonceId = v; }
}