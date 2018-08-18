package com.capgemini.storeserver.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.capgemini.storeserver.beans.Address;
import com.capgemini.storeserver.beans.Cart;
import com.capgemini.storeserver.beans.Category;
import com.capgemini.storeserver.beans.Coupon;
import com.capgemini.storeserver.beans.Customer;
import com.capgemini.storeserver.beans.Discount;
import com.capgemini.storeserver.beans.Merchant;
import com.capgemini.storeserver.beans.Orders;
import com.capgemini.storeserver.beans.Product;
import com.capgemini.storeserver.beans.Review;
import com.capgemini.storeserver.beans.Wishlist;
import com.capgemini.storeserver.exceptions.CustomerNotFoundException;
import com.capgemini.storeserver.exceptions.InvalidInputException;
import com.capgemini.storeserver.exceptions.MerchantNotFoundException;
import com.capgemini.storeserver.exceptions.ProductNotFoundException;
import com.capgemini.storeserver.exceptions.ProductUnavailableException;
import com.capgemini.storeserver.repo.AddressRepo;
import com.capgemini.storeserver.repo.AdminRepo;
import com.capgemini.storeserver.repo.CartRepo;
import com.capgemini.storeserver.repo.CategoryRepo;
import com.capgemini.storeserver.repo.CouponRepo;
import com.capgemini.storeserver.repo.CustomerRepo;
import com.capgemini.storeserver.repo.DiscountRepo;
import com.capgemini.storeserver.repo.MerchantRepo;
import com.capgemini.storeserver.repo.OrdersRepo;
import com.capgemini.storeserver.repo.ProductRepo;
import com.capgemini.storeserver.repo.ReviewRepo;
import com.capgemini.storeserver.repo.WishlistRepo;

@Service(value="adminServices")
public class AdminServicesImpl implements AdminServices {
	
	Customer customer;
	Review review;
	Product product;
	
	@Autowired
	private AdminRepo adminRepo;

	@Autowired
	private MerchantRepo merchantRepo;
	
	@Autowired
	private CustomerRepo customerRepo;
	@Autowired
	private CartRepo cartRepo;
	@Autowired
	private ReviewRepo reviewRepo;
	@Autowired
	private OrdersRepo ordersRepo;
	@Autowired
	private WishlistRepo wishlistRepo;
	@Autowired
	private AddressRepo addressRepo;
	@Autowired
	private DiscountRepo discountRepo;
	@Autowired
	private CouponRepo couponRepo;
	

	@Override
	public Merchant addMerchant(Merchant merchant) {
		
		return merchantRepo.save(merchant);
	}
	
	@Override
	public void removeMerchant(int merchantId) {

		merchantRepo.deleteById(merchantId);
	}
	
	@Autowired
	private CategoryRepo categoryRepo;
	
	@Autowired
	private ProductRepo productRepo;
	
	@Override
	public List<Product> viewAllProducts() {
		System.out.println(productRepo.findAll());

		return productRepo.findAll();
	}

	@Override
	@Transactional
	public Category updateCategory(int categoryId, String categoryName, String type) {
		Category category = categoryRepo.updateCategory(categoryId);
		category.setCategoryName(categoryName);
		category.setType(type);
		return category;

	}
	
	@Override
	public Customer signUp(Customer customer) {
		return customerRepo.save(customer);
	}

	@Override
	public String forgotPassword(String mobileNumber) throws CustomerNotFoundException{


		Customer customer = customerRepo.getOne(mobileNumber);
		if(customer==null)
			throw new CustomerNotFoundException("customer not found with mobile no.");
		else
			return customer.getSecurityQuestion();
	}

	@Override//encryption 
	public String securityQuestion(String phoneNumber,String securityAnswer) throws InvalidInputException {
		Customer customer = customerRepo.getOne(phoneNumber);
		if(securityAnswer.equals(customer.getSecurityAnswer()))
		{
			return customer.getPassword();
		}
		else
			throw new InvalidInputException("Invalid answer");
	}

	@Override
	public void onlinePayment(String cardNumber, String customerPhoneNumber) {
		Customer cust = new Customer();
		cust = customerRepo.getOne(customerPhoneNumber);
		cust.setCardNumber(cardNumber);
		customerRepo.save(cust);

	}


	@Override
	public void addAddress(Address address) {
		addressRepo.save(address);
	}

	@Override
	public Address getAddress(int addressId) {
		return addressRepo.getOne(addressId);
	}

	/*MERCHANT	
 @Override
	public void updateQuantity(int productId, int quantityOrdered, int orderId) {
		Orders order = ordersRepo.getOne(orderId);
		List<Product> products =order.getProducts();
		int productIndex =products.indexOf(new Product(productId));
		Product product =products.get(productIndex);
		int quantityLeft=0;
		if(order.getDeliveryStatus()!=null && order.isRefundRequest()==false)
		{
			quantityLeft = product.getProductQuantityAvailable()-quantityOrdered;
			if(quantityLeft==0)
			{
				product.setProductStatus(true);// product is now out of stock
			}
			product.setProductQuantityAvailable(quantityLeft);
			productRepo.save(product);
		}
		if(order.isRefundRequest()==true)
		{
			if(isRefundRequestValid(order))
			{
				quantityLeft = product.getProductQuantityAvailable()+quantityOrdered;
				product.setProductQuantityAvailable(quantityLeft);
				product.setProductStatus(false); // product is in stock
				productRepo.save(product);

			}
		}

	}*/

	public boolean isRefundRequestValid(Orders order) {
		Date date1 =order.getElligibleReturnDate();
		Date date2 = order.getRefundRequestDate();
		if(date1.after(date2))
		{return false;}
		else
			return true;


	}

	@Override //if discount is valid, we will get the discounted product price  
	//else we get the original product price
	public double applyDiscount(int productId) {
		Product product =productRepo.getOne(productId);
		System.out.println(product.getBrand());
		Discount discount = product.getDiscount();
		System.out.println(discount.getPercentDiscount());
		double price = product.getProductPrice();
		double finalPrice=price;
		if(discountIsValid(discount))
		{
			double percentDiscount =discount.getPercentDiscount();
			finalPrice=price-((price*percentDiscount)/100);
			product.setProductPrice(finalPrice);
		}
		return finalPrice;
	}

	public boolean discountIsValid(Discount discount) {
		System.out.println(discount.getPercentDiscount());
		Date date2 = discount.getEndDateOfDiscount();
		Date date1 = discount.getStartDateOfDiscount();
		if(date1.before(new Date()))
		{
			if(date2.after(new Date()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	//if coupon is valid, we will get the discounted cart price  
	//else we get the original cart price
	public double applyCoupon(int cartId) {
		Cart cart = cartRepo.getOne(cartId);
		Coupon coupon = cart.getCoupon();
		double cartAmount = cart.getTotalAmount();
		double finalPrice = cartAmount; 
		if(couponIsValid(coupon))
		{
			double couponDiscount = coupon.getCouponDiscountValue();
			finalPrice=cartAmount-((cartAmount*couponDiscount)/100);
			cart.setTotalAmount(finalPrice);
		}
		return finalPrice;
	}

	public boolean couponIsValid(Coupon coupon) {
		Date date2 = coupon.getCouponEndDate();
		Date date1 = coupon.getCouponStartDate();
		if(date1.before(new Date()))
		{
			if(date2.after(new Date()))
			{
				return true;
			}
		}
		return false;
	}
	@Override
	public List<Product> getAllProductsFromCart(String phoneNumber) throws InvalidInputException {
		Cart cart;

		Customer customer=customerRepo.getOne(phoneNumber);
		cart = cartRepo.findByCustomer(customer);
		return cart.getProducts();
	}
	@Override
	public void buyNowProduct(int productId, String phoneNumber, int quantity) {
		Product product =productRepo.getOne(productId);
		if(product.getProductQuantityAvailable()>quantity)
		{
			product.setProductQuantityAvailable(product.getProductQuantityAvailable()-quantity);
			product.setProductStatus(true);
		}
	}
	@Override
	public void buyNowCart(String phoneNumber) throws ProductUnavailableException {
		Customer customer = customerRepo.getOne(phoneNumber);
		Cart cart = cartRepo.findByCustomer(customer);
		List<Product> products = cart.getProducts();
		for (Product product : products) {
			if(product.getProductQuantityAvailable()>product.getCartQuantity())
			{
				product.setProductQuantityAvailable(product.getProductQuantityAvailable()-product.getCartQuantity());
			}
			else throw new ProductUnavailableException("This quantity of the product is unavailable");
		}

	}
	//gvk
	@Override
	public boolean updateSecurityQuestion(String phoneNumber,String securityQuestion) throws InvalidInputException {
		Customer customer=customerRepo.getOne(phoneNumber);
		if(customer!=null) 
		{	if(customer.getSecurityQuestion().equals(securityQuestion))
		{
			return false;
		}
		else
		{
			customer.setSecurityQuestion(securityQuestion);
			customerRepo.save(customer);
			return true;
		}
		}
		else
			return false;
	}
	@Override
	public boolean updateSecurityAnswer(String phoneNumber,String securityAnswer) throws InvalidInputException {
		Customer customer=customerRepo.getOne(phoneNumber);
		if(customer!=null) 
		{if(customer.getSecurityAnswer().equals(securityAnswer))
		{
			return false;
		}
		else
		{
			customer.setSecurityAnswer(securityAnswer);
			customerRepo.save(customer);
			return true;
		}

		}
		else
			return false;
	}
	@Override
	public boolean updateCardNumber(String phoneNumber,String cardNumber) throws InvalidInputException {
		Customer customer=customerRepo.getOne(phoneNumber);
		if(customer!=null) 
		{if(customer.getCardNumber().equals(cardNumber))
		{
			return false;
		}
		else
		{
			customer.setCardNumber(cardNumber);
			customerRepo.save(customer);
			return true;
		}
		}
		else
			return false;
	}
	@Override
	public boolean updateCustomerName(String phoneNumber,String customerName) throws InvalidInputException {
		Customer customer=customerRepo.getOne(phoneNumber);
		if(customer!=null) 
		{if(customer.getCustomerName().equals(customerName))
		{
			return false;
		}
		else {
			customer.setCustomerName(customerName);
			customerRepo.save(customer);
			return true;
		}
		}
		else
			return false;
	}

	@Override
	public Orders getTransaction(int orderId) throws InvalidInputException {
		return ordersRepo.getOne(orderId);
	}

	@Override
	public Product searchByProductName(String productName) throws InvalidInputException {

		Product product =productRepo.findByProductName(productName);
		if(product!=null)
			return product;
		else
			throw new InvalidInputException("Product with this name not found");
	}

	
	@Override
	public Customer customerSignIn(String email, String inputPassword) throws InvalidInputException {
		Customer customer=customerRepo.findByEmail(email);
		if(customer!=null){
			String password=customer.getPassword();
			System.out.println(password);
			if(password.equals(inputPassword)){
				return customer;
			}
			else{
				throw new InvalidInputException("Incorrect Password");
			}
		}
		else{
			throw new InvalidInputException("Account with this email doesnot exist");
		}
	}
	@Override
	public Customer getCustomerDetails(String phoneNumber) throws InvalidInputException {
		System.out.println("in");
		return customerRepo.getOne(phoneNumber);
	}
	@Override
	public List<Product> getAllProducts() throws InvalidInputException  {
		if(productRepo.findAll()!=null)
			return productRepo.findAll();
		else
			throw new InvalidInputException();

	}

	@Override
	public Product getProductById(int productId) throws InvalidInputException {
		if(productRepo.findByProductId(productId)!=null)
			return productRepo.findByProductId(productId);
		else
			throw new InvalidInputException();
	}

	@Override
	public List<Product> getProductByCategory(Category category) throws InvalidInputException {
		String categoryName=category.getCategoryName();
		category=categoryRepo.findByCategoryName(categoryName);
		return category.getProducts();
	}

	@Override
	public void setReviewMethod(String phoneNumber,int rating,String comments,int productId) throws InvalidInputException {
		Review review=new Review();
		review.getProduct().setProductId(productId);
		review.getCustomer().setPhoneNumber(phoneNumber);
		review.setComments(comments);
		review.setProductRating(rating);
		//saving reviews in customer
		Customer customer=customerRepo.getOne(phoneNumber);
		List<Review> customerReviewList =customer.getReviews();
		customerReviewList.add(review);
		customer.setReviews(customerReviewList);
		customerRepo.save(customer);
		//saving reviews in product
		Product product=productRepo.getOne(productId);
		List<Review> productReviewList=product.getReview();
		productReviewList.add(review);
		product.setReview(productReviewList);
		productRepo.save(product);
	}
	@Override
	public String getDeliveryStatus(int orderId) throws InvalidInputException {
		Orders order=ordersRepo.getOne(orderId);
		return order.getDeliveryStatus();
	}

	@Override
	public boolean addProductToWishlist(String phoneNumber,int productId) throws InvalidInputException {
		Product product=productRepo.getOne(productId);
		Customer customer=customerRepo.getOne(phoneNumber);
		Wishlist wishlist=wishlistRepo.findByCustomer(customer);
		if(wishlist==null)
		{
			wishlist=new Wishlist();
			product.setWishlist(wishlist);
			wishlist.setCustomer(customer);
			List<Product> productsList=new ArrayList<Product>();
			productsList.add(product);
			wishlist.setProducts(productsList);
			wishlistRepo.save(wishlist);
			return true;
		}
		else
		{
			product.setWishlist(wishlist);
			List<Product> productsList=wishlist.getProducts();
			productsList.add(product);
			wishlist.setProducts(productsList);
			wishlistRepo.save(wishlist);
			return true;
		}
	}
	@Override
	public boolean removeProductFromWishlist(String phoneNumber,int productId) throws InvalidInputException {
		Product product=productRepo.getOne(productId);
		Customer customer=customerRepo.getOne(phoneNumber);
		Wishlist wishlist=wishlistRepo.findByCustomer(customer);
		List<Product> productsList=wishlist.getProducts();
		productsList.remove(product);
		wishlist.setProducts(productsList);
		product.setWishlist(null);
		wishlistRepo.save(wishlist);
		return true;
	}
	@Override
	public List<Product> getWishlist(String phoneNumber) throws InvalidInputException {
		Wishlist wishlist= new Wishlist();
		try {
			Customer customer=customerRepo.getOne(phoneNumber);
			wishlist = wishlistRepo.findByCustomer(customer);
		} catch (Exception e) {
			e.getMessage();
		}
		return wishlist.getProducts();
	}
	@Override
	public boolean changePassword(String mobileNumber, String newPassword) throws InvalidInputException, CustomerNotFoundException{
		customer = customerRepo.getOne(mobileNumber);
		if(customer==null)
		{
			return false;
		}
		else
		{
			if(!customer.getPassword().equals(newPassword))
			{
				customer.setPassword(newPassword);
				customerRepo.save(customer);
				return true;
			}
			else
			{
				return false;
			}
		}

	}
	@Override
	public List<Orders> getAllOrders(String phoneNumber) throws CustomerNotFoundException {
		customer = customerRepo.getOne(phoneNumber);
		if(customer==null)
		{
			throw new CustomerNotFoundException("customer not found with mobile no.");
		}
		else
		{
			return 	customer.getOrders();
		}
	}



	@Override
	public Cart addProductToNewCart(String phoneNumber,int productId, int quantity) throws ProductUnavailableException {
		Product product= productRepo.getOne(productId);
		if(product.getProductQuantityAvailable()>quantity)
		{
		Cart cart = new Cart();
		List<Product> products = new ArrayList<Product>();
		product.setCartQuantity(quantity);
		products.add(product);
		double productPrice = product.getProductPrice();
		double amount = productPrice*quantity;
		cart.setTotalAmount(amount);
		cart.setProducts(products);
		cart.setCustomer(customerRepo.getOne(phoneNumber));
		return cartRepo.save(cart);
		}
		else 
			throw new ProductUnavailableException("This quantity of the product is unavailable");
	}

	@Override
	public Cart updateCart(String phoneNumber,int productId, int quantity) throws ProductUnavailableException {
		Customer customer=customerRepo.getOne(phoneNumber);
		//System.out.println(customer.getCustomerName());
		Cart cart = cartRepo.findByCustomer(customer);
		System.out.println(cart);
		List<Product> products =cart.getProducts();
		System.out.println(products);
		int productIndex= products.indexOf(new Product(productId));
		System.out.println(productIndex);
		Product product= products.get(productIndex);
		if(product.getProductQuantityAvailable()>quantity)
		{
			if(product.getCartQuantity()>quantity)
			{
				double productPrice = product.getProductPrice();
				double productAmount = productPrice*(product.getCartQuantity()-quantity);
				double amount =cart.getTotalAmount();
				double totalAmount = amount - productAmount;
				product.setCartQuantity(quantity);
				cart.setTotalAmount(totalAmount);
			}
			else if(product.getCartQuantity()<quantity)
			{
		double productPrice = product.getProductPrice();
		double productAmount = productPrice*(quantity-product.getCartQuantity());
		double amount =cart.getTotalAmount();
		double totalAmount = amount + productAmount;
		cart.setTotalAmount(totalAmount);
		}
		products.set(productIndex, product);
		cart.setProducts(products);
		return cartRepo.save(cart);}
		else throw new ProductUnavailableException("This quantity of the product is unavailable");
	}

	@Override
	public Cart removeProductFromCart(String phoneNumber,int productId) {
		Customer customer=customerRepo.getOne(phoneNumber);
		Cart cart = cartRepo.findByCustomer(customer);
		List<Product> products =cart.getProducts();
		int productIndex =products.indexOf(new Product(productId));
		//Product product= products.get(productIndex);
		products.remove(productIndex);
		cart.setProducts(products);
		return cartRepo.save(cart);
		/*if(product.getCartQuantity()==quantity)
		{
			
			cart.setProducts(null);
			cart.setTotalAmount(totalAmount);
			return cartRepo.save(cart);
		}
		else {
		cart.setTotalAmount(totalAmount);
		cart.setProducts(products);
		return cartRepo.save(cart);
		}*/
	}

	@Override
	public Merchant registerMerchant(Merchant merchant) {
		System.out.println("from service  "+merchant.toString());
		merchant.setAddMerchantDate(new Date());
		return merchantRepo.save(merchant);
	}

	@Override
	public Merchant updateMerchantProfile(Merchant merchant) {
		return merchantRepo.save(merchant);
	}

	@Override
	public Merchant deleteMerchantInventory(String username) {
		
		Merchant merchant = merchantRepo.findByUsername(username);
		merchantRepo.deleteById(merchant.getMerchantId());
		return merchant;
	}

	@Override
	public Merchant getMerchant(String username) throws MerchantNotFoundException {
		if(merchantRepo.findByUsername(username) == null) {
			throw new MerchantNotFoundException("No Merchant Found");
		}
		 Merchant merchant = merchantRepo.findByUsername(username);
		 merchant.setPassword(Base64Coder.decodeString(merchant.getPassword()));
		 return merchant;
	}

	@Override
	public Merchant getMerchantById(int merchantId) {
		Merchant merchant = merchantRepo.getOne(merchantId);
		merchant.setPassword(Base64Coder.decodeString(merchant.getPassword()));
		return merchant;
	}

	@Override
	public void changePassword(Merchant merchant, String password) {
		merchant.setPassword(Base64Coder.encodeString(password));
		merchantRepo.save(merchant);
		
	}

	@Override
	public Product addProduct(Product product) throws ProductNotFoundException {
		return productRepo.save(product);
	}

	@Override
	public List<Product> getAllMerchantProducts() {
		return productRepo.findAll();
	}

	@Override
	public void updateProduct(Product product) throws ProductNotFoundException {
		if(productRepo.findById(product.getProductId())==null)
			throw new ProductNotFoundException("product not found");
		productRepo.updateProduct(product.getProductId(), product.getProductName(), product.getBrand(), product.getProductQuantityAvailable(), product.getProductPrice(), product.getProductDesc(), product.isProductStatus());
	}

	@Override
	public Product getProductDetails(int productId) {
		return productRepo.getOne(productId);
	}

	@Override
	public void removeProduct(int productId) {
		productRepo.deleteById(productId);
		
	}

	@Override
	public Merchant findMerchantId(int merchantId) {
		Merchant merchant = merchantRepo.getOne(merchantId);
		return merchant;
	}
}