package com.gfg.product.controller;

import com.gfg.product.entity.Seller;
import com.gfg.product.exception.ResourceNotFoundException;
import com.gfg.product.service.SellerService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(value = "/api")
public class SellerController {

    private SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @ApiOperation(value = "View seller information by UUID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Processed successfully", response = Seller.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/v1/sellers/{uuid}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Seller> getSellerById(@PathVariable @NotNull String uuid) {
        Seller seller = sellerService.getByUuid(uuid);
        if (seller == null) {
            throw new ResourceNotFoundException("Seller", "UUID", uuid);
        }

        return new ResponseEntity<>(seller, HttpStatus.OK);
    }
    
    @ApiOperation(value = "Fetch top 10 seller information based on product count")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Processed successfully", response = Seller.class),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GetMapping(value = "/v2/top10", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object[]> getTop10Seller() {
        List<Seller> topTenSellers = sellerService.getTopTenSellers();
        return new ResponseEntity<>(topTenSellers.toArray(), HttpStatus.OK);
    }
}
