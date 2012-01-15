package com.spring.bl.dao;

import javax.persistence.Id;

import org.hibernate.annotations.Entity;
@Entity

public class Product {
	@Id
	private Integer id;

	private String name;

	private String description;

	private float price;

	Product() {
		// S—lo el manager puede constuir nuevas instancias
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

}