# Spring Cloud demo: bemásolandó kódrészletek



## Config server

###  application.yml
```
server:
 port: 8081
spring:
 application: 
  name: config
 profiles:
  active: native 
 cloud:
  config:
   server:
     native:
       search-locations: classpath:/config
```

## Config server kliensei

### pom.xml-be
```
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-config</artifactId>
</dependency>
```
### application.yml-be
```
spring:
  application:
    name: flights
  config:
    import: optional:configserver:http://localhost:8081
```

## Discovery server

### discovery.yml a config serverben
```
server:
  port: 8084
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
```

## Discovery kliensek

### pom.xml-be
```
<dependency>
      <groupId>org.springframework.cloud</groupId>
      <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

### config serverben a yml-ekbe

```
eureka:
  client:	
    serviceUrl:
      defaultZone: http://localhost:8084/eureka/
```

## API Gateway

### config serverben gateway.yml
```
spring:
  cloud:
    gateway:
      routes:
      - id: currency
        uri: lb://currency
        predicates:
        - Path=/currency/**
        filters:
        - RewritePath=/currency(?<segment>/?.*), /api$\{segment}
      - id: flights
        uri: lb://flights
        predicates:
        - Path=/flights/**
        filters:
        - RewritePath=/flights(?<segment>/?.*), /api$\{segment}
      - id: token
        uri: lb://token
        predicates:
        - Path=/token/**
        filters:
        - RewritePath=/token(?<segment>/?.*), /api$\{segment}
```


## Security a gateway-ben

### pom.xml-be
```
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-security</artifactId>
</dependency>
<dependency>
	<groupId>com.auth0</groupId>
	<artifactId>java-jwt</artifactId>
	<version>4.0.0</version>
</dependency>
```

### SecurityConfig osztály
```
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http.authorizeExchange(exchanges ->
                exchanges.anyExchange().permitAll())
        .csrf().disable();
        return http.build();
    }  
}
```

### JwtFilter
```
@Component
public class JwtFilter implements GlobalFilter{

	private static final String BEARER = "Bearer ";
	
    @Autowired
    private JwtService jwtService;
    
    private PathPattern currencyPathPattern = PathPatternParser.defaultInstance.parse("/currency/**");
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        if(isCurrencyRequest(exchange))
            return chain.filter(exchange);
        
        ServerHttpRequest request = exchange.getRequest();
		List<String> authHeaders = request.getHeaders().get("Authorization");
        
        if(ObjectUtils.isEmpty(authHeaders)) {
            return send401Response(exchange);
        }else {
            String authHeader = authHeaders.get(0);
            UsernamePasswordAuthenticationToken userDetails = null;
            try {
                userDetails = createUserDetailsFromAuthHeader(authHeader);
                String userName = userDetails.getName();
                String authCsv = String.join(",", userDetails.getAuthorities().stream().map(auth -> auth.getAuthority()).toList());
                
                ServerHttpRequest requestWithCustomHeaders = request
                	.mutate()
                	.header("x-jwt-username", userName)
                	.header("x-jwt-auth", authCsv)
                	.build();
                
                exchange = exchange
                	.mutate()
                	.request(requestWithCustomHeaders)
                	.build();
                
            }catch (Exception e) {
                e.printStackTrace();
            }
            if(userDetails == null)
                return send401Response(exchange);
        }
        
        return chain.filter(exchange);
    }
    
    private Mono<Void> send401Response(ServerWebExchange exchange) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
    }

    private boolean isCurrencyRequest(ServerWebExchange exchange) {
        Set<URI> origUrls = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        
        URI originalUri = origUrls.iterator().next();
        return currencyPathPattern.matches(PathContainer.parsePath(originalUri.toString()).subPath(4));
        
    }

    private UsernamePasswordAuthenticationToken createUserDetailsFromAuthHeader(String authHeader) {

    	if(authHeader != null && authHeader.startsWith(BEARER)) {
			String jwtToken = authHeader.substring(BEARER.length());
			UserDetails principal = jwtService.parseJwt(jwtToken);
			
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
			return authentication;
		}
		return null;
	}
}
```
