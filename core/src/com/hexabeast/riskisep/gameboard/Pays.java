package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public class Pays {
	public String nom = "";
	public int x=0;
	public int y=0;
	public int w=0;
	public int h=0;
	public int id=0;
	
	public float numberx;
	public float numbery;
	
	public int team = -1;
	public ArrayList<Unite> occupants = new ArrayList<Unite>();
	
	public ArrayList<Pays> adjacents = new ArrayList<Pays>();
	
	Pixmap pixmap;
	
	private float biggify = 1.02f;
	
	public Pays(int id,String nom, int x, int y, int w, int h)
	{
		this.id=id;
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		numberx = x+w/2;
		numbery = y+h/2;
		this.nom=nom;
		pixmap = TextureManager.loadPixMap(nom);
		
	}
	
	
	
	public void update(int select)
	{
		//if(occupants.size()>0)team = occupants.get(0).team;
		
		if(select==3)
		{
			Shaders.setBlueShader();
			for(int i=0; i<adjacents.size(); i++)
			{
				if(adjacents.get(i).team==team)adjacents.get(i).update(0);
			}
			Shaders.setDefaultShader();
		}
		if(select==2)
		{
			Shaders.setRedShader();
			for(int i=0; i<adjacents.size(); i++)
			{
				if(adjacents.get(i).team!=team)adjacents.get(i).update(0);
			}
			Shaders.setDefaultShader();
		}
		if(select >= 1)
		{
				
			Shaders.setGreenShader();
			Main.batch.draw(TextureManager.tex.get(nom),x+(w-w*biggify)/2,y+(h-h*biggify)/2,w*biggify,h*biggify);
			Shaders.setDefaultShader();
		}
		else
		{
			Main.batch.draw(TextureManager.tex.get(nom),x,y,w,h);
		}
		
		/*
		if(occupants.size()==0)
		Main.batch.draw(TextureManager.tex.get("rond"),numberx-25,numbery-25,50,50);
		else if(team == 0)Main.batch.draw(TextureManager.tex.get("rondb"),numberx-25,numbery-25,50,50);
		else if(team == 1)Main.batch.draw(TextureManager.tex.get("rondr"),numberx-25,numbery-25,50,50);
		
		Main.drawfontCenter(TextureManager.font, numberx, numbery, String.valueOf(occupants.size()));
		*/
		}
	
	public boolean isTouched(float xx, float yy)
	{
		Rectangle spriteBounds = new Rectangle(x,y,w,h);
	    if (spriteBounds.contains(xx,yy)) {
	        

	        int spriteLocalX = (int) (xx - x);
	        
	        int spriteLocalY = (int) ((yy) -y);

	        int textureLocalX = spriteLocalX*2;
	        int textureLocalY = spriteLocalY*2;
	        
	        Color col = new Color(pixmap.getPixel(textureLocalX, TextureManager.tex.get(nom).getHeight()-textureLocalY));
	        if(col.a<0.5)return false;
	        return true;
	    }
	    return false;
	}
	
	public boolean isTouched()
	{
	    return isTouched(GameScreen.gameMouse.x,GameScreen.gameMouse.y);
	}
	
	public Vector2 getRandomPos()
	{
		float posx = 0;
		float posy = 0;
		
		boolean dedans = false;
		while(!dedans)
		{
			dedans = true;
			posx=(float) (x+Math.random()*w);
			posy=(float) (y+Math.random()*h);
			if(!isTouched(posx,posy))dedans=false;
		}
		return new Vector2(posx,posy);
		
	}
	
	public void dispose()
	{
		pixmap.dispose();
	}
}
