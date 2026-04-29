package com.marketplace.backend.controller;

import com.marketplace.backend.dto.request.CreateAnnonceRequest;
import com.marketplace.backend.dto.response.AnnonceResponse;
import com.marketplace.backend.service.AnnonceService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/annonces")
public class AnnonceController {

    private final AnnonceService annonceService;

    public AnnonceController(AnnonceService annonceService) {
        this.annonceService = annonceService;
    }

    @PostMapping
    public ResponseEntity<AnnonceResponse> creer(
            @Valid @RequestBody CreateAnnonceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(annonceService.creer(request, userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnnonceResponse> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails != null ? userDetails.getUsername() : null;
        return ResponseEntity.ok(annonceService.getById(id, email));
    }

    @GetMapping
    public ResponseEntity<Page<AnnonceResponse>> rechercher(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) UUID categorieId,
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) BigDecimal prixMin,
            @RequestParam(required = false) BigDecimal prixMax,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "createdAt") String tri) {
        return ResponseEntity.ok(
                annonceService.rechercher(query, categorieId, ville, prixMin, prixMax, page, size, tri));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnnonceResponse> modifier(
            @PathVariable UUID id,
            @Valid @RequestBody CreateAnnonceRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(annonceService.modifier(id, request, userDetails.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> supprimer(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserDetails userDetails) {
        annonceService.supprimer(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mes-annonces")
    public ResponseEntity<Page<AnnonceResponse>> getMesAnnonces(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return ResponseEntity.ok(
                annonceService.getMesAnnonces(userDetails.getUsername(), page, size));
    }

    @GetMapping("/{id}/similaires")
    public ResponseEntity<List<AnnonceResponse>> getSimilaires(@PathVariable UUID id) {
        return ResponseEntity.ok(annonceService.getSimilaires(id));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(annonceService.getStats(userDetails.getUsername()));
    }

    @PatchMapping("/{id}/statut")
    public ResponseEntity<AnnonceResponse> changerStatut(
            @PathVariable UUID id,
            @RequestBody Map<String, String> body,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                annonceService.changerStatut(id, body.get("statut"), userDetails.getUsername()));
    }
}