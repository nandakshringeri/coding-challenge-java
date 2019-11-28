package com.gfg.product.notification;

import org.springframework.stereotype.Component;

import com.gfg.product.entity.Product;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SMSSender implements ISender {

	@Override
	public void sendPriceChangeWarning(Product product) {
		  log.info("SMS Warning sent to " + product.getSeller().getUuid() + " (Phone: " + product.getSeller().getPhone()
	                + "): " + product.getName() + " Product stock changed.");
	}

}
