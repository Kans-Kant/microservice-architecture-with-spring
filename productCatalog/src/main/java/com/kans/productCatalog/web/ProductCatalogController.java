package com.kans.productCatalog.web;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kans.productCatalog.model.Product;

@RestController
@RequestMapping("/product")
public class ProductCatalogController {
	
	@Autowired
    private MongoTemplate mongoTemplate;
	
	private Logger logger = LoggerFactory.getLogger(ProductCatalogController.class);
	
	@GetMapping("/version")
    public String getVersionInfo() {
        return "Version - V1";
    }
	
	@PostMapping
	public Product addProduct(@RequestBody Product product) { 
		return mongoTemplate.insert(product);
	} 
	 
	@GetMapping("/{id}") 
	public Product getProductDetails(@PathVariable String id) {
		
		Product product = mongoTemplate.findById(id, Product.class);
		if (product != null)
            logger.info("get product details - product found");
        else
            logger.info("get product details - product not found");
		
		return product;
	} 
	 
	@GetMapping
	public List < Product > getProductList() {
		return mongoTemplate.findAll(Product.class); 
	 } 
	 
	@PutMapping
	public Product updateProduct(@RequestBody Product product) {
		return mongoTemplate.save(product);
	} 
	
	@DeleteMapping("/{id}")
	public String deleteProduct(@PathVariable String id) {
		Product toDeleteProduct = new Product();
        toDeleteProduct.setId(id);
        mongoTemplate.remove(toDeleteProduct);
        return "Product Deleted-" + id;
	}
}
