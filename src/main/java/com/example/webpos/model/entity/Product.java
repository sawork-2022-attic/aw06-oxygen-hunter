package com.example.webpos.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @Id
    private String id;

    @Column(name = "title")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "image")
    private String image;

    public int getPrice() {
        return 100;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o instanceof Product) {
            Product p = (Product) o;
            return this.id.equals(p.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 17;
        return 31*result + id.hashCode();
    }

}
