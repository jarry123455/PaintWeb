package com.pant.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.pant.model.Admin;
import com.pant.model.Category;
import com.pant.model.Product;
import com.pant.service.AdminService;
import com.pant.service.CategoryService;
import com.pant.service.CustomerService;
import com.pant.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private ProductService productService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private CustomerService customerService;

	@GetMapping("/customerOrder")
	public String customerOrder() {

		return "/admin/customerOrder";
	}

	@GetMapping("/")
	public String index(Model model) {

		model.addAttribute("title", "Admin Home Page");
		return "admin/index";
	}

	@GetMapping("/userDetails")
	public String userDetails(Model model) {

		model.addAttribute("customers", customerService.getAllCustomers());
		model.addAttribute("title", "View Customer Details");
		return "/admin/userDetails";
	}

	@PostMapping("/saveProduct")
	public String saveProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile image,
			HttpSession session) throws IOException {

		String imageName = image.isEmpty() ? "default.jpg" : image.getOriginalFilename();

		product.setImage(imageName);
		product.setDiscount(0);
		product.setDiscountPrice(product.getPrice());

		Product saveProduct = productService.saveProduct(product);

		if (!ObjectUtils.isEmpty(saveProduct)) {

			if (!image.isEmpty()) {

				String uploadDir = "static/img/product_img";

				// Create directory if it does not exist
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				Path path = Paths.get(uploadDir + File.separator + image.getOriginalFilename());
				System.out.println(path);
				Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("succMsg", "Product Saved Successfully");
		} else {
			session.setAttribute("errMsg", "Internal Server Error!");
		}

		return "redirect:/admin/addProduct";
	}

	@GetMapping("/addProduct")
	public String addProduct(Model model) {

		List<Category> allCategory = categoryService.getAllCategory();
		model.addAttribute("allCategory", allCategory);
		model.addAttribute("title", "Add Product");
		return "admin/addProduct";
	}

	// per page 2 products
	@GetMapping("/viewProduct")
	public String viewProduct(Model model) {

		model.addAttribute("products", productService.getAllProducts());
		// model.addAttribute("title", "View All Products Details");
		return findPaginated(0, model);
	}

	// return findPaginated(0, model);

	@GetMapping("/page/{pageno}")
	public String findPaginated(@PathVariable int pageno, Model model) {

		Page<Product> prodlist = productService.getProdPaginated(pageno, 2);

		model.addAttribute("products", prodlist);
		model.addAttribute("currentPage", pageno);
		model.addAttribute("totalPage", prodlist.getTotalPages());
		model.addAttribute("totalItem", prodlist.getTotalElements());
		return "admin/viewProduct";
	}

	@GetMapping("/deleteProduct/{id}")
	public String deleteProduct(@PathVariable int id, HttpSession session) {

		boolean deleteProduct = productService.deleteProduct(id);

		if (deleteProduct) {
			session.setAttribute("succMsg", "Product Deleleted  Successfully");
		} else {
			session.setAttribute("errMsg", "Something went wrong !");
		}

		return "redirect:/admin/viewProduct";
	}

	@GetMapping("/editProuduct/{id}")
	public String editProudct(@PathVariable int id, Model model) {

		model.addAttribute("product", productService.getProductById(id));
		List<Category> allCategory = categoryService.getAllCategory();
		model.addAttribute("allCategory", allCategory);
		model.addAttribute("title", "Edit Product");
		return "/admin/editProduct";
	}

	@PostMapping("/updateProduct")
	public String updateProduct(@ModelAttribute Product product, @RequestParam("file") MultipartFile file,
			HttpSession session) {

		if (product.getDiscount() < 0 || product.getDiscount() > 100) {
			session.setAttribute("errMsg", "Invalid Discount");
		} else {

			Product updateProduct = productService.updateProduct(product, file);

			if (!ObjectUtils.isArray(updateProduct)) {
				session.setAttribute("succMsg", "Product Updated Successfully");
			} else {
				session.setAttribute("errMsg", "Something went wrong");
			}
		}
		return "redirect:/admin/editProuduct/" + product.getId();
	}

	@GetMapping("/category")
	public String category(Model model) {

		model.addAttribute("categories", categoryService.getAllCategory());
		// model.addAttribute("title", "View All Category");
		return "admin/category";
	}

	@PostMapping("/saveCategory")
	public String saveCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
		category.setImageName(imageName);

		Boolean existCategory = categoryService.existsByName(category.getName());

		if (existCategory) {

			session.setAttribute("errMsg", "Category Name Already exits");
		} else {
			Category saveCategory = categoryService.saveCategory(category);

			if (ObjectUtils.isEmpty(saveCategory)) {
				session.setAttribute("errMsg", "Not Saved ! Internal Server Error");
			} else {

//				File saveFile = new ClassPathResource("static/img").getFile();
//
//				Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + "category_img" + File.separator
//						+ file.getOriginalFilename());
//
//				System.out.println(path);
//				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				// chatgpt
				// Define the directory where you want to save the image
				String uploadDir = "static/img/category_img";

				// Create directory if it does not exist
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				session.setAttribute("succMsg", "Saved Successfully");
			}
		}
		return "redirect:/admin/category";
	}

	@GetMapping("/deleteCategory/{id}")
	public String deleteCategory(@PathVariable int id, HttpSession session) {

		boolean deleteCategory = categoryService.deleteCategory(id);

		if (deleteCategory) {
			session.setAttribute("succMsg", "Category Deleted Sucessfully");
		} else {
			session.setAttribute("errMsg", "Something went wrong !");
		}
		return "redirect:/admin/category";
	}

	@GetMapping("/editCategory/{id}")
	public String loadEditCategory(@PathVariable int id, Model model) {

		model.addAttribute("title", "Edit the Category");
		model.addAttribute("category", categoryService.getCategoryById(id));
		return "/admin/editCategory";
	}

	@PostMapping("/updateCategory")
	public String updateCategory(@ModelAttribute Category category, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		Category oldCategory = categoryService.getCategoryById(category.getId());

		String imageName = file.isEmpty() ? oldCategory.getImageName() : file.getOriginalFilename();

		if (!ObjectUtils.isEmpty(category)) {

			oldCategory.setName(category.getName());
			oldCategory.setIsActive(category.getIsActive());
			oldCategory.setImageName(imageName);
		}

		Category updateCategory = categoryService.saveCategory(oldCategory);

		if (!ObjectUtils.isEmpty(updateCategory)) {

			if (!file.isEmpty()) {

				String uploadDir = "static/img/category_img";

				// Create directory if it does not exist
				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				Path path = Paths.get(uploadDir + File.separator + file.getOriginalFilename());
				System.out.println(path);
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			}

			session.setAttribute("succMsg", "Category Updated Successfully");
		} else {
			session.setAttribute("errMsg", "Something went wrong");
		}

		return "redirect:/admin/editCategory/" + category.getId();
	}

	@PostMapping("/loadAdmin")
	public String loadAdmin(@ModelAttribute Admin admin, HttpSession session) {

		Admin saveAdmin = adminService.saveAdmin(admin);

		if (!ObjectUtils.isEmpty(saveAdmin)) {

			session.setAttribute("succMsg", "Admin Saved Sucessfully");
		} else {
			session.setAttribute("errMsg", "Internal Server Error!");
		}
		return "redirect:/admin/addAdmin";
	}

	@GetMapping("/addAdmin")
	public String addAdmin(Model model) {

		model.addAttribute("admins", adminService.getAllAdmin());
		model.addAttribute("title", "View All Admin");
		return "/admin/addAdmin";
	}

	@GetMapping("/deleteAdmin/{id}")
	public String deleteAdmin(@PathVariable int id, HttpSession session) {

		boolean deleteAdmin = adminService.deleteAdmin(id);

		if (deleteAdmin) {
			session.setAttribute("succMsg", "Admin Deleleted  Successfully");
		} else {
			session.setAttribute("errMsg", "Something went wrong !");
		}

		return "redirect:/admin/addAdmin";
	}

	@GetMapping("/editAdmin/{id}")
	public String editAdmin(@PathVariable int id, Model model) {

		model.addAttribute("title", "Edit Admin Details");
		model.addAttribute("admin", adminService.getAdminById(id));
		return "/admin/editAdmin";
	}

	@PostMapping("/updateAdmin")
	public String updateAdmin(@ModelAttribute Admin admin, HttpSession session) {
		Admin updateAdmin = adminService.updateAdmin(admin);

		if (!ObjectUtils.isArray(updateAdmin)) {
			session.setAttribute("succMsg", "Admin Details Updated Successfully");
		} else {
			session.setAttribute("errMsg", "Something went wrong");
		}
		return "redirect:/admin/editAdmin/" + admin.getId();
	}

	@GetMapping("/deleteCustomer/{id}")
	public String deleteCustomer(@PathVariable int id, HttpSession session) {

		boolean deleteCustomer = customerService.deleteCustomer(id);

		if (deleteCustomer) {
			session.setAttribute("succMsg", "Customer Deleleted  Successfully");
		} else {
			session.setAttribute("errMsg", "Something went wrong !");
		}
		return "redirect:/admin/userDetails";
	}

}
