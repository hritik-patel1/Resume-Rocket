package com.resume.builder.controller;

import com.resume.builder.model.Product;
import com.resume.builder.service.GeminiService;
import com.resume.builder.service.ProductService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService service;

    @Autowired
    GeminiService geminiService;

    @GetMapping("/products")
    public List<Product> getProducts(){
        return service.getProducts();
    }

    @RequestMapping("/product/{prodId}")
    public Product getProduct(@PathVariable int prodId){
        return service.getProductById(prodId);
    }
    @PostMapping(value = "/products/multi-part", consumes = "multipart/form-data")
    public String addProduct(@ModelAttribute ProductForm form) throws IOException {
        // handle form.getProdName(), form.getPrice(), form.getPdf()
        String resume = "";
        try (InputStream inputStream = form.getPdf().getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            resume = stripper.getText(document);

            // Print to console
            //System.out.println("Extracted PDF Text:\n" + resume);

//            return ResponseEntity.ok("PDF text extracted and printed to console.");
        } catch (Exception e) {
            e.printStackTrace();
//            return ResponseEntity.status(500).body("Failed to extract text from PDF.");
        }
        String jd  = form.getProdName();
        //System.out.println(jd);

        return geminiService.generate(jd, resume);

    }

    @PostMapping("/products")
    public void addProduct(@RequestBody Product product){
        service.addProduct(product);
    }
}
