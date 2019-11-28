package com.gfg.product.notification;

import com.gfg.product.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EmailSender implements ISender {

    public void sendPriceChangeWarning(Product product) {
        log.info("Email Warning sent to " + product.getSeller().getUuid() + " (Email: " + product.getSeller().getEmail()
                + "): " + product.getName() + " Product stock changed.");
    }

}
