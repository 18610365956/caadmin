/**      
 * @Author 江岩    
 * @Time 2019-08-01 16:51     
 */  
package cn.com.infosec.netcert.caAdmin.test;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

/**   
 * @Description  构建IP输入框
 * @Author 江岩    
 * @Time 2019-08-01 16:51
 */
public class Panel_IP_Demo extends ApplicationWindow {

	private Text text_0, text_1, text_2, text_3;
	private Text text_printIP;
	/**
	 * Create the application window.
	 */
	public Panel_IP_Demo() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);	
		
		comp_IP(container);
		
		
		
		
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setBounds(86, 42, 24, 17);
		lblNewLabel.setText("IP:");
		
		Button btn_getIP = new Button(container, SWT.NONE);
		btn_getIP.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String ip = getIP();
				text_printIP.setText(ip);
			}
		});
		btn_getIP.setBounds(161, 90, 117, 27);
		btn_getIP.setText("获取输入的IP地址");
		
		text_printIP = new Text(container, SWT.BORDER);
		text_printIP.setBounds(114, 144, 164, 25);
		
		Label lbl_printIP = new Label(container, SWT.NONE);
		lbl_printIP.setBounds(10, 147, 97, 17);
		lbl_printIP.setText("输入的IP地址为：");
		
		return container;
	}

	// IP组件
	public Control comp_IP(Composite container){
		
		Composite composite = new Composite(container, SWT.BORDER);
		composite.setForeground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		composite.setBounds(116, 34, 162, 25);
	
		text_0 = new Text(composite, SWT.NONE);
		text_0.setText("255");
		text_0.setTextLimit(3);
		text_0.setBounds(6, 5, 25, 18);

		Label label_1 = new Label(composite, SWT.NONE);
		label_1.setText(".");
		label_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_1.setBounds(32, 5, 3, 17);

		text_1 = new Text(composite, SWT.NONE);
		text_1.setBounds(42, 5, 25, 17);
		text_1.setTextLimit(3);
		
		Label label_2 = new Label(composite, SWT.NONE);
		label_2.setText(".");
		label_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_2.setBounds(70, 6, 3, 17);

		text_2 = new Text(composite, SWT.NONE);
		text_2.setBounds(79, 5, 25, 17);
		text_2.setTextLimit(3);

		Label label_3 = new Label(composite, SWT.NONE);
		label_3.setText(".");
		label_3.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		label_3.setBounds(110, 6, 3, 17);

		text_3 = new Text(composite, SWT.NONE);
		text_3.setBounds(120, 5, 26, 18);
		text_3.setTextLimit(3);
		
		Label lblNewLabel_2 = new Label(composite, SWT.NONE);
		lblNewLabel_2.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		lblNewLabel_2.setBounds(172, 37, 3, 17);
		lblNewLabel_2.setText(".");

		return composite;
	}
	
	// 封装方法
	private String getIP() {
		
		StringBuffer ip = new StringBuffer();
		ip.append(text_0.getText().trim()+"."+text_1.getText().trim()+"."+text_2.getText().trim()+"."+text_3.getText().trim());
		
		return ip.toString();
	}



	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Panel_IP_Demo window = new Panel_IP_Demo();
			window.setBlockOnOpen(true);
			window.open();
			Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Tets_ip_Comp");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}
}
