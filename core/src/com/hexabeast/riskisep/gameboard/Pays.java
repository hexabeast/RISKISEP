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
	
	public int team = 0;
	public ArrayList<Soldier> occupants = new ArrayList<Soldier>();
	
	private float biggify = 1.02f;
	
	public Pays(int id,String nom, int x, int y, int w, int h)
	{
		this.id=id;
		this.x=x;
		this.y=y;
		this.w=w;
		this.h=h;
		this.nom=nom;
	}
	
	public void update(int select)
	{
		if(select == 1)
		{
			Shaders.setGreenShader();
			Main.batch.draw(TextureManager.tex.get(nom),x+(w-w*biggify)/2,y+(h-h*biggify)/2,w*biggify,h*biggify);
			Shaders.setDefaultShader();
		}
		else
		{
			Main.batch.draw(TextureManager.tex.get(nom),x,y,w,h);
		}
	}
	
	public boolean isTouched(float xx, float yy)
	{
		Rectangle spriteBounds = new Rectangle(x,y,w,h);
	    if (spriteBounds.contains(xx,yy)) {
	        Texture texture = TextureManager.tex.get(nom);

	        int spriteLocalX = (int) (xx - x);
	        
	        int spriteLocalY = (int) ((yy) -y);

	        int textureLocalX = spriteLocalX*2;
	        int textureLocalY = spriteLocalY*2;

	        if (!texture.getTextureData().isPrepared()) {
	            texture.getTextureData().prepare();
	        }
	        Pixmap pixmap = texture.getTextureData().consumePixmap();
	        Color col = new Color(pixmap.getPixel(textureLocalX, texture.getHeight()-textureLocalY));
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
		int rotations = 0;
		while(!dedans)
		{
			rotations+=1;
			dedans = true;
			posx=(float) (x+Math.random()*w);
			posy=(float) (y+Math.random()*h);
			if(!isTouched(posx,posy))dedans=false;
			else
			{
				if(occupants.size()<10)
				{
					Vector2 posvec = new Vector2(posx,posy);
					for(int i=0; i<occupants.size(); i++)
					{
						float otherx = occupants.get(i).x;
						float othery = occupants.get(i).y;
						if(new Vector2(otherx,othery).dst(posvec)<(w+h/4)/rotations)dedans=false;
					}
				}
				
			}
		}
		return new Vector2(posx,posy);
		
	}
}
