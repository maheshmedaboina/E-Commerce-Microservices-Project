package com.order.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.order.client.ProductClient;
import com.order.client.ProductResponse;
import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;
import com.order.entity.Order;
import com.order.entity.OrderStatus;
import com.order.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final ProductClient productClient;

    public OrderService(OrderRepository repo, ProductClient productClient) {
        this.repo = repo;
        this.productClient = productClient;
    }

    public OrderResponse placeOrder(OrderRequest req) {
        ProductResponse product;
        try {
            product = productClient.getById(req.getProductId());
        } catch (Exception ex) {
            return saveFailed(req, "PRODUCT_SERVICE_DOWN_OR_NOT_FOUND");
        }

        Integer qty = req.getQuantity();
        boolean ok = product != null
                && qty != null && qty > 0
                && product.getStock() != null
                && product.getStock() >= qty;

        if (!ok) return saveFailed(req, "INVALID_OR_INSUFFICIENT_STOCK");

        try {
            productClient.decreaseStock(req.getProductId(), qty); // <-- real stock reduction
        } catch (Exception ex) {
            // stock could have changed between getById and decrease (race condition)
            return saveFailed(req, "STOCK_UPDATE_FAILED");
        }

        Order order = new Order();
        order.setProductId(req.getProductId());
        order.setQuantity(qty);
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.CREATED);
        order.setTotalAmount(product.getPrice() * qty);

        return toResponse(repo.save(order));
    }

    private OrderResponse saveFailed(OrderRequest req, String reason) {
        Order order = new Order();
		order.setProductId(req.getProductId());
		order.setQuantity(req.getQuantity());
        order.setCreatedAt(Instant.now());
        order.setStatus(OrderStatus.FAILED);
        order.setTotalAmount(0.0);
        // optionally store reason in a field if you have one
        return toResponse(repo.save(order));
    }


    public List<OrderResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public OrderResponse getById(Long id) {
        Order o = repo.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
        return toResponse(o);
    }

    private OrderResponse toResponse(Order o) {
        OrderResponse res = new OrderResponse();
        res.setId(o.getId());
        res.setProductId(o.getProductId());
        res.setQuantity(o.getQuantity());
        res.setTotalAmount(o.getTotalAmount());
        res.setStatus(o.getStatus());
        res.setCreatedAt(o.getCreatedAt());
        return res;
    }
    
    
}

