package com.gfg.product.notification;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * This NotificationFactory implements Abstract factory pattern to fetch the
 * instances of specific type
 *
 */

public class NotificationFactory implements AbstractFactory<ISender> {

	public String notificationType;

	public NotificationFactory(String type) {
		this.notificationType = type;
	}

	public List<ISender> getSenders() {
		List<ISender> senderList = new ArrayList<>();
		String[] senders = this.notificationType.split(",");
		for (String sender : senders) {
			ISender senderInstance = create(sender);
			if (senderInstance != null) {
				senderList.add(senderInstance);
			}
		}
		return senderList;
	}

	@Override
	public ISender create(String senderType) {
		switch (senderType) {
		case "EMAIL":
			return new EmailSender();
		case "SMS":
			return new SMSSender();
		}

		return null;

	}
}
