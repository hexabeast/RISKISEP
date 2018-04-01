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
		Color tcol = new Color(col);
		if(this.ishovered())
		{
			tcol.add(0.1f, 0.1f, 0.15f, 0);
		}
		if(this.ispressed())
		{
			tcol.add(0.2f, 0.2f, 0.25f, 0);
		}
		Main.drawRectangle(x, y, w, h, tcol);
		Main.drawfontCenter(TextureManager.fontButton, x+w/2, y+h/2, text);
	}
	
	public boolean isclicked()
	{
		if(Inputs.instance.leftmousedown && this.ishovered())
		{
			return true;
		}
		return false;
	}
	
	public boolean ishovered()
	{
		Rectangle rec = new Rectangle(x,y,w,h);
		if(rec.contains(GameScreen.gameMouse))
		{
			return true;
		}
		return false;
	}
	
	public boolean ispressed()
	{
		if(Inputs.instance.leftpress && this.ishovered())
		{
			return true;
		}
		return false;
	}
}
