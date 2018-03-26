package com.hexabeast.riskisep;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class AllPays {
	private static ArrayList<Pays> pays = new ArrayList<Pays>();
	
	
	public static void loadPays()
	{
		JsonReader json = new JsonReader();
		JsonValue base = json.parse(Gdx.files.internal("pays/risk.json"));

		//array objects in json if you would have more components
		for (JsonValue comp : base.get("regions"))
		{
			String nom = comp.getString("name");
			TextureManager.loadOne(nom, "pays/"+nom+".png");
		    int w = Integer.parseInt(comp.getString("width"))/2;
		    int h = Integer.parseInt(comp.getString("height"))/2;
		    int x = Integer.parseInt(comp.getString("x"))/2;
		    int y = Integer.parseInt(comp.getString("y"))/2-30;
		    pays.add(new Pays(nom,x,GameScreen.camh-y-TextureManager.tex.get(nom).getHeight()/2,w,h));
		}
	}
	
	public static void update()
	{
		Pays selection = null;
		for(int i=0; i<pays.size(); i++)
		{
			if(pays.get(i).isTouched() && selection == null)
			{
				selection = pays.get(i);
			}
			else
			{
				pays.get(i).update(0);
			}
			if(selection != null)selection.update(1);
			
		}
	}
	
}
