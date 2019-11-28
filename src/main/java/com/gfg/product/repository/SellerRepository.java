package com.gfg.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gfg.product.entity.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {

  List<Seller> findByUuid(@Param("uuid") String uuid);

  @Query(value ="select * from seller inner join (select fk_seller from product group by fk_seller order by count(*) desc limit 0,10) p1 on seller.id_seller=p1.fk_seller"
		  ,nativeQuery = true)
  List<Seller> findTopTenSellers();
}
