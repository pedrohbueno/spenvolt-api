package com.spenvolt.api.controller;

import com.spenvolt.api.model.Device;
import com.spenvolt.api.service.DeviceOfferService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class DeviceOfferController {

    // Service responsável pela lógica de busca dos produtos.
    private final DeviceOfferService service;

    // O Spring injeta automaticamente o ProductService.
    public DeviceOfferController(DeviceOfferService service) {
        this.service = service;
    }

    // Endpoint:
    // GET /products/search?query=geladeira
    //
    // O @RequestParam pega o valor de "query" da URL.
    @GetMapping("/search")
    public List<Device> search(@RequestParam String query) {

        // Passa a pesquisa para o Service.
        return service.search(query);
    }
}