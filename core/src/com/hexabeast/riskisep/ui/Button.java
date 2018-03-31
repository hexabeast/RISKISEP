package com.hexabeast.riskisep.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Inputs;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.ressources.TextureManager;

public class Button {
	public float x;
	public float y;
	public float w;
	public float h;
	Color col;
	String text;
	
	public Button(float x, float y, float w, float h, Color col, String st)
	{
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.col=col;
		text=st;
	}
	
	public void update()
	{
		Main.drawRectangle(x, y, w, h, col);
		Main.drawfontCenter(TextureManager.fontButton, x+w/2, y+h/2, text);
	}
	
	public boolean isclicked()
	{
		Rectangle rec = new Rectangle(x,y,w,h);
		if(Inputs.instance.leftmousedown && rec.contains(GameScreen.gameMouse))
		{
			return true;
		}
		return false;
	}
}
