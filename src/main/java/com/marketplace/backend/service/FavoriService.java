package com.marketplace.backend.service;

import com.marketplace.backend.dto.response.AnnonceResponse;
import com.marketplace.backend.entity.Favori;
import com.marketplace.backend.entity.User;
import com.marketplace.backend.entity.Annonce;
import com.marketplace.backend.entity.Image;
import com.marketplace.backend.exception.ResourceNotFoundException;
import com.marketplace.backend.repository.AnnonceRepository;
import com.marketplace.backend.repository.FavoriRepository;
import com.marketplace.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FavoriService {

    private final FavoriRepository favoriRepository;
    private final UserRepository userRepository;
    private final AnnonceRepository annonceRepository;

    public FavoriService(FavoriRepository favoriRepository,
                         UserRepository userRepository,
                         AnnonceRepository annonceRepository) {
        this.favoriRepository = favoriRepository;
        this.userRepository = userRepository;
        this.annonceRepository = annonceRepository;
    }

    // ── Ajouter aux favoris ────────────────────────────────────────────────────

    @Transactional
    public Map<String, Object> ajouter(UUID annonceId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        Annonce annonce = annonceRepository.findById(annonceId)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce non trouvee : " + annonceId));

        if (favoriRepository.existsByUserIdAndAnnonceId(user.getId(), annonceId)) {
            return Map.of("message", "Annonce deja dans les favoris", "favori", true);
        }

        Favori favori = new Favori();
        favori.setUser(user);
        favori.setAnnonce(annonce);
        favoriRepository.save(favori);

        return Map.of("message", "Annonce ajoutee aux favoris", "favori", true);
    }

    // ── Retirer des favoris ────────────────────────────────────────────────────

    @Transactional
    public Map<String, Object> retirer(UUID annonceId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        Favori favori = favoriRepository
                .findByUserIdAndAnnonceId(user.getId(), annonceId)
                .orElseThrow(() -> new ResourceNotFoundException("Favori non trouve"));

        favoriRepository.delete(favori);
        return Map.of("message", "Annonce retiree des favoris", "favori", false);
    }

    // ── Mes favoris ────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AnnonceResponse> getMesFavoris(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        return favoriRepository.findByUserIdAvecAnnonce(user.getId())
                .stream()
                .map(f -> toResponse(f.getAnnonce()))
                .toList();
    }

    // ── Vérifier si une annonce est en favori ──────────────────────────────────

    @Transactional(readOnly = true)
    public Map<String, Object> estEnFavori(UUID annonceId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        boolean favori = favoriRepository.existsByUserIdAndAnnonceId(user.getId(), annonceId);
        return Map.of("favori", favori, "annonceId", annonceId.toString());
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

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