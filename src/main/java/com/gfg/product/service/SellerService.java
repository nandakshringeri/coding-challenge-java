package com.gfg.product.service;

import com.gfg.product.entity.Seller;
import com.gfg.product.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    private SellerRepository repository;

    public SellerService(SellerRepository repository) {
        this.repository = repository;
    }

    public Seller getByUuid(String sellerUuid) {
        List<Seller> sellers = this.repository.findByUuid(sellerUuid);

        return sellers.size() == 0 ? null : sellers.get(0);
    }
    
	public List<Seller> getTopTenSellers(){
		  return this.repository.findTopTenSellers();
    }
}
