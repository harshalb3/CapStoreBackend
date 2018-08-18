package com.capgemini.storeserver.controllers;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.storeserver.beans.Address;
import com.capgemini.storeserver.beans.Cart;
import com.capgemini.storeserver.beans.Category;
import com.capgemini.storeserver.beans.Customer;
import com.capgemini.storeserver.beans.Merchant;
import com.capgemini.storeserver.beans.Product;
import com.capgemini.storeserver.exceptions.CustomerNotFoundException;
import com.capgemini.storeserver.exceptions.InvalidInputException;
import com.capgemini.storeserver.exceptions.ProductNotFoundException;
import com.capgemini.storeserver.exceptions.ProductUnavailableException;
import com.capgemini.storeserver.services.AdminServices;

@RestController	
public class AdminActionController {
	
	public Customer customer;

	@Autowired
	private AdminServices adminService; 
	
	//Working
	@RequestMapping(value="/addMerchant", method=RequestMethod.POST)
	public void registerMerchant(@RequestBody Merchant merchant) {
		
		adminService.addMerchant(merchant);
	}
	
	@RequestMapping(value="/removeMerchant")
	public void removeMerchant(int merchantId) {
		
		adminService.removeMerchant(merchantId);
	}
	@RequestMapping(value = "/getAllProduct", method = RequestMethod.GET)
	public List<Product> getAllMerchantProduct() {
		List<Product> product = adminService.viewAllProducts();
		Iterator<Product> iterator = product.iterator();
		while (iterator.hasNext())
			System.out.println(iterator.next());
		return adminService.viewAllProducts();
	}

	@RequestMapping(value = "/updateCategory", method = RequestMethod.POST)
	public void updateCategory(@RequestParam("categoryId") int categoryId,
			@RequestParam("categoryName") String categoryName, @RequestParam("type") String type) {
		adminService.updateCategory(categoryId, categoryName, type);
	}
	
	
	// Customer SignUp
		@RequestMapping(value = "/signUp", method = RequestMethod.POST)
		public void signUp(@RequestBody Customer customer) {
			adminService.signUp(customer);
		}

		// CustomerSignIn
		@RequestMapping(value = "/customerSignIn")
		public ResponseEntity<String> customerSignIn(String email, String password) throws InvalidInputException {
			customer = adminService.customerSignIn(email, password);
			String name = customer.getCustomerName();
			return new ResponseEntity<String>(name, HttpStatus.OK);
		}

		// getCustomerDetails
		@RequestMapping(value = "/getCustomerDetails")
		public Customer getCustomerDetails(String phoneNumber)
				throws InvalidInputException {
			customer = adminService.getCustomerDetails(phoneNumber);
			return  customer;
		}

		// getAllProducts
		@RequestMapping(value = "/getAllProducts")
		public List getAllProductsFromDB()
				throws InvalidInputException {
			List <Product> products = adminService.getAllProducts();
			return  products;
		}

		@RequestMapping(value = "/getProductById")
		public Product getProductById(int productId) throws InvalidInputException {
			Product product = adminService.getProductById(productId);
			return  product;
		}
		
		@RequestMapping(value = "/getProductByCategory")
		public List<Product> getProductByCategory(Category category ) throws InvalidInputException {
			List<Product> products = adminService.getProductByCategory(category);
			return  products;
		}
		
		@RequestMapping(value = "/getDeliveryStatus")
		public String getDeliveryStatus(int orderId) throws InvalidInputException {
			String status = adminService.getDeliveryStatus(orderId);
			return  status;
		}
		//gvk
		@RequestMapping(value="/updateSecurityQuestion")
		public boolean updateSecurityQuestion(String phoneNumber,String securityQuestion) throws InvalidInputException{
			return  adminService.updateSecurityQuestion(phoneNumber, securityQuestion);
		}
		
		@RequestMapping(value="/updateSecurityAnswer")
		public boolean updateSecurityAnswer(String phoneNumber,String securityAnswer){
			try {
				return  adminService.updateSecurityAnswer(phoneNumber, securityAnswer);
			} catch (InvalidInputException e) {
				e.printStackTrace();
				return false;
			}
		}
		@RequestMapping(value="/updateCardNumber")
		public boolean updateCardNumber(String phoneNumber,String cardNumber){
			try {
				return  adminService.updateCardNumber(phoneNumber, cardNumber);
			} catch (InvalidInputException e) {
				e.printStackTrace();
				return false;
			}
		}
		@RequestMapping(value="/updateCustomerName")
			public boolean updateCustomerName(String phoneNumber,String customerName){
				try {
					return  adminService.updateCustomerName(phoneNumber, customerName);
				} catch (InvalidInputException e) {
					e.printStackTrace();
					return false;
				}
		}
		//pavani
		@RequestMapping(value = "/addAddress")
		public void addAddressDetails(Address address)
		{
			adminService.addAddress(address);
		}
		@RequestMapping(value = "/viewAddressDetails")
		public Address getAddressDetails(int addressId)
		{
			Address address;

			address = adminService.getAddress(addressId);

			return  address;
		}
		@RequestMapping(value = "/getCoupon")
		public double getCouponDetails(int cartId)
		{
			double price =  adminService.applyCoupon(cartId);
	        return  price;
		}
		//aksh
		@RequestMapping(value= "/getWishlist")
		public List<Product> getWishlist(String phoneNumber) throws InvalidInputException {
			return adminService.getWishlist(phoneNumber);
		}

		@RequestMapping(value= "/addProductToWishlist")
		public boolean addProductToWishlist(String phoneNumber, int productId) throws InvalidInputException {
			return adminService.addProductToWishlist(phoneNumber, productId);
		}

		@RequestMapping(value= "/removeProductFromWishlist")
		public boolean removeProductFromWishlist(String phoneNumber,int productId) throws InvalidInputException {
			return adminService.removeProductFromWishlist(phoneNumber, productId);
		}


		@RequestMapping(value= "/setReview")
		public void setReview(String phoneNumber,int rating,String comments,int productId) throws InvalidInputException {
			adminService.setReviewMethod(phoneNumber, rating, comments, productId);
		}

		@RequestMapping(value= "/securityQuestion")
		public String securityQuestion(String phoneNumber, String securityAnswer) throws InvalidInputException {
			return adminService.securityQuestion(phoneNumber, securityAnswer);
		}
		
		@RequestMapping(value= "/applyDiscount")
		public double applyDiscount(int productId) throws InvalidInputException {
			return adminService.applyDiscount(productId);
		}
		@RequestMapping(value= "/forgotPassword")
		public String forgotPassword(String phoneNumber) throws InvalidInputException, CustomerNotFoundException {
			return adminService.forgotPassword(phoneNumber);
		}
		@RequestMapping(value= "/onlinePayment")
		public void onlinePayment(String phoneNumber,String cardNumber) throws InvalidInputException {
			adminService.onlinePayment(cardNumber, phoneNumber);
		}
		@RequestMapping(value= "/addProductToNewCart")
		public Cart addProductToNewCart(String phoneNumber,int quantity, int productId) throws InvalidInputException, ProductUnavailableException {
			return adminService.addProductToNewCart(phoneNumber, productId, quantity);
		}
		@RequestMapping(value= "/updateCart")
		public Cart updateCart(String phoneNumber,int quantity, int productId) throws InvalidInputException, ProductUnavailableException {
			return adminService.updateCart(phoneNumber, productId, quantity);
		}
		@RequestMapping(value= "/removeFromCart")
		public Cart removeFromCart(String phoneNumber,int productId, int quantity) throws InvalidInputException {
			return adminService.removeProductFromCart(phoneNumber, productId);
		}
		@RequestMapping(value= "/getCart")
		public List<Product> getCart(String phoneNumber) throws InvalidInputException {
			return adminService.getAllProductsFromCart(phoneNumber);
		}
		@RequestMapping(value="/changePassword")
		public boolean changePassword(String phoneNumber, String newPassword) throws InvalidInputException, CustomerNotFoundException {
			return adminService.changePassword(phoneNumber, newPassword);
		}
		
		
		@RequestMapping(value="/merchantSignIn",method=RequestMethod.POST)
		public void addMerchant(@RequestBody Merchant merchant) {
			
			merchant = adminService.registerMerchant(merchant);		
		}
		
		@RequestMapping(value = "/ProductSuccessPage/{merchantId}",method=RequestMethod.POST)
		public void addProduct(@PathVariable("merchantId") int merchantId,@RequestBody Product product) throws ProductNotFoundException {
			
			Merchant merchant = new Merchant(merchantId);
			product.setMerchant(merchant);
			product = adminService.addProduct(product);
		}
		
		
		@RequestMapping(value="addProduct", method=RequestMethod.POST)
		public void getAddProductPage(@RequestBody Product product) throws ProductNotFoundException {
			
			adminService.addProduct(product);
			
		}
		
		@RequestMapping(value = "removedProduct")
		public void removeProduct(int productId) {

			adminService.removeProduct(productId);
		}
		@RequestMapping(value ="updateProduct",method=RequestMethod.POST)
		public void updateProduct(@RequestBody Product product) throws ProductNotFoundException {
			adminService.updateProduct(product);
		}
		@RequestMapping(value="myProfilesuccess")
		public Merchant myProfile( int merchantId ) {
		Merchant merchant = adminService.findMerchantId(merchantId);
		return merchant;
		}
		
		@RequestMapping(value = "getAllMerchantProducts", method=RequestMethod.GET)
		public List<Product> getAllMerchantProducts() throws InvalidInputException {
			
			return adminService.getAllMerchantProducts();
		}
}
