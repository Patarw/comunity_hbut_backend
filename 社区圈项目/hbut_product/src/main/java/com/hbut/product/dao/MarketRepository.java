package com.hbut.product.dao;


import com.hbut.product.entity.Market;
import org.springframework.data.jpa.repository.JpaRepository;

/**商家dao**/
public interface MarketRepository extends JpaRepository<Market,Integer> {
    Market findMarketById(Integer id);
}
