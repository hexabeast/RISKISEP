package com.hexabeast.riskisep.gameboard;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public class AllPays {
	public ArrayList<Pays> pays = new ArrayList<Pays>();
	public ArrayList<PaysHighlight> highlights = new ArrayList<PaysHighlight>();
	public Pays selection = null;
	public int selectiontype = 1;
	
	public ArrayList<Continent> continents = new ArrayList<Continent>();
	
	public void loadPays()
	{
		pays.clear();
		JsonReader json = new JsonReader();
		JsonValue base = json.parse(Gdx.files.internal("pays/risk.json"));

		//array objects in json if you would have more components
		for (JsonValue comp : base.get("regions"))
		{
			String nom = comp.getString("name").replaceAll("[^a-zA-Z]+","");
			//TextureManager.loadOne(nom, "pays/"+nom+".png");
		    int w = Integer.parseInt(comp.getString("width"))/2;
		    int h = Integer.parseInt(comp.getString("height"))/2;
		    int x = Integer.parseInt(comp.getString("x"))/2-50;
		    int y = Integer.parseInt(comp.getString("y"))/2+80;
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
		
		file = Gdx.files.internal("Continents.txt");
		text = file.readString();
		String[] conts = text.split("\r\n");
		
		for(int i = 0; i<conts.length; i++)
		{
			String[] sep = conts[i].split(";");
			String[] payss = sep[1].split(":");
			int[] paysids = new int[payss.length];
			
			for(int j = 0; j<payss.length; j++)
			{
				paysids[j] = rechercheNom(payss[j]).id;
			}
			continents.add(new Continent(sep[0],paysids));
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
	
	
	public Pays rechercheNom(String st)
	{
		for(int i=0; i<pays.size(); i++)
		{
			if(pays.get(i).nom.equals(st))return pays.get(i);
		}
		System.out.println("Erreur pays "+st);
		return null;
	}
	
	public void addHighlight(int id)
	{
		highlights.add(new PaysHighlight(pays.get(id)));
	}
	
	public void update(int currenteam)
	{
		
		for(int i=0; i<pays.size(); i++)
		{
			boolean highlight = false;
			PaysHighlight phl = null;
			for(int j=highlights.size()-1; j>=0;j--)
			{
				if(pays.get(i)==highlights.get(j).p)
				{
					
					highlights.get(j).decay();
					if(highlights.get(j).transparency<=0)
					{
						highlights.remove(j);
					}
					else 
					{
						highlight=true;
						phl = highlights.get(j);
					}
				}
			}
			
			if(selection != pays.get(i))
			{
				if(highlight)
				{
					Shaders.setPaysTeamShader(phl.p.team,phl.transparency);
					phl.p.update(-1,currenteam);
				}
				else
				{
					pays.get(i).update(0,currenteam);
				}
			}
			
		}
		if(selection != null)
		{
			selection.update(selectiontype,currenteam);
		}
	}
	
	public int comptePaysTeam(int t)
	{
		int total=0;
		for(int i=0; i<pays.size(); i++)
		{
			if(pays.get(i).team==t)total+=1;
		}
		return total;
	}
	public int paysTouched()
	{
		for(int i=0; i<pays.size(); i++)
		{
			if(pays.get(i).isTouched())return i;
		}
		return -1;
	}
	
}
