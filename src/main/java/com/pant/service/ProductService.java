package com.pant.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import com.pant.model.Product;

public interface ProductService {

	public Product saveProduct(Product product);

	public List<Product> getAllProducts();
	
	public List<Product> getAllActiveProducts(String category);
	
	public Page<Product> getProdPaginated(int currentPage,int size);

	public Product getProductById(int id);

	public boolean deleteProduct(int id);

	public Product updateProduct(Product product, MultipartFile file);

}
