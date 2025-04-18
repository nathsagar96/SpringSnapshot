package dev.sagar.ai.controller;

import dev.sagar.ai.dto.ImageRequestDTO;
import dev.sagar.ai.dto.ImageResponseDTO;
import dev.sagar.ai.service.ImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/images")
class ImageController {

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @PostMapping("/generate")
  @ResponseStatus(HttpStatus.OK)
  public ImageResponseDTO generateImages(@Valid @RequestBody ImageRequestDTO request) {
    return imageService.generateImages(request);
  }
}
