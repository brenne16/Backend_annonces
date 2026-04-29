package com.marketplace.backend.controller;

import com.marketplace.backend.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/annonce/{annonceId}")
    public ResponseEntity<Map<String, Object>> upload(
            @PathVariable UUID annonceId,
            @RequestParam("fichiers") MultipartFile fichier,
            @AuthenticationPrincipal UserDetails userDetails) throws IOException {

        List<String> urls = imageService.uploadImages(
                annonceId,
                List.of(fichier),
                userDetails.getUsername());

        return ResponseEntity.ok(Map.of(
                "message", "1 image uploadée",
                "urls", urls
        ));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> supprimer(
            @PathVariable UUID imageId,
            @AuthenticationPrincipal UserDetails userDetails) {

        imageService.supprimerImage(imageId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}