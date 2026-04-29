package com.marketplace.backend.service;

import com.marketplace.backend.dto.response.DashboardAdminResponse;
import com.marketplace.backend.dto.response.DashboardVendeurResponse;
import com.marketplace.backend.entity.User;
import com.marketplace.backend.repository.FavoriRepository;
import com.marketplace.backend.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final FavoriRepository favoriRepository;
    private final JdbcTemplate jdbcTemplate;

    public DashboardService(UserRepository userRepository,
                            FavoriRepository favoriRepository,
                            JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
        this.favoriRepository = favoriRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    // ── Dashboard Vendeur ──────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public DashboardVendeurResponse getDashboardVendeur(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        UUID userId = user.getId();

        // Stats de base
        long annoncesActives = queryLong(
                "SELECT COUNT(*) FROM annonces WHERE user_id = ? AND statut = 'ACTIVE'", userId);
        long annoncesVendues = queryLong(
                "SELECT COUNT(*) FROM annonces WHERE user_id = ? AND statut = 'VENDU'", userId);
        long totalVues = queryLong(
                "SELECT COALESCE(SUM(vues), 0) FROM annonces WHERE user_id = ?", userId);
        long totalFavoris = favoriRepository.countFavorisByVendeurId(userId);
        double prixMoyen = queryDouble(
                "SELECT COALESCE(AVG(prix), 0) FROM annonces WHERE user_id = ? AND statut = 'ACTIVE' AND prix IS NOT NULL", userId);

        // Annonces par catégorie
        List<Map<String, Object>> annoncesParCategorie = jdbcTemplate.queryForList("""
            SELECT c.nom as categorie, COUNT(a.id) as total
            FROM annonces a
            JOIN categories c ON a.categorie_id = c.id
            WHERE a.user_id = ?
            GROUP BY c.nom
            ORDER BY total DESC
        """, userId);

        // Vues des 7 derniers jours par annonce
        List<Map<String, Object>> vuesDernieres7Jours = jdbcTemplate.queryForList("""
            SELECT DATE(i.created_at) as jour, COUNT(*) as vues
            FROM interactions i
            JOIN annonces a ON i.annonce_id = a.id
            WHERE a.user_id = ?
              AND i.type = 'VUE'
              AND i.created_at >= NOW() - INTERVAL '7 days'
            GROUP BY DATE(i.created_at)
            ORDER BY jour ASC
        """, userId);

        // Meilleures annonces par vues
        List<Map<String, Object>> meilleuresAnnonces = jdbcTemplate.queryForList("""
            SELECT a.id, a.titre, a.vues, a.prix, a.statut,
                   c.nom as categorie
            FROM annonces a
            JOIN categories c ON a.categorie_id = c.id
            WHERE a.user_id = ?
            ORDER BY a.vues DESC
            LIMIT 5
        """, userId);

        return DashboardVendeurResponse.builder()
                .annoncesActives(annoncesActives)
                .annoncesVendues(annoncesVendues)
                .totalVues(totalVues)
                .totalFavoris(totalFavoris)
                .prixMoyen(prixMoyen)
                .annoncesParCategorie(annoncesParCategorie)
                .vuesDernieres7Jours(vuesDernieres7Jours)
                .meilleuresAnnonces(meilleuresAnnonces)
                .build();
    }

    // ── Dashboard Admin ────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public DashboardAdminResponse getDashboardAdmin() {

        long totalUtilisateurs = queryLong("SELECT COUNT(*) FROM users WHERE actif = true");
        long totalAnnonces = queryLong("SELECT COUNT(*) FROM annonces");
        long annoncesActives = queryLong("SELECT COUNT(*) FROM annonces WHERE statut = 'ACTIVE'");
        long annoncesSuspendues = queryLong("SELECT COUNT(*) FROM annonces WHERE statut = 'SUSPENDU'");
        long totalInteractions = queryLong("SELECT COUNT(*) FROM interactions");

        List<Map<String, Object>> annoncesParCategorie = jdbcTemplate.queryForList("""
            SELECT c.nom as categorie, COUNT(a.id) as total,
                   SUM(CASE WHEN a.statut = 'ACTIVE' THEN 1 ELSE 0 END) as actives
            FROM categories c
            LEFT JOIN annonces a ON c.id = a.categorie_id
            GROUP BY c.nom
            ORDER BY total DESC
        """);

        List<Map<String, Object>> inscriptionsParJour = jdbcTemplate.queryForList("""
            SELECT DATE(created_at) as jour, COUNT(*) as inscriptions
            FROM users
            WHERE created_at >= NOW() - INTERVAL '30 days'
            GROUP BY DATE(created_at)
            ORDER BY jour ASC
        """);

        List<Map<String, Object>> top10Vendeurs = jdbcTemplate.queryForList("""
            SELECT u.nom, u.prenom, u.email,
                   COUNT(a.id) as nb_annonces,
                   COALESCE(SUM(a.vues), 0) as total_vues
            FROM users u
            LEFT JOIN annonces a ON u.id = a.user_id
            GROUP BY u.id, u.nom, u.prenom, u.email
            ORDER BY nb_annonces DESC
            LIMIT 10
        """);

        return DashboardAdminResponse.builder()
                .totalUtilisateurs(totalUtilisateurs)
                .totalAnnonces(totalAnnonces)
                .annoncesActives(annoncesActives)
                .annoncesSuspendues(annoncesSuspendues)
                .totalInteractions(totalInteractions)
                .annoncesParCategorie(annoncesParCategorie)
                .inscriptionsParJour(inscriptionsParJour)
                .top10Vendeurs(top10Vendeurs)
                .build();
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private long queryLong(String sql, Object... args) {
        Long result = jdbcTemplate.queryForObject(sql, Long.class, args);
        return result != null ? result : 0L;
    }

    private double queryDouble(String sql, Object... args) {
        Double result = jdbcTemplate.queryForObject(sql, Double.class, args);
        return result != null ? result : 0.0;
    }
}