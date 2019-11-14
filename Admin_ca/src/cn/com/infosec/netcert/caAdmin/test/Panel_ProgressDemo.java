package cn.com.infosec.netcert.caAdmin.test;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**   
 * @Description 进度条的效果 
 * @Author 江岩    
 * @Time 2019-10-11 17:39
 */
public class Panel_ProgressDemo {

	public static void main(String[] args) {
		
		final Display display = Display.getDefault();
		final Shell shell = new Shell();
		shell.setSize(500, 375);
		shell.setLayout(new RowLayout());
		
		new Panel_ProgressDemo().open(shell);
		
		shell.layout();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		
	}
	// 进度条方法
	public void open(Shell shell) {
		Button button = new Button(shell, SWT.NONE);
		button.setText("        GO          ");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// 创建进度条对话框的处理过程对象
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						monitor.beginTask("开始执行......", 10);
						for (int i = 0; i < 10; i++) {// 循环10次，每次间隔一秒
							if (monitor.isCanceled()) // 随时监控是否选择了对话框的“取消”按钮
								return;// 中断处理
							try {
								Thread.sleep(1000);
							} catch (Throwable t) {
							}
							monitor.setTaskName("第" + (i + 1) + "次循环");// 提示信息
							monitor.worked(1);// 进度条前进一步
						}
						monitor.done();// 进度条前进到完成
					}
				};
				try {
					// 创建一个进度条对话框，并将runnable传入
					// 第一个参数推荐设为true，如果设为false则处理程序会运行在UI线程里，界面将有一点停滞感。
					// 第二个参数：true＝对话框的“取消”按钮有效
					new ProgressMonitorDialog(shell).run(true, true, runnable);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		
	}

}