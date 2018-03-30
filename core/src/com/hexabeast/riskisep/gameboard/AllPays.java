package com.hexabeast.riskisep.gameboard;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Inputs;
import com.hexabeast.riskisep.ressources.TextureManager;

public class AllPays {
	public static ArrayList<Pays> pays = new ArrayList<Pays>();
	
	
	public static void loadPays()
	{
		JsonReader json = new JsonReader();
		JsonValue base = json.parse(Gdx.files.internal("pays/risk.json"));

		//array objects in json if you would have more components
		for (JsonValue comp : base.get("regions"))
		{
			String nom = comp.getString("name").replaceAll("[^a-zA-Z]+","");
			TextureManager.loadOne(nom, "pays/"+nom+".png");
		    int w = Integer.parseInt(comp.getString("width"))/2;
		    int h = Integer.parseInt(comp.getString("height"))/2;
		    int x = Integer.parseInt(comp.getString("x"))/2-50;
		    int y = Integer.parseInt(comp.getString("y"))/2+50;
		    pays.add(new Pays(pays.size(),nom,x,GameScreen.camh-y-TextureManager.tex.get(nom).getHeight()/2,w,h));
		}

		FileHandle file = Gdx.files.internal("PaysLiens.txt");
		String text = file.readString();
		String[] liens = text.split("\r\n");
		
		for(int i = 0; i<liens.length; i++)
		{
			String[] payss = liens[i].split(":");
			Pays p1 = rechercheNom(payss[0].replaceAll("[^a-zA-Z]+",""));
			Pays p2 = rechercheNom(payss[1].replaceAll("[^a-zA-Z]+",""));
			
			if(!p1.adjacents.contains(p2) && !p2.adjacents.contains(p1))
			{
				p1.adjacents.add(p2);
				p2.adjacents.add(p1);
			}
			else
			{
				System.out.println("Erreur double lien "+liens[i]);
			}
		}
		
		rechercheNom("Australieouest").numberx-=20;
		rechercheNom("Australieest").numberx+=30;
		rechercheNom("Alaskaouest").numbery+=70;
		rechercheNom("Afriquest").numberx+=20;
		rechercheNom("Amazonie").numberx-=20;
		rechercheNom("Amazonie").numbery+=20;
		rechercheNom("Bresilouest").numbery-=10;
		rechercheNom("Bresil").numberx+=20;
		rechercheNom("Chili").numberx-=20;
		rechercheNom("Russiecentre").numberx+=15;
		rechercheNom("Russiecentre").numbery+=15;
	}
	
	public static Pays rechercheNom(String st)
	{
		for(int i=0; i<pays.size(); i++)
		{
			if(pays.get(i).nom.equals(st))return pays.get(i);
		}
		System.out.println("Erreur pays "+st);
		return null;
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
			if(selection != null)
			{
				if(Inputs.instance.rightpress)selection.update(2);
				else selection.update(1);
			}
			
		}
	}
	
	public static int paysTouched()
	{
		for(int i=0; i<pays.size(); i++)
		{
			if(pays.get(i).isTouched())return i;
		}
		return -1;
	}
	
}
