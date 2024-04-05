//package com.yh.ssc.global;
//
//import com.yh.ssc.annotation.SafeEventListener;
//import com.yh.ssc.event.NewCycleEvent;
//import com.yh.ssc.event.NewPlanEvent;
//import com.yh.ssc.event.PlanDetailFailedEvent;
//import com.yh.ssc.event.PlanSuccessEvent;
//import com.yh.ssc.event.NewDetailEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.ApplicationEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
///**
// * @program: ssc
// * @description:
// * @author: yehang
// * @create: 2024-04-02 13:29
// **/
//@Component
//@Slf4j
//public class GlobalEventListener {
//
//    @Autowired
//    private ApplicationContext context;
//
//    @EventListener
//    public void onApplicationEvent(ApplicationEvent event) {
//        // 通过ApplicationContext获取所有bean
//        String[] beans = context.getBeanNamesForType(Object.class);
//
//        for (String beanName : beans) {
//            Object bean = context.getBean(beanName);
//            // 对于每个bean，获取它的方法
//            for (Method method : bean.getClass().getDeclaredMethods()) {
//                // 如果方法被SafeEventListener注解标记，并且是事件监听器
//                if (method.isAnnotationPresent(SafeEventListener.class)) {
//                    // 检查方法参数是否与事件类型匹配
//                    boolean interest = event instanceof NewCycleEvent
//                            || event instanceof NewPlanEvent
//                            || event instanceof PlanDetailFailedEvent
//                            || event instanceof PlanSuccessEvent
//                            || event instanceof NewDetailEvent;
//
//                    if (interest && method.getParameterCount() == 1 && method.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
//                        try {
//                            // 反射调用注解方法
//                            method.setAccessible(true);
//                            method.invoke(bean, event);
//                        }catch (Exception e) {
//                            // 如果异常发生，进行统一处理
//                            handleEventException(e, event);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void handleEventException(Exception e, ApplicationEvent event) {
//        // 这里加入你自己的异常处理逻辑
//        // 比如，记录日志等
//        log.error("Exception occurred while processing event:{},e:{} " + event.getClass().getName(),e);
//    }
//}
