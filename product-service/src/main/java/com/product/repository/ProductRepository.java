package com.product.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.product.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
	@Modifying
	@Transactional
	@Query("UPDATE Product p SET p.stock = p.stock - :qty WHERE p.id = :id AND p.stock >= :qty")
	int decreaseStockIfEnough(@Param("id") Long id, @Param("qty") Integer qty);
}