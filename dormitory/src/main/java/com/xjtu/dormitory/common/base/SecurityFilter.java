package com.xjtu.dormitory.common.base;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.xjtu.dormitory.common.*;

import javax.annotation.Resource;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Resource
    private CommonService commonService;



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String path = request.getServletPath();
        if("/security/login".equals(path)){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }

        String clientToken = request.getHeader(Constants.HEADER_TOKEN);

        try {
            TokenUtils.verifyToken(clientToken,commonService);
            filterChain.doFilter(servletRequest,servletResponse);
        } catch (Exception e) {
            e.printStackTrace();

            Utils.outJson(response, Result.fail(Result.ERR_CODE_UNLOGIN,e.getMessage()));

        }
    }

    @Override
    public void destroy() {

    }
}
