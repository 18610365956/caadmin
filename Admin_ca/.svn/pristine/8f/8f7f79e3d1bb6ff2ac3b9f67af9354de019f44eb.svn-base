package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.com.infosec.netcert.caAdmin.ui.template.Panel_BasicTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.Panel_Template;
import cn.com.infosec.netcert.caAdmin.utils.Env;

import org.eclipse.swt.widgets.Button;

/**   
 * @Description (≤π≥‰√Ë ˆ) 
 * @Author Ω≠—“    
 * @Time 2019-08-06 15:33
 */
public class Test_panel_template_ extends ApplicationWindow {

	private Panel_Template panel_Template;
	/**
	 * Create the application window.
	 */
	public Test_panel_template_() {
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
		
		Button button = new Button(container, SWT.NONE);
		button.setBounds(70, 193, 103, 27);
		button.setText("ÃÌº”ƒ£∞Â ");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

				if (!Env.validSession()) {
					handleShellCloseEvent();
				} else {
					Env.lastOperationTime();

					Panel_BasicTemplate panel_BasicTemp = new Panel_BasicTemplate();
					panel_BasicTemp.setBlockOnOpen(true);
					int w = panel_BasicTemp.open();
					if (w == 0) {
						panel_Template = new Panel_Template(panel_BasicTemp);
						panel_Template.setBlockOnOpen(true);
						panel_Template.open();
					}
				}
			}
		});
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
			Test_panel_template_ window = new Test_panel_template_();
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
		return new Point(422, 353);
	}
}
