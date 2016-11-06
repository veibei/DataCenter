package com.songxu.ui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;

import com.songxu.communication.Record;
import com.songxu.communication.RecordThread;
import com.songxu.interfaces.CommonUtil;
import com.songxu.memecached.MemecachedOperate;
import com.songxu.mina.client.MinaClient;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class Main extends JFrame {

	private JPanel contentPane;
	private JTextField txt_IPAddr;
	private JLabel label;
	private JTextField txt_Port;
	private JLabel label_1;
	private JTextField txt_Count;
	public static  JTextArea txtLog =null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		Thread thread=new Thread(new RecordThread());
		thread.start();
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setTitle("\u6570\u636E\u4E2D\u5FC3\u6A21\u62DF\u5BA2\u6237\u7AEF");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 457, 572);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblIp = new JLabel("I P");
		lblIp.setFont(new Font("宋体", Font.PLAIN, 14));
		lblIp.setBounds(25, 23, 22, 33);
		contentPane.add(lblIp);
		
		txt_IPAddr = new JTextField();
		txt_IPAddr.setText("172.20.202.33");
		txt_IPAddr.setBounds(99, 29, 189, 21);
		contentPane.add(txt_IPAddr);
		txt_IPAddr.setColumns(10);
		
		label = new JLabel("\u7AEF\u53E3\u53F7");
		label.setFont(new Font("宋体", Font.PLAIN, 14));
		label.setBounds(25, 61, 48, 33);
		contentPane.add(label);
		
		txt_Port = new JTextField();
		txt_Port.setText("50116");
		txt_Port.setColumns(10);
		txt_Port.setBounds(99, 67, 189, 21);
		contentPane.add(txt_Port);
		
		label_1 = new JLabel("\u6570 \u91CF");
		label_1.setFont(new Font("宋体", Font.PLAIN, 14));
		label_1.setBounds(25, 104, 48, 33);
		contentPane.add(label_1);
		
		txt_Count = new JTextField();
		txt_Count.setBounds(99, 110, 66, 21);
		contentPane.add(txt_Count);
		txt_Count.setColumns(10);
		
		JRadioButton r_Client = new JRadioButton("\u5BA2\u6237\u7AEF");
		r_Client.setBounds(194, 109, 66, 23);
		contentPane.add(r_Client);
		r_Client.setSelected(true);
		final JRadioButton r_DTU = new JRadioButton("DTU");
		r_DTU.setBounds(273, 109, 58, 23);
		contentPane.add(r_DTU);
		
		 ButtonGroup group = new ButtonGroup();// 创建单选按钮组
		 
		 group.add(r_Client);
		 group.add(r_DTU);
		 
		  txtLog = new JTextArea();
			contentPane.add(txtLog);
			txtLog.setEditable(false);
			txtLog.setBounds(27, 53, 392, 286);
			txtLog.setLineWrap(true); 
			JScrollPane scrollPane = new JScrollPane(txtLog);
			scrollPane.setBounds(25, 141, 405, 302);
			contentPane.add(scrollPane);	
			
		
		JButton button_Connect = new JButton("\u8FDE\u63A5");
		
		
		
		
		
		
		
		/**
		 * 连接事件
		 */
		button_Connect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{
				int c_Count=0;
				//连接数量
				try {
					c_Count=new Integer(txt_Count.getText());
				} catch (Exception e2) {
					return;
				}
				
				//连接类型
				char type='3';
				
				String ip=txt_IPAddr.getText();
				int port=new Integer(txt_Port.getText());
				
				
				
				if(r_DTU.isSelected())
				{
					type='2';
					System.out.println("DTU端启动。。。");
				}
				else {
					System.out.println("客户端启动。。。");
				}
				List<String> imei_collection=CommonUtil.generalIMEI(c_Count, type);
				txtLog.append(System.currentTimeMillis()+":注册码生成完毕!"+"\n==================\n");
				for (String string : imei_collection) 
				{
					
					txtLog.append("abc:"+string+"\n");
					Record.getInstance().add(string);
					IoSession ioSession=MinaClient.getConnect(ip,port);
					if(null==ioSession)
					{
						JOptionPane.showMessageDialog(contentPane, "远程主机尚未开机");
					}
					
					
				}
				
				/*IoSession ioSession=MinaClient.getConnect(ip,port);
				if(null==ioSession)
				{
					JOptionPane.showMessageDialog(contentPane, "远程主机尚未开机");
				}*/
				//ioSession.write("123");
				
				
				
				
				
				
				
				
				
				
			}
		});
		
		
		
		
		
		button_Connect.setBounds(337, 109, 93, 23);
		contentPane.add(button_Connect);
		
		
		JButton button_Lazy = new JButton("\u6D4B\u8BD5\u5EF6\u8FDF");
		
		
		/*
		 * 测试延迟
		 * 
		 * */
		button_Lazy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				
				
				
			}
		});
		
		button_Lazy.setBounds(25, 443, 93, 23);
		contentPane.add(button_Lazy);
		
		JTextArea txt_Lazy = new JTextArea();
		txt_Lazy.setEditable(false);
		txt_Lazy.setBounds(25, 476, 405, 48);
		contentPane.add(txt_Lazy);
		
		JScrollPane scrollPane_1 = new JScrollPane(txt_Lazy);
		scrollPane_1.setBounds(25, 469, 405, 55);
		contentPane.add(scrollPane_1);
		
		JButton button = new JButton("\u6E05\u9664\u7F13\u5B58");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MemecachedOperate.remove("DTU");
				MemecachedOperate.remove("client");
			}
		});
		button.setBounds(337, 80, 93, 23);
		contentPane.add(button);
		
		
		
		
	}
}
