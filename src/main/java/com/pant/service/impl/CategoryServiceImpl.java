package com.pant.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.pant.model.Category;
import com.pant.repository.CategoryRepository;
import com.pant.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public Category saveCategory(Category category) {
		return categoryRepository.save(category);
	}

	@Override
	public List<Category> getAllCategory() {
		
		List<Category> categories = categoryRepository.findByIsActiveTrue();		
		return categories;
		
	}

	@Override
	public boolean existsByName(String name) {
		
		return categoryRepository.existsByName(name);
	}

	@Override
	public boolean deleteCategory(int id) {
		Category category = categoryRepository.findById(id).orElse(null);
		if(!ObjectUtils.isEmpty(category))
		{
			categoryRepository.delete(category);
			return true;
		}
		return false;
	}

	@Override
	public Category getCategoryById(int id) {
		
		Category category = categoryRepository.findById(id).orElse(null);
		return category;
	}

	@Override
	public Page<Category> getCategoryPaginated(int currentPage, int size) {
		
		PageRequest pageRequest = PageRequest.of(currentPage, size);
		
		return categoryRepository.findAll(pageRequest);
	}

	@Override
	public List<Category> getAllActiveCategory() {
		List<Category> categories = categoryRepository.findByIsActiveTrue();
		
		return categories;
	}

	

	

}
