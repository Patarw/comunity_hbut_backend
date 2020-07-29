package com.hbut.product.service;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.hbut.product.VO.OrderDetailVO;
import com.hbut.product.VO.OrderMasterVO;
import com.hbut.product.dao.OrderDetailRepository;
import com.hbut.product.dao.OrderMasterRepository;
import com.hbut.product.dao.ProductInfoRepository;
import com.hbut.product.entity.OrderDetail;
import com.hbut.product.entity.OrderMaster;
import com.hbut.product.entity.ProductInfo;
import com.hbut.product.form.OrderForm;
import com.hbut.product.form.ShopCart;
import enums.OrderStatusEnums;
import enums.PayStatusEnums;
import enums.ResultEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utils.IdWorker;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private HttpServletRequest request;

    /**创建订单，但未支付*/
    public Result<Map<String,String>> createOrder(OrderForm orderForm){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        String orderId = idWorker.nextId() + "";
        //创建orderMaster
        OrderMaster orderMaster = new OrderMaster();
        BeanUtils.copyProperties(orderForm,orderMaster);
        orderMaster.setOrderId(orderId);
        orderMaster.setOrderStatus(OrderStatusEnums.NEW.getCode()); //新下单
        orderMaster.setPayStatus(PayStatusEnums.WAIT.getCode()); //未支付
        orderMaster.setCreateTime(new Date());
        orderMaster.setUpdateTime(new Date());

        //查询出商品，并且生成orderDetail
        List<ShopCart> shopCartList  = orderForm.getShopCartList();
        if (shopCartList.isEmpty()){
            return Result.newResult(ResultEnum.CART_EMPTY);
        }
        for(ShopCart shopCart : shopCartList){
            ProductInfo productInfo = productInfoRepository.findProductInfoByProductId(shopCart.getProductId());
            //判断商品库存是否够,并且减库存
            productService.decreaseStock(shopCart);

            //够的话就将orderDetail存入数据库
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetail.setDetailId(idWorker.nextId() + "");
            orderDetail.setOrderId(orderId);
            orderDetail.setProductQuantity(shopCart.getQuantity());
            orderDetail.setCreateTime(new Date());
            orderDetail.setUpdateTime(new Date());
            orderDetailRepository.save(orderDetail);
        }

        orderMasterRepository.save(orderMaster);
        Map<String,String> map = new HashMap<>();
        map.put("orderId",orderId);
        return Result.newSuccess(map);
    }

    /**查询单个订单*/
    public Result<OrderMasterVO> findOrder(String orderId){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        OrderMaster orderMaster = orderMasterRepository.findOrderMasterByOrderId(orderId);
        if (orderMaster == null){
            return Result.newResult(ResultEnum.ORDER_NOT_EXIST);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailByOrderId(orderId);
        List<OrderDetailVO> orderDetailVOList = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetailList){
            OrderDetailVO orderDetailVO = new OrderDetailVO();
            BeanUtils.copyProperties(orderDetail,orderDetailVO);
            orderDetailVOList.add(orderDetailVO);
        }
        OrderMasterVO orderMasterVO = new OrderMasterVO();
        BeanUtils.copyProperties(orderMaster,orderMasterVO);
        orderMasterVO.setOrderDetailVOList(orderDetailVOList);

        return Result.newSuccess(orderMasterVO);
    }

    /**查询用户订单列表*/
    public Result<PageVO<OrderMasterVO>> findOrderList(Integer buyerId, PageArg arg){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        arg.validate();
        if (buyerId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        Pageable pageable = PageRequest.of(arg.getPageNo() - 1,arg.getPageSize());
        Page<OrderMaster> orderMasterPage = orderMasterRepository.findOrderMasterByBuyerIdOrderByUpdateTimeDesc(buyerId,pageable);

        //获取分页参数,拼接数据
        List<OrderMaster> orderMasterList = orderMasterPage.getContent();
        List<OrderMasterVO> orderMasterVOList = new ArrayList<>();
        for (OrderMaster orderMaster : orderMasterList){
            List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailByOrderId(orderMaster.getOrderId());
            List<OrderDetailVO> orderDetailVOList = new ArrayList<>();

            for (OrderDetail orderDetail : orderDetailList){
                OrderDetailVO orderDetailVO = new OrderDetailVO();
                BeanUtils.copyProperties(orderDetail,orderDetailVO);
                orderDetailVOList.add(orderDetailVO);
            }

            OrderMasterVO orderMasterVO = new OrderMasterVO();
            BeanUtils.copyProperties(orderMaster,orderMasterVO);
            orderMasterVO.setOrderDetailVOList(orderDetailVOList);
            orderMasterVOList.add(orderMasterVO);
        }

        int total = orderMasterPage.getTotalPages();
        //构建pageVo对象
        PageVO<OrderMasterVO> pageVo = PageVO.<OrderMasterVO>builder()
                .totalPage(total)
                .pageNo(arg.getPageNo())
                .pageSize(arg.getPageSize())
                .rows(orderMasterVOList)
                .build();
        return Result.newSuccess(pageVo);
    }

    /**取消订单*/
    public Result<Void> cancelOrder(String orderId){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        if (orderId == null){
            return Result.newResult(ResultEnum.PARAM_ERROR);
        }
        OrderMaster orderMaster = orderMasterRepository.findOrderMasterByOrderId(orderId);
        if (orderMaster == null){
            return Result.newResult(ResultEnum.ORDER_NOT_EXIST);
        }

        //取消订单
        orderMaster.setOrderStatus(OrderStatusEnums.CANCEL.getCode());
        orderMaster.setUpdateTime(new Date());

        //返回库存
        List<OrderDetail> orderDetailList = orderDetailRepository.findOrderDetailByOrderId(orderId);
        for (OrderDetail orderDetail : orderDetailList){
            ShopCart shopCart = new ShopCart();
            shopCart.setProductId(orderDetail.getProductId());
            shopCart.setQuantity(orderDetail.getProductQuantity());
            productService.addStock(shopCart);
        }

        orderMasterRepository.save(orderMaster);
        return Result.newSuccess();
    }

    /**支付订单*/
    public Result<Void> payOrder(String orderId){
        //验证token
        String token = (String) request.getAttribute("claims_user");
        if (token == null || "".equals(token)){
            return Result.newResult(ResultEnum.AUTHENTICATION_ERROR);
        }

        OrderMaster orderMaster = orderMasterRepository.findOrderMasterByOrderId(orderId);
        if (orderMaster == null){
            return Result.newResult(ResultEnum.ORDER_NOT_EXIST);
        }
        if (orderMaster.getPayStatus() == PayStatusEnums.SUCCESS.getCode() || orderMaster.getOrderStatus() == OrderStatusEnums.CANCEL.getCode()){
            return Result.newResult(ResultEnum.ORDER_STATUS_ERROR);
        }
        orderMaster.setOrderStatus(OrderStatusEnums.FINISHED.getCode());
        orderMaster.setPayStatus(PayStatusEnums.SUCCESS.getCode());
        orderMaster.setUpdateTime(new Date());
        orderMasterRepository.save(orderMaster);
        return Result.newSuccess();
    }

}
