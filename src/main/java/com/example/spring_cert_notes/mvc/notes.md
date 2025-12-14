# NGÀY 15-16: SPRING MVC & REST APIs

## 📚 MỤC TIÊU HỌC TẬP

### 1. Hiểu request processing lifecycle
### 2. Thành thạo @RestController, @RequestMapping, @PathVariable, @RequestParam
### 3. Implement CRUD operations với proper HTTP methods
### 4. Exception handling với @ControllerAdvice

---

## 🎯 PHẦN 1: REQUEST PROCESSING LIFECYCLE

### DispatcherServlet Flow

```
┌─────────────────────────────────────────────────────────────┐
│                 REQUEST PROCESSING FLOW                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  1. HTTP Request                                            │
│         ↓                                                   │
│  2. DispatcherServlet (Front Controller)                    │
│         ↓                                                   │
│  3. HandlerMapping (find controller)                        │
│         ↓                                                   │
│  4. HandlerAdapter (invoke controller)                      │
│         ↓                                                   │
│  5. Controller (process request)                            │
│         ↓                                                   │
│  6. ViewResolver (resolve view) [MVC only]                  │
│         ↓                                                   │
│  7. View (render response) [MVC only]                       │
│         ↓                                                   │
│  8. HTTP Response                                           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### REST API Flow (simplified)

```
Request → DispatcherServlet → HandlerMapping → Controller → Response
                                                    ↓
                                          HttpMessageConverter
                                          (JSON/XML conversion)
```

---

## 🎯 PHẦN 2: CONTROLLER ANNOTATIONS

### @Controller vs @RestController

```java
// MVC Controller - returns view names
@Controller
public class WebController {
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Hello");
        return "home";  // View name
    }
}

// REST Controller - returns data directly
@RestController  // = @Controller + @ResponseBody
public class ApiController {
    @GetMapping("/api/data")
    public Data getData() {
        return new Data();  // Converted to JSON
    }
}
```

### @RequestMapping

```java
@RestController
@RequestMapping("/api/users")  // Base path
public class UserController {
    
    @GetMapping           // GET /api/users
    @GetMapping("/{id}")  // GET /api/users/{id}
    @PostMapping          // POST /api/users
    @PutMapping("/{id}")  // PUT /api/users/{id}
    @PatchMapping("/{id}")// PATCH /api/users/{id}
    @DeleteMapping("/{id}")// DELETE /api/users/{id}
}
```

**Xem code:** `UserController.java`

---

## 🎯 PHẦN 3: PARAMETER BINDING

### @PathVariable - URL path segments

```java
// /api/users/123
@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) { }

// /api/users/123/orders/456
@GetMapping("/users/{userId}/orders/{orderId}")
public Order getOrder(
    @PathVariable Long userId,
    @PathVariable Long orderId) { }

// Different name
@GetMapping("/users/{user_id}")
public User getUser(@PathVariable("user_id") Long id) { }
```

### @RequestParam - Query parameters

```java
// /api/users?page=0&size=10
@GetMapping("/users")
public Page<User> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) { }

// Optional parameter
@GetMapping("/search")
public List<User> search(
    @RequestParam(required = false) String name) { }

// Multiple values: /api/users?ids=1,2,3
@GetMapping("/users")
public List<User> getUsers(@RequestParam List<Long> ids) { }
```

### @RequestHeader - HTTP headers

```java
@GetMapping("/info")
public Info getInfo(
    @RequestHeader("User-Agent") String userAgent,
    @RequestHeader(value = "X-Custom", required = false) String custom) { }
```

### @RequestBody - Request body

```java
@PostMapping("/users")
public User createUser(@RequestBody UserDto dto) { }

// With validation
@PostMapping("/users")
public User createUser(@Valid @RequestBody UserDto dto) { }
```

**Xem code:** `ParameterExamplesController.java`

---

## 🎯 PHẦN 4: COMPLETE CRUD API

### HTTP Methods

| Method | Operation | URL | Request Body | Response |
|--------|-----------|-----|--------------|----------|
| GET | Read all | /api/users | - | 200 + List |
| GET | Read one | /api/users/{id} | - | 200 + Object |
| POST | Create | /api/users | Object | 201 + Object |
| PUT | Update | /api/users/{id} | Object | 200 + Object |
| PATCH | Partial | /api/users/{id} | Partial | 200 + Object |
| DELETE | Delete | /api/users/{id} | - | 204 |

### ResponseEntity

```java
// 200 OK
return ResponseEntity.ok(user);

// 201 Created with Location header
URI location = URI.create("/api/users/" + user.getId());
return ResponseEntity.created(location).body(user);

// 204 No Content
return ResponseEntity.noContent().build();

// 404 Not Found
return ResponseEntity.notFound().build();

// Custom status
return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);

// With headers
return ResponseEntity.ok()
    .header("X-Custom", "value")
    .body(data);
```

### @ResponseStatus

```java
@DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void delete(@PathVariable Long id) {
    service.delete(id);
}
```

**Xem code:** `UserController.java`

---

## 🎯 PHẦN 5: VALIDATION

### Bean Validation Annotations

```java
public class UserDto {
    
    @NotNull(message = "ID is required")
    private Long id;
    
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be 2-50 chars")
    private String name;
    
    @Email(message = "Invalid email")
    private String email;
    
    @Min(value = 0, message = "Age must be positive")
    @Max(value = 150, message = "Age must be < 150")
    private Integer age;
    
    @Pattern(regexp = "^\\d{10}$", message = "Invalid phone")
    private String phone;
}
```

### Using @Valid

```java
@PostMapping
public ResponseEntity<User> create(@Valid @RequestBody UserDto dto) {
    // If validation fails, MethodArgumentNotValidException is thrown
    return ResponseEntity.ok(service.create(dto));
}
```

**Xem code:** `UserDto.java`

---

## 🎯 PHẦN 6: EXCEPTION HANDLING

### @ControllerAdvice / @RestControllerAdvice

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, WebRequest request) {
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Not Found",
            ex.getMessage(),
            request.getDescription(false)
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        
        ErrorResponse error = new ErrorResponse();
        error.setStatus(400);
        error.setMessage("Validation failed");
        
        ex.getBindingResult().getFieldErrors().forEach(e ->
            error.addFieldError(e.getField(), e.getDefaultMessage())
        );
        
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        ErrorResponse error = new ErrorResponse(
            500, "Internal Server Error", "An error occurred", null
        );
        return ResponseEntity.status(500).body(error);
    }
}
```

**Xem code:** `GlobalExceptionHandler.java`, `ResourceNotFoundException.java`

---

## 🎯 PHẦN 7: RESTTEMPLATE CLIENT

### GET Requests

```java
RestTemplate restTemplate = new RestTemplate();

// Simple GET
User user = restTemplate.getForObject(url, User.class);

// GET with ResponseEntity
ResponseEntity<User> response = restTemplate.getForEntity(url, User.class);
User user = response.getBody();
HttpStatus status = response.getStatusCode();

// GET with path variables
User user = restTemplate.getForObject(
    "/api/users/{id}", User.class, 123);
```

### POST Requests

```java
// Simple POST
User created = restTemplate.postForObject(url, userDto, User.class);

// POST with ResponseEntity
ResponseEntity<User> response = restTemplate.postForEntity(url, userDto, User.class);
```

### PUT/DELETE

```java
// PUT
restTemplate.put(url + "/" + id, userDto);

// DELETE
restTemplate.delete(url + "/" + id);
```

### Exchange (flexible)

```java
HttpHeaders headers = new HttpHeaders();
headers.setContentType(MediaType.APPLICATION_JSON);
headers.set("Authorization", "Bearer token");

HttpEntity<UserDto> request = new HttpEntity<>(userDto, headers);

ResponseEntity<User> response = restTemplate.exchange(
    url,
    HttpMethod.POST,
    request,
    User.class
);
```

**Xem code:** `RestTemplateExamples.java`

---

## 🎯 PHẦN 8: CONTENT NEGOTIATION

### Accept Header

```java
// Client requests JSON
Accept: application/json

// Client requests XML
Accept: application/xml
```

### Content-Type Header

```java
// Client sends JSON
Content-Type: application/json

// Client sends XML
Content-Type: application/xml
```

### produces/consumes

```java
@GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public List<User> getUsers() { }

@PostMapping(value = "/users", 
             consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE)
public User createUser(@RequestBody UserDto dto) { }
```

---

## ✅ CHECKLIST HOÀN THÀNH

### Controller Basics
- [ ] @RestController vs @Controller
- [ ] @RequestMapping, @GetMapping, @PostMapping, etc.
- [ ] @PathVariable, @RequestParam, @RequestHeader
- [ ] @RequestBody, @ResponseBody

### CRUD Operations
- [ ] GET - Read (200 OK)
- [ ] POST - Create (201 Created)
- [ ] PUT - Update (200 OK)
- [ ] PATCH - Partial update
- [ ] DELETE - Delete (204 No Content)

### ResponseEntity
- [ ] ResponseEntity.ok()
- [ ] ResponseEntity.created()
- [ ] ResponseEntity.noContent()
- [ ] ResponseEntity.notFound()
- [ ] Custom headers

### Validation
- [ ] @Valid annotation
- [ ] @NotNull, @NotBlank, @Size
- [ ] @Email, @Min, @Max, @Pattern
- [ ] Handle MethodArgumentNotValidException

### Exception Handling
- [ ] @ControllerAdvice / @RestControllerAdvice
- [ ] @ExceptionHandler
- [ ] Custom exception classes
- [ ] ErrorResponse DTO

### RestTemplate
- [ ] getForObject / getForEntity
- [ ] postForObject / postForEntity
- [ ] put / delete
- [ ] exchange (flexible)

---

## 🚀 CÁCH TEST API

### Sử dụng curl

```bash
# GET all
curl http://localhost:8080/api/users

# GET by id
curl http://localhost:8080/api/users/1

# POST
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"firstName":"John","lastName":"Doe","email":"john@example.com"}'

# PUT
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Jane","lastName":"Doe","email":"jane@example.com"}'

# DELETE
curl -X DELETE http://localhost:8080/api/users/1
```

---

## 📝 GHI CHÚ QUAN TRỌNG

### Best Practices

1. **Use proper HTTP methods**
2. **Return appropriate status codes**
3. **Use @Valid for input validation**
4. **Centralize exception handling**
5. **Use DTOs instead of entities**

### Common Mistakes

1. ❌ Using GET for modifications
2. ❌ Returning 200 for everything
3. ❌ Missing validation
4. ❌ Exposing entities directly
5. ❌ Not handling exceptions

---

## 📚 FILES TRONG PACKAGE

**Controllers:**
1. `UserController.java` - Complete CRUD
2. `ParameterExamplesController.java` - Parameter binding

**DTOs:**
3. `UserDto.java` - With validation
4. `ErrorResponse.java` - Error response

**Exceptions:**
5. `ResourceNotFoundException.java` - Custom exception
6. `GlobalExceptionHandler.java` - @ControllerAdvice

**Service:**
7. `UserService.java` - Business logic

**Client:**
8. `RestTemplateExamples.java` - HTTP client
