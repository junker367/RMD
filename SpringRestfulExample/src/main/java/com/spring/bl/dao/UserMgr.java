package com.spring.bl.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


@Service
public class UserMgr {

	@Resource
	private Dao dao;

	public Product newUser() {
		return new Product();
	}

	public void persist(Product product) {
		dao.persist(product);
	}

	public List<Product> getProducts() {
		final List<Product> list = dao.find(Product.class);
		return list;
	}
}