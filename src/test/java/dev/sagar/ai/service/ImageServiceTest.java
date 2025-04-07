package dev.sagar.ai.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import dev.sagar.ai.dto.ImageRequestDTO;
import dev.sagar.ai.dto.ImageResponseDTO;
import dev.sagar.ai.exception.InvalidParameterException;
import dev.sagar.ai.validator.ImageRequestValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.image.*;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

  private final String testUserId = "user123";
  private final String testModel = OpenAiImageApi.ImageModel.DALL_E_3.getValue();
  private final int testWidth = 1024;
  private final int testHeight = 1024;
  private final String testQuality = "standard";
  private final String testStyle = "vivid";
  private final int testNumImages = 1;
  private final String testImageUrl = "https://example.com/image.png";

  @Mock private ImageModel imageModel;
  @Mock private ImageResponse imageResponse;
  @Mock private ImageGeneration imageGeneration;
  @Mock private  Image image;
  @Mock private ImageRequestValidator validator;
  @InjectMocks private ImageService imageService;

  @Test
  void generateImage_shouldValidateRequestBeforeProcessing() {
    ImageRequestDTO request = createValidRequest();
    mockSuccessfulImageGeneration();

    imageService.generateImage(request);

    verify(validator).validateRequest(request);
  }

  @Test
  void generateImage_shouldThrowWhenValidationFails() {
    ImageRequestDTO request = createValidRequest();
    doThrow(new InvalidParameterException("Invalid request"))
        .when(validator)
        .validateRequest(request);

    assertThrows(InvalidParameterException.class, () -> imageService.generateImage(request));

    verify(imageModel, never()).call(any());
  }

  @Test
  void generateImage_shouldBuildCorrectOptions() {
    ImageRequestDTO request = createValidRequest();
    mockSuccessfulImageGeneration();

    imageService.generateImage(request);

    verify(imageModel)
        .call(
            argThat(
                (ImagePrompt prompt) -> {
                  OpenAiImageOptions options = (OpenAiImageOptions) prompt.getOptions();
                  return options.getUser().equals(testUserId)
                      && options.getModel().equals(testModel)
                      && options.getHeight() == testHeight
                      && options.getWidth() == testWidth
                      && options.getQuality().equals(testQuality)
                      && options.getStyle().equals(testStyle)
                      && options.getN() == testNumImages;
                }));
  }

  @Test
  void generateImage_shouldReturnCorrectResponse() {
    ImageRequestDTO request = createValidRequest();
    ImageResponse mockResponse = mockSuccessfulImageGeneration();

    ImageResponseDTO response = imageService.generateImage(request);

    assertEquals(testImageUrl, response.imageUrl());
    verify(imageModel).call(any(ImagePrompt.class));
  }

  @Test
  void generateImage_shouldPropagateModelExceptions() {
    ImageRequestDTO request = createValidRequest();
    when(imageModel.call(any(ImagePrompt.class))).thenThrow(new RuntimeException("API Error"));

    assertThrows(RuntimeException.class, () -> imageService.generateImage(request));
  }

  @Test
  void generateImage_shouldHandleNullMetadata() {
    ImageRequestDTO request = createValidRequest();
    when(imageResponse.getResult()).thenReturn(imageGeneration);
    when(imageGeneration.getOutput()).thenReturn(image);
    when(image.getUrl()).thenReturn(testImageUrl);
    when(imageModel.call(any(ImagePrompt.class))).thenReturn(imageResponse);

    ImageResponseDTO response = imageService.generateImage(request);

    assertEquals(testImageUrl, response.imageUrl());
    assertNull(response.metadata());
  }

  private ImageRequestDTO createValidRequest() {
    String testPrompt = "A beautiful sunset over mountains";
    return new ImageRequestDTO(
        testUserId,
        testPrompt,
        testModel,
        testHeight,
        testWidth,
        testQuality,
        testStyle,
        testNumImages);
  }

  private ImageResponse mockSuccessfulImageGeneration() {
    when(imageResponse.getResult()).thenReturn(imageGeneration);
    when(imageResponse.getResult().getOutput()).thenReturn(image);
    when(image.getUrl()).thenReturn(testImageUrl);
    when(imageModel.call(any(ImagePrompt.class))).thenReturn(imageResponse);

    return imageResponse;
  }
}
