package cn.com.infosec.netcert.caAdmin.ui.admin;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**   
 * @Description (≤π≥‰√Ë ˆ) 
 * @Author Ω≠—“    
 * @Time 2019-10-11 17:57
 */
public class Notice_Running {



	public static void main(String[] args){
		
		JFrame frame = new JFrame();
		
		JLabel lable=new JLabel(new ImageIcon("res/Progress.gif"));
		frame.add(lable);
		frame.setBounds(100, 100, 500, 375);
		
		frame.setDefaultCloseOperation(JFrame.NORMAL);
		frame.setVisible(true);

		
		//frame.dispose();
	}


}
