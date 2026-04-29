package com.marketplace.backend.controller;

import com.marketplace.backend.dto.response.DashboardAdminResponse;
import com.marketplace.backend.dto.response.DashboardVendeurResponse;
import com.marketplace.backend.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    // Dashboard vendeur — accessible par tout utilisateur connecté
    @GetMapping("/vendeur")
    public ResponseEntity<DashboardVendeurResponse> getDashboardVendeur(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                dashboardService.getDashboardVendeur(userDetails.getUsername()));
    }

    // Dashboard admin — réservé aux ADMIN uniquement
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DashboardAdminResponse> getDashboardAdmin() {
        return ResponseEntity.ok(dashboardService.getDashboardAdmin());
    }
}