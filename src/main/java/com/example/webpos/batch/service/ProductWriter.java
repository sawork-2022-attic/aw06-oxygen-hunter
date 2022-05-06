package com.example.webpos.batch.service;

import com.example.webpos.batch.model.Product;
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

    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductWriter(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("products").usingColumns("id","title","category","image");
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
        //save to database
        System.out.println("chunk written");
        final int columnLength = 255;
        for (Product product : list) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("id", product.getAsin());
            parameters.put("title", product.getTitle().length() < columnLength ? product.getTitle() : product.getTitle().substring(0, columnLength));
            parameters.put("category", product.getMain_cat().length() < columnLength ? product.getMain_cat() : product.getMain_cat().substring(0, columnLength));
            parameters.put("image", product.getImageURLHighRes().size() > 0 ? product.getImageURLHighRes().get(0) : null);
            try {
                simpleJdbcInsert.execute(parameters);
            } catch (Exception e) {
                //duplicate key during insertion
            }
        }
    }
}
