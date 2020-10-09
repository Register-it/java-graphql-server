package it.register.edu.graphql.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {

  private static final String[] ALLOWED_HEADER = {"authorization", "content-type"};
  private static final String[] EXPOSE_HEADER = {"X-B3-TraceId", "X-B3-SpanId", "Content-Length"};


  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
      .addMapping("/**")
      .allowedOrigins("*")
      .allowedMethods("POST")
      .exposedHeaders(EXPOSE_HEADER)
      .allowedHeaders(ALLOWED_HEADER)
      .allowCredentials(true)
      .maxAge(3600);

  }

}
