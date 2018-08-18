package com.capgemini.storeserver.services;

import java.util.List;

import com.capgemini.storeserver.beans.Address;
import com.capgemini.storeserver.beans.Cart;
import com.capgemini.storeserver.beans.Category;
import com.capgemini.storeserver.beans.Customer;
import com.capgemini.storeserver.beans.Merchant;
import com.capgemini.storeserver.beans.Orders;
import com.capgemini.storeserver.beans.Product;
import com.capgemini.storeserver.exceptions.CustomerNotFoundException;
import com.capgemini.storeserver.exceptions.InvalidInputException;
import com.capgemini.storeserver.exceptions.MerchantNotFoundException;
import com.capgemini.storeserver.exceptions.ProductNotFoundException;
import com.capgemini.storeserver.exceptions.ProductUnavailableException;


public interface AdminServices {
	
	public Merchant addMerchant(Merchant merchant);
	
	public void removeMerchant(int id);
	
public List<Product> viewAllProducts();
	
	Category updateCategory(int categoryId, String categoryName,String type);
	
	
	
	
	public Orders getTransaction(int orderId) throws InvalidInputException;
	
	public Product searchByProductName(String productName)  throws InvalidInputException;
	//naya wala methods
	public Customer customerSignIn(String email,String password) throws InvalidInputException;
	
	public Customer getCustomerDetails(String phoneNumber) throws InvalidInputException;
	
	public List<Product> getAllProducts() throws InvalidInputException  ;
	
	public Product getProductById(int productId) throws InvalidInputException;
	
	public List<Product> getProductByCategory(Category category) throws InvalidInputException;
	
	public void setReviewMethod(String phoneNumber,int rating,String comments,int productId) throws InvalidInputException;
	
	public String getDeliveryStatus(int orderId) throws InvalidInputException;
	
	public boolean addProductToWishlist(String phoneNumber,int productId) throws InvalidInputException;
	
	public boolean removeProductFromWishlist(String phoneNumber,int productId) throws InvalidInputException;
	
	public List<Product> getWishlist(String phoneNumber) throws InvalidInputException;
	
	public boolean updateSecurityQuestion(String phoneNumber,String securityQuestion) throws InvalidInputException;
	
	public boolean updateSecurityAnswer(String phoneNumber,String securityAnswer) throws InvalidInputException;
	
	public boolean updateCardNumber(String phoneNumber,String cardNumber)throws InvalidInputException;
	
	public boolean updateCustomerName(String phoneNumber,String customerName)throws InvalidInputException;
	
	public List<Product> getAllProductsFromCart(String phoneNumber) throws InvalidInputException;
	
	Customer signUp(Customer customer);
	
	String forgotPassword(String mobileNumber) throws CustomerNotFoundException;
	
	String securityQuestion(String phoneNumber,String securityAnswer) throws InvalidInputException;
	
	void onlinePayment(String cardNumber, String customerPhoneNumber);
	
	void buyNowProduct(int productId, String phoneNumber, int quantity);
	
	void buyNowCart(String phoneNumber) throws ProductUnavailableException;
	
	Cart addProductToNewCart(String phoneNumber,int productId, int quantity) throws ProductUnavailableException;
	
	Cart updateCart(String phoneNumber,int productId, int quantity) throws ProductUnavailableException;
	
	Cart removeProductFromCart(String phoneNumber,int productId);//once removed from cart, update stock & quantity in product
	
	void addAddress(Address address);
	
	Address getAddress(int addressId);
	
	double applyDiscount(int productId);     
	
	double applyCoupon(int cartId);
	
	public boolean changePassword(String phoneNumber,String newPassword)throws InvalidInputException, CustomerNotFoundException;
	
	public List<Orders> getAllOrders(String phoneNumber)throws CustomerNotFoundException;
	
	
	
	
	public Merchant registerMerchant(Merchant merchant);
	
	public Merchant updateMerchantProfile(Merchant merchant);
	
	public Merchant deleteMerchantInventory(String username);
	
	public Merchant getMerchant(String username) throws MerchantNotFoundException;
	
	public Merchant getMerchantById(int merchantId);
	
	public void changePassword(Merchant merchant,String password);
	
	public Product addProduct(Product product) throws ProductNotFoundException;
	
	public List<Product> getAllMerchantProducts();
	
	public void updateProduct(Product product)throws ProductNotFoundException;
	
	public Product getProductDetails(int productId);
	
	public void removeProduct(int productId);
	
	public Merchant findMerchantId(int merchantId);	

}
