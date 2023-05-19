package solita.citybike;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableCaching
@EntityScan("solita.citybike")
public class JourneyBackend {


    public static void main(String [] args){
        SpringApplication.run(JourneyBackend.class, args);
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                /*
                Wildcard all routes to be CORS-compliant.
                ABSOLUTELY NOT FOR REAL-WORLD PURPOSES, JUST FOR API
                TESTING
                 */
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }
}
