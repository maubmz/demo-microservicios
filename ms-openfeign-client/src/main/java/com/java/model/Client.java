package com.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class Client {
    private Integer id;
    private String name;
    private String email;
    private String phoneNumber;
    @JsonIgnoreProperties("client")
    private List<Purchase> purchases;
}
