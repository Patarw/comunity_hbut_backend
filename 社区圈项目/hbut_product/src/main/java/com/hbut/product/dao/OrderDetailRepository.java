package com.hbut.product.dao;


import com.hbut.product.entity.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**订单详情dao**/
public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {
    OrderDetail findOrderDetailByDetailId(String detailId);
    List<OrderDetail> findOrderDetailByOrderId(String orderId);
}
