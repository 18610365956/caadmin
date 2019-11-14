package cn.com.infosec.netcert.caAdmin.test;

import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.log.FileLogger;

/**   
 * @Description (≤π≥‰√Ë ˆ) 
 * @Author Ω≠—“    
 * @Time 2019-07-29 14:02
 */
public class Test_Composite extends ApplicationWindow {

	private boolean flag = false;
	private static ResourceBundle l = Env.getLanguage();
	private Table table_1;
	private Text text;

	/**
	 * Create the application window.
	 */
	public Test_Composite() {
		super(null);
		addToolBar(SWT.FLAT | SWT.WRAP);
		flag = true;
	}

	/**
	 * Create contents of the application window.
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FormLayout());

		table_1 = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_table_1 = new FormData();
		fd_table_1.left = new FormAttachment(0, 10);
		fd_table_1.top = new FormAttachment(0, 20);
		table_1.setLayoutData(fd_table_1);
		table_1.setHeaderVisible(true);
		table_1.setLinesVisible(true);

		TableItem tableItem = new TableItem(table_1, SWT.NONE);
		tableItem.setText("New TableItem");

		TableItem tableItem_1 = new TableItem(table_1, SWT.NONE);
		tableItem_1.setText("New TableItem");

		TableItem tableItem_2 = new TableItem(table_1, SWT.NONE);
		tableItem_2.setText("New TableItem");

		Menu menu = new Menu(table_1);

		MenuItem item = new MenuItem(menu, SWT.NONE);
		item.setText("111");

		Tree tree = new Tree(container, SWT.BORDER);
		fd_table_1.right = new FormAttachment(tree, -144);
		fd_table_1.bottom = new FormAttachment(tree, 0, SWT.BOTTOM);
		FormData fd_tree = new FormData();
		fd_tree.bottom = new FormAttachment(100, -317);
		fd_tree.top = new FormAttachment(0, 20);
		fd_tree.left = new FormAttachment(0, 376);
		fd_tree.right = new FormAttachment(100, -10);
		tree.setLayoutData(fd_tree);

		TreeItem treeItem_root = new TreeItem(tree, SWT.NONE);
		treeItem_root.setText("111");

		TreeItem trtmNewTreeitem = new TreeItem(treeItem_root, SWT.NONE);
		trtmNewTreeitem.setText("222");
		treeItem_root.setExpanded(true);
		
		text = new Text(container, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(table_1, 18);
		fd_text.left = new FormAttachment(table_1, 0, SWT.LEFT);
		fd_text.right = new FormAttachment(0, 204);
		text.setLayoutData(fd_text);
		
		Button btnNewButton = new Button(container, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setFilterExtensions(new String[] { "*.txt","*.p7b" });
				fd.open();
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(text, 58);
		fd_btnNewButton.left = new FormAttachment(0, 10);
		btnNewButton.setLayoutData(fd_btnNewButton);
		btnNewButton.setText("New Button");
		
		Combo combo = new Combo(container, SWT.NONE | SWT.READ_ONLY);
		FormData fd_combo = new FormData();
		fd_combo.top = new FormAttachment(text, 0, SWT.TOP);
		fd_combo.right = new FormAttachment(100, -51);
		combo.setLayoutData(fd_combo);
		
		Spinner spinner = new Spinner(container, SWT.BORDER | SWT.READ_ONLY);
		fd_text.bottom = new FormAttachment(100, -260);
		FormData fd_spinner = new FormData();
		fd_spinner.top = new FormAttachment(text, 22);
		fd_spinner.right = new FormAttachment(text, 0, SWT.RIGHT);
		spinner.setLayoutData(fd_spinner);
		
		text.addListener(SWT.FocusOut, new Listener(){
			@Override
			public void handleEvent(Event arg0) {
				try {
					int number = Integer.parseInt(text.getText().trim());
					if (text.getText().trim().indexOf("0") == 0 || number <= 0 || number > 1000) {
						MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
						mb.setMessage(l.getString("Notice_validTimeRange"));
						mb.open();
					}
				} catch (NumberFormatException e) {
					MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
					mb.setMessage(l.getString("Notice_error_validTime"));
					mb.open();
				}	
			}
		});

		tree.addMouseListener(new MouseAdapter() {
			public void mouseDown(MouseEvent me) {
				if (me.button == 1) {
					TreeItem node = tree.getItem(new Point(me.x, me.y));
					
					if (node == null || node.getParentItem() == null) {
						System.out.println("111");
						tree.deselectAll();
						return;
					} else {
						System.out.println("222");
						if (node == null || node.getText() == null || node.getText().length() <= 0) {
							tree.setMenu(null);
						} else {
							Menu menu = new Menu(tree);
							MenuItem del_all = new MenuItem(menu, SWT.PUSH);
							del_all.setText("1111");
							del_all.addSelectionListener(new SelectionListener() {
								@Override
								public void widgetSelected(SelectionEvent arg0) {
									tree.getSelection()[0] = null;
								}
	
								@Override
								public void widgetDefaultSelected(SelectionEvent paramSelectionEvent) {
								}
							});
							tree.setMenu(menu);
						}
					}
				}
				tree.clear(0, true);
			}
		});
		return container;
	}

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			System.out.println("main");
			Test_Composite window = new Test_Composite();
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
		if (flag) {
			newShell.setText("true");
		} else {
			newShell.setText("false");
		}

	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(539, 598);
	}
}
