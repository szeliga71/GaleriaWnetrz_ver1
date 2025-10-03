package szeliga71.pl.wp.galeriawnetrz_ver1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class GaleriaWnetrzVer1Application {

    public static void main(String[] args) {
        SpringApplication.run(GaleriaWnetrzVer1Application.class, args);
    }

}
