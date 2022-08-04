package com.kans.shoppingCart.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import com.kans.shoppingCart.dao.ShoppingCartDao;
import com.kans.shoppingCart.model.Cart;
import com.kans.shoppingCart.model.CartItem;
import com.kans.shoppingCart.model.Product;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

	 @Autowired
	 RestTemplate restTemplate;
	 
	 @Autowired
	 DiscoveryClient discoveryClient;

	 @Autowired
	 ShoppingCartDao shoppingCartDao;
	 
	 private Logger logger = LoggerFactory.getLogger(ShoppingCartController.class);

	@PostMapping("/zuul/cart/{cartId}/item")
	public Cart addItem(@PathVariable String cartId, @RequestBody CartItem item) {
	    if (cartId != null && item != null && item.getProductId() != null) {
	        
	        String productCatalogUrl = "http://localhost:8762/product/"
	          + item.getProductId();
	        
	        // get product details
	        Product itemProduct = restTemplate.getForObject(productCatalogUrl, 
	                                                        Product.class);
	        if (itemProduct != null && itemProduct.getId() != null) {
	            // adding total item price in the shopping cart item
	            item.setTotalItemPrice(itemProduct.getUnitPrice() * item.getQuantity());
	            return shoppingCartDao.addItem(cartId, item);
	        }
	        throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
	                                          "item product not found");
	    }
	    throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
	                                      "cart or item missing");
	}
	
	//test distribution tracing
	@GetMapping("/zuul/cart/item/product/{productId}")
	public Product getProductDetails(@PathVariable String productId) {
		
		logger.info("get item product details");
		
		if (productId != null) {
			String productCatalogUrl = "http://localhost:8081/product/"
			          + productId;
			 Product itemProduct = restTemplate.getForObject(productCatalogUrl, 
                     Product.class);
			 
			 return itemProduct;
		}
		else {
			
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, 
                "item product not found");
		}
	}
}
