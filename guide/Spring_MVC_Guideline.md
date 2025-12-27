# SPRING MVC
## Guideline Chi Tiết cho Spring Professional Certification

---

**Tài liệu ôn tập toàn diện về Spring MVC Framework**

*Tạo ngày: 26/12/2024*

---

## MỤC LỤC

1. [Giới thiệu về Spring MVC](#1-giới-thiệu-về-spring-mvc)
2. [Spring MVC Architecture](#2-spring-mvc-architecture)
3. [Controllers và Request Mapping](#3-controllers-và-request-mapping)
4. [Request Parameters và Path Variables](#4-request-parameters-và-path-variables)
5. [Request và Response Body](#5-request-và-response-body)
6. [Model và View](#6-model-và-view)
7. [View Resolvers](#7-view-resolvers)
8. [Form Handling và Data Binding](#8-form-handling-và-data-binding)
9. [Validation](#9-validation)
10. [Exception Handling](#10-exception-handling)
11. [Interceptors](#11-interceptors)
12. [REST APIs](#12-rest-apis)
13. [Content Negotiation](#13-content-negotiation)
14. [Async Processing](#14-async-processing)
15. [File Upload và Download](#15-file-upload-và-download)
16. [CORS Configuration](#16-cors-configuration)
17. [Best Practices](#17-best-practices)
18. [Câu hỏi mẫu cho kỳ thi](#18-câu-hỏi-mẫu-cho-kỳ-thi)
19. [Tóm tắt và mẹo thi](#19-tóm-tắt-và-mẹo-thi)

---

## 1. GIỚI THIỆU VỀ SPRING MVC

### 1.1. Spring MVC là gì?

**Spring MVC** (Model-View-Controller) là một web framework trong Spring Framework, được xây dựng trên Servlet API và là implementation của MVC design pattern.

**Core Features:**
- ✅ Request-driven MVC framework
- ✅ Built on Servlet API
- ✅ Flexible và extensible
- ✅ RESTful web services support
- ✅ Strong validation support
- ✅ Multiple view technologies

### 1.2. MVC Pattern

```
┌────────────────────────────────────────────┐
│           MVC Pattern                      │
├────────────────────────────────────────────┤
│                                            │
│  User → Controller → Model → View → User  │
│                                            │
│  Controller: Handles requests              │
│  Model: Business logic & data              │
│  View: Presentation layer                  │
│                                            │
└────────────────────────────────────────────┘
```

### 1.3. Tại sao dùng Spring MVC?

**Lợi ích:**
- 🎯 Loose coupling giữa layers
- 🎯 Testable components
- 🎯 Flexible configuration
- 🎯 Rich annotation support
- 🎯 RESTful API development
- 🎯 Content negotiation
- 🎯 Async request processing

---

## 2. SPRING MVC ARCHITECTURE

### 2.1. Request Processing Flow

```
┌──────────────────────────────────────────────────────┐
│         Spring MVC Request Flow                      │
├──────────────────────────────────────────────────────┤
│                                                      │
│  1. HTTP Request                                     │
│       ↓                                              │
│  2. DispatcherServlet (Front Controller)             │
│       ↓                                              │
│  3. HandlerMapping (Find Controller)                 │
│       ↓                                              │
│  4. Controller (Process Request)                     │
│       ↓                                              │
│  5. ModelAndView (Return Data & View)                │
│       ↓                                              │
│  6. ViewResolver (Resolve View Name)                 │
│       ↓                                              │
│  7. View (Render Response)                           │
│       ↓                                              │
│  8. HTTP Response                                    │
│                                                      │
└──────────────────────────────────────────────────────┘
```

### 2.2. Core Components

#### DispatcherServlet

**Front Controller** - central entry point cho all requests.

```java
// web.xml configuration (legacy)
<servlet>
    <servlet-name>dispatcher</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/spring/mvc-config.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>dispatcher</servlet-name>
    <url-pattern>/</url-pattern>
</servlet-mapping>
```

**Java Configuration:**

```java
public class WebAppInitializer implements WebApplicationInitializer {
    
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = 
            new AnnotationConfigWebApplicationContext();
        context.register(WebConfig.class);
        
        DispatcherServlet servlet = new DispatcherServlet(context);
        ServletRegistration.Dynamic registration = 
            servletContext.addServlet("dispatcher", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
```

**Spring Boot (automatic):**

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
// DispatcherServlet automatically configured
```

#### HandlerMapping

Maps requests to handlers (controllers).

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Bean
    public HandlerMapping handlerMapping() {
        RequestMappingHandlerMapping mapping = 
            new RequestMappingHandlerMapping();
        mapping.setOrder(0);
        return mapping;
    }
}
```

#### HandlerAdapter

Executes the handler (controller method).

#### ViewResolver

Resolves view names to actual views.

```java
@Bean
public ViewResolver viewResolver() {
    InternalResourceViewResolver resolver = 
        new InternalResourceViewResolver();
    resolver.setPrefix("/WEB-INF/views/");
    resolver.setSuffix(".jsp");
    return resolver;
}
```

### 2.3. Enable Spring MVC

```java
@Configuration
@EnableWebMvc
@ComponentScan("com.example.web")
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                .addResourceLocations("/static/");
    }
}
```

---

## 3. CONTROLLERS VÀ REQUEST MAPPING

### 3.1. @Controller vs @RestController

```java
// @Controller - for traditional web apps (returns views)
@Controller
public class UserController {
    
    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/list"; // Returns view name
    }
}

// @RestController - for REST APIs (returns data)
@RestController
public class UserRestController {
    
    @GetMapping("/api/users")
    public List<User> listUsers() {
        return userService.findAll(); // Returns JSON/XML
    }
}
```

> 💡 **@RestController = @Controller + @ResponseBody**

### 3.2. @RequestMapping

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    // GET /users
    @RequestMapping(method = RequestMethod.GET)
    public String list() {
        return "users/list";
    }
    
    // POST /users
    @RequestMapping(method = RequestMethod.POST)
    public String create(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users";
    }
    
    // Multiple methods
    @RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
    public String search() {
        return "users/search";
    }
    
    // With headers
    @RequestMapping(value = "/api", headers = "API-Version=1")
    public String apiV1() {
        return "api-v1";
    }
    
    // With params
    @RequestMapping(value = "/search", params = "type=advanced")
    public String advancedSearch() {
        return "users/advanced-search";
    }
    
    // With consumes/produces
    @RequestMapping(
        value = "/api",
        method = RequestMethod.POST,
        consumes = "application/json",
        produces = "application/json"
    )
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }
}
```

### 3.3. Shortcut Annotations

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    @GetMapping                          // GET /users
    public String list() { }
    
    @GetMapping("/{id}")                 // GET /users/123
    public String view(@PathVariable Long id) { }
    
    @PostMapping                         // POST /users
    public String create(@ModelAttribute User user) { }
    
    @PutMapping("/{id}")                 // PUT /users/123
    public String update(@PathVariable Long id, @ModelAttribute User user) { }
    
    @DeleteMapping("/{id}")              // DELETE /users/123
    public String delete(@PathVariable Long id) { }
    
    @PatchMapping("/{id}")               // PATCH /users/123
    public String patch(@PathVariable Long id) { }
}
```

### 3.4. Request Mapping Patterns

```java
@Controller
public class PatternController {
    
    // Exact match
    @GetMapping("/users")
    public String exact() { }
    
    // Path variable
    @GetMapping("/users/{id}")
    public String pathVariable(@PathVariable Long id) { }
    
    // Multiple path variables
    @GetMapping("/users/{userId}/orders/{orderId}")
    public String multiple(@PathVariable Long userId, 
                          @PathVariable Long orderId) { }
    
    // Wildcard
    @GetMapping("/files/*")
    public String wildcard() { }
    
    // Double wildcard (any depth)
    @GetMapping("/files/**")
    public String doubleWildcard() { }
    
    // Regex pattern
    @GetMapping("/users/{id:[0-9]+}")
    public String regex(@PathVariable Long id) { }
    
    // Extension
    @GetMapping("/users/{id}.json")
    public String jsonExtension(@PathVariable Long id) { }
}
```

---

## 4. REQUEST PARAMETERS VÀ PATH VARIABLES

### 4.1. @RequestParam

```java
@Controller
public class ParamController {
    
    // Simple parameter
    // GET /search?name=John
    @GetMapping("/search")
    public String search(@RequestParam String name) {
        return "search";
    }
    
    // Optional parameter
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String name) {
        return "search";
    }
    
    // With default value
    @GetMapping("/search")
    public String search(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return "search";
    }
    
    // Custom parameter name
    @GetMapping("/search")
    public String search(@RequestParam("q") String query) {
        return "search";
    }
    
    // Multiple parameters
    @GetMapping("/search")
    public String search(
        @RequestParam String name,
        @RequestParam int age,
        @RequestParam(required = false) String city
    ) {
        return "search";
    }
    
    // List parameter
    // GET /users?id=1&id=2&id=3
    @GetMapping("/users")
    public String users(@RequestParam List<Long> id) {
        return "users";
    }
    
    // Map of all parameters
    @GetMapping("/search")
    public String search(@RequestParam Map<String, String> params) {
        return "search";
    }
}
```

### 4.2. @PathVariable

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    // Single path variable
    // GET /users/123
    @GetMapping("/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "users/view";
    }
    
    // Custom variable name
    @GetMapping("/{userId}")
    public String view(@PathVariable("userId") Long id) {
        return "users/view";
    }
    
    // Multiple path variables
    // GET /users/123/orders/456
    @GetMapping("/{userId}/orders/{orderId}")
    public String viewOrder(
        @PathVariable Long userId,
        @PathVariable Long orderId,
        Model model
    ) {
        model.addAttribute("order", 
            orderService.findByUserIdAndOrderId(userId, orderId));
        return "orders/view";
    }
    
    // Optional path variable (with regex)
    @GetMapping({"/", "/{id}"})
    public String viewOptional(@PathVariable(required = false) Long id) {
        if (id != null) {
            return "users/view";
        }
        return "users/list";
    }
    
    // Map of all path variables
    @GetMapping("/{userId}/orders/{orderId}")
    public String viewOrder(@PathVariable Map<String, String> pathVars) {
        return "orders/view";
    }
    
    // Regex constraint
    @GetMapping("/{id:[0-9]+}")
    public String view(@PathVariable Long id) {
        return "users/view";
    }
}
```

### 4.3. @RequestHeader

```java
@Controller
public class HeaderController {
    
    @GetMapping("/user-agent")
    public String userAgent(@RequestHeader("User-Agent") String userAgent) {
        return "info";
    }
    
    // Optional header
    @GetMapping("/auth")
    public String auth(
        @RequestHeader(value = "Authorization", required = false) 
        String token
    ) {
        return "auth";
    }
    
    // Multiple headers
    @GetMapping("/headers")
    public String headers(
        @RequestHeader("Accept") String accept,
        @RequestHeader("Content-Type") String contentType
    ) {
        return "headers";
    }
    
    // All headers as map
    @GetMapping("/all-headers")
    public String allHeaders(@RequestHeader Map<String, String> headers) {
        return "headers";
    }
}
```

### 4.4. @CookieValue

```java
@Controller
public class CookieController {
    
    @GetMapping("/cookie")
    public String readCookie(@CookieValue("sessionId") String sessionId) {
        return "cookie";
    }
    
    // Optional cookie
    @GetMapping("/cookie")
    public String readCookie(
        @CookieValue(value = "sessionId", required = false) 
        String sessionId
    ) {
        return "cookie";
    }
    
    // With default value
    @GetMapping("/cookie")
    public String readCookie(
        @CookieValue(value = "theme", defaultValue = "light") 
        String theme
    ) {
        return "cookie";
    }
}
```

### 4.5. @MatrixVariable

```java
@Controller
public class MatrixController {
    
    // GET /users/123;role=admin;status=active
    @GetMapping("/users/{id}")
    public String user(
        @PathVariable Long id,
        @MatrixVariable String role,
        @MatrixVariable String status
    ) {
        return "users/view";
    }
    
    // GET /users/123;q=50/orders/456;q=20
    @GetMapping("/users/{userId}/orders/{orderId}")
    public String order(
        @MatrixVariable(pathVar = "userId") int userQuantity,
        @MatrixVariable(pathVar = "orderId") int orderQuantity
    ) {
        return "orders/view";
    }
}

// Enable matrix variables
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setRemoveSemicolonContent(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
}
```

---

## 5. REQUEST VÀ RESPONSE BODY

### 5.1. @RequestBody

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    // POST with JSON body
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    // PUT with JSON body
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
        @PathVariable Long id,
        @RequestBody User user
    ) {
        User updated = userService.update(id, user);
        return ResponseEntity.ok(updated);
    }
    
    // Validation with @RequestBody
    @PostMapping
    public ResponseEntity<?> createUser(
        @Valid @RequestBody User user,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return ResponseEntity.ok(userService.save(user));
    }
}
```

**Request example:**
```json
POST /api/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30
}
```

### 5.2. @ResponseBody

```java
@Controller
public class UserController {
    
    // Return JSON
    @GetMapping("/api/users/{id}")
    @ResponseBody
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
    
    // Return custom response
    @GetMapping("/api/users")
    @ResponseBody
    public List<User> listUsers() {
        return userService.findAll();
    }
}

// Or use @RestController
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    // @ResponseBody automatic
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.findById(id);
    }
}
```

### 5.3. ResponseEntity

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    // Simple response
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(user);
    }
    
    // Not found response
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // Created response with location
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userService.save(user);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getId())
            .toUri();
        return ResponseEntity.created(location).body(saved);
    }
    
    // Custom headers
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok()
            .header("Custom-Header", "value")
            .header("X-Total-Count", "100")
            .body(user);
    }
    
    // No content
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
    
    // Bad request
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (user.getName() == null) {
            return ResponseEntity.badRequest()
                .body("Name is required");
        }
        return ResponseEntity.ok(userService.save(user));
    }
}
```

### 5.4. HttpEntity

```java
@RestController
public class UserController {
    
    // HttpEntity for request
    @PostMapping("/users")
    public ResponseEntity<User> createUser(HttpEntity<User> httpEntity) {
        User user = httpEntity.getBody();
        HttpHeaders headers = httpEntity.getHeaders();
        
        User saved = userService.save(user);
        return ResponseEntity.ok(saved);
    }
    
    // HttpEntity for response
    @GetMapping("/users/{id}")
    public HttpEntity<User> getUser(@PathVariable Long id) {
        User user = userService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "value");
        return new HttpEntity<>(user, headers);
    }
}
```

---

## 6. MODEL VÀ VIEW

### 6.1. Model Interface

```java
@Controller
public class UserController {
    
    @GetMapping("/users")
    public String list(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("title", "User List");
        return "users/list";
    }
    
    // Add multiple attributes
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("stats", statsService.getStats());
        model.addAttribute("notifications", notificationService.getRecent());
        return "dashboard";
    }
}
```

### 6.2. ModelMap

```java
@Controller
public class UserController {
    
    @GetMapping("/users")
    public String list(ModelMap model) {
        model.addAttribute("users", userService.findAll());
        model.put("title", "User List");
        return "users/list";
    }
}
```

### 6.3. ModelAndView

```java
@Controller
public class UserController {
    
    @GetMapping("/users")
    public ModelAndView list() {
        ModelAndView mav = new ModelAndView("users/list");
        mav.addObject("users", userService.findAll());
        mav.addObject("title", "User List");
        return mav;
    }
    
    // Redirect
    @PostMapping("/users")
    public ModelAndView create(@ModelAttribute User user) {
        userService.save(user);
        return new ModelAndView("redirect:/users");
    }
    
    // Forward
    @GetMapping("/old-url")
    public ModelAndView oldUrl() {
        return new ModelAndView("forward:/new-url");
    }
}
```

### 6.4. @ModelAttribute

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    // Pre-populate model
    @ModelAttribute("countries")
    public List<String> countries() {
        return Arrays.asList("Vietnam", "USA", "UK", "Japan");
    }
    
    // Method parameter binding
    @PostMapping
    public String create(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users";
    }
    
    // Get from model
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "users/edit";
    }
    
    @PostMapping("/{id}")
    public String update(
        @PathVariable Long id,
        @ModelAttribute("user") User user
    ) {
        userService.update(id, user);
        return "redirect:/users/" + id;
    }
    
    // Populate for all methods
    @ModelAttribute
    public void addCommonAttributes(Model model) {
        model.addAttribute("appName", "My Application");
        model.addAttribute("version", "1.0.0");
    }
}
```

### 6.5. @SessionAttributes

```java
@Controller
@RequestMapping("/checkout")
@SessionAttributes("cart")
public class CheckoutController {
    
    @ModelAttribute("cart")
    public Cart createCart() {
        return new Cart();
    }
    
    @GetMapping
    public String showCart(@ModelAttribute("cart") Cart cart, Model model) {
        return "checkout/cart";
    }
    
    @PostMapping("/add-item")
    public String addItem(
        @ModelAttribute("cart") Cart cart,
        @RequestParam Long productId
    ) {
        cart.addItem(productService.findById(productId));
        return "redirect:/checkout";
    }
    
    @PostMapping("/complete")
    public String complete(
        @ModelAttribute("cart") Cart cart,
        SessionStatus status
    ) {
        orderService.createOrder(cart);
        status.setComplete(); // Clear session attributes
        return "redirect:/orders";
    }
}
```

### 6.6. RedirectAttributes

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    @PostMapping
    public String create(
        @ModelAttribute User user,
        RedirectAttributes redirectAttributes
    ) {
        User saved = userService.save(user);
        
        // Flash attributes (available only for next request)
        redirectAttributes.addFlashAttribute("message", "User created successfully");
        redirectAttributes.addFlashAttribute("user", saved);
        
        // URL parameters
        redirectAttributes.addAttribute("id", saved.getId());
        
        return "redirect:/users/{id}";
    }
    
    @GetMapping("/{id}")
    public String view(
        @PathVariable Long id,
        @ModelAttribute("message") String message,
        Model model
    ) {
        // message from flash attributes
        model.addAttribute("user", userService.findById(id));
        return "users/view";
    }
}
```

---

## 7. VIEW RESOLVERS

### 7.1. InternalResourceViewResolver (JSP)

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = 
            new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        return resolver;
    }
}
```

**View name resolution:**
- `"users/list"` → `/WEB-INF/views/users/list.jsp`
- `"home"` → `/WEB-INF/views/home.jsp`

### 7.2. ThymeleafViewResolver

```java
@Configuration
public class ThymeleafConfig {
    
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = 
            new SpringResourceTemplateResolver();
        resolver.setPrefix("classpath:/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); // Development mode
        return resolver;
    }
    
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        return engine;
    }
    
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }
}
```

### 7.3. Multiple View Resolvers

```java
@Configuration
public class ViewResolverConfig {
    
    @Bean
    public ViewResolver thymeleafResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setOrder(1); // Higher priority
        resolver.setViewNames(new String[]{"thymeleaf/*"});
        return resolver;
    }
    
    @Bean
    public ViewResolver jspResolver() {
        InternalResourceViewResolver resolver = 
            new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(2); // Lower priority
        return resolver;
    }
}
```

### 7.4. ContentNegotiatingViewResolver

```java
@Configuration
public class ViewResolverConfig {
    
    @Bean
    public ViewResolver contentNegotiatingResolver() {
        ContentNegotiatingViewResolver resolver = 
            new ContentNegotiatingViewResolver();
        
        List<ViewResolver> resolvers = new ArrayList<>();
        resolvers.add(jsonViewResolver());
        resolvers.add(xmlViewResolver());
        resolvers.add(htmlViewResolver());
        
        resolver.setViewResolvers(resolvers);
        return resolver;
    }
}
```

---

## 8. FORM HANDLING VÀ DATA BINDING

### 8.1. Simple Form

**Controller:**

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }
    
    @PostMapping
    public String create(@ModelAttribute User user) {
        userService.save(user);
        return "redirect:/users";
    }
}
```

**View (Thymeleaf):**

```html
<form th:action="@{/users}" th:object="${user}" method="post">
    <input type="text" th:field="*{name}" />
    <input type="email" th:field="*{email}" />
    <input type="number" th:field="*{age}" />
    <button type="submit">Submit</button>
</form>
```

**View (JSP):**

```jsp
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<form:form action="/users" modelAttribute="user" method="post">
    <form:input path="name" />
    <form:input path="email" type="email" />
    <form:input path="age" type="number" />
    <button type="submit">Submit</button>
</form:form>
```

### 8.2. Form with Dropdowns

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    @ModelAttribute("countries")
    public List<String> countries() {
        return Arrays.asList("Vietnam", "USA", "UK");
    }
    
    @GetMapping("/new")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "users/form";
    }
}
```

**Thymeleaf:**

```html
<form th:action="@{/users}" th:object="${user}" method="post">
    <select th:field="*{country}">
        <option value="">Select Country</option>
        <option th:each="country : ${countries}" 
                th:value="${country}" 
                th:text="${country}">
        </option>
    </select>
</form>
```

### 8.3. Form with Checkboxes

```java
@Data
public class User {
    private String name;
    private List<String> hobbies;
}

@Controller
public class UserController {
    
    @ModelAttribute("availableHobbies")
    public List<String> hobbies() {
        return Arrays.asList("Reading", "Sports", "Music", "Travel");
    }
}
```

**Thymeleaf:**

```html
<form th:object="${user}">
    <div th:each="hobby : ${availableHobbies}">
        <input type="checkbox" 
               th:field="*{hobbies}" 
               th:value="${hobby}" />
        <label th:text="${hobby}"></label>
    </div>
</form>
```

### 8.4. Data Binding

```java
@Controller
public class UserController {
    
    // Simple types
    @PostMapping("/users")
    public String create(
        @RequestParam String name,
        @RequestParam int age,
        @RequestParam LocalDate birthDate
    ) {
        // Parameters automatically converted
        return "redirect:/users";
    }
    
    // Complex object
    @PostMapping("/users")
    public String create(@ModelAttribute User user) {
        // All fields automatically bound
        return "redirect:/users";
    }
    
    // Nested objects
    @PostMapping("/users")
    public String create(@ModelAttribute User user) {
        // user.address.street, user.address.city automatically bound
        return "redirect:/users";
    }
}
```

### 8.5. InitBinder

```java
@Controller
public class UserController {
    
    // Custom property editor
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, 
            new CustomDateEditor(dateFormat, false));
        
        // String trimming
        binder.registerCustomEditor(String.class, 
            new StringTrimmerEditor(true));
    }
    
    // Disallow certain fields
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("id", "createdDate");
    }
    
    // Required fields
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setRequiredFields("name", "email");
    }
}
```

---

## 9. VALIDATION

### 9.1. Bean Validation (JSR-303/380)

**Entity with validation:**

```java
public class User {
    
    @NotNull(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be at most 100")
    private Integer age;
    
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;
    
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;
    
    @AssertTrue(message = "Must agree to terms")
    private Boolean agreeToTerms;
}
```

**Controller with validation:**

```java
@Controller
@RequestMapping("/users")
public class UserController {
    
    @PostMapping
    public String create(
        @Valid @ModelAttribute User user,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            // Re-display form with errors
            return "users/form";
        }
        
        userService.save(user);
        return "redirect:/users";
    }
}
```

**Thymeleaf error display:**

```html
<form th:action="@{/users}" th:object="${user}" method="post">
    <div>
        <input type="text" th:field="*{name}" />
        <span th:if="${#fields.hasErrors('name')}" 
              th:errors="*{name}" 
              class="error">
        </span>
    </div>
    
    <div>
        <input type="email" th:field="*{email}" />
        <span th:if="${#fields.hasErrors('email')}" 
              th:errors="*{email}" 
              class="error">
        </span>
    </div>
    
    <!-- Display all errors -->
    <div th:if="${#fields.hasErrors('*')}">
        <ul>
            <li th:each="err : ${#fields.errors('*')}" 
                th:text="${err}">
            </li>
        </ul>
    </div>
    
    <button type="submit">Submit</button>
</form>
```

### 9.2. Common Validation Annotations

| Annotation | Description |
|------------|-------------|
| `@NotNull` | Field cannot be null |
| `@NotEmpty` | Collection/String not null and not empty |
| `@NotBlank` | String not null and trimmed length > 0 |
| `@Size(min, max)` | Size between min and max |
| `@Min(value)` | Number must be >= value |
| `@Max(value)` | Number must be <= value |
| `@Email` | Valid email format |
| `@Pattern(regexp)` | Must match regex pattern |
| `@Past` | Date must be in the past |
| `@Future` | Date must be in the future |
| `@Positive` | Number must be positive |
| `@Negative` | Number must be negative |
| `@AssertTrue` | Boolean must be true |
| `@AssertFalse` | Boolean must be false |

### 9.3. Custom Validator

**Annotation:**

```java
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```

**Validator:**

```java
public class UniqueEmailValidator 
        implements ConstraintValidator<UniqueEmail, String> {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // Use @NotNull for null check
        }
        return !userRepository.existsByEmail(email);
    }
}
```

**Usage:**

```java
public class User {
    @NotBlank
    @Email
    @UniqueEmail
    private String email;
}
```

### 9.4. Validation Groups

```java
// Marker interfaces for groups
public interface Create {}
public interface Update {}

public class User {
    @NotNull(groups = Update.class)
    private Long id;
    
    @NotBlank(groups = {Create.class, Update.class})
    private String name;
    
    @NotBlank(groups = Create.class)
    private String password;
}

@Controller
public class UserController {
    
    @PostMapping
    public String create(
        @Validated(Create.class) @ModelAttribute User user,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return "users/form";
        }
        userService.save(user);
        return "redirect:/users";
    }
    
    @PutMapping("/{id}")
    public String update(
        @PathVariable Long id,
        @Validated(Update.class) @ModelAttribute User user,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            return "users/edit";
        }
        userService.update(id, user);
        return "redirect:/users/" + id;
    }
}
```

### 9.5. REST API Validation

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @PostMapping
    public ResponseEntity<?> create(
        @Valid @RequestBody User user,
        BindingResult result
    ) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

// Or with @ControllerAdvice
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
```

---

## 10. EXCEPTION HANDLING

### 10.1. @ExceptionHandler

```java
@Controller
public class UserController {
    
    @GetMapping("/users/{id}")
    public String view(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        model.addAttribute("user", user);
        return "users/view";
    }
    
    // Handle exception in controller
    @ExceptionHandler(UserNotFoundException.class)
    public String handleUserNotFound(
            UserNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/404";
    }
}
```

### 10.2. @ControllerAdvice

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFound(UserNotFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("error", ex.getMessage());
        return mav;
    }
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("error", "An unexpected error occurred");
        return mav;
    }
    
    // Multiple exceptions
    @ExceptionHandler({
        IllegalArgumentException.class,
        IllegalStateException.class
    })
    public String handleBadRequest(Exception ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error/400";
    }
}
```

### 10.3. @RestControllerAdvice

```java
@RestControllerAdvice
public class RestExceptionHandler {
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return errors;
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGenericException(Exception ex) {
        return new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal server error",
            LocalDateTime.now()
        );
    }
}

@Data
@AllArgsConstructor
class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
}
```

### 10.4. @ResponseStatus

```java
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User not found with id: " + id);
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String message) {
        super(message);
    }
}
```

### 10.5. ResponseStatusException (Spring 5+)

```java
@Controller
public class UserController {
    
    @GetMapping("/users/{id}")
    public String view(@PathVariable Long id, Model model) {
        User user = userService.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "User not found with id: " + id
            ));
        
        model.addAttribute("user", user);
        return "users/view";
    }
}
```

---

## 11. INTERCEPTORS

### 11.1. HandlerInterceptor

```java
public class LoggingInterceptor implements HandlerInterceptor {
    
    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);
    
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        log.info("Request URL: {}", request.getRequestURL());
        log.info("Method: {}", request.getMethod());
        return true; // Continue processing
    }
    
    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView) throws Exception {
        if (modelAndView != null) {
            log.info("View: {}", modelAndView.getViewName());
        }
    }
    
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) throws Exception {
        if (ex != null) {
            log.error("Exception: ", ex);
        }
        log.info("Request completed");
    }
}
```

### 11.2. Register Interceptors

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Apply to all URLs
        registry.addInterceptor(new LoggingInterceptor());
        
        // Apply to specific patterns
        registry.addInterceptor(new AuthInterceptor())
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login");
        
        // Multiple interceptors
        registry.addInterceptor(new FirstInterceptor())
                .order(1);
        registry.addInterceptor(new SecondInterceptor())
                .order(2);
    }
}
```

### 11.3. Authentication Interceptor

```java
public class AuthInterceptor implements HandlerInterceptor {
    
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false; // Stop processing
        }
        
        return true; // Continue
    }
}
```

### 11.4. Performance Monitoring Interceptor

```java
public class PerformanceInterceptor implements HandlerInterceptor {
    
    private static final String START_TIME = "startTime";
    
    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {
        request.setAttribute(START_TIME, System.currentTimeMillis());
        return true;
    }
    
    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex) {
        Long startTime = (Long) request.getAttribute(START_TIME);
        long duration = System.currentTimeMillis() - startTime;
        
        log.info("Request {} took {} ms", 
                 request.getRequestURI(), duration);
        
        if (duration > 1000) {
            log.warn("Slow request detected: {} ms", duration);
        }
    }
}
```

---

## 12. REST APIs

### 12.1. RESTful Controller

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @Autowired
    private UserService userService;
    
    // GET /api/users
    @GetMapping
    public List<User> list() {
        return userService.findAll();
    }
    
    // GET /api/users/123
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // POST /api/users
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User saved = userService.save(user);
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(saved.getId())
            .toUri();
        return ResponseEntity.created(location).body(saved);
    }
    
    // PUT /api/users/123
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        return userService.update(id, user)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // PATCH /api/users/123
    @PatchMapping("/{id}")
    public ResponseEntity<User> partialUpdate(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        return userService.partialUpdate(id, updates)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    // DELETE /api/users/123
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (userService.existsById(id)) {
            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
```

### 12.2. HATEOAS

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @GetMapping("/{id}")
    public EntityModel<User> getById(@PathVariable Long id) {
        User user = userService.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
        
        EntityModel<User> resource = EntityModel.of(user);
        
        // Add links
        resource.add(linkTo(methodOn(UserRestController.class)
            .getById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserRestController.class)
            .list()).withRel("users"));
        resource.add(linkTo(methodOn(OrderController.class)
            .getUserOrders(id)).withRel("orders"));
        
        return resource;
    }
}
```

**Response:**
```json
{
  "id": 123,
  "name": "John Doe",
  "email": "john@example.com",
  "_links": {
    "self": {
      "href": "http://localhost:8080/api/users/123"
    },
    "users": {
      "href": "http://localhost:8080/api/users"
    },
    "orders": {
      "href": "http://localhost:8080/api/users/123/orders"
    }
  }
}
```

### 12.3. Pagination

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @GetMapping
    public Page<User> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        return userService.findAll(pageable);
    }
}
```

**Response:**
```json
{
  "content": [...],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {...}
  },
  "totalPages": 5,
  "totalElements": 50,
  "last": false,
  "size": 10,
  "number": 0
}
```

---

## 13. CONTENT NEGOTIATION

### 13.1. Accept Header

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    // Produces JSON by default
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User getJson(@PathVariable Long id) {
        return userService.findById(id).orElseThrow();
    }
    
    // Produces XML
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public User getXml(@PathVariable Long id) {
        return userService.findById(id).orElseThrow();
    }
    
    // Produces both
    @GetMapping(value = "/{id}", 
                produces = {MediaType.APPLICATION_JSON_VALUE, 
                           MediaType.APPLICATION_XML_VALUE})
    public User get(@PathVariable Long id) {
        return userService.findById(id).orElseThrow();
    }
}
```

**Request:**
```
GET /api/users/123
Accept: application/json
```

### 13.2. Configuration

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        configurer
            .favorParameter(true)
            .parameterName("mediaType")
            .ignoreAcceptHeader(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML);
    }
}
```

**URL parameter:**
```
GET /api/users/123?mediaType=json
GET /api/users/123?mediaType=xml
```

---

## 14. ASYNC PROCESSING

### 14.1. Callable

```java
@RestController
@RequestMapping("/api")
public class AsyncController {
    
    @GetMapping("/users")
    public Callable<List<User>> listUsers() {
        return () -> {
            Thread.sleep(2000); // Simulate long processing
            return userService.findAll();
        };
    }
}
```

### 14.2. DeferredResult

```java
@RestController
public class AsyncController {
    
    @GetMapping("/api/users/{id}")
    public DeferredResult<User> getUser(@PathVariable Long id) {
        DeferredResult<User> result = new DeferredResult<>(5000L); // 5s timeout
        
        result.onTimeout(() -> 
            result.setErrorResult(new TimeoutException())
        );
        
        CompletableFuture.supplyAsync(() -> userService.findById(id))
            .whenComplete((user, ex) -> {
                if (ex != null) {
                    result.setErrorResult(ex);
                } else {
                    result.setResult(user);
                }
            });
        
        return result;
    }
}
```

### 14.3. CompletableFuture

```java
@RestController
@RequestMapping("/api")
public class AsyncController {
    
    @GetMapping("/users")
    public CompletableFuture<List<User>> listUsers() {
        return CompletableFuture.supplyAsync(() -> 
            userService.findAll()
        );
    }
}
```

### 14.4. Configuration

```java
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(25);
        executor.setThreadNamePrefix("async-");
        executor.initialize();
        return executor;
    }
}
```

---

## 15. FILE UPLOAD VÀ DOWNLOAD

### 15.1. File Upload

```java
@Controller
@RequestMapping("/files")
public class FileController {
    
    @GetMapping("/upload")
    public String uploadForm() {
        return "files/upload";
    }
    
    @PostMapping("/upload")
    public String upload(
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", 
                "Please select a file");
            return "redirect:/files/upload";
        }
        
        try {
            String filename = file.getOriginalFilename();
            Path path = Paths.get("uploads/" + filename);
            Files.copy(file.getInputStream(), path, 
                      StandardCopyOption.REPLACE_EXISTING);
            
            redirectAttributes.addFlashAttribute("message",
                "File uploaded successfully: " + filename);
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message",
                "Failed to upload file");
        }
        
        return "redirect:/files/upload";
    }
    
    // Multiple files
    @PostMapping("/upload-multiple")
    public String uploadMultiple(
            @RequestParam("files") MultipartFile[] files) {
        
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // Save file
            }
        }
        
        return "redirect:/files";
    }
}
```

**Form:**
```html
<form method="post" enctype="multipart/form-data" 
      th:action="@{/files/upload}">
    <input type="file" name="file" />
    <button type="submit">Upload</button>
</form>

<!-- Multiple files -->
<form method="post" enctype="multipart/form-data">
    <input type="file" name="files" multiple />
    <button type="submit">Upload</button>
</form>
```

### 15.2. File Download

```java
@Controller
@RequestMapping("/files")
public class FileController {
    
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> download(
            @PathVariable String filename) {
        
        try {
            Path path = Paths.get("uploads/" + filename);
            Resource resource = new UrlResource(path.toUri());
            
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }
            
            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                       "attachment; filename=\"" + filename + "\"")
                .body(resource);
                
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    // Inline display (e.g., images)
    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<Resource> view(@PathVariable String filename) {
        // ... load resource
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                   "inline; filename=\"" + filename + "\"")
            .contentType(MediaType.IMAGE_JPEG)
            .body(resource);
    }
}
```

### 15.3. Configuration

```properties
# application.properties
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

```java
@Configuration
public class FileUploadConfig {
    
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        factory.setMaxRequestSize(DataSize.ofMegabytes(10));
        return factory.createMultipartConfig();
    }
}
```

---

## 16. CORS CONFIGURATION

### 16.1. @CrossOrigin

```java
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserRestController {
    
    @GetMapping
    public List<User> list() {
        return userService.findAll();
    }
    
    // Method-level override
    @GetMapping("/{id}")
    @CrossOrigin(origins = "*", maxAge = 3600)
    public User getById(@PathVariable Long id) {
        return userService.findById(id).orElseThrow();
    }
}
```

### 16.2. Global CORS

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
```

### 16.3. CORS Filter

```java
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = 
            new UrlBasedCorsConfigurationSource();
        
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        
        source.registerCorsConfiguration("/api/**", config);
        
        return new CorsFilter(source);
    }
}
```

---

## 17. BEST PRACTICES

### 17.1. Controller Design

✅ **DO:**
- Keep controllers thin (delegate to services)
- Use appropriate HTTP methods
- Return proper HTTP status codes
- Use DTOs for API responses
- Handle exceptions properly

❌ **DON'T:**
- Put business logic in controllers
- Expose entities directly
- Ignore validation
- Return generic error messages

```java
// ✅ GOOD
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return userService.findById(id)
            .map(user -> ResponseEntity.ok(toDTO(user)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<UserDTO> create(
            @Valid @RequestBody UserDTO dto) {
        User user = userService.create(fromDTO(dto));
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(toDTO(user));
    }
}

// ❌ BAD
@RestController
public class UserController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/users/{id}")
    public User get(@PathVariable Long id) {
        return userRepository.findById(id).orElse(null); // No error handling
    }
}
```

### 17.2. RESTful API Design

```java
// ✅ GOOD - RESTful URLs
GET    /api/users              // List users
GET    /api/users/{id}         // Get user
POST   /api/users              // Create user
PUT    /api/users/{id}         // Update user
PATCH  /api/users/{id}         // Partial update
DELETE /api/users/{id}         // Delete user

GET    /api/users/{id}/orders  // Get user's orders

// ❌ BAD - Non-RESTful
GET    /api/getUsers
POST   /api/createUser
GET    /api/user/delete/{id}
```

### 17.3. HTTP Status Codes

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)               // 200 OK
            .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }
    
    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        User saved = userService.save(user);
        return ResponseEntity
            .status(HttpStatus.CREATED)            // 201 Created
            .body(saved);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        return userService.update(id, user)
            .map(ResponseEntity::ok)               // 200 OK
            .orElse(ResponseEntity.notFound().build()); // 404 Not Found
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
```

**Common HTTP Status Codes:**
- **200 OK**: Successful GET, PUT, PATCH
- **201 Created**: Successful POST
- **204 No Content**: Successful DELETE
- **400 Bad Request**: Validation errors
- **401 Unauthorized**: Authentication required
- **403 Forbidden**: Authorization failed
- **404 Not Found**: Resource not found
- **500 Internal Server Error**: Server error

---

## 18. CÂU HỎI MẪU CHO KỲ THI

### 18.1. Câu hỏi lý thuyết

#### Câu 1: Sự khác biệt giữa @Controller và @RestController?

**Trả lời**:
- **@Controller**: Traditional MVC controller, returns view names
- **@RestController**: Combination of @Controller + @ResponseBody, returns data (JSON/XML)

**@RestController = @Controller + @ResponseBody**

---

#### Câu 2: DispatcherServlet là gì và vai trò của nó?

**Trả lời**: **DispatcherServlet** là Front Controller trong Spring MVC. Nó:
- Receives all HTTP requests
- Delegates to appropriate handlers (controllers)
- Resolves views
- Returns responses

Flow: Request → DispatcherServlet → HandlerMapping → Controller → ViewResolver → View → Response

---

#### Câu 3: @RequestParam và @PathVariable khác nhau như thế nào?

**Trả lời**:

| Aspect | @RequestParam | @PathVariable |
|--------|---------------|---------------|
| **Location** | Query string | URL path |
| **Example** | `/users?id=123` | `/users/123` |
| **Optional** | Can be optional | Usually required |
| **Use case** | Filters, pagination | Resource identifiers |

---

#### Câu 4: Khi nào dùng @ModelAttribute?

**Trả lời**: **@ModelAttribute** dùng để:
1. Bind form data to object
2. Pre-populate model attributes for all methods
3. Access data from model in method parameters

```java
// Form binding
@PostMapping("/users")
public String create(@ModelAttribute User user) { }

// Pre-populate
@ModelAttribute("countries")
public List<String> countries() {
    return Arrays.asList("Vietnam", "USA");
}
```

---

#### Câu 5: @Valid và @Validated khác nhau thế nào?

**Trả lời**:
- **@Valid**: JSR-303 standard, supports nested validation
- **@Validated**: Spring-specific, supports validation groups

**@Validated** can specify groups:
```java
@PostMapping
public String create(
    @Validated(Create.class) @ModelAttribute User user
) { }
```

---

### 18.2. Câu hỏi code-based

#### Câu 6: Code sau có vấn đề gì?

```java
@Controller
public class UserController {
    
    @GetMapping("/api/users")
    public List<User> list() {
        return userService.findAll();
    }
}
```

**Trả lời**: Method returns List nhưng controller là @Controller (không phải @RestController), nên Spring sẽ coi "users" là view name, không phải data.

**Fix:**
```java
// Option 1: Use @RestController
@RestController
public class UserController {
    @GetMapping("/api/users")
    public List<User> list() {
        return userService.findAll();
    }
}

// Option 2: Use @ResponseBody
@Controller
public class UserController {
    @GetMapping("/api/users")
    @ResponseBody
    public List<User> list() {
        return userService.findAll();
    }
}
```

---

#### Câu 7: Làm thế nào để handle validation errors trong REST API?

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody User user,
            BindingResult result) {
        
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error ->
                errors.put(error.getField(), 
                          error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }
        
        User saved = userService.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

// Or with @ControllerAdvice
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }
}
```

---

### 18.3. Scenario-based Questions

#### Câu 8: Làm thế nào để implement REST API với pagination?

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @GetMapping
    public Page<User> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort) {
        
        Pageable pageable = PageRequest.of(page, size, 
            Sort.by(sort));
        
        return userService.findAll(pageable);
    }
}
```

**Request:**
```
GET /api/users?page=0&size=20&sort=name
```

---

#### Câu 9: Làm thế nào để upload và download files?

**Upload:**
```java
@PostMapping("/upload")
public ResponseEntity<String> upload(
        @RequestParam("file") MultipartFile file) {
    
    if (file.isEmpty()) {
        return ResponseEntity.badRequest()
            .body("File is empty");
    }
    
    try {
        Path path = Paths.get("uploads/" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), path,
                  StandardCopyOption.REPLACE_EXISTING);
        return ResponseEntity.ok("File uploaded successfully");
    } catch (IOException e) {
        return ResponseEntity.internalServerError()
            .body("Failed to upload file");
    }
}
```

**Download:**
```java
@GetMapping("/download/{filename:.+}")
public ResponseEntity<Resource> download(
        @PathVariable String filename) {
    
    Path path = Paths.get("uploads/" + filename);
    Resource resource = new UrlResource(path.toUri());
    
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
               "attachment; filename=\"" + filename + "\"")
        .body(resource);
}
```

---

#### Câu 10: Configure CORS cho REST API?

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}

// Or per controller
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/users")
public class UserRestController { }
```

---

## 19. TÓM TẮT VÀ MẸO THI

### 19.1. Core Annotations

| Annotation | Purpose |
|------------|---------|
| `@Controller` | MVC controller (returns views) |
| `@RestController` | REST controller (returns data) |
| `@RequestMapping` | Map requests to methods |
| `@GetMapping` | GET requests |
| `@PostMapping` | POST requests |
| `@PutMapping` | PUT requests |
| `@DeleteMapping` | DELETE requests |
| `@RequestParam` | Query parameters |
| `@PathVariable` | Path variables |
| `@RequestBody` | Request body (JSON/XML) |
| `@ResponseBody` | Response body |
| `@ModelAttribute` | Form/model binding |
| `@Valid` | Validation |
| `@ExceptionHandler` | Handle exceptions |
| `@ControllerAdvice` | Global exception handling |

### 19.2. Request Flow

```
Client Request
    ↓
DispatcherServlet (Front Controller)
    ↓
HandlerMapping (Find Controller)
    ↓
Controller (Process Request)
    ↓
ModelAndView (Data + View Name)
    ↓
ViewResolver (Resolve View)
    ↓
View (Render Response)
    ↓
Client Response
```

### 19.3. REST API Best Practices

```java
// ✅ Resource-based URLs
GET    /api/users
GET    /api/users/{id}
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}

// ✅ Proper HTTP status codes
200 OK, 201 Created, 204 No Content
400 Bad Request, 401 Unauthorized, 403 Forbidden
404 Not Found, 500 Internal Server Error

// ✅ Use DTOs
public ResponseEntity<UserDTO> create(@RequestBody UserDTO dto)

// ✅ Validation
public ResponseEntity<?> create(@Valid @RequestBody User user)

// ✅ Exception handling
@RestControllerAdvice for global handling
```

### 19.4. Common Pitfalls

❌ **Mistake 1**: @Controller without @ResponseBody
```java
// BAD - Returns view name, not data
@Controller
public class UserController {
    @GetMapping("/api/users")
    public List<User> list() { }
}

// GOOD
@RestController
public class UserController {
    @GetMapping("/api/users")
    public List<User> list() { }
}
```

❌ **Mistake 2**: Missing validation handling
```java
// BAD
@PostMapping
public User create(@Valid @RequestBody User user) { }

// GOOD
@PostMapping
public ResponseEntity<?> create(
    @Valid @RequestBody User user,
    BindingResult result) {
    if (result.hasErrors()) {
        return ResponseEntity.badRequest().body(errors);
    }
}
```

❌ **Mistake 3**: Ignoring HTTP status codes
```java
// BAD
@PostMapping
public User create(@RequestBody User user) {
    return userService.save(user); // 200 OK
}

// GOOD
@PostMapping
public ResponseEntity<User> create(@RequestBody User user) {
    User saved = userService.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(saved); // 201 Created
}
```

### 19.5. Mẹo làm bài thi

1. ✅ **@Controller vs @RestController**: Controller returns views, RestController returns data
2. ✅ **DispatcherServlet**: Front Controller pattern
3. ✅ **@RequestParam vs @PathVariable**: Query string vs URL path
4. ✅ **@ModelAttribute**: Form binding và model pre-population
5. ✅ **Validation**: @Valid cho validation, BindingResult cho errors
6. ✅ **Exception Handling**: @ExceptionHandler local, @ControllerAdvice global
7. ✅ **HTTP Methods**: GET (read), POST (create), PUT (update), DELETE (delete)
8. ✅ **Status Codes**: 200 OK, 201 Created, 404 Not Found, 400 Bad Request
9. ✅ **ResponseEntity**: Full control over HTTP response
10. ✅ **CORS**: @CrossOrigin hoặc global configuration

### 19.6. Checklist ôn tập

- [ ] Spring MVC architecture và request flow
- [ ] @Controller vs @RestController
- [ ] Request mapping annotations
- [ ] @RequestParam, @PathVariable, @RequestHeader
- [ ] @RequestBody và @ResponseBody
- [ ] Model, ModelAndView, @ModelAttribute
- [ ] View resolvers
- [ ] Form handling và data binding
- [ ] Validation (@Valid, @Validated)
- [ ] Exception handling (@ExceptionHandler, @ControllerAdvice)
- [ ] Interceptors
- [ ] REST API best practices
- [ ] HTTP status codes
- [ ] Content negotiation
- [ ] File upload/download
- [ ] CORS configuration

---

## KẾT LUẬN

Spring MVC là core của Spring web development và là topic quan trọng trong Spring Professional Certification. Để thành công:

- ✅ Hiểu rõ MVC pattern và request flow
- ✅ Nắm vững các annotations
- ✅ Biết khi nào dùng @Controller vs @RestController
- ✅ Master REST API design principles
- ✅ Understand validation và exception handling
- ✅ Know HTTP methods và status codes

### Key Points:

> **Spring MVC = Model + View + Controller**
>
> Essential concepts:
> - DispatcherServlet = Front Controller
> - @Controller returns views
> - @RestController returns data
> - @RequestParam = query string
> - @PathVariable = URL path
> - @ModelAttribute = form binding
> - ResponseEntity = full HTTP control

**Request Flow:**
```
Request → DispatcherServlet → HandlerMapping → Controller 
→ Model & View → ViewResolver → View → Response
```

**REST API Principles:**
- Resource-based URLs
- Proper HTTP methods (GET, POST, PUT, DELETE)
- Appropriate status codes
- DTOs for data transfer
- Proper error handling

Hãy thực hành với examples trong tài liệu này và focus vào understanding flow rather than memorization. Spring MVC là foundation cho modern web development!

---

**Chúc bạn thành công với Spring Professional Certification!** 🚀🌐

*Tài liệu được tạo ngày 26/12/2024*
