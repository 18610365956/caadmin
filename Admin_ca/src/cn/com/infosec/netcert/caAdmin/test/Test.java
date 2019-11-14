/**
 * 
 */
package cn.com.infosec.netcert.caAdmin.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;

/**  
* @ClassName: Test  
* @Description:   
* @author wanghaixiang  
* @date 2019年10月30日 下午3:39:53   
*    
*/
public class Test {

	/**
	 * @throws IOException 
	 * @throws FileNotFoundException   
	*
	* @Description:  
	* @param args  
	* @return void    
	* @throws  
	*/
	public static void main(String[] args) throws FileNotFoundException, IOException {
//		Properties properties = new Properties();
//		String path = Thread.currentThread().getContextClassLoader().getResource("").getPath();
//		FileInputStream inStream = new FileInputStream(new File(path+"cn/com/infosec/netcert/caAdmin/ui/resource/language_zh.properties"));
//		properties.load(inStream);
//		System.out.println(properties.getProperty("whx"));
		
		
		Properties[] arr =new Properties[10];
		for (int i = 0; i < arr.length; i++) {
			Properties properties = new Properties();
			properties.setProperty("i"+1, "property"+i);
			arr[i]=properties;
		}
		//System.out.println(arr[0]);
		for (Properties properties : arr) {
			 Set<Object> set = properties.keySet();
			 for (Object object : set) {
				System.out.println("git2"+object);
			}
		}
	}

}
