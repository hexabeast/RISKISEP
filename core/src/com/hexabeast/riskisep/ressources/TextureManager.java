package com.hexabeast.riskisep.ressources;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class TextureManager { 
	private static ArrayList<Texture> textures = new ArrayList<Texture>();
	public static Map<String, Texture> tex = new HashMap<String, Texture>();
	
	public static Texture img;
	
	public static BitmapFont font;
	public static BitmapFont fontButton;
	public static GlyphLayout fontlayout;
	
	
	
	
	
	public static void loadOne(String name, String namefile)
	{
		Texture temp = new Texture(namefile);
		tex.put(name, temp);
		textures.add(temp);
	}
	
	public static void load()
	{
		loadOne("background","ocean.png");
		loadOne("soldierb","soldieriskblue.png");
		loadOne("soldierr","soldieriskred.png");
		loadOne("rond","rond.png");
		loadOne("rondr","rondr.png");
		loadOne("rondb","rondb.png");
		loadOne("panneau","panneaubois.png");
		loadOne("panneauv","panneauvert.png");
		loadOne("blank","blank.png");
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/cartoon.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 28;
		font = generator.generateFont(parameter);
		parameter.size = 35;
		fontButton = generator.generateFont(parameter);
		generator.dispose();
		
		fontlayout = new GlyphLayout();
		
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
