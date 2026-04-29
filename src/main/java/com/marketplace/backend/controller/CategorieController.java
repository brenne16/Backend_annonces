package com.marketplace.backend.controller;

import com.marketplace.backend.entity.Categori;
import com.marketplace.backend.repository.CategorieRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    private final CategorieRepository categorieRepository;

    public CategorieController(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    @GetMapping
    public ResponseEntity<List<Categori>> getAll() {
        return ResponseEntity.ok(categorieRepository.findByActifTrue());
    }

    @GetMapping("/racines")
    public ResponseEntity<List<Categori>> getRacines() {
        return ResponseEntity.ok(categorieRepository.findByParentIsNull());
    }
}