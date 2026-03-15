//package com.tqwc.feastcommon.aspect;
//
//import java.util.Arrays;
//
//
//
///**
// * @author Tang
// * @data 2026/3/12 11:24
// */
//
//@Aspect
//@Component
//public class APiOperationLogAspect {
//
//    private static final Logger log = LoggerFactory.getLogger(ApiOperationLogAspect.class);
//
//    @Around("@annotation(com.xxx.common.aspect.ApiOperationLog)")
//    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        long startTime = System.currentTimeMillis();
//
//        // 获取方法信息
//        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
//        Method method = signature.getMethod();
//
//        // 获取注解
//        ApiOperationLog annotation = method.getAnnotation(ApiOperationLog.class);
//        String operation = annotation.value();
//
//        // 获取类名、方法名、参数
//        String className = joinPoint.getTarget().getClass().getSimpleName();
//        String methodName = method.getName();
//        Object[] args = joinPoint.getArgs();
//
//        log.info("========== 接口操作开始 ==========");
//        log.info("操作名称: {}", operation);
//        log.info("类名: {}", className);
//        log.info("方法名: {}", methodName);
//        log.info("请求参数: {}", Arrays.toString(args));
//
//        Object result;
//        try {
//            result = joinPoint.proceed();
//            long endTime = System.currentTimeMillis();
//
//            log.info("返回结果: {}", result);
//            log.info("执行耗时: {} ms", (endTime - startTime));
//            log.info("========== 接口操作结束 ==========");
//
//            return result;
//        } catch (Throwable e) {
//            long endTime = System.currentTimeMillis();
//
//            log.error("接口执行异常，耗时: {} ms", (endTime - startTime));
//            log.error("异常信息: {}", e.getMessage(), e);
//            log.info("========== 接口操作结束 ==========");
//
//            throw e;
//        }
//    }
//}
