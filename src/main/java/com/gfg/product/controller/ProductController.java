package com.gfg.product.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gfg.product.dto.ProductDTO;
import com.gfg.product.entity.Product;
import com.gfg.product.exception.ResourceNotFoundException;
import com.gfg.product.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api")
@Api(value = "Products")
public class ProductController {

    private ProductService productService;

    private static final ModelMapper mapper = new ModelMapper();

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "View product information by UUID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Processed successfully", response = ProductDTO.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/v1/products/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> getProductBUuid(@PathVariable String uuid) {
        Product product = productService.getByUuid(uuid);
        if (product == null) {
            throw new ResourceNotFoundException("Product", "UUID", uuid);
        }

        return new ResponseEntity<>(mapper.map(product, ProductDTO.class), HttpStatus.OK);
    }

    @ApiOperation(value = "View all products")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Processed successfully", response = ProductDTO.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/v1/products/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAll();

        List<ProductDTO> productDTOList = products.stream()
                .map(product -> mapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(productDTOList, HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/v2/products/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resources<ProductResource>> getAllProductWithLinks() {
        List<Product> products = productService.getAll();
        final List<ProductResource> productList =
        		products.stream().map(ProductResource::new).collect(Collectors.toList());
        final Resources<ProductResource> resources = new Resources<>(productList);
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));
        return ResponseEntity.ok(resources);
 
    }

    @ApiOperation(value = "Delete ane product by UUID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Processed successfully", response = Object.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @DeleteMapping(value = "/v1/products/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity delete(@PathVariable String uuid) {
        Product product = productService.getByUuid(uuid);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        productService.delete(product);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Update one product by UUID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Processed successfully", response = ProductDTO.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found (Seller or Product)")
    })
    @PutMapping(value = "/v1/products/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ProductDTO> update(@PathVariable String uuid, @Valid @RequestBody ProductDTO productDTO) {
        Product product = mapper.map(productDTO, Product.class);

        return new ResponseEntity<>(
                mapper.map(
                        productService.updateProductByUuid(uuid, product),
                        ProductDTO.class),
                HttpStatus.OK
        );
    }

    @ApiOperation(value = "Create a new product")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Processed successfully", response = ProductDTO.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found (Seller)")
    })
    @PostMapping(value = "/v1/products/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Valid @RequestBody ProductDTO productDTO) {
        Product product = mapper.map(productDTO, Product.class);
        productService.save(product);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
