package com.hexabeast.riskisep;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class Pays {
	public String nom = "";
	public int x=0;
	public int y=0;
	public int w=0;
	public int h=0;
	
	private float biggify = 1.02f;
	
	public Pays(String nom, int x, int y, int w, int h)
	{
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
	
	public boolean isTouched()
	{
		Rectangle spriteBounds = new Rectangle(x,y,w,h);
	    if (spriteBounds.contains(GameScreen.gameMouse.x,GameScreen.gameMouse.y)) {
	        Texture texture = TextureManager.tex.get(nom);

	        int spriteLocalX = (int) (GameScreen.gameMouse.x - x);
	        
	        int spriteLocalY = (int) ((GameScreen.gameMouse.y) -y);

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

}
