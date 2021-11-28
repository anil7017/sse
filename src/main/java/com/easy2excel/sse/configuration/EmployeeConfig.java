package com.easy2excel.sse.configuration;

import com.easy2excel.sse.model.Employee;
import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.function.Supplier;

@Configuration
public class EmployeeConfig {
    Faker faker = new Faker();
    @Bean
    public Sinks.Many<Employee> many(){
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    Supplier<Flux<Employee>> send() {
        Faker faker = new Faker();
        return () -> {
            return Flux.interval(Duration.ofSeconds(1)).map(aLong -> {
                return Employee.builder()
                        .firstName(faker.name().firstName())
                        .lastName(faker.name().lastName())
                        .address(faker.address().streetAddress())
                        .company( (aLong % 2 == 0) ? "Inoveren" : "ITC")
                        .id(aLong)
                        .build();
            });
        };
    }

}
