package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.Tools;
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
	
	private float biggify = 1.007f;
	
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
	
	
	
	public void update(int select, int currenteam)
	{
		//if(occupants.size()>0)team = occupants.get(0).team;
		
		/*if(select==3 && team==currenteam)
		{
			Shaders.setBlueShader();
			for(int i=0; i<adjacents.size(); i++)
			{
				if(adjacents.get(i).team==team)adjacents.get(i).update(0,currenteam);
			}
			Shaders.setDefaultShader();
		}*/
		if(select>=2)
		{
			
			for(int i=0; i<adjacents.size(); i++)
			{
				Shaders.setPaysTeamShader(adjacents.get(i).team,0.9f);
				adjacents.get(i).update(-1,currenteam);
				Shaders.setDefaultShader();
			}
		}
		if(select >= 1)
		{
				
			Shaders.setPaysTeamShader(team,1.5f);
			Main.batch.draw(TextureManager.tex.get(nom),x+(w-w*biggify)/2,y+(h-h*biggify)/2,w*biggify,h*biggify);
			Shaders.setDefaultShader();
		}
		else
		{
			if(select==0)Shaders.setPaysTeamShader(team,0);
			Main.batch.draw(TextureManager.tex.get(nom),x,y,w,h);
			Shaders.setDefaultShader();
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
		return TextureManager.isPixTouched(x,y,w,h,TextureManager.tex.get(nom),pixmap,xx,yy);
	}
	
	public boolean isTouched()
	{
	    return isTouched(GameScreen.gameMouse.x,GameScreen.gameMouse.y);
	}
	
	public ArrayList<Unite> getChallengers()
	{
		int size = Math.min(2, occupants.size());
		ArrayList<Unite> challengers = new ArrayList<Unite>();
		
		for(int i=0;i<occupants.size();i++)
		{
			for(int j=0;j<size;j++)
			{
				if(challengers.size()>j)
				{
					if(occupants.get(i).def<challengers.get(j).def)
					{
						challengers.add(j, occupants.get(i));
						if(challengers.size()>size)challengers.remove(challengers.size()-1);
						break;
					}
				}
				else
				{
					challengers.add(occupants.get(i));
					break;
				}
			}
		}
		for(int i=0;i<size;i++)
		{
			challengers.get(i).scoreactuel=challengers.get(i).puissance+Tools.lancerDe();
		}
		
		Collections.sort(challengers, new Comparator<Unite>() {
	        @Override
	        public int compare(Unite o1, Unite o2) {
	        	int basescore =o2.scoreactuel*1000-o1.scoreactuel*1000;
	        	int departage =o1.def-o2.def;
	            return basescore+departage;
	        }
	    });
		
		return challengers;
	}
	
	public Vector2 getRandomPos()
	{
		float posx = 0;
		float posy = 0;
		
		boolean dedans = false;
		while(!dedans)
		{
			dedans = true;
			posx=(float) (x+w*0.1f+Math.random()*w*0.8f);
			posy=(float) (y+h*0.1f+Math.random()*h*0.8f);
			if(!isTouched(posx,posy))dedans=false;
		}
		return new Vector2(posx,posy);
		
	}
	
	public int totalCost()
	{
		int cost = 0;
		for(int i=0;i<occupants.size(); i++)
		{
			cost+=occupants.get(i).cout;
		}
		return cost;
	}
	
	public void dispose()
	{
		pixmap.dispose();
	}
}
