package solita.citybike;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("solita.citybike")
public class JourneyBackend {


    public static void main(String [] args){
        SpringApplication.run(JourneyBackend.class, args);
    }
}
