package com.java.model;

import lombok.Data;

@Data
public class Product {
    private Integer id;
    private String name;
    private double price;
    private int stock;
}
