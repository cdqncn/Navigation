package com.chen;

import java.util.ArrayList;

public class MapData {
	private  String name;
	private  int type;//1表示景点，0表示宿舍
	private  int x;//横坐标
	private  int y;//纵坐标
	private  ArrayList<Integer> path;//该点到每个点的距离，9999表示距离为不可达
	
	public MapData(){
		this.name=null;
		this.x=0;
		this.y=0;
		this.path=new ArrayList<Integer>();
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name=name;
	}
	public int getType(){
		return type;
	}
	public void setType(int type) {
		this.type=type;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x=x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y=y;
	}
	public ArrayList<Integer> getPath(){
		return path;
	}
	public void setPath(ArrayList<Integer> path) {
		this.path=path;
	}
}


