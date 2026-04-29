package com.marketplace.backend.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardAdminResponse {

    private long totalUtilisateurs;
    private long totalAnnonces;
    private long annoncesActives;
    private long annoncesSuspendues;
    private long totalInteractions;
    private List<Map<String, Object>> annoncesParCategorie;
    private List<Map<String, Object>> inscriptionsParJour;
    private List<Map<String, Object>> top10Vendeurs;

    public DashboardAdminResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long totalUtilisateurs;
        private long totalAnnonces;
        private long annoncesActives;
        private long annoncesSuspendues;
        private long totalInteractions;
        private List<Map<String, Object>> annoncesParCategorie;
        private List<Map<String, Object>> inscriptionsParJour;
        private List<Map<String, Object>> top10Vendeurs;

        public Builder totalUtilisateurs(long v) { this.totalUtilisateurs = v; return this; }
        public Builder totalAnnonces(long v) { this.totalAnnonces = v; return this; }
        public Builder annoncesActives(long v) { this.annoncesActives = v; return this; }
        public Builder annoncesSuspendues(long v) { this.annoncesSuspendues = v; return this; }
        public Builder totalInteractions(long v) { this.totalInteractions = v; return this; }
        public Builder annoncesParCategorie(List<Map<String, Object>> v) { this.annoncesParCategorie = v; return this; }
        public Builder inscriptionsParJour(List<Map<String, Object>> v) { this.inscriptionsParJour = v; return this; }
        public Builder top10Vendeurs(List<Map<String, Object>> v) { this.top10Vendeurs = v; return this; }

        public DashboardAdminResponse build() {
            DashboardAdminResponse r = new DashboardAdminResponse();
            r.totalUtilisateurs = this.totalUtilisateurs;
            r.totalAnnonces = this.totalAnnonces;
            r.annoncesActives = this.annoncesActives;
            r.annoncesSuspendues = this.annoncesSuspendues;
            r.totalInteractions = this.totalInteractions;
            r.annoncesParCategorie = this.annoncesParCategorie;
            r.inscriptionsParJour = this.inscriptionsParJour;
            r.top10Vendeurs = this.top10Vendeurs;
            return r;
        }
    }

    public long getTotalUtilisateurs() { return totalUtilisateurs; }
    public long getTotalAnnonces() { return totalAnnonces; }
    public long getAnnoncesActives() { return annoncesActives; }
    public long getAnnoncesSuspendues() { return annoncesSuspendues; }
    public long getTotalInteractions() { return totalInteractions; }
    public List<Map<String, Object>> getAnnoncesParCategorie() { return annoncesParCategorie; }
    public List<Map<String, Object>> getInscriptionsParJour() { return inscriptionsParJour; }
    public List<Map<String, Object>> getTop10Vendeurs() { return top10Vendeurs; }
}