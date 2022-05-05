package com.example.webpos.service;


import com.example.webpos.model.entity.Product;
import com.example.webpos.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PosServiceImp implements PosService {

    private final ProductRepository repository;
    private final Map<Product, Integer> cart = new HashMap<>();

    @Autowired
    PosServiceImp(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<Product> products() {
        return repository.findAll();
    }

    @Override
    public void resetCart() {
        cart.clear();
    }

    private Product checkArgument(String id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("product id does not exist"));
    }

    @Override
    public void addProduct(String id, int amount) {
        var product = checkArgument(id);
        cart.merge(product, amount, Integer::sum);
        if (cart.get(product) <= 0) {
            cart.remove(product);
        }
    }

    @Override
    public void removeProduct(String id) {
        var product = checkArgument(id);
        cart.remove(product);
    }

    @Override
    public void modifyCart(String id, int amount) {
        var product = checkArgument(id);
        cart.put(product, amount);
        if (cart.get(product) <= 0) {
            cart.remove(product);
        }
    }

    @Override
    public Set<Map.Entry<Product, Integer>> content() {
        return cart.entrySet();
    }
}
