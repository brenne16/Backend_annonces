package com.marketplace.backend.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/files")
public class FichierController {

    private static final String UPLOAD_DIR =
            "C:/Users/Hello/Documents/Ma_version_projet_tutore/annonces-platform/backend/backend/uploads";

    @GetMapping("/{annonceId}/{nomFichier}")
    public ResponseEntity<Resource> getFichier(
            @PathVariable String annonceId,
            @PathVariable String nomFichier) {

        try {
            Path cheminFichier = Paths.get(UPLOAD_DIR)
                    .resolve(annonceId)
                    .resolve(nomFichier)
                    .normalize();

            System.out.println("Cherche fichier : " + cheminFichier.toAbsolutePath());

            Resource resource = new UrlResource(cheminFichier.toUri());

            if (!resource.exists()) {
                System.err.println("Fichier non trouvé : " + cheminFichier);
                return ResponseEntity.notFound().build();
            }

            if (!resource.isReadable()) {
                System.err.println("Fichier non lisible : " + cheminFichier);
                return ResponseEntity.status(403).build();
            }

            MediaType mediaType = MediaType.IMAGE_JPEG;
            String nom = nomFichier.toLowerCase();
            if (nom.endsWith(".png")) mediaType = MediaType.IMAGE_PNG;
            else if (nom.endsWith(".gif")) mediaType = MediaType.IMAGE_GIF;
            else if (nom.endsWith(".webp")) mediaType = MediaType.valueOf("image/webp");

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .body(resource);

        } catch (MalformedURLException e) {
            System.err.println("URL malformée : " + e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}