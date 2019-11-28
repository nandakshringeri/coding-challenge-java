package com.gfg.product.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import com.gfg.product.entity.Product;

import lombok.Getter;


@Getter
public class ProductResource extends ResourceSupport {
	 private final Product product;
	 public ProductResource(final Product product) {
		    this.product = product;
		    final String id = product.getSeller().getUuid();
		    add(linkTo(methodOn(SellerController.class).getSellerById(id)).withSelfRel());
		  }
	 
}
