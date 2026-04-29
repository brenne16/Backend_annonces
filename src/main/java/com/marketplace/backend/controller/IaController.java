package com.marketplace.backend.controller;

import com.marketplace.backend.service.IaClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ia")
public class IaController {

    private final IaClientService iaClientService;

    public IaController(IaClientService iaClientService) {
        this.iaClientService = iaClientService;
    }

    @GetMapping("/recommandations")
    public ResponseEntity<Map> getRecommandations(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(
                iaClientService.getRecommandations(userDetails.getUsername(), limit));
    }

    @GetMapping("/similaires/{annonceId}")
    public ResponseEntity<Map> getSimilaires(
            @PathVariable String annonceId,
            @RequestParam(defaultValue = "6") int limit) {
        return ResponseEntity.ok(
                iaClientService.getSimilaires(annonceId, limit));
    }

    @PostMapping("/chatbot")
    public ResponseEntity<Map> chatbot(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(
                iaClientService.envoyerMessageChatbot(
                        request.get("message"),
                        request.get("session_id"),
                        request.get("user_id")
                ));
    }
}