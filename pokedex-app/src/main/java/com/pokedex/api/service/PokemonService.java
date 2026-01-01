package com.pokedex.api.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    @Autowired
    private PokemonCache cache;
    
    private final RestTemplate http = new RestTemplate();
    private final String API_BASE = "https://pokeapi.co/api/v2/pokemon/";
    
    // In-memory storage for autocomplete names
    private final List<String> allPokemonNames = new ArrayList<>();

    @PostConstruct
    public void init() {
        // Pre-fetch names list for autocomplete performance
        try {
            Map response = http.getForObject("https://pokeapi.co/api/v2/pokemon?limit=1000", Map.class);
            List<Map<String, String>> results = (List<Map<String, String>>) response.get("results");
            for (Map<String, String> entry : results) {
                allPokemonNames.add(entry.get("name"));
            }
        } catch (Exception e) {
            System.err.println("Failed to initialize pokemon list: " + e.getMessage());
        }
    }

    public Map<String, Object> fetchPokemon(String name) {
        // Cache lookup
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = (Map<String, Object>) cache.get(name);
        if (cached != null) {
            System.out.println("Cache hit: " + name);
            return cached;
        }

        // External API fetch
        System.out.println("Cache miss. Fetching upstream: " + name);
        try {
            Map<String, Object> data = http.getForObject(API_BASE + name, Map.class);
            cache.put(name, data);
            return data;
        } catch (Exception e) {
            throw new RuntimeException("Resource not found");
        }
    }

    public List<String> getSuggestions(String query) {
        if (query == null || query.isEmpty()) return new ArrayList<>();
        return allPokemonNames.stream()
                .filter(name -> name.startsWith(query.toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
