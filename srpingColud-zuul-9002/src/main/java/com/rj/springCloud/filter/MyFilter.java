package com.rj.springCloud.filter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0.0
 * @descripton
 * @auth rj
 * @date 2018/11/11
 */
@Component
public class MyFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String loginName = request.getParameter("loginName");
        if (loginName == null || !"admin".equals(loginName)) {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(500);
            Gson gson = new GsonBuilder().create();
            requestContext.addZuulResponseHeader("content-type", "application/json;charset=utf-8");
            requestContext.setResponseBody(gson.toJson(new ResponseEntity("没有登录名", HttpStatus.CONFLICT)));
            return null;
        }
        return null;
    }
}
