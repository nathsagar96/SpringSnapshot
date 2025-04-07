package dev.sagar.ai.dto;

import org.springframework.ai.image.ImageGenerationMetadata;

public record ImageResponseDTO(String imageUrl, ImageGenerationMetadata metadata) {}
