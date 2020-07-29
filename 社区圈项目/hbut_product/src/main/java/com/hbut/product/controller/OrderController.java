package com.hbut.product.controller;

import VO.PageVO;
import VO.Result;
import VO.Void;
import args.PageArg;
import com.hbut.product.VO.OrderMasterVO;
import com.hbut.product.form.OrderForm;
import com.hbut.product.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Api(tags = "订单管理接口")
@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**创建订单，但未支付*/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("创建订单接口，但未支付（需要token）")
    @RequestMapping(value = "/createOrder",method = RequestMethod.POST)
    public Result<Map<String,String>> createOrder(@Valid @RequestBody OrderForm orderForm){
       return orderService.createOrder(orderForm);
    }

    /**查询单个订单*/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("查询单个订单,需要订单id（需要token）")
    @RequestMapping(value = "/findOrder",method = RequestMethod.POST)
    public Result<OrderMasterVO> findOrder(String orderId){
        return orderService.findOrder(orderId);
    }

    /**查询用户订单列表*/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("查询用户订单列表,需要传用户的id（需要token）")
    @RequestMapping(value = "/findOrderList",method = RequestMethod.POST)
    public Result<PageVO<OrderMasterVO>> findOrderList(Integer buyerId,@RequestBody PageArg arg){
        return orderService.findOrderList(buyerId,arg);
    }

    /**取消订单*/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("取消订单,需要订单id（需要token）")
    @RequestMapping(value = "/cancelOrder",method = RequestMethod.POST)
    public Result<Void> cancelOrder(String orderId){
        return orderService.cancelOrder(orderId);
    }

    /**支付订单*/
    @ApiImplicitParam(name = "Authorization", value = "需要token认证", required = false, dataType = "String",paramType = "header")
    @ApiOperation("支付订单,需要订单id（需要token）")
    @RequestMapping(value = "/payOrder",method = RequestMethod.POST)
    public Result<Void> payOrder(String orderId){
        return orderService.payOrder(orderId);
    }
}
