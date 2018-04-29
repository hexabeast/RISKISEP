package com.hexabeast.riskisep.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.ressources.TextureManager;

public class AllUI {
	public static ArrayList<Button> buttons = new ArrayList<Button>();
	public static Button turnButton;
	
	public static Color[] teamcol = {new Color(0.5f,0.5f,1,1),new Color(1,0.5f,0.5f,1),new Color(0.5f,1,0.5f,1),new Color(1,0.6f,0.5f,1)};
	public static String[] teamname = {"bleue","rouge","verte","orange"};
	public static String[] soldiername = {"Bleus","Rouges","Verts","Oranges"};
	public static String[] phases = {"Placement","Déplacement"};
	
	public static void loadUI()
	{
		turnButton = addButton(1870,60,300,100,Color.DARK_GRAY, "Fin placement");
	}
	
	public static Button addButton(float x, float y, float w, float h, Color col, String str)
	{
		Button tep = new Button(x,y,w,h,col,str);
		buttons.add(tep);
		return tep;
	}
	
	public static void update()
	{
		
		Main.batch.setColor(0.2f, 0.2f, 0.25f, 1);
		//Main.batch.draw(TextureManager.tex.get("panneau"),-20,-90);
		Main.batch.draw(TextureManager.tex.get("panneauv"),1750,-90);
		Main.batch.setColor(1,1,1,1);
		
		
		if(!GameScreen.master.gamend)
		{
			if(turnButton.isclicked() && GameScreen.master.human[GameScreen.master.teamactuel]==1)GameScreen.master.nextPhase();
			
			Main.drawfontCenter(TextureManager.fontButton, 2020, 1210, phases[GameScreen.master.phase]);
			TextureManager.fontButton.setColor(teamcol[GameScreen.master.teamactuel]);
			Main.drawfontCenter(TextureManager.fontButton, 2020, 1150, "Armée "+teamname[GameScreen.master.teamactuel]);
			TextureManager.fontButton.setColor(Color.WHITE);
		}
		else 
		{
			if(turnButton.isclicked())Main.game=new GameScreen();
			
			Main.drawfontCenter(TextureManager.fontButton, 2020, 1210, "Partie Terminée");
			TextureManager.fontButton.setColor(teamcol[GameScreen.master.winner]);
			Main.drawfontCenter(TextureManager.fontButton, 2020, 1150, teamname[GameScreen.master.winner]+" gagne!");
			TextureManager.fontButton.setColor(Color.WHITE);
		}
		
		
		for(int i=0;i<GameScreen.master.njoueurs;i++)
		{
			TextureManager.fontButton.setColor(teamcol[i]);
			Main.drawfontCenter(TextureManager.fontButton, 2020, 960-i*140, soldiername[i]+" libres: "+String.valueOf(GameScreen.master.armies.get(i).newsoldiers) );
			Main.drawfontCenter(TextureManager.fontButton, 2020, 900-i*140, soldiername[i]+" placés: "+String.valueOf(GameScreen.master.armies.get(i).soldiers.size()) );
			TextureManager.fontButton.setColor(Color.WHITE);
		}
		
		if(GameScreen.master.phase==0)turnButton.text="Fin placement";
		if(GameScreen.master.phase==1)turnButton.text="Fin déplacement";
		if(GameScreen.master.human[GameScreen.master.teamactuel]!=1)turnButton.text="Tour de l'IA...";
		if(GameScreen.master.gamend)turnButton.text="Recommencer";
		for(int i=0;i<buttons.size();i++)buttons.get(i).update();
	}
	
	public static boolean UIpretest()
	{
		if(turnButton.isclicked() || GameScreen.gameMouse.x>1850)return true;
		return false;
	}
}
