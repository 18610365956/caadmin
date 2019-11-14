/**
 * 
 */
package cn.com.infosec.netcert.caAdmin.test;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

/**  
* @ClassName: Uuuu  
* @Description:   
* @author wanghaixiang  
* @date 2019年11月5日 上午11:45:36   
*    
*/
public class Uuuu extends ApplicationWindow {

	/**
	 * Create the application window.
	 */
	public Uuuu() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());
		
		Button btnNewButton = new Button(container, SWT.NONE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(0, 46);
		fd_btnNewButton.left = new FormAttachment(0, 58);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("New Button");
		
		Button btnNewButton_1 = new Button(container, SWT.NONE);
		FormData fd_btnNewButton_1 = new FormData();
		fd_btnNewButton_1.top = new FormAttachment(btnNewButton, 38);
		fd_btnNewButton_1.left = new FormAttachment(btnNewButton, 0, SWT.LEFT);
		btnNewButton_1.setLayoutData(fd_btnNewButton_1);
		btnNewButton_1.setText("New Button");
		
		Button btnNewButton_2 = new Button(container, SWT.NONE);
		FormData fd_btnNewButton_2 = new FormData();
		fd_btnNewButton_2.top = new FormAttachment(btnNewButton, 0, SWT.TOP);
		btnNewButton_2.setLayoutData(fd_btnNewButton_2);
		btnNewButton_2.setText("New Button");
		
		Button btnNewButton_3 = new Button(container, SWT.NONE);
		fd_btnNewButton_2.left = new FormAttachment(btnNewButton_3, 32);
		FormData fd_btnNewButton_3 = new FormData();
		fd_btnNewButton_3.top = new FormAttachment(btnNewButton, 0, SWT.TOP);
		fd_btnNewButton_3.left = new FormAttachment(btnNewButton, 35);
		btnNewButton_3.setLayoutData(fd_btnNewButton_3);
		btnNewButton_3.setText("New Button");
		
		Button btnNewButton_4 = new Button(container, SWT.NONE);
		FormData fd_btnNewButton_4 = new FormData();
		fd_btnNewButton_4.top = new FormAttachment(btnNewButton_1, 0, SWT.TOP);
		fd_btnNewButton_4.left = new FormAttachment(btnNewButton_3, 0, SWT.LEFT);
		btnNewButton_4.setLayoutData(fd_btnNewButton_4);
		btnNewButton_4.setText("New Button");

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Uuuu window = new Uuuu();
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
		newShell.setText("New Application");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(627, 489);
	}
}
