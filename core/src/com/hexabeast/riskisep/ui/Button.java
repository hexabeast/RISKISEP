package com.hexabeast.riskisep.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
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
	Texture tex;
	
	public Button(float x, float y, float w, float h, Color col, String st)
	{
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.col=col;
		text=st;
	}
	public Button(float x, float y, float w, float h, Texture tex)
	{
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.tex=tex;
	}
	
	public void update()
	{
		update(GameScreen.gameMouse);
	}
	
	public void update(Vector2 mouse)
	{
		if(text!=null)
		{
			Color tcol = new Color(col);
			if(this.ishovered(mouse))
			{
				tcol.add(0.1f, 0.1f, 0.15f, 0);
			}
			if(this.ispressed(mouse))
			{
				tcol.add(0.2f, 0.2f, 0.25f, 0);
			}
			Main.drawRectangle(x, y, w, h, tcol);
			Main.drawfontCenter(TextureManager.fontButton, x+w/2, y+h/2, text);
		}
		else
		{
			Main.batch.draw(tex, x, y, w,h);
		}
		
	}
	
	public boolean isclicked()
	{
		return isclicked(GameScreen.gameMouse);
	}
	
	public boolean isclicked(Vector2 mouse)
	{
		if(Inputs.instance.leftmousedown && this.ishovered(mouse))
		{
			return true;
		}
		return false;
	}
	
	public boolean ishovered()
	{
		return ishovered(GameScreen.gameMouse);
	}
	
	public boolean ishovered(Vector2 mouse)
	{
		Rectangle rec = new Rectangle(x,y,w,h);
		if(rec.contains(mouse))
		{
			return true;
		}
		return false;
	}
	
	public boolean ispressed()
	{
		return ispressed(GameScreen.gameMouse);
	}
	
	public boolean ispressed(Vector2 mouse)
	{
		if(Inputs.instance.leftpress && this.ishovered(mouse))
		{
			return true;
		}
		return false;
	}
}
