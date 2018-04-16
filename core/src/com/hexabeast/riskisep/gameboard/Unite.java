package com.hexabeast.riskisep.gameboard;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public abstract class Unite {
	public int id;
	public int team = 0;
	public int pays = 0;
	public int type=-1;
	public int cout = 0;
	public int mvt = 0;
	public int att = 0;
	public int def = 0;
	public int puissance = 0;
	public Texture tex;
	public Pixmap pixmap;
	
	public float x;
	public float y;
	
	public float fx;
	public float fy;
	
	public float h = 50;
	public float w;
	
	public float centery = 15;
	
	public int scoreactuel = 0;
	public int mvtactuel = 0;
	
	public float graphicmoveslow = 2;
	public float graphicmovefast = 150;
	public boolean running = false;
	
	public Unite(int id, int pays, int team)
	{
		this.id=id;
		this.pays=pays;
		this.team=team;
		randomizePos();
		this.x=this.fx;
		this.y=this.fy;
    }
	
	public void randomizePos()
	{
		Vector2 pos = AllPays.pays.get(pays).getRandomPos();
		this.fx = pos.x;
		this.fy = pos.y;
		running=true;
	}
	
	public void randomizePosChill()
	{
		randomizePos();
		running=false;
	}
	
	public void init()
	{
		w = h*tex.getWidth()/tex.getHeight();
		pixmap = TextureManager.unitePixmap.get(type);
	}
	
	public void update(int type)
	{
		float movespeed = graphicmovefast;
		if(!running)movespeed = graphicmoveslow;
		Vector2 distance = new Vector2(this.fx-this.x, this.fy-this.y);
		Vector2 velocity = new Vector2(distance);
		if(velocity.len()>20 || !running)velocity.nor();
		else {
			velocity.scl(1f/20f);
		}
		x+=movespeed*velocity.x*Main.delta;
		y+=movespeed*velocity.y*Main.delta;
		
		if(distance.len()<3)randomizePosChill();
		
		if(type==0)Shaders.setSoldierTeamShader(team);
		else if(type==1)Shaders.setWhiteTeam();
		Main.batch.draw(tex, x-w/2, y-centery, w,h);
		Shaders.setDefaultShader();
	}
	
	public void update()
	{
		update(0);
	}
	
	public void updatehighlight()
	{
		update(1);
	}
	
	public boolean isTouched(float xx, float yy)
	{
		return TextureManager.isPixTouched(x-w/2,y-centery,w,h,tex,pixmap,xx,yy);
	}
}
