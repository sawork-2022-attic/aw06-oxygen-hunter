package com.example.webpos.model.repository;

import com.example.webpos.model.entity.Product;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, String> {

    Optional<Product> findById(String s);

    Iterable<Product> findAll();
}
