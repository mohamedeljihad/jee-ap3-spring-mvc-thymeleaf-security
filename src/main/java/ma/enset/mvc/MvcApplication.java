package ma.enset.mvc;

import ma.enset.mvc.entities.Product;
import ma.enset.mvc.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MvcApplication {

    public static void main(String[] args) {
        SpringApplication.run(MvcApplication.class, args);
    }

    /** Étape 4 : test de la couche DAO + jeu de données initial. */
    @Bean
    CommandLineRunner start(ProductRepository productRepository) {
        return args -> {
            productRepository.save(Product.builder().name("Computer").price(5400).quantity(12).build());
            productRepository.save(Product.builder().name("Printer").price(1200).quantity(11).build());
            productRepository.save(Product.builder().name("Smart Phone").price(12000).quantity(33).build());
            productRepository.save(Product.builder().name("Smart Watch").price(2300).quantity(8).build());
            productRepository.save(Product.builder().name("Keyboard").price(250).quantity(40).build());
            productRepository.save(Product.builder().name("Mouse").price(120).quantity(60).build());
            productRepository.save(Product.builder().name("Monitor 27").price(2900).quantity(9).build());
            productRepository.save(Product.builder().name("Headset").price(450).quantity(25).build());
            productRepository.findAll().forEach(p -> System.out.println(p.getName() + " : " + p.getPrice()));
        };
    }
}
