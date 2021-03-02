package com.jqy.aop;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.jqy.admin.model.po.Logs;
import com.jqy.admin.model.po.User;
import com.jqy.admin.service.LogsService;
import com.jqy.result.CommonsReturn;
import com.jqy.result.ReturnCode;
import com.jqy.utils.JWT;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Base64;
import java.util.Date;

@Aspect
@Component
@Order(3)
public class LogAop {

    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private LogsService LogsService;

    @Pointcut("execution(* com.jqy.*.controller..*.*(..))")
    public void logPointCut() {
    }


    @Around("logPointCut() && @annotation(logsAnnotation) ")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint, LogsAnnotation logsAnnotation) throws Throwable {
        /*--------------------------前置通知-------------------------*/
        Object obj = null;
        Logs logs = new Logs();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String args = JSON.toJSONString(joinPoint.getArgs());
        //获取访问的类路径
        logs.setClassname(className);
        //获取访问的方法名
        logs.setMethodName(methodName);
        //获取访问的参数
        logs.setArgs(args);
        //获取操作时间
        logs.setCreateTime(new Date());
        //获取描述信息
        logs.setDescribeInfo(logsAnnotation.value());
        //获取当前毫秒值
        long startTime = System.currentTimeMillis();
        try {
            logs.setType(1);
            //执行目标方法
            obj = joinPoint.proceed();
        } catch (Throwable throwable) {
            /*----------------------异常通知-----------------------*/
            logs.setType(2);
            throwable.printStackTrace();
            //获取异常信息
            logs.setErrorinfo(exception(throwable));
            throw throwable;
        } finally {
            /*----------------------后置通知------------------------*/
            //获取当前用户的信息
            String token = httpServletRequest.getParameter("token");
            //解密Base64
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decode = decoder.decode(token);
            //name+jqy+token
            String tokenInfo = new String(decode);
            //截取jqy  得到 name 和 token
            String[] jqies = tokenInfo.split("jqy");
            //认证token信息是否正确
            User user = JWT.unsign(jqies[1], User.class);
            //获取操作人ID
            logs.setUserId(user.getId());
            //获取操作人姓名
            logs.setRealName(user.getRealName());
            //获取运行时间
            //logs.setRuntime(System.currentTimeMillis() - startTime);
            //增加日志记录
            LogsService.add(logs);
        }
        return obj;
    }

    /**
     * 将异常信息转化成字符串
     *
     * @param
     * @return
     * @throws IOException
     */
    private static String exception(Throwable t) throws IOException {
        if (t == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            t.printStackTrace(new PrintStream(baos));
        } finally {
            baos.close();
        }
        return baos.toString();
    }
}
