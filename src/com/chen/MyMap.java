package com.chen;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;



public class MyMap {
	
	public JFrame Jf;//主体框
	public JButton Jb1;//起点按钮
	public JButton Jb2;//终点按钮
	public JLabel Jl1;//"起点"标识符
	public JLabel Jl2;//"终点"标识符
	public JComboBox<String> JCb1;//"导航功能"的起点
	public JComboBox<String> JCb2;//"导航功能"的终点
	public MyButton MJp;//"地图"
	public JPanel Jp;//承载按钮以及标签的panel
	

	public MyMap(String pathname) throws IOException{
		this.MJp = new MyButton(pathname);//实例化地图
		
		
		this.Jb1 = new JButton("环游");
		Font h1=new Font("宋体", Font.CENTER_BASELINE, 20);
		Jb1.setFont(h1);
		Jb1.setPreferredSize(new Dimension(140,50));
		Jb1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MJp.mission1(JCb1.getSelectedIndex());
			}
		});
		
		this.Jl1 = new JLabel("出发点");
		Font f1=new Font("宋体", Font.BOLD, 25);
		Jl1.setFont(f1);
		Jl1.setPreferredSize(new Dimension(140,50));
		
		this.JCb1=new JComboBox<String>(MJp.getPointName());
		JCb1.setPreferredSize(new Dimension(140,50));
		Font d1=new Font("宋体",Font.BOLD, 18);
		JCb1.setFont(d1);

		this.Jl2 = new JLabel("目的地");
		Font f2=new Font("宋体", Font.BOLD, 25);
		Jl2.setFont(f2);
		Jl2.setPreferredSize(new Dimension(140,50));

		this.JCb2=new JComboBox<String>(MJp.getPointName());
		JCb2.setPreferredSize(new Dimension(140,50));
		Font d2=new Font("宋体",Font.BOLD, 18);
		JCb2.setFont(d2);

				
		this.Jb2 = new JButton("导航");
		Font h2=new Font("宋体", Font.CENTER_BASELINE, 20);
		Jb2.setFont(h2);
		Jb2.setPreferredSize(new Dimension(140,50));
		Jb2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MJp.mession2(JCb1.getSelectedIndex(), JCb2.getSelectedIndex());
			}
			
		});
		
		this.Jp = new JPanel();
		Jp.setPreferredSize(new Dimension(141, 50));
		Jp.setBackground(Color.YELLOW);
		this.Jp.add(Jl1);
		this.Jp.add(JCb1);
		this.Jp.add(Jl2);
		this.Jp.add(JCb2);
		this.Jp.add(Jb1);
		this.Jp.add(Jb2);
		
		
		this.Jf = new JFrame("HUST-校园导航系统");
		
		Jf.setBounds(250, 120, 1150, 800);
		Jf.setLayout(new BorderLayout());   
		Jf.setResizable(false);//不可调大小
		Jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Jf.add(Jp,BorderLayout.WEST);
		Jf.add(MJp,BorderLayout.CENTER);
				
		Jf.setVisible(true);
		
		MJp.setG();
	}

	
	public static void main(String[] args) throws IOException 
	{
		new MyMap("C:\\Users\\chend\\Desktop\\MySW\\HUSTMap.txt");
	}
}
