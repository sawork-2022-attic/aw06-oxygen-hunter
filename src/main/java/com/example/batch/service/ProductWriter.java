package com.example.batch.service;

import com.example.batch.model.Product;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductWriter implements ItemWriter<Product>, StepExecutionListener {

    private final JdbcTemplate jdbcTemplate;

    private final DataSource dataSource;

    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public ProductWriter(DataSource dataSource) {
        this.dataSource = dataSource;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("products").usingColumns("id","title","category","image");
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {

    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }

    @Override
    public void write(List<? extends Product> list) throws Exception {
        //TODO: save to database
        //list.stream().forEach(System.out::println);
        System.out.println("chunk written");
        for (Product product : list) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", product.getAsin());
            parameters.put("title", product.getTitle());
            parameters.put("category", product.getMain_cat());
            parameters.put("image", product.getImageURLHighRes().size() > 0? product.getImageURLHighRes().get(0):"empty");
            simpleJdbcInsert.execute(parameters);
        }
    }
}
