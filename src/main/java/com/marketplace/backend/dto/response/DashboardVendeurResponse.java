package com.marketplace.backend.dto.response;

import java.util.List;
import java.util.Map;

public class DashboardVendeurResponse {

    private long annoncesActives;
    private long annoncesVendues;
    private long totalVues;
    private long totalFavoris;
    private double prixMoyen;
    private List<Map<String, Object>> annoncesParCategorie;
    private List<Map<String, Object>> vuesDernieres7Jours;
    private List<Map<String, Object>> meilleuresAnnonces;

    public DashboardVendeurResponse() {}

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private long annoncesActives;
        private long annoncesVendues;
        private long totalVues;
        private long totalFavoris;
        private double prixMoyen;
        private List<Map<String, Object>> annoncesParCategorie;
        private List<Map<String, Object>> vuesDernieres7Jours;
        private List<Map<String, Object>> meilleuresAnnonces;

        public Builder annoncesActives(long v) { this.annoncesActives = v; return this; }
        public Builder annoncesVendues(long v) { this.annoncesVendues = v; return this; }
        public Builder totalVues(long v) { this.totalVues = v; return this; }
        public Builder totalFavoris(long v) { this.totalFavoris = v; return this; }
        public Builder prixMoyen(double v) { this.prixMoyen = v; return this; }
        public Builder annoncesParCategorie(List<Map<String, Object>> v) { this.annoncesParCategorie = v; return this; }
        public Builder vuesDernieres7Jours(List<Map<String, Object>> v) { this.vuesDernieres7Jours = v; return this; }
        public Builder meilleuresAnnonces(List<Map<String, Object>> v) { this.meilleuresAnnonces = v; return this; }

        public DashboardVendeurResponse build() {
            DashboardVendeurResponse r = new DashboardVendeurResponse();
            r.annoncesActives = this.annoncesActives;
            r.annoncesVendues = this.annoncesVendues;
            r.totalVues = this.totalVues;
            r.totalFavoris = this.totalFavoris;
            r.prixMoyen = this.prixMoyen;
            r.annoncesParCategorie = this.annoncesParCategorie;
            r.vuesDernieres7Jours = this.vuesDernieres7Jours;
            r.meilleuresAnnonces = this.meilleuresAnnonces;
            return r;
        }
    }

    public long getAnnoncesActives() { return annoncesActives; }
    public long getAnnoncesVendues() { return annoncesVendues; }
    public long getTotalVues() { return totalVues; }
    public long getTotalFavoris() { return totalFavoris; }
    public double getPrixMoyen() { return prixMoyen; }
    public List<Map<String, Object>> getAnnoncesParCategorie() { return annoncesParCategorie; }
    public List<Map<String, Object>> getVuesDernieres7Jours() { return vuesDernieres7Jours; }
    public List<Map<String, Object>> getMeilleuresAnnonces() { return meilleuresAnnonces; }
}