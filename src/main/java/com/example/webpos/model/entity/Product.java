package com.example.webpos.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "products")
public class Product implements Serializable {
    @Id
    private String id;

    @Column(name = "title")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "image")
    private String image;

    public Product() {

    }

    public Product(String id, String name, String category, String image) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.image = image;
    }
}
