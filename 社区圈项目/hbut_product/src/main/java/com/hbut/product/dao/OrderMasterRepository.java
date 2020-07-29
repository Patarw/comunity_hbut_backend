package com.hbut.product.dao;


import com.hbut.product.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**订单用户信息dao**/
public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
    OrderMaster findOrderMasterByOrderId(String orderId);
    Page<OrderMaster> findOrderMasterByBuyerIdOrderByUpdateTimeDesc(Integer buyerId, Pageable pageable);
}
