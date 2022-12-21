package com.example.school.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.school.common.Result;
import com.example.school.utils.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求头中，key为“token”的内容，这应该存着我们的token
        String token = request.getHeader("token");
        Result<String> wrong = new Result<>();
        wrong.setCode("0");
        if(token==null){
            wrong.setCode("-435");
            wrong.setMsg("找不到token");
        }
        else{
            try{
                JWTUtils.verify(token);
                return true;
            } catch(SignatureVerificationException e){
//                e.printStackTrace();
                wrong.setCode("-436");
                wrong.setMsg("无效签名");
            } catch (TokenExpiredException e){
//                e.printStackTrace();
                wrong.setCode("-437");
                wrong.setMsg("登录过期");
            } catch (AlgorithmMismatchException e){
//                e.printStackTrace();
                wrong.setCode("-438");
                wrong.setMsg("后台算法不一致");
            } catch (Exception e){
//                e.printStackTrace();
                wrong.setCode("-439");
                wrong.setMsg("未知的错误原因");
            }
        }

        String json = new ObjectMapper().writeValueAsString(wrong);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
        return false;
    }
}
