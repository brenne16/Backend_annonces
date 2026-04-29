package com.marketplace.backend.dto.request;

import jakarta.validation.constraints.*;

public class RegisterRequest {

    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 8)
    private String password;

    @NotBlank
    private String nom;

    @NotBlank
    private String prenom;

    private String telephone;
    private String ville;

    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public String getVille() { return ville; }

    public void setEmail(String v) { this.email = v; }
    public void setPassword(String v) { this.password = v; }
    public void setNom(String v) { this.nom = v; }
    public void setPrenom(String v) { this.prenom = v; }
    public void setTelephone(String v) { this.telephone = v; }
    public void setVille(String v) { this.ville = v; }
}