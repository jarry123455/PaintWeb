package com.pant.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.pant.model.Category;

public interface CategoryService {
	
	public Category saveCategory(Category category);

	public boolean existsByName(String name);

	public List<Category> getAllCategory();
	
	public boolean deleteCategory(int id);
	
	public Category getCategoryById(int id);
	
	public Page<Category> getCategoryPaginated(int currentPage,int size);
	
	public List<Category> getAllActiveCategory();

}
