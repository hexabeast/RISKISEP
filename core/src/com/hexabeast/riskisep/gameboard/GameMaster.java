package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Inputs;
import com.hexabeast.riskisep.Main;
import com.hexabeast.riskisep.Tools;
import com.hexabeast.riskisep.ia.IASimple;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public class GameMaster {
	public ArrayList<Army> armies = new ArrayList<Army>();
	public ArrayList<IASimple> ias = new ArrayList<IASimple>();
	public Map<String, Unite> soldiersmap = new HashMap<String, Unite>();
	
	public static Color[] teamcol = {new Color(0,0,1,1),new Color(1,0,0,1),new Color(0,1,0,1),new Color(1,1,0,1)};
	
	public int curid = 0;
	
	public int njoueurs = 2;
	public int teamactuel = 0;
	public int phase = 0;
	public int[] human = {1,1};
	
	public ArrayList<Unite> selectedUnits = new ArrayList<Unite>();
	public int lastPays = -1;
	
	public GameMaster()
	{
		
	}
	
	public void beginGame(int njoueurs)
	{
		for(int i=0;i<njoueurs;i++)
		{
			armies.add(new Army(i));
		}
		
		for(int i=0;i<njoueurs;i++)
		{
			ias.add(new IASimple(i));
		}
		this.njoueurs = njoueurs;
		AllPays.selection=null;
		
		ArrayList<Pays> rlist = new ArrayList<Pays>();
		rlist.addAll(AllPays.pays);
		Collections.shuffle(rlist);
		for(int i=0;i<AllPays.pays.size()/njoueurs;i++)
		{
			for(int j=0;j<njoueurs;j++)
			armies.get(j).addSoldiersForce(0, 1, rlist.get(i*2+j).id);
		}
	}
	
	public Unite unitTouched()
	{
		Unite touch = null;
		for(int i=0;i<armies.size();i++)
		{
			for(int j=0;j<armies.get(i).soldiers.size();j++)
			{
				if(armies.get(i).soldiers.get(j).isTouched(GameScreen.gameMouse.x,GameScreen.gameMouse.y))touch=armies.get(i).soldiers.get(j);
			}
		}
		return touch;
	}
	
	public void update()
	{
		
		Shaders.setWaveShader();
		Main.batch.draw(TextureManager.tex.get("background"),-90-226,130-85);
		Shaders.setDefaultShader();
		Main.batch.setColor(0.2f, 0.2f, 0.25f, 1);
		//Main.batch.draw(TextureManager.tex.get("panneau"),-20,-90);
		Main.batch.draw(TextureManager.tex.get("panneauv"),1750,-90);
		Main.batch.setColor(1,1,1,1);
		
		AllPays.update(teamactuel);
		
		AllPays.selectiontype=1;
		
		if(human[teamactuel]==1)
		{
			int touche = AllPays.paysTouched();
			Unite unitouche = unitTouched();
			if(phase==0)
			{
				if(touche>=0)
				{
					if(Inputs.instance.leftmousedown || Inputs.instance.rightmousedown)
					{
						while(deployer(1,touche,teamactuel) && Inputs.instance.rightmousedown);
					}
					AllPays.selection=AllPays.pays.get(touche);
					AllPays.selectiontype=1;
				}
				else
				{
					AllPays.selection=null;
				}
			}
			else if(phase==1)
			{

				if(selectedUnits.size()>0)
				{
					AllPays.selection=AllPays.pays.get(selectedUnits.get(0).pays);
					AllPays.selectiontype=3;
				}
				else if(touche>=0)
				{
					AllPays.selection=AllPays.pays.get(touche);
					AllPays.selectiontype=3;
				}
				if(Inputs.instance.leftmousedown || Inputs.instance.rightmousedown)
				{
					
					//boolean atk = false;
					if(unitouche!=null && unitouche.team==teamactuel && unitouche.mvtactuel>0)
					{
						if(selectedUnits.size()==0 || selectedUnits.get(0).pays == unitouche.pays)
						{
							if(!selectedUnits.contains(unitouche))selectedUnits.add(unitouche);
							else selectedUnits.remove(unitouche);
							if(selectedUnits.size()>3)selectedUnits.remove(0);
						}
						else
						{
							selectedUnits.clear();
							selectedUnits.add(unitouche);
						}
						touche=-1;
					}
					if(selectedUnits.size()>0 && touche>=0 && AllPays.pays.get(touche).team != teamactuel)
					{
						if(attaquer(selectedUnits.get(0).pays,touche,teamactuel,selectedUnits))selectedUnits.clear();
					}
					if(selectedUnits.size()>0 && touche>=0 && AllPays.pays.get(touche).team == teamactuel)
					{
						if(fortifier(selectedUnits.get(0).pays,touche,teamactuel,selectedUnits))selectedUnits.clear();
					}
				}
			}
		}
		else
		{
			ias.get(teamactuel).play(phase);
		}
		
		
		for(int i=0;i<armies.size();i++)
		{
			armies.get(i).update();
		}
		for(int i=0;i<selectedUnits.size();i++)
		{
			selectedUnits.get(i).updatehighlight();
		}
	}
	
	public boolean deployer(int n, int paysid, int team)
	{
		if(AllPays.pays.get(paysid).team==team && armies.get(teamactuel).newsoldiers>0)
		{
			armies.get(teamactuel).addSoldiers(0, n, paysid);
			return true;
		}
		return false;
	}
	
	public boolean attaquer(int paysattaque, int paysdefense, int team, ArrayList<Unite> units)
	{
		if(AllPays.pays.get(paysattaque).team==team && AllPays.pays.get(paysdefense).team!=team )
		{
			if(AllPays.pays.get(paysattaque).adjacents.contains(AllPays.pays.get(paysdefense)))
			{
				if(units.size()<=0 || units.size()>3 || AllPays.pays.get(paysattaque).occupants.size()<units.size()+1)
				{
					return false;
				}
				ArrayList<Unite> unitsreal = new ArrayList<Unite>();
				
				
				
				Collections.sort(unitsreal, new Comparator<Unite>() {
			        @Override
			        public int compare(Unite o1, Unite o2) {
			        	int basescore =o2.scoreactuel*1000-o1.scoreactuel*1000;
			        	int departage =o1.att-o2.att;
			            return basescore+departage;
			        }
			    });
				ArrayList<Unite> challengers = AllPays.pays.get(paysdefense).getChallengers();
				
				boolean troopsok = true;
				for(int i=0;i<units.size();i++)
				{
					Unite unitreal = units.get(i);
					if(unitreal.mvtactuel <= 0 ||unitreal.team != team || unitreal.pays != paysattaque)troopsok = false;
					
					unitreal.scoreactuel=unitreal.puissance+Tools.lancerDe();
					unitsreal.add(unitreal);
				}
				if(troopsok)
				{
					int nbcombats = Math.min(challengers.size(), unitsreal.size());
					for(int i=0;i<nbcombats;i++)
					{
						if(challengers.get(i).scoreactuel>unitsreal.get(i).scoreactuel)
						{
							armies.get(teamactuel).removeSoldier(unitsreal.get(i).id);
						}
						else
						{
							armies.get(AllPays.pays.get(paysdefense).team).removeSoldier(challengers.get(i).id);
						}
					}
					if(AllPays.pays.get(paysdefense).occupants.size()==0)
					{
						for(int i=0;i<unitsreal.size();i++)armies.get(teamactuel).transferSoldiers(unitsreal.get(i).id, paysdefense);
					}
					return true;
				}
			}
		}
		return false;
	}
	public boolean fortifier( int paysource, int paysdest, int team, ArrayList<Unite> units)
	{
		if(AllPays.pays.get(paysource).team==team && AllPays.pays.get(paysdest).team==team )
		{
			if(AllPays.pays.get(paysource).adjacents.contains(AllPays.pays.get(paysdest)))
			{
				
				if(units.size()<=0 || units.size()>3 || AllPays.pays.get(paysource).occupants.size()<units.size()+1)
				{
					return false;
				}
				boolean troopsok = true;
				
				for(int i=0;i<units.size();i++)
				{
					Unite unitreal = units.get(i);
					if(unitreal.mvtactuel <= 0 ||unitreal.team != team || unitreal.pays != paysource)troopsok = false;
				}
				
				if(troopsok)
				{
					for(int i=0;i<units.size();i++)armies.get(teamactuel).transferSoldiers(units.get(i).id, paysdest);
					return true;
				}
					
			}
			
		}
		return false;
	}
	public void nextPhase()
	{
		selectedUnits.clear();
		lastPays = -1;
		for(int i=0;i<armies.size();i++)
		{
			for(int j=0;j<armies.get(i).soldiers.size();j++)
			{
				armies.get(i).soldiers.get(j).scoreactuel=0;
				armies.get(i).soldiers.get(j).mvtactuel=armies.get(i).soldiers.get(j).mvt;
			}
		}
		phase+=1;
		AllPays.selection=null;
		AllPays.selectiontype=1;
		if(phase==2)
		{
			phase=0;
			teamactuel+=1;
			if(teamactuel==njoueurs)
			{
				teamactuel=0;
			}
			armies.get(teamactuel).newsoldiers+=(int)(armies.get(teamactuel).getCountries().size()/3);
		}
	}

}
