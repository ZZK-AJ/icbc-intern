package com.icbcintern.prepaycard.utils;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class Interceptor implements HandlerInterceptor {
    /**
     * 在请求处理之前进行调用(Controller方法调用之前)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("执行 UserLoginInterceptor 拦截器的 preHandle 方法");
        try {
            response.setCharacterEncoding("UTF-8");
            //获取 token
            final String token = request.getHeader("authorization");

            if (token == null) {
                response.getWriter().write("未登录，请登录后获取 token！");
                return false;
            }

//            Map<String, Claim> userData = JwtTools.verifyToken(token);
            DecodedJWT jwt = JwtTools.verifyToken(token);
            if (jwt == null || StringUtils.isEmpty(jwt.getClaim("userName").asString())) {
                response.getWriter().write("token 不合法！");
                return false;
            }
            // todo 重定向到登录页
            // System.out.println(request.getContextPath());
            // response.sendRedirect(request.getContextPath() + "UserLogin");  // 重定向
            return true;


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
        //如果设置为false时，被请求时，拦截器执行到此处将不会继续操作
        //如果设置为true时，请求将会继续执行后面的操作
    }

    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("执行了拦截器的 postHandle 方法");
    }

    /**
     * 整个请求结束之后被调用，也就是在DispatchServlet渲染了对应的视图之后执行（主要用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("执行了拦截器的 afterCompletion 方法");
    }
}
