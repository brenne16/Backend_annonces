package com.marketplace.backend.service;

import com.marketplace.backend.dto.request.CreateAnnonceRequest;
import com.marketplace.backend.dto.response.AnnonceResponse;
import com.marketplace.backend.entity.*;
import com.marketplace.backend.exception.ResourceNotFoundException;
import com.marketplace.backend.repository.AnnonceRepository;
import com.marketplace.backend.repository.CategorieRepository;
import com.marketplace.backend.repository.InteractionRepository;
import com.marketplace.backend.repository.UserRepository;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final UserRepository userRepository;
    private final CategorieRepository categorieRepository;
    private final InteractionRepository interactionRepository;

    public AnnonceService(AnnonceRepository annonceRepository,
                          UserRepository userRepository,
                          CategorieRepository categorieRepository,
                          InteractionRepository interactionRepository) {
        this.annonceRepository = annonceRepository;
        this.userRepository = userRepository;
        this.categorieRepository = categorieRepository;
        this.interactionRepository = interactionRepository;
    }

    @Transactional
    public AnnonceResponse creer(CreateAnnonceRequest request, String emailUtilisateur) {
        User user = userRepository.findByEmail(emailUtilisateur)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Categori categorie = categorieRepository.findById(request.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Catégorie non trouvée : " + request.getCategorieId()));

        Annonce annonce = new Annonce();
        annonce.setUser(user);
        annonce.setCategorie(categorie);
        annonce.setTitre(request.getTitre());
        annonce.setDescription(request.getDescription());
        annonce.setPrix(request.getPrix());
        annonce.setVille(request.getVille());
        annonce.setStatut(Annonce.Statut.ACTIVE);
        annonce.setVues(0);
        if (request.getTags() != null) {
            annonce.setTags(request.getTags().toArray(new String[0]));
        }

        annonceRepository.save(annonce);
        return toResponse(annonce);
    }

    @Transactional
    public AnnonceResponse getById(UUID id, String emailUtilisateur) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvée : " + id));

        annonceRepository.incrementerVues(id);
        annonce.setVues(annonce.getVues() + 1);

        if (emailUtilisateur != null) {
            userRepository.findByEmail(emailUtilisateur).ifPresent(user -> {
                Interaction interaction = new Interaction();
                interaction.setUser(user);
                interaction.setAnnonce(annonce);
                interaction.setType(Interaction.TypeInteraction.VUE);
                interactionRepository.save(interaction);
            });
        }

        return toResponse(annonce);
    }

    @Transactional(readOnly = true)
    public Page<AnnonceResponse> rechercher(String query, UUID categorieId, String ville,
                                            BigDecimal prixMin, BigDecimal prixMax,
                                            int page, int size, String tri) {
        // Pageable SANS Sort pour éviter le double ORDER BY
        Pageable pageable = PageRequest.of(page, size);
        String categorieIdStr = categorieId != null ? categorieId.toString() : null;
        return annonceRepository
                .rechercher(query, categorieIdStr, ville, prixMin, prixMax, pageable)
                .map(this::toResponse);
    }

    @Transactional
    public AnnonceResponse changerStatut(UUID id, String nouveauStatut, String emailUtilisateur) {
        Annonce annonce = annonceRepository.findByIdAvecUser(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvee : " + id));

        if (!annonce.getUser().getEmail().equals(emailUtilisateur)) {
            throw new SecurityException("Non autorise");
        }

        try {
            annonce.setStatut(Annonce.Statut.valueOf(nouveauStatut.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Statut invalide : " + nouveauStatut +
                    ". Valeurs acceptees : ACTIVE, VENDU, SUSPENDU, SUPPRIME");
        }

        return toResponse(annonceRepository.save(annonce));
    }

    @Transactional
    public AnnonceResponse modifier(UUID id, CreateAnnonceRequest request, String emailUtilisateur) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvée : " + id));

        if (!annonce.getUser().getEmail().equals(emailUtilisateur)) {
            throw new SecurityException("Non autorisé");
        }

        Categori categorie = categorieRepository.findById(request.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Catégorie non trouvée : " + request.getCategorieId()));

        annonce.setTitre(request.getTitre());
        annonce.setDescription(request.getDescription());
        annonce.setPrix(request.getPrix());
        annonce.setVille(request.getVille());
        annonce.setCategorie(categorie);
        if (request.getTags() != null) {
            annonce.setTags(request.getTags().toArray(new String[0]));
        }

        return toResponse(annonceRepository.save(annonce));
    }

    @Transactional
    public void supprimer(UUID id, String emailUtilisateur) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvée : " + id));

        if (!annonce.getUser().getEmail().equals(emailUtilisateur)) {
            throw new SecurityException("Non autorisé");
        }

        annonce.setStatut(Annonce.Statut.SUPPRIME);
        annonceRepository.save(annonce);
    }

    @Transactional(readOnly = true)
    public Page<AnnonceResponse> getMesAnnonces(String emailUtilisateur, int page, int size) {
        User user = userRepository.findByEmail(emailUtilisateur)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return annonceRepository
                .findByUserIdAndStatutNot(user.getId(), Annonce.Statut.SUPPRIME, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public List<AnnonceResponse> getSimilaires(UUID annonceId) {
        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvée : " + annonceId));

        return annonceRepository
                .findSimilaires(annonce.getCategorie().getId(), annonceId, PageRequest.of(0, 6))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Map<String, Long> getStats(String emailUtilisateur) {
        User user = userRepository.findByEmail(emailUtilisateur)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return Map.of(
                "annoncesActives", annonceRepository.countActiveByUserId(user.getId()),
                "totalVues", annonceRepository.totalVuesByUserId(user.getId())
        );
    }

    private AnnonceResponse toResponse(Annonce a) {
        List<String> urls = a.getImages() != null
                ? a.getImages().stream().map(Image::getUrl).toList()
                : List.of();

        String urlPrincipale = a.getImages() != null
                ? a.getImages().stream()
                .filter(img -> Boolean.TRUE.equals(img.getIsPrincipale()))
                .map(Image::getUrl)
                .findFirst()
                .orElse(urls.isEmpty() ? null : urls.get(0))
                : null;

        return AnnonceResponse.builder()
                .id(a.getId())
                .titre(a.getTitre())
                .description(a.getDescription())
                .prix(a.getPrix())
                .ville(a.getVille())
                .statut(a.getStatut().name())
                .tags(a.getTags())
                .vues(a.getVues())
                .createdAt(a.getCreatedAt())
                .categorieId(a.getCategorie().getId())
                .categorieNom(a.getCategorie().getNom())
                .vendeurId(a.getUser().getId())
                .vendeurNom(a.getUser().getNom())
                .vendeurPrenom(a.getUser().getPrenom())
                .imageUrls(urls)
                .imagePrincipaleUrl(urlPrincipale)
                .build();
    }
}