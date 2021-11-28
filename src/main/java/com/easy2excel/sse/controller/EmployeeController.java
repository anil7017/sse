package com.easy2excel.sse.controller;

import com.easy2excel.sse.model.Employee;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.function.Supplier;

@RestController
@CrossOrigin(maxAge = 3600)
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private Sinks.Many<Employee> many;

    @Autowired
    private Supplier<Flux<Employee>> send;

    @GetMapping(value = "/sse/{name}",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Employee> employeeFlux(@PathVariable("name")String name){
        //m1().subscribe();
        Flux<Employee> emps = many.asFlux().filter(employee -> employee.getCompany().equals(name));
        //Flux<Employee> emps = m1().filter(employee -> employee.getCompany().equals(name));
        return emps;
    }
    @GetMapping("/test/{id}")
    public String test(@PathVariable("id")Long id){
        Faker faker = new Faker();
        Employee employee = Employee.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .address(faker.address().streetAddress())
                .company((id % 2 == 0) ? "Inoveren" : "ITC")
                .id(id)
                .build();
        many.tryEmitNext(employee);
        return "hii";
    }

    public Flux<Employee> m1(){
        /*return send.get().doOnNext(s -> {
            //many.tryEmitNext(s);
        });*/
        return send.get().doOnNext(employee -> {
            //many.tryEmitNext(employee);
        });
    }


    @GetMapping(value = "/fluxtest",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Employee> fluxTest(){
        Faker faker = new Faker();
        return Flux.interval(Duration.ofSeconds(5))
                .map(aLong -> {
                    Employee employee = Employee.builder()
                            .firstName(faker.name().firstName())
                            .lastName(faker.name().lastName())
                            .address(faker.address().streetAddress())
                            .company((aLong % 2 == 0) ? "Inoveren" : "ITC")
                            .id(aLong)
                            .build();
                    return employee;
                });

    }
}
