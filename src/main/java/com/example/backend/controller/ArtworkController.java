
package com.example.backend.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.entity.Artwork;
import com.example.backend.service.ArtworkService;
import com.example.backend.service.ImageStorageService;

@RestController
@RequestMapping("/api/artworks")
@CrossOrigin(origins = "*")
public class ArtworkController {

    private final ArtworkService artworkService;
    private final ImageStorageService imageStorageService;

    public ArtworkController(
            ArtworkService artworkService,
            ImageStorageService imageStorageService) {
        this.artworkService = artworkService;
        this.imageStorageService = imageStorageService;
    }

    @GetMapping
    public List<Artwork> getAllArtworks() {
        return artworkService.getAllArtworks();
    }

    @GetMapping("/{id}")
    public Artwork getArtworkById(@PathVariable Long id) {
        return artworkService.getArtworkById(id);
    }

    @PostMapping
    public Artwork addArtwork(@RequestBody Artwork artwork) {
        return artworkService.addArtwork(artwork);
    }

    @PostMapping(
        value = "/upload",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadArtwork(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image) {

        try {
            String imageUrl = imageStorageService.saveImage(image);

            Artwork artwork = new Artwork();
            artwork.setTitle(title);
            artwork.setDescription(description);
            artwork.setCategory(category);
            artwork.setImageUrl(imageUrl);

            Artwork savedArtwork = artworkService.addArtwork(artwork);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedArtwork);

        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));

        } catch (IOException e) {
            return ResponseEntity
                    .internalServerError()
                    .body(Map.of("message", "Could not save the image."));
        }
    }

    @PutMapping("/{id}")
    public Artwork updateArtwork(
            @PathVariable Long id,
            @RequestBody Artwork artwork) {
        return artworkService.updateArtwork(id, artwork);
    }

    @DeleteMapping("/{id}")
    public void deleteArtwork(@PathVariable Long id) {
        artworkService.deleteArtwork(id);
    }
}
