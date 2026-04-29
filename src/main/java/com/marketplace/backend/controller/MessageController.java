package com.marketplace.backend.controller;

import com.marketplace.backend.dto.request.MessageRequest;
import com.marketplace.backend.dto.response.ConversationResponse;
import com.marketplace.backend.dto.response.MessageResponse;
import com.marketplace.backend.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    // POST /api/messages — envoyer un message
    @PostMapping
    public ResponseEntity<MessageResponse> envoyer(
            @Valid @RequestBody MessageRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(messageService.envoyer(request, userDetails.getUsername()));
    }

    // GET /api/messages/conversations — liste des conversations
    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationResponse>> getConversations(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                messageService.getConversations(userDetails.getUsername()));
    }

    // GET /api/messages/{interlocuteurId} — messages avec un utilisateur
    @GetMapping("/{interlocuteurId}")
    public ResponseEntity<List<MessageResponse>> getConversation(
            @PathVariable UUID interlocuteurId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                messageService.getConversation(interlocuteurId, userDetails.getUsername()));
    }

    // GET /api/messages/non-lus — nombre de messages non lus
    @GetMapping("/non-lus")
    public ResponseEntity<Map<String, Long>> getNonLus(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(Map.of(
                "nonLus", messageService.getNombreNonLus(userDetails.getUsername())));
    }
}