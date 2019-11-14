package cn.com.infosec.netcert.caAdmin.ui.template;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import cn.com.infosec.netcert.caAdmin.ui.admin.Panel_ExportCSV;
import cn.com.infosec.netcert.caAdmin.ui.login.Panel_MessageDialog;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplateParser;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Keypolicy;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.caAdmin.utils.Utils;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;
import cn.com.infosec.util.Base64;

/**
 * @Desc ģ�����
 * @Author Infosec_jy
 * @Date 2019��3��5�� ����10:23:28
 */
public class Panel_Template extends ApplicationWindow {

	private CertTemplate certTemplate, certTemplate_enc;
	private Sign_Template signal_temp;
	private Encry_Template encry_Template;
	private Panel_BasicTemplate basicTemplate;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private String operationType;
	private static ResourceBundle l = Env.getLanguage();

	/**
	 * ���췽�� ����ģ�壬���� ģ�������Ϣ
	 * @param panel_BasicTemplate
	 */
	public Panel_Template(Panel_BasicTemplate basicTemplate) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MIN);
		this.basicTemplate = basicTemplate; // ����ֵ�����ύ��Serverʱʹ��

		certTemplate = new CertTemplate();
		certTemplate.setAdd(true); // ȷ���Ƿ�����ͼ����ʾ ������Ϣģ��
		certTemplate.setSignal(basicTemplate.isSignal); // ȷ�� �Ƿ�����ͼ����ʾ ����ģ��
		certTemplate.setSpecialType(basicTemplate.tempType); // �� ǩ��ģ���� ��ѡ����ģ����

		certTemplate_enc = new CertTemplate();
		// certTemplate_enc.setAdd(true); // ȷ�� �Ƿ���� ����ģ����Ϣ
	}

	/**
	 * ���췽�� ���� �鿴 �޸�  ��¡ ģ��ʱ��������󣬸��� �ύ������ֵ����ѯ ģ�岢��װ�� ��Ӧ��ͼ��
	 * type  ����  view ��  modify
	 * @param templateName 
	 */
	public Panel_Template(String templateName, String operationType) {
		super(null);
		this.operationType = operationType;
		setShellStyle(SWT.CLOSE | SWT.MIN);
		certTemplate = new CertTemplate();
		certTemplate.setSignal(true); // Ĭ���ǵ�֤
		certTemplate.setAdd(false); // �ֶ�����

		certTemplate_enc = new CertTemplate();
		// certTemplate_enc.setAdd(false); // ȷ�� �Ƿ���� ����ģ����Ϣ
		// certTemplate_enc.setSignal(true); // ������ �޸�ģ��ʱ ��ʾ ����ҳ��
		queryTemplateDetail(templateName, certTemplate, certTemplate_enc); // ��certTemplate��ֵ
	}

	/**
	 * ҳ�����ͼ��ʾ�� ��������������������� ������Ϣҳ��һ������ ǩ��ģ��ҳ���Ƿ���� ����ģ��ҳ ���� ������Ϣҳѡ��ĵ�˫ֵ֤ ���� ����
	 * ������޸Ĳ��������� ������Ϣҳ��ǩ��ģ��ҳ������ģ��ҳ�����ȷ����ť �������б�����װ���µ� ģ������У����� ����
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 10;
		container.setLayout(formLayout);
		// ҳ�� tab ������
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.left = new FormAttachment(0, 10);
		fd_tabFolder.right = new FormAttachment(100, -10);
		fd_tabFolder.top = new FormAttachment(0, 5);
		tabFolder.setLayoutData(fd_tabFolder);
		FormLayout fl_tabFolder = new FormLayout();

		tabFolder.setLayout(fl_tabFolder);

		if (!certTemplate.isAdd()) { // �Ƿ���ʾ ������Ϣҳ ���������������������ʾ
			basicTemplate = new Panel_BasicTemplate(certTemplate, operationType);
			basicTemplate.createBasicPanel(tabFolder);
		}
		signal_temp = new Sign_Template(certTemplate); // ǩ��ģ����չ��Ϣ
		signal_temp.createContents(tabFolder);

		if (!certTemplate.isSignal()) { // ����ģ�� �Ƿ���ʾ
			encry_Template = new Encry_Template(certTemplate_enc);
			encry_Template.createContents(tabFolder);
		}
		// ������ť
		Button btn_submit = new Button(container, SWT.NONE);
		btn_submit.setText(l.getString("OK"));
		FormData fd_btn_submit = new FormData();
		fd_btn_submit.top = new FormAttachment(tabFolder, 10);
		fd_btn_submit.left = new FormAttachment(0, 540);
		fd_btn_submit.right = new FormAttachment(0, 615);
		btn_submit.setLayoutData(fd_btn_submit);
		if ("view".equalsIgnoreCase(operationType)) {
			btn_submit.setEnabled(false);
		}
		btn_submit.addSelectionListener(new SelectionAdapter() { // ��װ������Ϣ���ύ�� Server
			public void widgetSelected(SelectionEvent e) {
				CertTemplate certTemplate_new = new CertTemplate(); // ��һ���µĶ���ֵ
				boolean isAdd = certTemplate.isAdd(); // ����� ��������ͨ�� ��ǰҳ��� panel_BasicTemplate �����ȡֵ
				if (isAdd) { // ����� �޸ģ���ͨ�� panel_BasicTemplate ҳ���ȡ ʵʱֵ
					tempAppendBasicInfo(certTemplate_new);
				} else {
					try {
						basicTemplate.tempAppendBasicInfo(certTemplate_new);
					} catch (Exception e1) {
						Panel_MessageDialog dialog = new Panel_MessageDialog("error", e1.getMessage());
						dialog.setBlockOnOpen(true);
						dialog.open();
						return;
					}
				}
				try {
					signal_temp.packageTempInfo(certTemplate_new); // ��װ ǩ��ģ����Ϣ
				} catch (Exception e1) {
					Panel_MessageDialog dialog = new Panel_MessageDialog("error", e1.getMessage());
					dialog.setBlockOnOpen(true);
					dialog.open();
					return;
				}
				// ��װ����ģ��ı�����Ϣ
				CertTemplate certTemplate_new_enc = new CertTemplate();
				if (!certTemplate.isSignal()) {
					try {
						encry_Template.packageTempInfo(certTemplate_new_enc); // ����� ����,���� tempAppendBasicInfo �����ã������ �޸�,���ڲ�ѯģ��ʱ����
					} catch (Exception e1) {
						Panel_MessageDialog dialog = new Panel_MessageDialog("error", e1.getMessage());
						dialog.setBlockOnOpen(true);
						dialog.open();
						return;
					}
					if (isAdd) {
						tempAppendBasicInfo(certTemplate_new_enc);
					} else {
						try {
							basicTemplate.tempAppendBasicInfo(certTemplate_new_enc);
						} catch (Exception e1) {
							return;
						}
					}
					ArrayList<Extension> ex_List = certTemplate_new.getStandardExtensions(); // ͬ�� ǩ��ģ�� ĳЩ��
					ArrayList<Extension> ex_List_enc = certTemplate_new_enc.getStandardExtensions();
					for (Extension ex : ex_List) {
						if ("1.3.6.1.5.5.7.1.1".equalsIgnoreCase(ex.getOID())) {
							ex_List_enc.add(ex);
						}
						if ("1.3.6.1.4.1.27971.35.2".equalsIgnoreCase(ex.getOID())) {
							ex_List_enc.add(ex);
						}
						if ("2.5.29.35".equalsIgnoreCase(ex.getOID())) {
							ex_List_enc.add(ex);
						}
						if ("2.5.29.14".equalsIgnoreCase(ex.getOID())) {
							ex_List_enc.add(ex);
						}
					}
					certTemplate_new_enc.setOtherExtensions(certTemplate_new.getOtherExtensions());
					certTemplate_new_enc.setCustomerExtensions(certTemplate_new.getCustomerExtensions());
					certTemplate_new_enc.setCheckCsrSubject(certTemplate_new.isCheckCsrSubject());
					certTemplate_new_enc.setPermitSameDN(certTemplate_new.getPermitSameDN());
					certTemplate_new_enc.setKeyPolicy(certTemplate_new.getKeyPolicy());
					certTemplate_new_enc.setIspublishCert(certTemplate_new.getIspublishCert());
					certTemplate_new_enc.setIspublishCrl(certTemplate_new.getIspublishCrl());
				}
				certTemplate.setIsused("0");
				certTemplate.setIsSysTemplate("1");
				certTemplate_new_enc.setIsused("0");
				certTemplate_new_enc.setIsSysTemplate("1");
				addOrUpdateTemplate(certTemplate_new, certTemplate_new_enc,isAdd);
				close();
			}
		});
		Button btn_cancle = new Button(container, SWT.NONE);
		FormData fd_btn_cancle = new FormData();
		fd_btn_cancle.top = new FormAttachment(tabFolder, 10);
		fd_btn_cancle.bottom = new FormAttachment(btn_submit, 0, SWT.BOTTOM);
		fd_btn_cancle.left = new FormAttachment(btn_submit, 25, SWT.RIGHT);
		fd_btn_cancle.right = new FormAttachment(btn_submit, 100, SWT.RIGHT);
		btn_cancle.setLayoutData(fd_btn_cancle);
		btn_cancle.setText(l.getString("cancle"));
		btn_cancle.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				handleShellCloseEvent();
			}
		});
		return container;
	}

	/**
	 * @Desc ���� �� �޸�ģ��, ��ѯ���ݿ��Ƿ����ͬ��ģ��, ���� ��ִ�� ���²���,�����ھ�ִ�� ���� ����,
	 * @when ���� �� �޸�
	 * @Authod ����
	 * @Date 2019��3��20�� ����3:03:16
	 */
	private void addOrUpdateTemplate(CertTemplate certTemplate, CertTemplate certTemplate_enc, boolean isAdd) {
		String templateInfo = certTemplate.toString();
		//System.out.println(templateInfo);
		try {
			Properties p = new Properties();
			p.setProperty(PropertiesKeysRes.TEMPLATENAME, certTemplate.getCerttemplateName());
			p.setProperty(PropertiesKeysRes.TEMPLATECONTENT, Base64.encode(templateInfo));
			if (!certTemplate.isSignal()) {
				String templateInfo_enc = certTemplate_enc.toString();
				p.setProperty(PropertiesKeysRes.TEMPLATECONTENT + "_ENC", Base64.encode(templateInfo_enc));
			}
			Response resp = Env.client.sendRequest("QUERYTEMPLATELIST", p);
			Properties[] rows = resp.getItemData();
			if (rows.length > 0) {
				if ("modify_isUsed".equalsIgnoreCase(operationType)) {
					Panel_ExportCSV exportCSV = new Panel_ExportCSV();
					exportCSV.setBlockOnOpen(true);
					int w = exportCSV.open();
					if (w != 0) {
						return;
					}
					String result = exportCSV.returnCode;
					if ("2".equalsIgnoreCase(result)) {
						Properties p_exportUserInfo = new Properties();
						p_exportUserInfo.setProperty(PropertiesKeysRes.TEMPLATENAME,
								certTemplate.getCerttemplateName());
						Response resp_exportUserInfo = Env.client.sendRequest("EXPORTNOTDOWNLOADINFO", p);
						Properties[] ps = resp_exportUserInfo.getItemData();

						FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
						fd.setFilterExtensions(new String[] { "*.csv" });
						fd.setFileName(certTemplate.getCerttemplateName());
						String f = fd.open();
						if (f != null) {
							try {
								FileOutputStream fos = new FileOutputStream(f);
								OutputStreamWriter osw = new OutputStreamWriter(fos);
								StringBuffer sb = new StringBuffer();
								sb.append("Cert DN" + "," + "User ID" + "," + "User Name" + Utils.newLine);
								for (Properties p1 : ps) {
									String uuid = p1.getProperty(PropertiesKeysRes.UUID);
									String userName = p1.getProperty(PropertiesKeysRes.USERNAME);
									String subjectDN = p1.getProperty(PropertiesKeysRes.SUBJECTDN);
									sb.append("\"" + uuid + "\"" + ",\"" + userName + "\",\"" + subjectDN + "\""
											+ Utils.newLine);
								}
								osw.write(sb.toString());
								osw.close();
								fos.close();
							} catch (FileNotFoundException e1) {
								log.errlog("File not found", e1);
								MessageBox mb = new MessageBox(getShell());
								mb.setMessage(l.getString("Notice_not_foundFile"));
								mb.open();
								return;
							} catch (IOException e1) {
								log.errlog("Base64.decode fail/Save file file", e1);
								MessageBox mb = new MessageBox(getShell());
								mb.setMessage(l.getString("Notice_fail_saveFile"));
								mb.open();
								return;
							}
						}
					} else if ("-1".equalsIgnoreCase(result)) {
						return;
					}
				}
				Response resp_update = Env.client.sendRequest("UPDATETEMPLATE", p);
				Properties pRes = resp_update.getP();
				if ("1".equalsIgnoreCase(pRes.getProperty(PropertiesKeysRes.TEMPLATE_UPDATE_RESULT))) {
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_succ_modifyTemplate"));
					mb.open();
				}
			} else {
				Response resp_add = Env.client.sendRequest("ADDTEMPLATE", p);
				Properties pRes = resp_add.getP();
				String tempName = pRes.getProperty(PropertiesKeysRes.TEMPLATENAME);
				if (tempName.equalsIgnoreCase(certTemplate.getCerttemplateName())) {
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setMessage(l.getString("Notice_succ_addTemplate"));
					mb.open();
				}
			}
		} catch (IOException e1) {
			log.errlog("Base64 decode fail", e1);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_Base64Decode"));
			mb.open();
		} catch (ServerException se) {
			log.errlog("Operating templates.", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_operationTemplate") + ":" + "[" + se.getErrorNum() + "]:"
					+ se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Add/Modify template fail", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_operationTemplate"));
			mb.open();
		}
	}

	/**
	 * @Desc ��ѯ֤��ģ�壬 ����ɽ�������ȡ certTemplate ,���� Signal_Template �� Encry_Template��ʾ
	 * @When �޸�ģ�� ������ʹ��
	 * @Authod ����
	 * @Date 2019��3��7�� ����10:26:12
	 */
	private void queryTemplateDetail(String templateName, CertTemplate certTemplate, CertTemplate certTemplate_enc) {
		Properties p = new Properties();
		p.setProperty(PropertiesKeysRes.TEMPLATENAME, templateName);
		try {
			Response resp = Env.client.sendRequest("QUERYTEMPLATEDETAIL", p);
			Properties row = resp.getP();

			String template = new String(Base64.decode(row.getProperty(PropertiesKeysRes.TEMPLATECONTENT)));
			CertTemplateParser.parser(templateName, template, certTemplate);
			certTemplate.setCerttemplateName(templateName);

			String template_enc = row.getProperty(PropertiesKeysRes.TEMPLATECONTENT + "_ENC");
			if (template_enc != null) {
				certTemplate.setSignal(false); // Ϊ��ȷ�� ��һ��֮�����ͼҳ�棬�Ƿ���� ����ģ��
				template_enc = new String(Base64.decode(template_enc));
				CertTemplateParser.parser(templateName, template_enc, certTemplate_enc);
			}
		} catch (ServerException se) {
			log.errlog("View templates", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(
					l.getString("Notice_fail_viewTemplate") + ":" + "[" + se.getErrorNum() + "]:" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("View templates", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_viewTemplate"));
			mb.open();
		}
	}

	/**
	 * @Desc �� Server�ύ����ʱ���� ������Ϣ��װ�� ģ�������(��˫֤����Ҫ����)
	 * @When �޸�ģ�� ������ʹ��
	 * @Authod ����
	 * @Date 2019��3��20�� ����3:01:18
	 */
	private void tempAppendBasicInfo(CertTemplate certTemplate) {
		if (basicTemplate.isSignal) {
			certTemplate.setSignal(true);
		}
		certTemplate.setCerttemplateName(basicTemplate.tempName);
		certTemplate.setSpecialType(basicTemplate.tempType);
		certTemplate.setValiditylimit(Integer.valueOf(basicTemplate.validTime));
		if (l.getString("day").equalsIgnoreCase(basicTemplate.unit)) {
			certTemplate.setValidityUnit("d");
		} else if (l.getString("month").equalsIgnoreCase(basicTemplate.unit)) {
			certTemplate.setValidityUnit("m");
		} else if (l.getString("year").equalsIgnoreCase(basicTemplate.unit)) {
			certTemplate.setValidityUnit("y");
		}
		Keypolicy keyPolicy_rsa = new Keypolicy();
		keyPolicy_rsa.setAlgorithmidentifier("RSA");
		keyPolicy_rsa.setMinkeylength(basicTemplate.rsaMinLen);
		Keypolicy keyPolicy_sm2 = new Keypolicy();
		keyPolicy_sm2.setAlgorithmidentifier("SM2");
		keyPolicy_sm2.setMinkeylength("256");

		List<Keypolicy> pubKeyList = new ArrayList<Keypolicy>();
		pubKeyList.add(keyPolicy_rsa);
		pubKeyList.add(keyPolicy_sm2);

		certTemplate.setPubKeyPolicy(pubKeyList);
		certTemplate.setSignAlg((basicTemplate.signAlg));
	}

	/**
	 * ��ͼ����������
	 * @param   (Shell)   
	 * @Author ����      
	 * @Time 2019-06-11 14:07
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (certTemplate.isAdd()) {
			shell.setText(l.getString("addTemplate"));
		} else if ("view".equalsIgnoreCase(operationType)) {
			shell.setText(l.getString("viewTemplate"));
		} else if ("modify".equalsIgnoreCase(operationType) || "modify_isUsed".equalsIgnoreCase(operationType)) {
			shell.setText(l.getString("modifyTemplate"));
		} else if ("clone".equalsIgnoreCase(operationType)) {
			shell.setText(l.getString("cloneTemplate"));
		} else {
			shell.setText("");
		}
		shell.setImage(new Image(shell.getDisplay(), "res/logo.png"));
	}

}