package com.gfg.product.notification;

import com.gfg.product.entity.Product;

/**
 * 
 * Clients implementing this interface should provide implementation for the sendPriceChangeWarning and type of notification
 *
 */
public interface ISender {
	
	/**
	 * Clients implementing this should provide concreting implementation of type of Warning when Price is changed to the seller
	 */
	public void sendPriceChangeWarning(Product product);
	

}
