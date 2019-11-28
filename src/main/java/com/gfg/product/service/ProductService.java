package com.gfg.product.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gfg.product.entity.Product;
import com.gfg.product.entity.Seller;
import com.gfg.product.exception.ResourceNotFoundException;
import com.gfg.product.notification.ISender;
import com.gfg.product.notification.NotificationFactory;
import com.gfg.product.repository.ProductRepository;

@Service
public class ProductService {

	private ProductRepository repository;

	private SellerService sellerService;

	@Value("${seller.notification.type}")
	private String notificationType;
	
	private List<ISender> senders = new ArrayList<ISender>() ;
	
	@PostConstruct
	private void init() {
		NotificationFactory factory = new NotificationFactory(notificationType);
		senders = factory.getSenders();
	}

	public ProductService(ProductRepository repository, SellerService sellerService) {
		this.repository = repository;
		this.sellerService = sellerService;

	}

	public Product getByUuid(String productUuid) {
		List<Product> products = this.repository.findByUuid(productUuid);

		return products.size() == 0 ? null : products.get(0);
	}

	public List<Product> getAll() {
		return this.repository.findAll();
	}

	public void delete(Product product) {
		this.repository.delete(product);
	}

	public Product updateProductByUuid(String uuid, Product product) {
		Product originalProduct = getByUuid(uuid);
		if (originalProduct == null) {
			throw new ResourceNotFoundException("Product", "UUID", uuid);
		}

		product.setId(originalProduct.getId());
		mapSellerToProduct(product, product.getSeller().getUuid());

		// if stock changed, send notification email
		if (Math.abs(product.getStock() - originalProduct.getStock()) > 0) {
			for (ISender sender : senders) {
				sender.sendPriceChangeWarning(product);
			}
		}

		return repository.save(product);
	}

	public Product save(Product product) {
		mapSellerToProduct(product, product.getSeller().getUuid());
		return repository.save(product);
	}

	private void mapSellerToProduct(Product product, String sellerUuid) {
		Seller seller = sellerService.getByUuid(sellerUuid);
		if (seller == null) {
			throw new ResourceNotFoundException("Seller", "UUID", sellerUuid);
		}
		product.setSeller(seller);
	}
}
