package com.marketplace.backend.service;

import com.marketplace.backend.entity.Annonce;
import com.marketplace.backend.entity.Image;
import com.marketplace.backend.exception.ResourceNotFoundException;
import com.marketplace.backend.repository.AnnonceRepository;
import com.marketplace.backend.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final AnnonceRepository annonceRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.base-url}")
    private String baseUrl;

    public ImageService(ImageRepository imageRepository,
                        AnnonceRepository annonceRepository) {
        this.imageRepository = imageRepository;
        this.annonceRepository = annonceRepository;
    }

    @Transactional
    public List<String> uploadImages(UUID annonceId,
                                     List<MultipartFile> fichiers,
                                     String emailUtilisateur) throws IOException {

        Annonce annonce = annonceRepository.findByIdAvecUser(annonceId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Annonce non trouvée : " + annonceId));

        if (!annonce.getUser().getEmail().equals(emailUtilisateur)) {
            throw new SecurityException("Non autorisé");
        }

        // Chemin absolu du dossier uploads
        String cheminAbsolu = "C:/Users/Hello/Documents/Ma_version_projet_tutore/annonces-platform/backend/backend/uploads";
        Path dossier = Paths.get(cheminAbsolu, annonceId.toString());
        Files.createDirectories(dossier);

        List<String> urls = new ArrayList<>();
        boolean premiereFois = imageRepository.findByAnnonceId(annonceId).isEmpty();

        for (int i = 0; i < fichiers.size(); i++) {
            MultipartFile fichier = fichiers.get(i);

            if (fichier.isEmpty()) {
                System.err.println("Fichier vide ignoré");
                continue;
            }

            String extension = getExtension(fichier.getOriginalFilename());
            String nomFichier = UUID.randomUUID() + "." + extension;
            Path cheminFichier = dossier.resolve(nomFichier);

            // Écriture avec les bytes directement
            byte[] bytes = fichier.getBytes();
            System.out.println("Taille fichier reçu : " + bytes.length + " bytes");
            Files.write(cheminFichier, bytes);
            System.out.println("Fichier écrit : " + cheminFichier.toAbsolutePath());

            String url = baseUrl + "/" + annonceId + "/" + nomFichier;

            Image image = new Image();
            image.setAnnonce(annonce);
            image.setUrl(url);
            image.setNomFichier(nomFichier);
            image.setIsPrincipale(premiereFois && i == 0);
            image.setOrdre(i);
            imageRepository.save(image);
            urls.add(url);
        }

        return urls;
    }

    @Transactional
    public void supprimerImage(UUID imageId, String emailUtilisateur) {
        Image image = imageRepository.findByIdAvecAnnonce(imageId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image non trouvée : " + imageId));

        if (!image.getAnnonce().getUser().getEmail().equals(emailUtilisateur)) {
            throw new SecurityException("Non autorisé");
        }

        try {
            String cheminAbsolu = "C:/Users/Hello/Documents/Ma_version_projet_tutore/annonces-platform/backend/backend/uploads";
            Path fichier = Paths.get(cheminAbsolu,
                    image.getAnnonce().getId().toString(),
                    image.getNomFichier());
            Files.deleteIfExists(fichier);
        } catch (IOException e) {
            System.err.println("Erreur suppression : " + e.getMessage());
        }

        imageRepository.delete(image);
    }

    private String getExtension(String nomFichier) {
        if (nomFichier == null || !nomFichier.contains(".")) return "jpg";
        return nomFichier.substring(nomFichier.lastIndexOf('.') + 1).toLowerCase();
    }
}