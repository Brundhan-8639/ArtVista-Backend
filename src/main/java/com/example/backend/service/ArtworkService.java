package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.entity.Artwork;
import com.example.backend.repository.ArtworkRepository;

@Service
public class ArtworkService {

    private final ArtworkRepository artworkRepository;

    public ArtworkService(ArtworkRepository artworkRepository) {
        this.artworkRepository = artworkRepository;
    }

    public List<Artwork> getAllArtworks() {
        return artworkRepository.findAll();
    }

    public Artwork getArtworkById(Long id) {
        return artworkRepository.findById(id).orElse(null);
    }

    public Artwork addArtwork(Artwork artwork) {
        return artworkRepository.save(artwork);
    }

    public Artwork updateArtwork(Long id, Artwork artwork) {

        Artwork existingArtwork = artworkRepository.findById(id).orElse(null);

        if (existingArtwork != null) {

            existingArtwork.setTitle(artwork.getTitle());
            existingArtwork.setDescription(artwork.getDescription());
            existingArtwork.setCategory(artwork.getCategory());
            existingArtwork.setImageUrl(artwork.getImageUrl());

            return artworkRepository.save(existingArtwork);
        }

        return null;
    }

    public void deleteArtwork(Long id) {
        artworkRepository.deleteById(id);
    }
}