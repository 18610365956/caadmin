package cn.com.infosec.netcert.caAdmin.ui.admin;

import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cn.com.infosec.netcert.caAdmin.utils.Env;

/**   
 * 修改已使用模板时，如果有代下载证书，需要导出待下载证书信息
 * @Author 江岩    
 * @Time 2019-08-26 21:38
 */
public class Panel_ExportCSV extends ApplicationWindow {

	private static ResourceBundle l = Env.getLanguage();
	public String returnCode = "0";
	
	/**
	 * Create the application window.
	 */
	public Panel_ExportCSV() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		setShellStyle(SWT.CLOSE | SWT.APPLICATION_MODAL);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		
		Label lbl_notice = new Label(container, SWT.NONE);
		lbl_notice.setBounds(23, 27, 313, 17);
		lbl_notice.setText(l.getString("Notice_exportUserInfo") + "?");
		
		Button btn_ok = new Button(container, SWT.NONE);
		btn_ok.setBounds(59, 70, 80, 27);
		btn_ok.setText(l.getString("next"));
		btn_ok.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				returnCode = "1";
				close();
			}
		});
		
		Button btn_exportNext = new Button(container, SWT.NONE);
		btn_exportNext.setBounds(154, 70, 80, 27);
		btn_exportNext.setText(l.getString("export_next"));
		btn_exportNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				returnCode = "2";
				close();
			}
		});
		
		Button btn_cancle = new Button(container, SWT.NONE);
		btn_cancle.setBounds(253, 70, 80, 27);
		btn_cancle.setText(l.getString("cancle"));
		btn_cancle.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				returnCode = "-1";
				close();
			}
		});
		return container;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(l.getString("OK"));
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(375, 163);
	}
}
