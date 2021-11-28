package com.easy2excel.sse.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNo;
    private String address;
    private String company;
}
