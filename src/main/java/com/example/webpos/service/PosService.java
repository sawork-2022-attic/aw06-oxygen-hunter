package com.example.webpos.service;

import com.example.webpos.model.entity.Product;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;

public interface PosService {

    Iterable<Product> products(int page);

    Set<Map.Entry<Product, Integer>> content();

    void addProduct(String id, int amount);

    void removeProduct(String id);

    void resetCart();

    void charge();

    BigDecimal getTax();

    BigDecimal getDiscount();

    BigDecimal getSubTotal();

    BigDecimal getTotal();
}
