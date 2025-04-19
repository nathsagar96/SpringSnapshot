# 🎨 SpringSnapshot

Generate high-quality images using OpenAI's DALL-E models through a simple REST API built with Spring Boot and Spring AI.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/technologies/downloads/#java21)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.4-brightgreen)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M7-blue)](https://docs.spring.io/spring-ai/reference/)

## 📑 Table of Contents

- [🎨 SpringSnapshot](#-springsnapshot)
  - [📑 Table of Contents](#-table-of-contents)
  - [✨ Features](#-features)
  - [🚀 Installation](#-installation)
  - [📖 Usage](#-usage)
  - [📚 API Reference](#-api-reference)
    - [Image Generation Endpoint](#image-generation-endpoint)
      - [Request Body Parameters:](#request-body-parameters)
  - [🛠 Technology Stack](#-technology-stack)
  - [🤝 Contributing](#-contributing)
  - [📄 License](#-license)
  - [📫 Contact](#-contact)
  - [🙏 Acknowledgements](#-acknowledgements)

## ✨ Features

- Generate images using DALL-E 2 and DALL-E 3 models
- Customize image dimensions, quality, and style
- Generate multiple images (DALL-E 2)
- Input validation and error handling
- RESTful API interface
- Spring Boot Actuator integration

## 🚀 Installation

1. Clone the repository
```bash
git clone https://github.com/nathsagar96/SpringSnapshot.git
cd SpringSnapshot
```

2. Set up your OpenAI API key
```bash
export OPENAI_API_KEY=your_api_key_here
```

3. Build and run the application
```bash
./mvnw spring-boot:run
```

## 📖 Usage

Generate an image using cURL:

```bash
curl -X POST http://localhost:8080/api/v1/images/generate \
-H "Content-Type: application/json" \
-d '{
    "userId": "user123",
    "prompt": "A beautiful sunset over mountains",
    "model": "dall-e-3",
    "height": 1024,
    "width": 1024,
    "quality": "standard",
    "style": "vivid",
    "numImages": 1
}'
```

## 📚 API Reference

### Image Generation Endpoint

`POST /api/v1/images/generate`

#### Request Body Parameters:
- `userId`: User identifier
- `prompt`: Image description
- `model`: Either "dall-e-2" or "dall-e-3"
- `height`: Image height in pixels
- `width`: Image width in pixels
- `quality`: "standard" or "hd" (HD available for DALL-E 3 only)
- `style`: "natural" or "vivid"
- `numImages`: Number of images (1-10 for DALL-E 2, 1 for DALL-E 3)

## 🛠 Technology Stack

- Java 21
- Spring Boot 3.4.4
- Spring AI
- Spring Validation
- Spring Actuator
- Maven
- JUnit 5
- Mockito

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📫 Contact

Sagar Nath - [@nathsagar96](https://github.com/nathsagar96)

## 🙏 Acknowledgements

- [Spring AI](https://docs.spring.io/spring-ai/reference/) for the AI integration framework
- [OpenAI](https://openai.com/) for the DALL-E models
- Spring Boot team for the excellent framework
