package com.pant.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import com.pant.model.Product;
import com.pant.repository.ProductRepository;
import com.pant.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepository productRepository;

	@Override
	public Product saveProduct(Product product) {

		return productRepository.save(product);
	}

	@Override
	public List<Product> getAllProducts() {

		return productRepository.findAll();
	}

	@Override
	public boolean deleteProduct(int id) {
		Product product = productRepository.findById(id).orElse(null);
		if (!ObjectUtils.isEmpty(product)) {
			productRepository.delete(product);
			return true;
		}
		return false;
	}

	@Override
	public Product getProductById(int id) {
		Product product = productRepository.findById(id).orElse(null);

		return product;
	}

	@Override
	public Product updateProduct(Product product, MultipartFile file) {

		Product dbProduct = getProductById(product.getId());

		String image = file.isEmpty() ? dbProduct.getImage() : file.getOriginalFilename();

		dbProduct.setTitle(product.getTitle());
		dbProduct.setDescription(product.getDescription());
		dbProduct.setCategory(product.getCategory());
		dbProduct.setPrice(product.getPrice());
		dbProduct.setStock(product.getStock());
		dbProduct.setImage(image);
		dbProduct.setDiscount(product.getDiscount());
		dbProduct.setIsActive(product.getIsActive());
		double discount = product.getPrice() * (product.getDiscount() / 100.0);
		double discountPrice = product.getPrice() - discount;

		dbProduct.setDiscountPrice(discountPrice);

		Product updateProduct = productRepository.save(dbProduct);

		if (!ObjectUtils.isEmpty(updateProduct)) {
			if (!file.isEmpty()) {

				try {

					String uploadDir = "static/img/product_img";

					// Create directory if it does not exist
					File directory = new File(uploadDir);
					if (!directory.exists()) {
						directory.mkdirs();
					}

					Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
					System.out.println(path);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return product;

		}
		return null;
	}

	@Override
	public Page<Product> getProdPaginated(int currentPage, int size) {

		Pageable p = PageRequest.of(currentPage, size);

		return productRepository.findAll(p);
	}

	@Override
	public List<Product> getAllActiveProducts(String category) {

		List<Product> products = null;
		if (ObjectUtils.isEmpty(category)) {
			products = productRepository.findByIsActiveTrue();
		}else {
			products = productRepository.findByCategory(category);
		}

		return products;
	}

}
