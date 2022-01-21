package br.com.furlam.products.model;

import lombok.Data;

@Data
public class ProductEvent {

    private Long productId;

    private String code;

    private String username;
}
