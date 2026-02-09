package com.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "product-service"
		)
public interface ProductClient {

    @GetMapping("/products/{id}")
    ProductResponse getById(@PathVariable("id") Long id);
    @PutMapping("/products/{id}/decrease")
    void decreaseStock(@PathVariable("id") Long id, @RequestParam("qty") Integer qty);
}

