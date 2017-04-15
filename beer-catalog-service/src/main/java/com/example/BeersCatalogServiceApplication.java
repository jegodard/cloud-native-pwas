package com.example;


import java.security.Principal;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@SpringBootApplication
@EnableDiscoveryClient
public class BeersCatalogServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeersCatalogServiceApplication.class, args);
    }
}

@Data
@NoArgsConstructor  // pourquoi JPA pourquoi??
@AllArgsConstructor
@ToString
@Entity
class Beer {

    @GeneratedValue
    @Id
    private Long id;

    private String name;
    
    public Beer(String name) {
    	this.name = name;
    }
}

@RepositoryRestResource
interface BeerRepository extends JpaRepository<Beer, Long> {}

@Component
class BlogCommandLineRunner implements CommandLineRunner {

    public BlogCommandLineRunner(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        Stream.of("Kronenbourg", "Budweiser", "Leffe", "Rochefort",
                "Heineken", "Duvel", "Brooklyn Lager", "Karmeliet").forEach(x ->
            repository.save(new Beer(x))
        );
        repository.findAll().forEach(System.out::println);
    }

    private final BeerRepository repository;
}

@RestController
class BeerController {

    @RequestMapping("/foo")
    void list(Principal principal) {
        System.out.print(principal);
    }
}
