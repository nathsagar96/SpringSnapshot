package dev.sagar.ai.service;

import dev.sagar.ai.dto.ImageRequestDTO;
import dev.sagar.ai.dto.ImageResponseDTO;
import dev.sagar.ai.validator.ImageRequestValidator;
import org.springframework.ai.image.*;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private final ImageModel imageModel;
  private final ImageRequestValidator validator;

  public ImageService(ImageModel imageModel, ImageRequestValidator validator) {
    this.imageModel = imageModel;
    this.validator = validator;
  }

  public ImageResponseDTO generateImages(ImageRequestDTO request) {
    validator.validateRequest(request);

    OpenAiImageOptions options =
        OpenAiImageOptions.builder()
            .withUser(request.userId())
            .withModel(request.model())
            .withHeight(request.height())
            .withWidth(request.width())
            .withQuality(request.quality())
            .withStyle(request.style())
            .withN(request.numImages())
            .build();

    ImagePrompt imagePrompt = new ImagePrompt(request.prompt(), options);
    ImageResponse imageResponse = imageModel.call(imagePrompt);

    return new ImageResponseDTO(
        imageResponse.getResults().stream()
            .map(imageGeneration -> imageGeneration.getOutput().getUrl())
            .toList());
  }
}
