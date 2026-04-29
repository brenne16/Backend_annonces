package com.marketplace.backend.service;

import com.marketplace.backend.dto.request.MessageRequest;
import com.marketplace.backend.dto.response.ConversationResponse;
import com.marketplace.backend.dto.response.MessageResponse;
import com.marketplace.backend.entity.Annonce;
import com.marketplace.backend.entity.Message;
import com.marketplace.backend.entity.User;
import com.marketplace.backend.exception.ResourceNotFoundException;
import com.marketplace.backend.repository.AnnonceRepository;
import com.marketplace.backend.repository.MessageRepository;
import com.marketplace.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final AnnonceRepository annonceRepository;

    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          AnnonceRepository annonceRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.annonceRepository = annonceRepository;
    }

    // ── Envoyer un message ─────────────────────────────────────────────────────

    @Transactional
    public MessageResponse envoyer(MessageRequest request, String emailExpediteur) {
        User expediteur = userRepository.findByEmail(emailExpediteur)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        User destinataire = userRepository.findById(request.getDestinataireId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destinataire non trouve : " + request.getDestinataireId()));

        if (expediteur.getId().equals(destinataire.getId())) {
            throw new IllegalArgumentException("Vous ne pouvez pas vous envoyer un message");
        }

        Message message = new Message();
        message.setExpediteur(expediteur);
        message.setDestinataire(destinataire);
        message.setContenu(request.getContenu());

        if (request.getAnnonceId() != null) {
            annonceRepository.findById(request.getAnnonceId())
                    .ifPresent(message::setAnnonce);
        }

        messageRepository.save(message);
        return toResponse(message);
    }

    // ── Conversation entre deux utilisateurs ──────────────────────────────────

    @Transactional
    public List<MessageResponse> getConversation(UUID interlocuteurId, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        // Marquer les messages reçus comme lus
        messageRepository.marquerLus(user.getId(), interlocuteurId);

        return messageRepository.findConversation(user.getId(), interlocuteurId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ── Liste des conversations ────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ConversationResponse> getConversations(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));

        return messageRepository.findDerniersMessagesParConversation(user.getId())
                .stream()
                .map(m -> {
                    boolean estExpediteur = m.getExpediteur().getId().equals(user.getId());
                    User interlocuteur = estExpediteur ? m.getDestinataire() : m.getExpediteur();

                    long nonLus = messageRepository.countNonLus(user.getId());

                    return ConversationResponse.builder()
                            .interlocuteurId(interlocuteur.getId())
                            .interlocuteurNom(interlocuteur.getNom())
                            .interlocuteurPrenom(interlocuteur.getPrenom())
                            .dernierMessage(m.getContenu())
                            .derniereDate(m.getCreatedAt())
                            .messagesNonLus(nonLus)
                            .annonceId(m.getAnnonce() != null ? m.getAnnonce().getId() : null)
                            .annonceTitre(m.getAnnonce() != null ? m.getAnnonce().getTitre() : null)
                            .build();
                })
                .toList();
    }

    // ── Nombre de messages non lus ─────────────────────────────────────────────

    @Transactional(readOnly = true)
    public long getNombreNonLus(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouve"));
        return messageRepository.countNonLus(user.getId());
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private MessageResponse toResponse(Message m) {
        return MessageResponse.builder()
                .id(m.getId())
                .contenu(m.getContenu())
                .lu(Boolean.TRUE.equals(m.getLu()))
                .createdAt(m.getCreatedAt())
                .expediteurId(m.getExpediteur().getId())
                .expediteurNom(m.getExpediteur().getNom())
                .expediteurPrenom(m.getExpediteur().getPrenom())
                .destinataireId(m.getDestinataire().getId())
                .destinataireNom(m.getDestinataire().getNom())
                .destinatairePrenom(m.getDestinataire().getPrenom())
                .annonceId(m.getAnnonce() != null ? m.getAnnonce().getId() : null)
                .annonceTitre(m.getAnnonce() != null ? m.getAnnonce().getTitre() : null)
                .build();
    }
}