import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;


@SpringBootApplication
@ComponentScan(basePackages = "config")
public class mainApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(mainApplication.class, args);
    }


}
