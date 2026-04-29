package com.marketplace.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class IaClientService {

    private final RestTemplate restTemplate;

    @Value("${app.ia.base-url}")
    private String iaBaseUrl;

    public IaClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map getRecommandations(String userId, int limit) {
        try {
            return restTemplate.getForObject(
                    iaBaseUrl + "/api/recommandations/utilisateur/" + userId + "?limit=" + limit,
                    Map.class
            );
        } catch (Exception e) {
            System.err.println("Erreur IA recommandations : " + e.getMessage());
            return Map.of("recommandations", List.of());
        }
    }

    public Map getSimilaires(String annonceId, int limit) {
        try {
            return restTemplate.getForObject(
                    iaBaseUrl + "/api/recommandations/annonce/" + annonceId + "?limit=" + limit,
                    Map.class
            );
        } catch (Exception e) {
            System.err.println("Erreur IA similaires : " + e.getMessage());
            return Map.of("similaires", List.of());
        }
    }

    public Map predireCategorie(String titre, String description) {
        try {
            Map<String, String> body = Map.of(
                    "titre", titre,
                    "description", description
            );
            return restTemplate.postForObject(
                    iaBaseUrl + "/api/categorisation/categorie",
                    body,
                    Map.class
            );
        } catch (Exception e) {
            System.err.println("Erreur IA catégorisation : " + e.getMessage());
            return Map.of("categorie", "loisirs", "confiance", 0.0);
        }
    }

    public Map envoyerMessageChatbot(String message, String sessionId, String userId) {
        try {
            Map<String, String> body = Map.of(
                    "message", message != null ? message : "",
                    "session_id", sessionId != null ? sessionId : "",
                    "user_id", userId != null ? userId : ""
            );
            return restTemplate.postForObject(
                    iaBaseUrl + "/api/chatbot/message",
                    body,
                    Map.class
            );
        } catch (Exception e) {
            System.err.println("Erreur IA chatbot : " + e.getMessage());
            return Map.of("reponse", "Service temporairement indisponible.");
        }
    }
}