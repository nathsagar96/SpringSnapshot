package dev.sagar.ai.validator;

import static org.junit.jupiter.api.Assertions.*;

import dev.sagar.ai.dto.ImageRequestDTO;
import dev.sagar.ai.exception.InvalidParameterException;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.ai.openai.api.OpenAiImageApi;

class ImageRequestValidatorTest {

  private ImageRequestValidator validator;

  static Stream<String> validModels() {
    return Stream.of(
        OpenAiImageApi.ImageModel.DALL_E_2.getValue(),
        OpenAiImageApi.ImageModel.DALL_E_3.getValue());
  }

  static Stream<String> invalidModels() {
    return Stream.of("dall-e-1", "midjourney", "stable-diffusion");
  }

  static Stream<String> validQualities() {
    return Stream.of("standard", "hd");
  }

  static Stream<String> invalidQualities() {
    return Stream.of("high", "low", "ultra");
  }

  static Stream<String> validStyles() {
    return Stream.of("natural", "vivid");
  }

  static Stream<String> invalidStyles() {
    return Stream.of("realistic", "cartoon", "abstract");
  }

  static Stream<Integer> validNumImages() {
    return Stream.of(1, 2, 3, 5, 10);
  }

  @BeforeEach
  void setUp() {
    validator = new ImageRequestValidator();
  }

  @ParameterizedTest
  @MethodSource("validModels")
  void shouldAcceptValidModels(String model) {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123", "A beautiful sunset", model, 1024, 1024, "standard", "natural", 1);
    assertDoesNotThrow(() -> validator.validateRequest(request));
  }

  @ParameterizedTest
  @MethodSource("invalidModels")
  void shouldRejectInvalidModels(String model) {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123", "A beautiful sunset", model, 1024, 1024, "standard", "natural", 1);
    assertThrows(InvalidParameterException.class, () -> validator.validateRequest(request));
  }

  @Test
  void shouldAcceptValidDallE2Dimensions() {
    int[][] validDimensions = {{256, 256}, {512, 512}, {1024, 1024}};

    for (int[] dim : validDimensions) {
      ImageRequestDTO request =
          new ImageRequestDTO(
              "user123",
              "A landscape",
              OpenAiImageApi.ImageModel.DALL_E_2.getValue(),
              dim[1],
              dim[0],
              "standard",
              "natural",
              1);
      assertDoesNotThrow(() -> validator.validateRequest(request));
    }
  }

  @Test
  void shouldRejectInvalidDallE2Dimensions() {
    int[][] invalidDimensions = {{128, 128}, {512, 256}, {2048, 2048}, {1792, 1024}};

    for (int[] dim : invalidDimensions) {
      ImageRequestDTO request =
          new ImageRequestDTO(
              "user123",
              "A landscape",
              OpenAiImageApi.ImageModel.DALL_E_2.getValue(),
              dim[1],
              dim[0],
              "standard",
              "natural",
              1);
      assertThrows(InvalidParameterException.class, () -> validator.validateRequest(request));
    }
  }

  @Test
  void shouldAcceptValidDallE3Dimensions() {
    int[][] validDimensions = {{1024, 1024}, {1792, 1024}, {1024, 1792}};

    for (int[] dim : validDimensions) {
      ImageRequestDTO request =
          new ImageRequestDTO(
              "user123",
              "A cityscape",
              OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
              dim[1],
              dim[0],
              "standard",
              "natural",
              1);
      assertDoesNotThrow(() -> validator.validateRequest(request));
    }
  }

  @Test
  void shouldRejectInvalidDallE3Dimensions() {
    int[][] invalidDimensions = {{256, 256}, {512, 512}, {2048, 2048}, {1024, 512}};

    for (int[] dim : invalidDimensions) {
      ImageRequestDTO request =
          new ImageRequestDTO(
              "user123",
              "A cityscape",
              OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
              dim[1],
              dim[0],
              "standard",
              "natural",
              1);
      assertThrows(InvalidParameterException.class, () -> validator.validateRequest(request));
    }
  }

  @ParameterizedTest
  @MethodSource("validQualities")
  void shouldAcceptValidQualities(String quality) {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "A portrait",
            OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
            1024,
            1024,
            quality,
            "natural",
            1);
    assertDoesNotThrow(() -> validator.validateRequest(request));
  }

  @ParameterizedTest
  @MethodSource("invalidQualities")
  void shouldRejectInvalidQualities(String quality) {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "A portrait",
            OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
            1024,
            1024,
            quality,
            "natural",
            1);
    assertThrows(InvalidParameterException.class, () -> validator.validateRequest(request));
  }

  @Test
  void shouldRejectHdQualityForDallE2() {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "A painting",
            OpenAiImageApi.ImageModel.DALL_E_2.getValue(),
            256,
            256,
            "hd",
            "natural",
            1);
    assertThrows(InvalidParameterException.class, () -> validator.validateRequest(request));
  }

  @ParameterizedTest
  @MethodSource("validStyles")
  void shouldAcceptValidStyles(String style) {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "An abstract concept",
            OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
            1024,
            1024,
            "standard",
            style,
            1);
    assertDoesNotThrow(() -> validator.validateRequest(request));
  }

  @ParameterizedTest
  @MethodSource("invalidStyles")
  void shouldRejectInvalidStyles(String style) {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "An abstract concept",
            OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
            1024,
            1024,
            "standard",
            style,
            1);
    assertThrows(InvalidParameterException.class, () -> validator.validateRequest(request));
  }

  @ParameterizedTest
  @MethodSource("validNumImages")
  void shouldAcceptValidNumImagesForDallE2(int numImages) {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "Multiple images",
            OpenAiImageApi.ImageModel.DALL_E_2.getValue(),
            256,
            256,
            "standard",
            "natural",
            numImages);
    assertDoesNotThrow(() -> validator.validateRequest(request));
  }

  @Test
  void shouldEnforceSingleImageForDallE3() {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "Single image only",
            OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
            1024,
            1024,
            "standard",
            "natural",
            2);
    assertThrows(InvalidParameterException.class, () -> validator.validateRequest(request));
  }

  @Test
  void shouldAcceptSingleImageForDallE3() {
    ImageRequestDTO request =
        new ImageRequestDTO(
            "user123",
            "Valid single image",
            OpenAiImageApi.ImageModel.DALL_E_3.getValue(),
            1024,
            1024,
            "standard",
            "natural",
            1);
    assertDoesNotThrow(() -> validator.validateRequest(request));
  }

  @Test
  void shouldHandleNullRequest() {
    assertThrows(NullPointerException.class, () -> validator.validateRequest(null));
  }

  @Test
  void shouldHandleCaseInsensitiveModelNames() {
    ImageRequestDTO request1 =
        new ImageRequestDTO("user123", "Case test", "DALL-E-2", 256, 256, "standard", "natural", 1);
    ImageRequestDTO request2 =
        new ImageRequestDTO(
            "user123", "Case test", "dall-e-3", 1024, 1024, "standard", "natural", 1);

    assertDoesNotThrow(() -> validator.validateRequest(request1));
    assertDoesNotThrow(() -> validator.validateRequest(request2));
  }
}
