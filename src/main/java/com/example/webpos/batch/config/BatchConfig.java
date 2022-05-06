package com.example.webpos.batch.config;

import com.example.webpos.batch.service.JsonFileReader;
import com.example.webpos.batch.service.ProductProcessor;
import com.example.webpos.batch.service.ProductWriter;
import com.example.webpos.batch.model.Product;
import com.example.webpos.model.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;

    public ProductRepository repository;

    public DataSource dataSource;

    @Autowired
    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, ProductRepository repository, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.repository = repository;
        this.dataSource = dataSource;
    }

    @Bean
    public Job partitioningJob() throws IOException {
        return jobBuilderFactory
                .get("partitioningJob")
                .incrementer(new RunIdIncrementer())
                .start(masterStep())
                .build();
    }
    @Bean
    public Step masterStep() throws IOException {
        return stepBuilderFactory
                .get("masterStep")
                .partitioner("slaveStep", partitioner())
                .step(slaveStep())
                .build();
    }

    @Bean
    public Step slaveStep() throws FileNotFoundException {
        return stepBuilderFactory
                .get("slaveStep")
                .<JsonNode, Product>chunk(1000)
                .reader(itemReader(null))
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    public Partitioner partitioner() throws IOException {
        MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        partitioner.setResources(resolver.getResources("file:src/main/resources/data/*100.json"));
        return partitioner;
    }

    @Bean
    @StepScope
    public ItemReader<JsonNode> itemReader(@Value("#{stepExecutionContext['fileName']}") String filename) {
        return new JsonFileReader(filename);
    }

    @Bean
    public ItemProcessor<JsonNode, Product> itemProcessor(){
        return new ProductProcessor();
    }

    @Bean
    public ItemWriter<Product> itemWriter(){
        return new ProductWriter(dataSource);
    }

}
