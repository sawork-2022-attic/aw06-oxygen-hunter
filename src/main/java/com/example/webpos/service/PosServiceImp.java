package com.example.webpos.service;


import com.example.webpos.model.entity.Product;
import com.example.webpos.model.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.print.Pageable;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PosServiceImp implements PosService {

    private final ProductRepository repository;

    private final Map<Product, Integer> cart = new HashMap<>();

    private final BigDecimal tax;

    private final BigDecimal discount;

    @Autowired
    PosServiceImp(ProductRepository repository, @Value("${tax}") String tax, @Value("${discount}") String discount) {
        this.repository = repository;
        this.tax = new BigDecimal(tax);
        this.discount = new BigDecimal(discount);
    }

    @Override
    public Set<Map.Entry<Product, Integer>> content() {
        return cart.entrySet();
    }

    @Override
    public Iterable<Product> products(int page) {
        PageRequest pageRequest = PageRequest.of(page, 30);
        return repository.findAll(pageRequest);
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
    public void resetCart() {
        cart.clear();
    }

    @Override
    public void charge() {
        cart.clear();
    }

    @Override
    public BigDecimal getTax() {
        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getDiscount() {
        return discount.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getSubTotal() {
        BigDecimal cartTotal = new BigDecimal(0);
        for (Map.Entry<Product, Integer> entry : cart.entrySet()) {
            BigDecimal price = BigDecimal.valueOf(entry.getKey().getPrice());
            BigDecimal quantity = BigDecimal.valueOf(entry.getValue());
            cartTotal = cartTotal.add(price.multiply(quantity));
        }
        BigDecimal subTotal = cartTotal.subtract(cartTotal.multiply(discount));
        return subTotal.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal getTotal() {
        BigDecimal subTotal = getSubTotal();
        BigDecimal total = subTotal.multiply(BigDecimal.valueOf(1).add(tax));
        return total.setScale(2, RoundingMode.HALF_UP);
    }

}
