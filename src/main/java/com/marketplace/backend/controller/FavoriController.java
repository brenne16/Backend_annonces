package com.marketplace.backend.controller;

import com.marketplace.backend.dto.response.AnnonceResponse;
import com.marketplace.backend.service.FavoriService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/favoris")
public class FavoriController {

    private final FavoriService favoriService;

    public FavoriController(FavoriService favoriService) {
        this.favoriService = favoriService;
    }

    // GET /api/favoris — mes favoris
    @GetMapping
    public ResponseEntity<List<AnnonceResponse>> getMesFavoris(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                favoriService.getMesFavoris(userDetails.getUsername()));
    }

    // POST /api/favoris/{annonceId} — ajouter
    @PostMapping("/{annonceId}")
    public ResponseEntity<Map<String, Object>> ajouter(
            @PathVariable UUID annonceId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                favoriService.ajouter(annonceId, userDetails.getUsername()));
    }

    // DELETE /api/favoris/{annonceId} — retirer
    @DeleteMapping("/{annonceId}")
    public ResponseEntity<Map<String, Object>> retirer(
            @PathVariable UUID annonceId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                favoriService.retirer(annonceId, userDetails.getUsername()));
    }

    // GET /api/favoris/{annonceId}/statut — vérifier si en favori
    @GetMapping("/{annonceId}/statut")
    public ResponseEntity<Map<String, Object>> estEnFavori(
            @PathVariable UUID annonceId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                favoriService.estEnFavori(annonceId, userDetails.getUsername()));
    }
}