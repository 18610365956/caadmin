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
 * @Description ��������Ч�� 
 * @Author ����    
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
	// ����������
	public void open(Shell shell) {
		Button button = new Button(shell, SWT.NONE);
		button.setText("        GO          ");
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// �����������Ի���Ĵ�����̶���
				IRunnableWithProgress runnable = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) {
						monitor.beginTask("��ʼִ��......", 10);
						for (int i = 0; i < 10; i++) {// ѭ��10�Σ�ÿ�μ��һ��
							if (monitor.isCanceled()) // ��ʱ����Ƿ�ѡ���˶Ի���ġ�ȡ������ť
								return;// �жϴ���
							try {
								Thread.sleep(1000);
							} catch (Throwable t) {
							}
							monitor.setTaskName("��" + (i + 1) + "��ѭ��");// ��ʾ��Ϣ
							monitor.worked(1);// ������ǰ��һ��
						}
						monitor.done();// ������ǰ�������
					}
				};
				try {
					// ����һ���������Ի��򣬲���runnable����
					// ��һ�������Ƽ���Ϊtrue�������Ϊfalse��������������UI�߳�����潫��һ��ͣ�͸С�
					// �ڶ���������true���Ի���ġ�ȡ������ť��Ч
					new ProgressMonitorDialog(shell).run(true, true, runnable);
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});

		
	}

}