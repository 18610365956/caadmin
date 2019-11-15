package cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.encry.Enc_Template;
import cn.com.infosec.netcert.caAdmin.ui.template.nextSubmit.sign.Sign_Template;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplate;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.CertTemplateParser;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Extension;
import cn.com.infosec.netcert.caAdmin.ui.template.templateUtils.Keypolicy;
import cn.com.infosec.netcert.caAdmin.utils.Env;
import cn.com.infosec.netcert.framework.Response;
import cn.com.infosec.netcert.framework.ServerException;
import cn.com.infosec.netcert.framework.log.FileLogger;
import cn.com.infosec.netcert.framework.resource.PropertiesKeysRes;
import cn.com.infosec.util.Base64;

/**
 * @Desc 模板管理
 * @Author Infosec_jy
 * @Date 2019年3月5日 上午10:23:28
 */
public class Panel_Template extends ApplicationWindow {

	private CertTemplate certTemplate_sign, certTemplate_enc;
	private Sign_Template signal_temp;
	private Enc_Template encry_Template;
	private Panel_BasicTemplate basicTemplate;
	private FileLogger log = FileLogger.getLogger(this.getClass());
	private String type;
	private static ResourceBundle l = Env.getLanguage();
	/**
	 * 构造方法 添加模板，保存 模板基础信息
	 * @param panel_BasicTemplate
	 */
	public Panel_Template(Panel_BasicTemplate basicTemplate) {
		super(null);
		setShellStyle(SWT.CLOSE | SWT.MIN);
		this.basicTemplate = basicTemplate; // 保存值，在提交到Server时使用

		certTemplate_sign = new CertTemplate();
		certTemplate_sign.setAdd(true); // 确定是否在视图中显示 基本信息模板
		certTemplate_sign.setSignal(basicTemplate.isSignal); // 确定 是否在视图中显示 加密模板
		certTemplate_sign.setSpecialType(basicTemplate.tempType); // 在 签名模板中 勾选特殊模板项

		certTemplate_enc = new CertTemplate();
		// certTemplate_enc.setAdd(true); // 确定 是否加载 加密模板信息
	}

	/**
	 * 构造方法 用于 查看 修改 克隆模板时，构造对象，根据 提交过来的值，查询 模板并封装到 对应对象中
	 * type  区分  view 和  modify
	 * @param templateName 
	 */
	public Panel_Template(String templateName, String type) {
		super(null);
		this.type = type;
		setShellStyle(SWT.CLOSE | SWT.MIN);
		certTemplate_sign = new CertTemplate();
		certTemplate_sign.setSignal(true); // 默认是单证
		certTemplate_sign.setAdd(false); // 手动设置

		certTemplate_enc = new CertTemplate();
<<<<<<< HEAD
		
=======
>>>>>>> 3a3ddb7246b2c9780cc0ebeb1393db79ca2f4945
		// certTemplate_enc.setAdd(false); // 确定 是否加载 加密模板信息
		// certTemplate_enc.setSignal(true); // 用来在 修改模板时 显示 加密页面
		queryTemplateDetail(templateName, certTemplate_sign, certTemplate_enc); // 给certTemplate赋值
	}

	/**
	 * 页面的视图显示， 如果是新增操作，不包含 基本信息页，一定包含 签名模板页，是否包含 加密模板页 依赖 基本信息页选择的单双证值 进行 新增
	 * 如果是修改操作，包含 基本信息页，签名模板页，加密模板页，点击确定按钮 ，将所有表单封装到新的 模板对象中，进行 更新
	 */
	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 10;
		container.setLayout(formLayout);
		// 页面 tab 导航栏
		TabFolder tabFolder = new TabFolder(container, SWT.NONE);
		FormData fd_tabFolder = new FormData();
		fd_tabFolder.left = new FormAttachment(0, 10);
		fd_tabFolder.right = new FormAttachment(100, -10);
		fd_tabFolder.top = new FormAttachment(0, 5);
<<<<<<< HEAD
		fd_tabFolder.top = new FormAttachment(0, 5);
		
		
		tabFolder.setLayoutData(fd_tabFolder);
		FormLayout fl_tabFolder = new FormLayout();

		
		
=======
		tabFolder.setLayoutData(fd_tabFolder);
		FormLayout fl_tabFolder = new FormLayout();

>>>>>>> 3a3ddb7246b2c9780cc0ebeb1393db79ca2f4945
		tabFolder.setLayout(fl_tabFolder);

		if (!certTemplate_sign.isAdd()) { // 是否显示 基本信息页 ，如果是新增操作，不显示
			basicTemplate = new Panel_BasicTemplate(certTemplate_sign, type);
			basicTemplate.createBasicPanel(tabFolder);
		}
		signal_temp = new Sign_Template(certTemplate_sign); // 签名模板扩展信息
		signal_temp.createContents(tabFolder);

		if (!certTemplate_sign.isSignal()) { // 加密模板 是否显示
			encry_Template = new Enc_Template(certTemplate_enc);
			encry_Template.createContents(tabFolder);
		}
		// 操作按钮
		Button btn_submit = new Button(container, SWT.NONE);
		btn_submit.setText(l.getString("OK"));
		FormData fd_btn_submit = new FormData();
		fd_btn_submit.top = new FormAttachment(tabFolder, 10);
		fd_btn_submit.left = new FormAttachment(0, 540);
		fd_btn_submit.right = new FormAttachment(0, 615);
		btn_submit.setLayoutData(fd_btn_submit);
		if("view".equalsIgnoreCase(type)){
			btn_submit.setEnabled(false);
		}
		btn_submit.addSelectionListener(new SelectionAdapter() { // 封装表单信息，提交给 Server

			public void widgetSelected(SelectionEvent e) {

				CertTemplate certTemplate_new = new CertTemplate(); // 对一个新的对象赋值

				boolean isAdd = certTemplate_sign.isAdd(); // 如果是 新增，则通过 当前页面的 panel_BasicTemplate 对象获取值
				if (isAdd) { 						 // 如果是 修改，则通过 panel_BasicTemplate 页面获取 实时值
					tempAppendBasicInfo(certTemplate_new);
				} else {
					basicTemplate.tempAppendBasicInfo(certTemplate_new);
				}
				signal_temp.packageTempInfo(certTemplate_new); // 封装 签名模板信息
				// 封装加密模板的表单信息
				CertTemplate certTemplate_new_enc = new CertTemplate();
				if (!certTemplate_sign.isSignal()) { 
				
					encry_Template.packageTempInfo(certTemplate_new_enc); // 如果是 新增,会在 tempAppendBasicInfo 中设置，如果是 修改,会在查询模板时设置
					if (isAdd) {
						tempAppendBasicInfo(certTemplate_new_enc);
					} else {
						basicTemplate.tempAppendBasicInfo(certTemplate_new_enc);
					}
					ArrayList<Extension> ex_List = certTemplate_new.getStandardExtensions(); // 同步 签名模板 某些项
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
				certTemplate_sign.setIsused("0");
				certTemplate_sign.setIsSysTemplate("1");
				certTemplate_new_enc.setIsused("0");
				certTemplate_new_enc.setIsSysTemplate("1");
				addOrUpdateTemplate(certTemplate_new, certTemplate_new_enc);

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
	 * @Desc 新增 或 修改模板, 查询数据库是否存在同名模板, 存在 就执行 更新操作,不存在就执行 新增 操作,
	 * @when 新增 和 修改
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午3:03:16
	 */
	private void addOrUpdateTemplate(CertTemplate certTemplate, CertTemplate certTemplate_enc) {
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
			log.errlog("Base64.decode(reqCert)", e1);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_Base64Decode") + e1.getMessage());
			mb.open();
		} catch (ServerException se) {
			log.errlog("Operating templates.", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_operationTemplate") + ":" + "[" + se.getErrorNum() + "]：" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("Operating templates.", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_operationTemplate") + ee.getMessage());
			mb.open();
		}
	}

	/**
	 * @Desc 查询证书模板， 并完成解析，获取 certTemplate ,调用 Signal_Template 和 Encry_Template显示
	 * @When 修改模板 操作中使用
	 * @Authod 江岩
	 * @Date 2019年3月7日 上午10:26:12
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
				certTemplate.setSignal(false); // 为了确定 下一步之后的视图页面，是否包含 加密模板
				template_enc = new String(Base64.decode(template_enc));
				CertTemplateParser.parser(templateName, template_enc, certTemplate_enc);
			}
		} catch (ServerException se) {
			log.errlog("View templates.", se);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_viewTemplate") + ":" + "[" + se.getErrorNum() + "]：" + se.getErrorMsg());
			mb.open();
		} catch (Exception ee) {
			log.errlog("View templates.", ee);
			MessageBox mb = new MessageBox(getShell(), SWT.ERROR);
			mb.setMessage(l.getString("Notice_fail_viewTemplate") + ee.getMessage());
			mb.open();
		}
	}

	/**
	 * @Desc 向 Server提交表单时，将 基本信息封装到 模板对象中(单双证都需要处理)
	 * @When 修改模板 操作中使用
	 * @Authod 江岩
	 * @Date 2019年3月20日 下午3:01:18
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
	 * 视图标题栏命名
	 * @param   (Shell)   
	 * @Author 江岩      
	 * @Time 2019-06-11 14:07
	 * @version 1.0
	 */
	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		if (certTemplate_sign.isAdd()) {
			shell.setText(l.getString("addTemplate"));
		} else if("view".equalsIgnoreCase(type)){
			shell.setText(l.getString("viewTemplate"));
		} else if("modify".equalsIgnoreCase(type)){
			shell.setText(l.getString("modifyTemplate"));
		} else if("clone".equalsIgnoreCase(type)){
			shell.setText(l.getString("cloneTemplate"));
		} else {
			shell.setText(l.getString(""));
		}
	}

}
