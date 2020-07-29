package com.hbut.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**过滤器**/
@Component
public class GatewayFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre"; //表示在请求之前过滤,post表示后执行
    }

    @Override
    public int filterOrder() {
        return 0; //执行次序
    }

    @Override
    public boolean shouldFilter() {
        return true; //是否开启
    }

    @Override
    public Object run() throws ZuulException {
        //过滤器内执行的操作
      /*  RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String header = request.getHeader("Authorization");
        if (header != null && !"".equals(header)){
            requestContext.addZuulRequestHeader("Authorization",header);
        }*/
        return null;
    }
}
