# Project Documentation: Market Depth Visualizer (v2)

## Project Overview
The Market Depth Visualizer is a Spring Boot microservice that fetches the live order book for a specific cryptocurrency market from the Quidax API. An order book consists of all open buy orders (bids) and sell orders (asks).

This service correctly handles the unique JSON structure of the Quidax depth endpoint, which uses arrays to represent individual orders for network efficiency.

`GET /api/v1/depth/{market}`: Fetches the current order book (bids and asks) for a given market pair (e.g., btcngn).

## Core Dependencies
- **spring-boot-starter-web**: Provides all necessary components for building REST APIs, including an embedded Tomcat server and the Jackson JSON library.
- **lombok**: A utility library used to reduce boilerplate code like getters and setters.

## Project Structure and Components
The DTO package is designed to accurately model the complex, nested JSON response from the Quidax API.

```
/dto/
 ├── Order.java           (Innermost: A single order [price, volume])
 ├── OrderBook.java       (Middle Layer: Contains timestamp, asks, and bids)
 └── DepthResponse.java   (Wrapper for the API's top-level response)
/service/
 └── DepthService.java    (Business Logic and API communication)
/controller/
 └── DepthController.java (API Endpoint Layer)
```

## Detailed Class Explanations

### The DTO Layer (The Data Models)
The DTOs are designed to exactly match the structure of the JSON response.

#### 📄 Order.java
**Purpose**: Represents a single order. It's specially configured to handle being deserialized from a JSON array (["price", "volume"]) instead of a JSON object.

**Code**:

```java
@Data
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({ "price", "volume" })
public class Order {
    private String price;
    private String volume;
}
```

#### 📄 OrderBook.java
**Purpose**: Represents the main data payload, containing the timestamp and the lists of buy and sell orders.

**Code**:

```java
@Data
public class OrderBook {
    private long timestamp;
    private List<Order> asks;
    private List<Order> bids;
}
```

#### 📄 DepthResponse.java
**Purpose**: Represents the top-level wrapper object for the /depth endpoint response.

**Code**:

```java
@Data
public class DepthResponse {
    private String status;
    private String message;
    private OrderBook data;
}
```

### service/DepthService.java - The Business Logic
This class communicates with the Quidax API and unwraps the nested JSON to extract the OrderBook.

**getOrderBook(String market) Method**:
- **Action**: Calls the `https://api.quidax.com/api/v1/markets/{market}/depth` endpoint.
- **Logic**: It tells RestTemplate to expect a DepthResponse object. It then drills down (`response.getData()`) to extract and return the final OrderBook object.

**Code**:

```java
@Service
public class DepthService {

    private final RestTemplate restTemplate;

    public DepthService() {
        this.restTemplate = new RestTemplate();
    }

    public OrderBook getOrderBook(String market) {
        String url = "https://api.quidax.com/api/v1/markets/" + market + "/depth";

        DepthResponse response = restTemplate.getForObject(url, DepthResponse.class);

        if (response != null && "success".equals(response.getStatus())) {
            return response.getData();
        }

        return null;
    }
}
```

### controller/DepthController.java - The API Layer

This class handles incoming HTTP requests and delegates the work to the DepthService.

#### getOrderBookForMarket(@PathVariable String market) Method:

- **Action**: Handles GET requests to `/api/v1/depth/{market}`.

- **Logic**: It uses `@PathVariable` to capture the market pair from the URL and passes it to the DepthService. It returns the OrderBook provided by the service, which Spring Boot automatically converts to JSON.

#### Code:

```java
@RestController
@RequestMapping("/api/v1/depth")
public class DepthController {

    private final DepthService depthService;

    public DepthController(DepthService depthService) {
        this.depthService = depthService;
    }

    @GetMapping("/{market}")
    public OrderBook getOrderBookForMarket(@PathVariable String market) {
        return depthService.getOrderBook(market);
    }
}
```

----------------------------------------------------------------------

# Core Purpose & Architectural Goal

This application serves as a specialized microservice whose primary function is to abstract the complexity of the Quidax depth endpoint. Its architectural goal is to provide a clean, well-structured, and reliable data source for any client (e.g., a frontend trading interface) that needs to visualize order book data.

The core business logic involves two key transformations:

- **Protocol Translation**: It translates a simple RESTful GET request into a specific, and potentially complex, external API call.

- **Data Structure Transformation**: It deserializes an efficient but unconventional JSON structure (using nested objects and value arrays) into a standard, developer-friendly Java object graph.

# Application Architecture & Design Principles

The application adheres to the fundamental principle of Separation of Concerns through a classic three-layer architecture. Each layer has a distinct responsibility, which makes the system more modular, testable, and maintainable.

1. **Controller Layer (DepthController.java)**: This is the HTTP Interface Layer. Its sole responsibility is to handle the mechanics of the HTTP protocol. It uses Spring MVC annotations (`@RestController`, `@RequestMapping`, `@GetMapping`, `@PathVariable`) to map incoming URLs to Java methods and extract parameters. It contains no business logic; it simply delegates tasks to the service layer.

2. **Service Layer (DepthService.java)**: This is the Business Logic Layer. It acts as a Facade to the external Quidax API, encapsulating all the logic required to fetch and prepare the order book data. Its responsibilities include constructing the correct external URL, executing the `RestTemplate` call, and orchestrating the "unwrapping" of the final OrderBook object from the nested response.

3. **DTO Layer (Data Structures)**: This is the Data Contract Layer. These classes are critical as they form a precise contract that describes the expected shape of the external JSON. A failure in this layer, as we saw during debugging, causes the entire data-fetching operation to fail.

# Deep Dive: The Jackson Deserialization Strategy

The most significant technical challenge in this project was correctly modeling the unconventional JSON response from the Quidax depth endpoint. The API prioritizes network efficiency by sending each order as a compact array (["price", "volume"]) rather than a more verbose object ({"price": "...", "volume": "..."}). Our solution demonstrates a sophisticated and robust approach to handling this.

## The DTO Hierarchy

We modeled the nested response with three distinct classes, each representing a layer of the JSON document:

- **DepthResponse.java**: Models the outermost wrapper, containing the request status and the primary data payload.

- **OrderBook.java**: Models the data object, which contains the timestamp and the two lists that make up the order book: asks and bids.

- **Order.java**: This is the most critical DTO. It models the innermost data point—a single order—which is transmitted as a JSON array.

## The Array-to-Object Mapping Strategy

To instruct the Jackson parser how to convert a JSON array into a Java object, we used a powerful and unambiguous annotation-based strategy on the `Order.java` class:

```java
@Data
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({ "price", "volume" })
public class Order {
    private String price;
    private String volume;
}
```

@JsonFormat(shape = JsonFormat.Shape.ARRAY): This is an explicit directive to the parser. It declares that any instance of the Order class, when being serialized or deserialized, corresponds to a JSON array. This removes the ambiguity that caused our previous MismatchedInputException.

@JsonPropertyOrder({ "price", "volume" }): This annotation provides the necessary metadata for the array mapping. It tells Jackson that the first element (index 0) of the JSON array should be mapped to the price field, and the second element (index 1) should be mapped to the volume field. The order of the field names in this annotation is critical and must match the order in the JSON array.