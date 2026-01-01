package com.pokedex.api.controller;

import com.pokedex.api.service.PokemonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pokemon")
public class PokemonController {

    @Autowired
    private PokemonService service;

    @GetMapping("/{name}")
    public ResponseEntity<?> search(@PathVariable String name) {
        try {
            return ResponseEntity.ok(service.fetchPokemon(name.toLowerCase().trim()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("error", "Pokemon not found"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Server error"));
        }
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam String query) {
        return ResponseEntity.ok(service.getSuggestions(query));
    }
}
