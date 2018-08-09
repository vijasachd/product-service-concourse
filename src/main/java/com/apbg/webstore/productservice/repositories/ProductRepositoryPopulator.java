package com.apbg.webstore.productservice.repositories;

import com.apbg.webstore.productservice.domain.Product;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.init.Jackson2ResourceReader;
import org.springframework.data.repository.init.ResourceReader;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ProductRepositoryPopulator implements ApplicationListener<ApplicationReadyEvent> {

    private final ResourceReader resourceReader;
    private final Resource sourceData;
    private ProductRepository productRepository;

    // Constructor. Inject the product repository and initialize resourceReader, sourceData, and productRepository.
    public ProductRepositoryPopulator(ProductRepository productRepository) {
        this.productRepository = productRepository;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(objectMapper);
        sourceData = new ClassPathResource("products.json");
    }

    // On application start, if the database is empty, attempt to read data from the JSON file as a list and
    // save it to the database.
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (productRepository.findAll().isEmpty()) {
            try {
                List<Product> entity = (List<Product>) resourceReader.readFrom(sourceData, getClass().getClassLoader());
                productRepository.saveAll(entity);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
    }
}