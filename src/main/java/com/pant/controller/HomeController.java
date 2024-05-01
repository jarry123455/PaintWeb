package com.pant.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.pant.model.Admin;
import com.pant.model.Category;
import com.pant.model.Customer;
import com.pant.model.Product;
import com.pant.service.AdminService;
import com.pant.service.CategoryService;
import com.pant.service.CustomerService;
import com.pant.service.ProductService;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private AdminService adminService;
	
	@PostMapping("/loadAdmin")
	public String loadAdmin(@ModelAttribute Admin admin, HttpSession session) {

		Admin saveAdmin = adminService.saveAdmin(admin);

		if (!ObjectUtils.isEmpty(saveAdmin)) {

			session.setAttribute("succMsg", "Admin Saved Sucessfully");
		} else {
			session.setAttribute("errMsg", "Internal Server Error!");
		}
		return "redirect:/addAdmin";
	}

	@GetMapping("/")
	public String index(Model model) {

		model.addAttribute("categories",categoryService.getAllCategory());
		model.addAttribute("title", "Home - Welcome to Paint Store");
		return "index";
	}
	
	@GetMapping("/signin")
	public String login(Model model) {
	    model.addAttribute("title", "Welcome to Login Page");
	    return "login";
	}

	@GetMapping("/register")
	public String register(Model model) {

		model.addAttribute("customers",customerService.getAllCustomers());
		model.addAttribute("title", "Register - Sign Up First");
		return "register";
	}

	@PostMapping("/registerCustomer")
	public String registerCustomer(@ModelAttribute Customer customer, @RequestParam("file") MultipartFile file,
			HttpSession session) throws IOException {

		String imageName = file != null ? file.getOriginalFilename() : "default.jpg";
		customer.setImage(imageName);

		boolean existsByEmail = customerService.existsByEmail(customer.getEmail());

		if (existsByEmail) {
			session.setAttribute("errMsg", "Customer Already Exists");
		} else {
			Customer saveCustomer = customerService.saveCustomer(customer);

			if (ObjectUtils.isEmpty(saveCustomer)) {
				session.setAttribute("errMsg", "Not Saved ! Internal Server Error");
			} else {

				String uploadDir = "static/img/customer_img";

				File directory = new File(uploadDir);
				if (!directory.exists()) {
					directory.mkdirs();
				}

				try {
					String filePath = uploadDir + File.separator + file.getOriginalFilename();
					java.nio.file.Path path = java.nio.file.Paths.get(filePath);

					System.out.println("Saving image to: " + filePath);

					// Copy file to the target location
					Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

					session.setAttribute("succMsg", "Saved Successfully");
				} catch (IOException e) {
					session.setAttribute("errMsg", "Error Saving Image");
				}
			}
		}

		return "redirect:/register";
	}

	@GetMapping("/products")
	public String products(Model model,@RequestParam(value = "category",defaultValue = "") String category) {

		//model.addAttribute("products",productService.getAllProducts());
		
		List<Category> allActiveCategory = categoryService.getAllActiveCategory();
		List<Product> allActiveProducts = productService.getAllActiveProducts(category);
		model.addAttribute("categories", allActiveCategory);
		model.addAttribute("products", allActiveProducts);
		model.addAttribute("paramValue",category );
		model.addAttribute("title", "Product - Different Product");
		
		return "product";
	}

	@GetMapping("/product/{id}")
	public String product(Model model,@PathVariable int id) {

		Product productById = productService.getProductById(id);
		model.addAttribute("p", productById);
		model.addAttribute("title", "Product Details");
		return "view_product";
	}

	
	
	@GetMapping("/artregister")
	public String artregister(Model model) {

		model.addAttribute("title", "For Login Signup First");
		return "artregister";
	}
	
	@GetMapping("/addAdmin")
	public String addAdmin(Model model) {
		
		model.addAttribute("title", "For Login Signup First");
		return "addAdmin";
	}
	
	@GetMapping("/adminLogin")
	public String adminLogin(Model model) {
		
		model.addAttribute("title", "Welcome to Admin Login Page");
		return "adminLogin";
	}
	
	@GetMapping("/artlogin")
	public String artlogin(Model model) {
		
		model.addAttribute("title", "Welcome to Artist Login Page");
		return "artlogin";
	}
}
