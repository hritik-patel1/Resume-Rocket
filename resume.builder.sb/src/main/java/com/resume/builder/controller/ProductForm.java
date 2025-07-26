package com.resume.builder.controller;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductForm {
    private String prodName;
    private Double price;
    private MultipartFile pdf;
}
