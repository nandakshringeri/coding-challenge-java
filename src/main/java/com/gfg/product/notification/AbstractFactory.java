package com.gfg.product.notification;

public interface AbstractFactory<T> {
	    T create(String senderType) ;
}
