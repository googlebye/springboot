package com.lang.service.impl;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * util类：可以方便的拿到当前的applicationContext，或者从applicationContext拿到任意的bean
 * @author yangnan
 *
 */
@Component
public class SpringContextHolder implements ApplicationContextAware,
		DisposableBean {

	private static ApplicationContext applicationContext = null;

	@Override
	public void destroy() throws Exception {
		// TODO Auto-generated method stub
		SpringContextHolder.clear();
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		SpringContextHolder.applicationContext = applicationContext; //NOSONAR

	}
	/**
	 * 返回当前的applicationContext
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		assertContextInjected();
		return applicationContext;
	}
	/**
	 * 从当前的applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(String name) {
		assertContextInjected();
		return (T) applicationContext.getBean(name);
	}
	/**
	 * 从当前的applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		assertContextInjected();
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringContextHolder中的ApplicationContext为Null.
	 */
	public static void clear() {
		applicationContext = null;
	}
	/**
	 * 检查ApplicationContext不为空.
	 */
	private static void assertContextInjected() {
		if(applicationContext == null)
			throw new IllegalStateException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder"); 
	}

}
