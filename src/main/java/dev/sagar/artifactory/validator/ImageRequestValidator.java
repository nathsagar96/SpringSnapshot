package dev.sagar.artifactory.validator;

import dev.sagar.artifactory.dto.ImageRequestDTO;
import dev.sagar.artifactory.exception.InvalidParameterException;
import java.util.Set;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.stereotype.Component;

@Component
public class ImageRequestValidator {

  public static final Set<String> VALID_MODELS =
      Set.of(
          OpenAiImageApi.ImageModel.DALL_E_2.getValue(),
          OpenAiImageApi.ImageModel.DALL_E_3.getValue());

  public static final Set<String> VALID_QUALITIES = Set.of("standard", "hd");
  public static final Set<String> VALID_STYLES = Set.of("natural", "vivid");
  public static final Set<String> DALL_E_2_VALID_DIMENSIONS =
      Set.of("256x256", "512x512", "1024x1024");
  public static final Set<String> DALL_E_3_VALID_DIMENSIONS =
      Set.of("1024x1024", "1792x1024", "1024x1792");

  public void validateRequest(ImageRequestDTO request) {
    validateModel(request.model());
    validateDimensions(request.model(), request.width(), request.height());
    validateQuality(request.model(), request.quality());
    validateStyle(request.style());
    validateNumImages(request.model(), request.numImages());
  }

  private void validateModel(String model) {
    if (!VALID_MODELS.contains(model.toLowerCase())) {
      throw new InvalidParameterException("Invalid model. Must be 'dall-e-2' or 'dall-e-3'");
    }
  }

  private void validateDimensions(String model, int width, int height) {
    String dimensions = width + "x" + height;
    if (OpenAiImageApi.ImageModel.DALL_E_2.getValue().equalsIgnoreCase(model)
        && !DALL_E_2_VALID_DIMENSIONS.contains(dimensions)) {
      throw new InvalidParameterException(
          "Invalid dimensions for DALL-E 2. Must be one of: " + DALL_E_2_VALID_DIMENSIONS);
    }
    if (OpenAiImageApi.ImageModel.DALL_E_3.getValue().equalsIgnoreCase(model)
        && !DALL_E_3_VALID_DIMENSIONS.contains(dimensions)) {
      throw new InvalidParameterException(
          "Invalid dimensions for DALL-E 3. Must be one of: " + DALL_E_3_VALID_DIMENSIONS);
    }
  }

  private void validateQuality(String model, String quality) {
    if (!VALID_QUALITIES.contains(quality)) {
      throw new InvalidParameterException("Invalid quality. Must be 'standard' or 'hd'");
    }
    if ("hd".equalsIgnoreCase(quality)
        && !OpenAiImageApi.ImageModel.DALL_E_3.getValue().equalsIgnoreCase(model)) {
      throw new InvalidParameterException("HD quality is only supported for dall-e-3 model");
    }
  }

  private void validateStyle(String style) {
    if (!VALID_STYLES.contains(style)) {
      throw new InvalidParameterException("Invalid style. Must be 'natural' or 'vivid'");
    }
  }

  private void validateNumImages(String model, int numImages) {
    if (OpenAiImageApi.ImageModel.DALL_E_3.getValue().equals(model) && numImages != 1) {
      throw new InvalidParameterException("For dall-e-3, number of images must be 1");
    }
  }
}
