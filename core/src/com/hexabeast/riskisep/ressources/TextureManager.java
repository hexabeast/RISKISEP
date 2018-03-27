package com.hexabeast.riskisep.ressources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Texture;

public class TextureManager {
	private static ArrayList<Texture> textures = new ArrayList<Texture>();
	public static Map<String, Texture> tex = new HashMap<String, Texture>();
	
	public static Texture img;
	
	public static void loadOne(String name, String namefile)
	{
		Texture temp = new Texture(namefile);
		tex.put(name, temp);
		textures.add(temp);
	}
	
	public static void load()
	{
		loadOne("background","background.png");
		loadOne("soldierb","soldieriskblue.png");
		loadOne("soldierr","soldieriskred.png");
	}
	
	public static Texture getsoldiertex(int team)
	{
		if(team==0)return tex.get("soldierr");
		else return tex.get("soldierb");
	}
	
	public static void dispose()
	{
		for(int i=0; i<textures.size(); i++)
		{
			textures.get(i).dispose();
		}
	}
}
