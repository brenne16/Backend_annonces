package com.marketplace.backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
public class Categori {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false, unique = true, length = 100)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Categori parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Categori> sousCategories;

    private String icone;

    private Boolean actif = true;

    public Categori() {}

    public UUID getId() { return id; }
    public String getNom() { return nom; }
    public String getSlug() { return slug; }
    public String getIcone() { return icone; }
    public Boolean getActif() { return actif; }

    public void setId(UUID id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setSlug(String slug) { this.slug = slug; }
    public void setParent(Categori parent) { this.parent = parent; }
    public void setSousCategories(List<Categori> sousCategories) { this.sousCategories = sousCategories; }
    public void setIcone(String icone) { this.icone = icone; }
    public void setActif(Boolean actif) { this.actif = actif; }
}