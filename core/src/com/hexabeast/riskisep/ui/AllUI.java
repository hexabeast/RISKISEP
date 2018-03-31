package com.hexabeast.riskisep.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

public class AllUI {
	public static ArrayList<Button> buttons = new ArrayList<Button>();
	
	public static void loadUI()
	{
		addButton(1800,100,200,50,Color.WHITE, "Fin placement");
	}
	
	public static void addButton(float x, float y, float w, float h, Color col, String str)
	{
		buttons.add(new Button(x,y,w,h,col,str));
	}
	
	public static void update()
	{
		for(int i=0;i<buttons.size();i++)buttons.get(i).update();
	}
}
