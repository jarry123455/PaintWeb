package com.pant.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(length = 120)
	private String title;
	
	@Column(length = 500)
	private String description;
	
	private String category;
	
	private double price;
	
	private int stock;
	
	private String image;
	
	private double discount;
	private double discountPrice;
	
	private Boolean isActive;
	
	private String artistName;
	
}
