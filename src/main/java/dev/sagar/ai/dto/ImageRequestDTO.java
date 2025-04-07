package dev.sagar.ai.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ImageRequestDTO(
    @NotBlank(message = "User ID cannot be blank") String userId,
    @NotBlank(message = "Prompt cannot be blank") String prompt,
    @NotBlank(message = "Model cannot be blank") String model,
    @NotNull(message = "Height cannot be null") int height,
    @NotNull(message = "Width cannot be null") int width,
    @NotBlank(message = "Quality cannot be blank") String quality,
    @NotBlank(message = "Style cannot be blank") String style,
    @NotNull(message = "Number of images cannot be null")
        @Min(value = 1, message = "Number of images must be at least 1")
        @Max(value = 10, message = "Number of images must be at most 10")
        int numImages) {}
