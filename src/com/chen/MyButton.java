package com.chen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;



class MyButton extends JPanel{//���Ƶ�ͼ�Լ�������Ӧ���㷨
	
	private static final int INFINITY = 9999;
	private ArrayList<MapData> data;
	private int num = 0;//��¼������
	private int task1Length =MyButton.INFINITY;
	private Graphics jg;
	//���������������������Ƶ���Ϣ���Է����ػ�
	private int type=0;
	Image image=null;
	private int task2StartPoint=0;
	private int task2SEndPoint=0;
	private int task1StartPoint=Integer.MAX_VALUE; 
	private ArrayList<Integer> task1Path;

	

	public MyButton(String pathname) {
		this.task1Path = new ArrayList<Integer>();
		ReadFile(pathname);//�����ļ�
	}
	
	//���û���
	public void setG() {
		this.jg = this.getGraphics();
	}
	
	//���ؾ�������������
	public String[] getPointName(){
 		String[] a =new String[this.data.size()];
 		for(int i=0;i<this.data.size();i++) {
 			a[i]=(this.data.get(i).getName());
 		}
		return a;	
 	}
	

	
	public void mission1(int startPoint) {
		if(this.task1StartPoint==startPoint) {//�Ѿ������·��
			
			if(type==1) {//�Ѿ����ƺ�ͼ��
				System.out.println("1");
			}
			else {
				type=1;
				showInit(this.jg);//��ʼ������ͼ
				task1draw(this.jg);
				System.out.println("2");
			}
		}
		else {
			this.type=1;
			this.task1StartPoint=startPoint;
			task1();//����·��
			showInit(this.jg);//��ʼ������ͼ
			task1draw(this.jg);//����·��
			System.out.println("3");
		}
	}
	
	
	public void mession2(int startPoint,int endPoint) {
		if(type==2&&this.task2StartPoint==startPoint&&this.task2SEndPoint==endPoint) {
			;
		}
		else {
			type=2;
			this.task2StartPoint=startPoint;
			this.task2SEndPoint=endPoint;
			showInit(this.jg);//��ʼ������ͼ
			task2(this.jg);
		}
	}
	
	
	//��������·���㷨
	private List<Route> shortestPath(Integer satartPoint) {
		int i = 0;
		List<Integer> p = new ArrayList<Integer>();
		List<Route> route = new ArrayList<Route>();
		for(;i<this.data.size();i++) {//��ʼ��
			Route temp = new Route();
			temp.pre=satartPoint;
			temp.length=this.data.get(satartPoint).getPath().get(i);
			route.add(temp);
			p.add(i);
		}
		
		for(;i>0;i--) {//
			int min=MyButton.INFINITY;
			int index=0;
			for(int j=0;j<p.size();j++) {
				if(route.get( p.get(j) ).length<min) {
					index=j;
					min=route.get( p.get(j) ).length;
				}
			}
			int point=p.remove(index);
			for(int j=0;j<p.size();j++) {
				if( (this.data.get(point).getPath().get(p.get(j))+route.get(point).length)<route.get(p.get(j)).length ) {
					route.get(p.get(j)).length= this.data.get(point).getPath().get(p.get(j))+route.get(point).length;
					route.get(p.get(j)).pre=point;
				}
			}
		}
		return route;
	}
	
	//�����
	private void task2(Graphics jg) {
		int minlength=MyButton.INFINITY;
		int index=0;
		List<Route> startRoute =new ArrayList<Route>();
		startRoute=shortestPath(task2StartPoint);
		List<Route> endRoute =new ArrayList<Route>();
		endRoute=shortestPath(task2SEndPoint);
		for(int i=0;i<this.num;i++) {
			if(startRoute.get(i).length+endRoute.get(i).length<minlength) {
				minlength=startRoute.get(i).length+endRoute.get(i).length;
				index = i;
			}
		}
		//����·��
		jg.setColor(Color.MAGENTA );//����·��Ϊ��ɫ
		int temp=index;
		Graphics2D g2=(Graphics2D) jg;
		while(index!=task2StartPoint) {
			jg.drawLine(this.data.get(index).getX(), this.data.get(index).getY(), this.data.get( startRoute.get(index).pre).getX(), this.data.get( startRoute.get(index).pre ).getY());
			index=startRoute.get(index).pre;
			g2.setStroke(new BasicStroke(2.6f));
		}
		index=temp;
		while(index!=task2SEndPoint) {
			jg.drawLine(this.data.get(index).getX(), this.data.get(index).getY(), this.data.get( endRoute.get(index).pre ).getX(), this.data.get( endRoute.get(index).pre ).getY());
			index=endRoute.get(index).pre;
			g2.setStroke(new BasicStroke(2.6f));
		}

	}
	
	//����1����·��
	private void task1draw(Graphics jg) {	
		jg.setColor(Color.MAGENTA);
		for(int i=0;i<this.task1Path.size()-1;i++) {
			jg.drawLine(this.data.get(this.task1Path.get(i)).getX(), this.data.get(this.task1Path.get(i)).getY(), this.data.get(this.task1Path.get(i+1)).getX(), this.data.get(this.task1Path.get(i+1)).getY());
			Graphics2D g2=(Graphics2D) jg;
			g2.setStroke(new BasicStroke(2.6f));
		}
	}
	//task1�ݹ麯��
 	private void fun2(ArrayList<Integer> path,int length,int n) {
 		
		if(n==this.num) {//�Ѿ��������о���
			int pl=this.data.get(path.get(path.size()-1)).getPath().get(this.task1StartPoint);
			if(pl<MyButton.INFINITY) {//������Իص����
				if(length+pl<this.task1Length) {//��·������
					this.task1Length=length+pl;
					this.task1Path.clear();
					for(int i=0;i<path.size();i++) {
						this.task1Path.add(path.get(i));
					}
					this.task1Path.add(this.task1StartPoint);
				}
			}
				for(int i=this.num;i<this.data.size();i++) {//ֻ��ȥ����
					int isGo=1;
					for(int j=path.size()-1;j>0&&path.get(j)>=this.num;j--) {
						if(i==path.get(j)) {//��������Ѿ�ȥ��
							isGo=0;
							break;
						}
					}
					if( this.data.get(path.get( path.size()-1) ).getPath().get(i)>=MyButton.INFINITY ) {
						isGo=0;//��ǰ�ڵ㲻��ȥ�����
						}
					if(isGo==1) {//���Թ�ȥ����ȥ
						ArrayList<Integer> temp =new ArrayList<Integer>();
						for(int k=0;k<path.size();k++) {
							temp.add(path.get(k));
						}
						temp.add(i);
						length+=this.data.get( path.get(path.size()-1) ).getPath().get(i);
						fun2(temp,length,n);
					}
				}
			return ;
		}else {//û������ȫ������
			for(int i=0;i<this.num;i++) {//ȥ��һ������
				int CanGo = 1;
				for(int j=0;j<path.size();j++) {
					if(i==path.get(j)) {//�Ѿ�ȥ��
						CanGo=0;//����ȥ
						break;
					}
				}//û��·��
				if( this.data.get(path.get( path.size()-1) ).getPath().get(i)>=MyButton.INFINITY ) {
					CanGo=0;//����ȥ
					}
				if(CanGo==1) {//��ȥ����ȥ
					ArrayList<Integer> temp =new ArrayList<Integer>();
					for(int k=0;k<path.size();k++) {
						temp.add(path.get(k));
					}
					temp.add(i);
					fun2(temp, length+this.data.get(path.get( path.size()-1) ).getPath().get(i),n+1);    
				}
			}
			for(int i=this.num;i<this.data.size();i++) {//��������
				int isGo=1;
				for(int j=path.size()-1;j>0&&path.get(j)>=this.num;j--) {
					if(i==path.get(j)) {//����ո������Ѿ�ȥ��
						isGo=0;
						break;
					}
				}
				if( this.data.get(path.get( path.size()-1) ).getPath().get(i)>=MyButton.INFINITY ) {
					isGo=0;//����ȥ
					}
				if(isGo==1) {//����ȥ����ȥ
					ArrayList<Integer> temp =new ArrayList<Integer>();
					for(int k=0;k<path.size();k++) {
						temp.add(path.get(k));
					}
					temp.add(i);
					length+=this.data.get( path.get(path.size()-1) ).getPath().get(i);
					fun2(temp,length,n);
				}
			}
		}
	}
 	//����1����·��
	private void task1() {
		this.task1Length=MyButton.INFINITY;//ÿ�μ���·�����뽲·�����ȳ�ʼ��Ϊ�����
		ArrayList<Integer> path =new ArrayList<Integer>();
		path.add(this.task1StartPoint);
		if(this.task1StartPoint<this.num) {
			fun2(path,0,1);
		}
		else {
			fun2(path,0,0);
		}
	}
 	
 	//��ʼ������ͼ
 	private void showInit(Graphics jg) {
		try {
			Thread.sleep(50);
			image=ImageIO.read(new File("C:\\Users\\chend\\Desktop\\demoProject\\Navigation\\bin\\res\\fangwei.jpg"));
			jg.drawImage(image, 863, 0, 130, 130, null);
		} catch (Exception e) {
			e.printStackTrace();
		}      

		jg.setFont(new Font("����", 0, 20));
		int k = this.data.size();//�ܵ���
		for(int i = 0;i<k;i++) {
			jg.setColor(Color.black );//·����Ȩֵ����Ϊ��ɫ
			for(int j = i+1;j<k;j++) {
				if(this.data.get(i).getPath().get(j)<MyButton.INFINITY)
				{
					//��·��
					jg.drawLine(this.data.get(i).getX(), this.data.get(i).getY(), this.data.get(j).getX(), this.data.get(j).getY());	
					//Ȩֵ
					jg.drawString(this.data.get(i).getPath().get(j).toString(),( this.data.get(i).getX()+this.data.get(j).getX())/2, (this.data.get(i).getY()+this.data.get(j).getY())/2);	
				}
			}
			if(this.data.get(i).getType()!=0)
			{
				jg.setColor(Color.blue );//������������Ϊ��ɫ
				jg.drawString(this.data.get(i).getName(), this.data.get(i).getX(), this.data.get(i).getY());
				try {
					Thread.sleep(50);
					image=ImageIO.read(new File("C:\\Users\\chend\\Desktop\\demoProject\\Navigation\\bin\\res\\jingdian.jpg"));
					jg.drawImage(image,this.data.get(i).getX(), this.data.get(i).getY(), 30, 35, null);
				} catch (Exception e) {
					e.printStackTrace();
				}      

			}
			else
			{
				jg.setColor(Color.RED );//������������Ϊ��ɫ
				jg.drawString(this.data.get(i).getName(), this.data.get(i).getX(), this.data.get(i).getY());
				try {
					Thread.sleep(50);
					image=ImageIO.read(new File("C:\\Users\\chend\\Desktop\\demoProject\\Navigation\\bin\\res\\sushe.jpg"));
					jg.drawImage(image,this.data.get(i).getX(), this.data.get(i).getY(), 45, 45, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
 	
	
 	//��ȡ�ļ�
	private void ReadFile(String pathname){
		this.data = new ArrayList<MapData>();		
		try { // ��ֹ�ļ��������ȡʧ�ܣ���catch��׽���󲢴�ӡ��Ҳ����throw
			/* ����TXT�ļ� */
			File file = new File(pathname); // Ҫ��ȡ����·����input.txt�ļ�
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file)); // ����һ������������reader
			BufferedReader br = new BufferedReader(reader); // ����һ�����������ļ�����ת�ɼ�����ܶ���������

			String line = new String();
			line = br.readLine();
			while (line != null) {
				
				String[] splited = line.split("\\s+");//�Կո� �ָ�
				
				MapData tempdata = new MapData();//������ʱ���󱣴��������
				
				tempdata.setName(splited[0]);
				tempdata.setType(Integer.parseInt(splited[1]));
				if(tempdata.getType()!=0) {
					this.num++;
				}
				tempdata.setX(Integer.parseInt(splited[2]));
				tempdata.setY(Integer.parseInt(splited[3]));
				for(int i=4;i<splited.length;i++)
				{
					tempdata.getPath().add( Integer.parseInt(splited[i]) );
				}
				this.data.add(tempdata);
				line = br.readLine(); // ������һ������
			}
			br.close();
			reader.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Override
    public void paint(Graphics g) {
		super.paint(g);	
		Graphics2D g2=(Graphics2D) g;
		g2.setStroke(new BasicStroke(2.6f));
		showInit(g2);
		if(this.type==1) {
			task1draw(g2);
		}
		if(this.type==2){
			task2(g2);
		}
		
	}
}
class Route {
	public Integer pre;//��һ��
	public Integer length;//�ӳ����㵽Ŀǰ���߹��ľ���
}