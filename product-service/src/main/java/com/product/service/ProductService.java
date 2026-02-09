package com.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;
import com.product.entity.Product;
import com.product.exception.ProductNotFoundException;
import com.product.repository.ProductRepository;

@Service
public class ProductService {

	private final ProductRepository repo;

	public ProductService(ProductRepository repo) {
		this.repo = repo;
	}

	public ProductResponse create(ProductRequest req) {
		Product p = new Product();

		p.setName(req.getName());
		p.setPrice(req.getPrice());
		p.setStock(req.getStock());

		Product saved = repo.save(p);
		return toResponse(saved);
	}

	public List<ProductResponse> getAll() {
		return repo.findAll().stream().map(this::toResponse).toList();
	}

	public ProductResponse getById(Long id) {
		Product p = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		return toResponse(p);
	}

	public ProductResponse update(Long id, ProductRequest req) {
		Product p = repo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

		// update fields
		p.setName(req.getName());
		p.setPrice(req.getPrice());
		p.setStock(req.getStock());

		Product saved = repo.save(p);
		return toResponse(saved);
	}

	public void delete(Long id) {
		if (!repo.existsById(id))
			throw new ProductNotFoundException(id);
		repo.deleteById(id);
	}

	@Transactional
	public void decreaseStock(Long id, Integer qty) {
		if (qty == null || qty <= 0)
			throw new IllegalArgumentException("qty must be > 0");

		int updated = repo.decreaseStockIfEnough(id, qty);
		if (updated == 0) {
			throw new RuntimeException("Not enough stock or product not found");
		}
	}

	private ProductResponse toResponse(Product p) {
		return new ProductResponse(p.getId(), p.getName(), p.getPrice(), p.getStock());
	}
}
