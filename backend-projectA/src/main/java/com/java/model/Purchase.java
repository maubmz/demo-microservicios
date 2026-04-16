package com.java.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
public class Purchase {
    private Integer id;
    @JsonIgnoreProperties("purchases")
    private Client client;
    private List<Product> products;
    private int quantity;
}
