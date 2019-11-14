/**  
 * @项目名称: CAServer
 * @标题: TemplateException.java
 * @包名：cn.com.infosec.template
 * @描述: TODO
 * @作者：任龙
 * @创建时间： 2012-4-19 下午05:49:20
 * @版权: 2012 www.1000chi.com Inc. All rights reserved.
 * @版本： V1.0  
 */

package cn.com.infosec.netcert.caAdmin.ui.template.templateUtils;

/**
 * @author Renlong
 * @date:2012-4-19 下午05:49:20
 * @version :
 * 
 */
public class TemplateException extends Exception {

	/**
	 * @方法名称: TemplateException
	 * @描述: 构造函数
	 * @参数：@param ex
	 * @参数：@param msg
	 */
	public TemplateException(Throwable t, String msg) {
		super(msg, t);
	}

	/**
	 * @方法名称: TemplateException
	 * @描述: 构造函数
	 * @参数：@param msg
	 */
	public TemplateException(String msg) {
		super(msg);
	}

	/**
	 * @方法名称: TemplateException
	 * @描述: 构造函数
	 * @参数：
	 */
	public TemplateException() {
		super();
	}
	
	/** 
	 * @方法名称: TemplateException
	 * @描述: 构造函数
	 * @参数：@param t
	 */
	public TemplateException(Throwable t) {
		super(t);
	}
}
