package com.marketplace.backend.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public class ConversationResponse {

    private UUID interlocuteurId;
    private String interlocuteurNom;
    private String interlocuteurPrenom;
    private String dernierMessage;
    private LocalDateTime derniereDate;
    private long messagesNonLus;
    private UUID annonceId;
    private String annonceTitre;

    public ConversationResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID interlocuteurId;
        private String interlocuteurNom;
        private String interlocuteurPrenom;
        private String dernierMessage;
        private LocalDateTime derniereDate;
        private long messagesNonLus;
        private UUID annonceId;
        private String annonceTitre;

        public Builder interlocuteurId(UUID v) { this.interlocuteurId = v; return this; }
        public Builder interlocuteurNom(String v) { this.interlocuteurNom = v; return this; }
        public Builder interlocuteurPrenom(String v) { this.interlocuteurPrenom = v; return this; }
        public Builder dernierMessage(String v) { this.dernierMessage = v; return this; }
        public Builder derniereDate(LocalDateTime v) { this.derniereDate = v; return this; }
        public Builder messagesNonLus(long v) { this.messagesNonLus = v; return this; }
        public Builder annonceId(UUID v) { this.annonceId = v; return this; }
        public Builder annonceTitre(String v) { this.annonceTitre = v; return this; }

        public ConversationResponse build() {
            ConversationResponse r = new ConversationResponse();
            r.interlocuteurId = this.interlocuteurId;
            r.interlocuteurNom = this.interlocuteurNom;
            r.interlocuteurPrenom = this.interlocuteurPrenom;
            r.dernierMessage = this.dernierMessage;
            r.derniereDate = this.derniereDate;
            r.messagesNonLus = this.messagesNonLus;
            r.annonceId = this.annonceId;
            r.annonceTitre = this.annonceTitre;
            return r;
        }
    }

    public UUID getInterlocuteurId() { return interlocuteurId; }
    public String getInterlocuteurNom() { return interlocuteurNom; }
    public String getInterlocuteurPrenom() { return interlocuteurPrenom; }
    public String getDernierMessage() { return dernierMessage; }
    public LocalDateTime getDerniereDate() { return derniereDate; }
    public long getMessagesNonLus() { return messagesNonLus; }
    public UUID getAnnonceId() { return annonceId; }
    public String getAnnonceTitre() { return annonceTitre; }
}