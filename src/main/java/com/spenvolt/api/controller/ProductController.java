package com.spenvolt.api.controller;

import com.spenvolt.api.model.Product;
import com.spenvolt.api.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    // Service responsável pela lógica de busca dos produtos.
    private final ProductService service;

    // O Spring injeta automaticamente o ProductService.
    public ProductController(ProductService service) {
        this.service = service;
    }

    // Endpoint:
    // GET /products/search?query=geladeira
    //
    // O @RequestParam pega o valor de "query" da URL.
    @GetMapping("/search")
    public List<Product> search(@RequestParam String query) {

        // Passa a pesquisa para o Service.
        return service.search(query);
    }
}