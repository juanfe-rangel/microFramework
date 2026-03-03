# MicroFramework - Java Micro Web Framework

## Project Description

MicroFramework is a lightweight Java web framework designed for building REST APIs and serving static files with minimal configuration. The framework provides a simple and intuitive API that allows developers to define HTTP endpoints using lambda expressions, making it easy to create web services without the complexity of larger frameworks.

## Features

1. **Simple REST API Definition** - Define REST endpoints using lambda expressions
2. **Query Parameter Extraction** - Easy access to query parameters from requests
3. **Static File Serving** - Serve static HTML, CSS, JS, and other files
4. **Developer-Friendly API** - Clean and intuitive API design

## Getting Started

### Example Application

```java
package org.example;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.example.utilities.HttpServer.*;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        // Set the location of static files
        staticfiles("/webroot");
        
        // Define REST endpoints with query parameters
        get("/App/hello", (req, resp) -> "Hello " + req.getValues("name"));
        
        // Define REST endpoints with complex logic
        get("/App/pi", (req, resp) -> {
            return String.valueOf(Math.PI); 
        });
        
        // Start the server on port 8080
        HttpServer.main(args);
    }
}
```

### API Documentation

#### 1. Defining REST Endpoints

Use the `get()` method to define REST endpoints:

```java
get("/path", (req, resp) -> {
    // Your handler logic here
    return "Response string";
});
```

**Parameters:**
- `req` - Request object containing path, method, and query parameters
- `resp` - Response object for setting status, content-type, etc.

#### 2. Accessing Query Parameters

Extract query parameters from the request:

```java
get("/hello", (req, resp) -> {
    String name = req.getValues("name");
    return "Hello " + name;
});
```

**Example Request:**
```
http://localhost:8080/hello?name=Pedro
```

**Response:**
```
Hello Pedro
```

#### 3. Setting Static Files Location

Specify where static files are located:

```java
staticfiles("/webroot");
```

The framework will look for static files in `target/classes/webroot/` directory.

## Building the Project

Compile the project using Maven:

```bash
mvn clean compile
```

## Running the Application

Run the main class to start the server:

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

Or run it from your IDE by executing the `Main` class.

## Testing the Application

### REST Endpoints

Once the server is running on port 8080, test the following endpoints:

1. **Hello Endpoint with Query Parameter:**
   ```
   http://localhost:8080/App/hello?name=Pedro
   ```
   Response: `Hello Pedro`

2. **Pi Constant:**
   ```
   http://localhost:8080/App/pi
   ```
   Response: `3.141592653589793`

### Static Files

Access static files directly:

```
http://localhost:8080/index.html
```

## Examples of Tests Performed

The following tests were executed to verify the framework functionality:

### Test 1: Query Parameter Extraction

**Command:**
```bash
curl "http://localhost:8080/App/hello?name=Pedro"
```

**Expected Result:** `Hello Pedro`

**Actual Result:** `Hello Pedro`

**Status:** PASSED

### Test 2: Lambda Function with Computed Value

**Command:**
```bash
curl "http://localhost:8080/App/pi"
```

**Expected Result:** `3.141592653589793`

**Actual Result:** `3.141592653589793`

**Status:** PASSED

### Test 3: Static File Serving

**Command:**
```bash
curl "http://localhost:8080/index.html"
```

**Expected Result:** HTML file content with correct Content-Type header

**Actual Result:** 
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>MicroFramework Demo</title>
    ...
</head>
<body>
    <h1>Welcome to MicroFramework!</h1>
    ...
</body>
</html>
```

**Status:** PASSED

### Test 4: Query Parameter with Different Values

**Command:**
```bash
curl "http://localhost:8080/App/hello?name=Maria"
```

**Expected Result:** `Hello Maria`

**Actual Result:** `Hello Maria`

**Status:** PASSED

### Test 5: Missing Query Parameter Handling

**Command:**
```bash
curl "http://localhost:8080/App/hello"
```

**Expected Result:** `Hello ` (empty string for missing parameter)

**Actual Result:** `Hello `

**Status:** PASSED

## Framework Architecture

### Core Classes

- **HttpServer** - Main server class that handles HTTP requests
- **WebMethod** - Functional interface for defining endpoint handlers
- **Request** - Contains request information (path, method, query params)
- **Response** - Allows customization of response (status, content-type)

### Request Class

```java
public class Request {
    public String getPath()                    // Get request path
    public String getMethod()                  // Get HTTP method
    public String getValues(String key)        // Get query parameter value
    public Map<String, String> getAllQueryParams()  // Get all parameters
}
```

### Response Class

```java
public class Response {
    public void setBody(String body)           // Set response body
    public void setContentType(String type)    // Set content type
    public void setStatusCode(int code)        // Set HTTP status code
}
```

## Directory Structure

```
microFramework/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── example/
│       │           ├── Main.java                    # Example application
│       │           ├── appexamples/
│       │           │   └── MathServices.java        # Math service examples
│       │           └── utilities/
│       │               ├── HttpServer.java          # Core server
│       │               ├── WebMethod.java           # Handler interface
│       │               ├── Request.java             # Request wrapper
│       │               └── Response.java            # Response wrapper
│       └── resources/
│           └── webroot/
│               └── index.html                       # Static files
└── pom.xml                                          # Maven configuration
```

## Example: Math Services

See `MathServices.java` for more examples:

```java
get("/pi", (req, resp) -> "PI=" + Math.PI);
get("/Hello", (req, resp) -> "Hello World");
get("/euler", (req, resp) -> "e=" + Math.E);
```

## License

This is an educational project for learning web framework development.
