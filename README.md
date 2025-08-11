# Quidax Market Depth API 📊

A Spring Boot REST API that fetches the live order book (market depth) for a specific cryptocurrency market from the public Quidax API.

This service is specifically designed to handle the Quidax API's efficient data structure, where individual orders are sent as JSON arrays `(["price", "volume"])`, and provides a clean, developer-friendly JSON object as output.

## Features
- Fetches the complete, real-time order book (bids and asks) for a given market.
- Correctly models and parses the complex nested and array-based JSON from the Quidax depth endpoint.
- Built with a clean, layered architecture (Controller, Service, DTO).

## Technologies Used
- Java 17
- Spring Boot 3
- Jackson Annotations (`@JsonFormat`, `@JsonPropertyOrder`)
- Maven
- Lombok

## API Endpoint

| Method | Endpoint                     | Description                                                       |
|--------|------------------------------|-------------------------------------------------------------------|
| GET    | `/api/v1/depth/{market}`    | Get the current order book (bids and asks) for a specific market. |

## Export to Sheets

### Example Usage:
A GET request to `http://localhost:8080/api/v1/depth/btcngn` will return a JSON object containing the timestamp and the lists of ask and bid orders:

```json
{
    "timestamp": 1753911821,
    "asks": [
        {
            "price": "180598206.0",
            "volume": "0.66481826"
        },
        {
            "price": "180581815.0",
            "volume": "2.17969402"
        }
    ],
    "bids": [
        {
            "price": "180122860.0",
            "volume": "2.37954535"
        },
        {
            "price": "180106469.0",
            "volume": "0.3291501"
        }
    ]
}
```

## Getting Started

To get a local copy up and running, follow these simple steps.

### Prerequisites
- JDK (Java Development Kit) 17 or later
- Maven

### Installation & Running the App
1. Clone the repository:

   ```bash
   git clone https://github.com/your-username/market-depth-visualizer.git
   ```

2. Navigate to the project directory:

   ```bash
   cd market-depth-visualizer
   ```

3. Run the application using the Maven wrapper:

   On macOS/Linux:
   ```bash
   ./mvnw spring-boot:run
   ```

   On Windows:
   ```bash
   mvnw.cmd spring-boot:run
   ```

The application will start on `http://localhost:8080`.