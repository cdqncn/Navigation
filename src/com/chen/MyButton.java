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



class MyButton extends JPanel{//绘制地图以及表达出相应的算法
	
	private static final int INFINITY = 9999;
	private ArrayList<MapData> data;
	private int num = 0;//记录景点数
	private int task1Length =MyButton.INFINITY;
	private Graphics jg;
	//下面五个参数用来保存绘制的信息，以方便重画
	private int type=0;
	Image image=null;
	private int task2StartPoint=0;
	private int task2SEndPoint=0;
	private int task1StartPoint=Integer.MAX_VALUE; 
	private ArrayList<Integer> task1Path;

	

	public MyButton(String pathname) {
		this.task1Path = new ArrayList<Integer>();
		ReadFile(pathname);//加载文件
	}
	
	//设置画笔
	public void setG() {
		this.jg = this.getGraphics();
	}
	
	//返回景点和宿舍的名字
	public String[] getPointName(){
 		String[] a =new String[this.data.size()];
 		for(int i=0;i<this.data.size();i++) {
 			a[i]=(this.data.get(i).getName());
 		}
		return a;	
 	}
	

	
	public void mission1(int startPoint) {
		if(this.task1StartPoint==startPoint) {//已经计算好路径
			
			if(type==1) {//已经绘制好图像
				System.out.println("1");
			}
			else {
				type=1;
				showInit(this.jg);//初始化拓扑图
				task1draw(this.jg);
				System.out.println("2");
			}
		}
		else {
			this.type=1;
			this.task1StartPoint=startPoint;
			task1();//计算路径
			showInit(this.jg);//初始化拓扑图
			task1draw(this.jg);//绘制路径
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
			showInit(this.jg);//初始化拓扑图
			task2(this.jg);
		}
	}
	
	
	//任务二最短路径算法
	private List<Route> shortestPath(Integer satartPoint) {
		int i = 0;
		List<Integer> p = new ArrayList<Integer>();
		List<Route> route = new ArrayList<Route>();
		for(;i<this.data.size();i++) {//初始化
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
	
	//任务二
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
		//画出路径
		jg.setColor(Color.MAGENTA );//设置路线为紫色
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
	
	//任务1绘制路径
	private void task1draw(Graphics jg) {	
		jg.setColor(Color.MAGENTA);
		for(int i=0;i<this.task1Path.size()-1;i++) {
			jg.drawLine(this.data.get(this.task1Path.get(i)).getX(), this.data.get(this.task1Path.get(i)).getY(), this.data.get(this.task1Path.get(i+1)).getX(), this.data.get(this.task1Path.get(i+1)).getY());
			Graphics2D g2=(Graphics2D) jg;
			g2.setStroke(new BasicStroke(2.6f));
		}
	}
	//task1递归函数
 	private void fun2(ArrayList<Integer> path,int length,int n) {
 		
		if(n==this.num) {//已经遍历所有景点
			int pl=this.data.get(path.get(path.size()-1)).getPath().get(this.task1StartPoint);
			if(pl<MyButton.INFINITY) {//如果可以回到起点
				if(length+pl<this.task1Length) {//且路径更短
					this.task1Length=length+pl;
					this.task1Path.clear();
					for(int i=0;i<path.size();i++) {
						this.task1Path.add(path.get(i));
					}
					this.task1Path.add(this.task1StartPoint);
				}
			}
				for(int i=this.num;i<this.data.size();i++) {//只能去宿舍
					int isGo=1;
					for(int j=path.size()-1;j>0&&path.get(j)>=this.num;j--) {
						if(i==path.get(j)) {//这个宿舍已经去过
							isGo=0;
							break;
						}
					}
					if( this.data.get(path.get( path.size()-1) ).getPath().get(i)>=MyButton.INFINITY ) {
						isGo=0;//当前节点不能去这个点
						}
					if(isGo==1) {//可以过去，则去
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
		}else {//没有走完全部景点
			for(int i=0;i<this.num;i++) {//去下一个景点
				int CanGo = 1;
				for(int j=0;j<path.size();j++) {
					if(i==path.get(j)) {//已经去过
						CanGo=0;//不能去
						break;
					}
				}//没有路径
				if( this.data.get(path.get( path.size()-1) ).getPath().get(i)>=MyButton.INFINITY ) {
					CanGo=0;//不能去
					}
				if(CanGo==1) {//能去，则去
					ArrayList<Integer> temp =new ArrayList<Integer>();
					for(int k=0;k<path.size();k++) {
						temp.add(path.get(k));
					}
					temp.add(i);
					fun2(temp, length+this.data.get(path.get( path.size()-1) ).getPath().get(i),n+1);    
				}
			}
			for(int i=this.num;i<this.data.size();i++) {//经过宿舍
				int isGo=1;
				for(int j=path.size()-1;j>0&&path.get(j)>=this.num;j--) {
					if(i==path.get(j)) {//这个刚刚宿舍已经去过
						isGo=0;
						break;
					}
				}
				if( this.data.get(path.get( path.size()-1) ).getPath().get(i)>=MyButton.INFINITY ) {
					isGo=0;//不能去
					}
				if(isGo==1) {//可以去，则去
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
 	//任务1计算路径
	private void task1() {
		this.task1Length=MyButton.INFINITY;//每次计算路径必须讲路径长度初始化为无穷大
		ArrayList<Integer> path =new ArrayList<Integer>();
		path.add(this.task1StartPoint);
		if(this.task1StartPoint<this.num) {
			fun2(path,0,1);
		}
		else {
			fun2(path,0,0);
		}
	}
 	
 	//初始化拓扑图
 	private void showInit(Graphics jg) {
		try {
			Thread.sleep(50);
			image=ImageIO.read(new File("C:\\Users\\chend\\Desktop\\demoProject\\Navigation\\bin\\res\\fangwei.jpg"));
			jg.drawImage(image, 863, 0, 130, 130, null);
		} catch (Exception e) {
			e.printStackTrace();
		}      

		jg.setFont(new Font("宋体", 0, 20));
		int k = this.data.size();//总点数
		for(int i = 0;i<k;i++) {
			jg.setColor(Color.black );//路径和权值设置为黑色
			for(int j = i+1;j<k;j++) {
				if(this.data.get(i).getPath().get(j)<MyButton.INFINITY)
				{
					//画路径
					jg.drawLine(this.data.get(i).getX(), this.data.get(i).getY(), this.data.get(j).getX(), this.data.get(j).getY());	
					//权值
					jg.drawString(this.data.get(i).getPath().get(j).toString(),( this.data.get(i).getX()+this.data.get(j).getX())/2, (this.data.get(i).getY()+this.data.get(j).getY())/2);	
				}
			}
			if(this.data.get(i).getType()!=0)
			{
				jg.setColor(Color.blue );//景点名字字体为蓝色
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
				jg.setColor(Color.RED );//宿舍名字字体为黑色
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
 	
	
 	//读取文件
	private void ReadFile(String pathname){
		this.data = new ArrayList<MapData>();		
		try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
			/* 读入TXT文件 */
			File file = new File(pathname); // 要读取以上路径的input.txt文件
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(file)); // 建立一个输入流对象reader
			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言

			String line = new String();
			line = br.readLine();
			while (line != null) {
				
				String[] splited = line.split("\\s+");//以空格 分割
				
				MapData tempdata = new MapData();//创建临时对象保存改行数据
				
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
				line = br.readLine(); // 读入下一行数据
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
	public Integer pre;//上一跳
	public Integer length;//从出发点到目前的走过的距离
}