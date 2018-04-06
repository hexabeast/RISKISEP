package com.hexabeast.riskisep.gameboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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
	
	public int curid = 0;
	
	public int njoueurs = 2;
	public int teamactuel = 0;
	public int phase = 0;
	public int[] human = {1,1};
	public int lastsource=-1;
	public int lastcible = -1;
	
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
	
	public void update()
	{
		
		Shaders.setWaveShader();
		Main.batch.draw(TextureManager.tex.get("background"),-90-226,130-85);
		Shaders.setDefaultShader();
		Main.batch.setColor(0.2f, 0.2f, 0.25f, 1);
		//Main.batch.draw(TextureManager.tex.get("panneau"),-20,-90);
		Main.batch.draw(TextureManager.tex.get("panneauv"),1750,-90);
		Main.batch.setColor(1,1,1,1);
		
		AllPays.update();
		
		if(human[teamactuel]==1)
		{
			int touche = AllPays.paysTouched();
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
				if(touche>=0)
				{
					if(AllPays.selection==null || AllPays.selectiontype==1)
					{
						AllPays.selection=AllPays.pays.get(touche);
						AllPays.selectiontype=1;
					}
					if(Inputs.instance.leftmousedown || Inputs.instance.rightmousedown)
					{
						
						boolean atk = false;
						if(AllPays.selection!=null)
						{
							do
							{
								atk = false;
								if(attaquer(1,AllPays.selection.id,touche,teamactuel))atk=true;
							}while(Inputs.instance.rightmousedown && atk);
						}
						if(!atk && AllPays.pays.get(touche).team==teamactuel)
						{
							AllPays.selection=AllPays.pays.get(touche);
							AllPays.selectiontype=2;
						}
					}
				}
			}
			else if(phase==2)
			{
				if(touche>=0)
				{
					if(AllPays.selection==null || AllPays.selectiontype==1)
					{
						AllPays.selection=AllPays.pays.get(touche);
						AllPays.selectiontype=1;
					}
					if(Inputs.instance.leftmousedown || Inputs.instance.rightmousedown)
					{
						if(AllPays.selection!=null && AllPays.selectiontype==3)
						{
							boolean fo = false;
							do
							{
								if(fortifier(1,AllPays.selection.id,touche,teamactuel))
								{
									fo=true;
								}
								else fo=false; 
							}while(fo && Inputs.instance.rightmousedown);
						}
						if(lastcible<0 && AllPays.pays.get(touche).team==teamactuel)
						{
							AllPays.selection=AllPays.pays.get(touche);
							AllPays.selectiontype=3;
						}
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
	
	public boolean attaquer(int n, int paysattaque, int paysdefense, int team, int[] units)
	{
		if(AllPays.pays.get(paysattaque).team==team && AllPays.pays.get(paysdefense).team!=team )
		{
			if(AllPays.pays.get(paysattaque).adjacents.contains(AllPays.pays.get(paysdefense)))
			{
				boolean troopsok = true;
				if(units.length>=0 && units.length<3)
				{
					troopsok = false;
				}
				ArrayList<Unite> unitsreal = new ArrayList<Unite>();
				if(troopsok)
				{
					for(int i=0;i<units.length;i++)
					{
						Unite unitreal = GameScreen.master.soldiersmap.get(String.valueOf(units[i]));
						if(unitreal.team != team || unitreal.pays != paysattaque)troopsok = false;
						unitsreal.add(unitreal);
					}
				}
				Collections.sort(unitsreal, new Comparator<Unite>() {
			        @Override
			        public int compare(Unite o1, Unite o2) {
			            return o1.att-o2.att;
			        }
			    });
				ArrayList<Unite> challengers = AllPays.pays.get(paysdefense).getChallengers();
				if(troopsok)
				{
					int lancer = Tools.lancerDe();
					if()
					{
						armies.get(AllPays.pays.get(paysdefense).team).removeSoldiers(1, paysdefense);
						if(AllPays.pays.get(paysdefense).occupants.size()==0)
						{
							armies.get(team).addSoldiersForce(1, paysdefense);
							armies.get(team).removeSoldiers(1, paysattaque);
						}
						/*else
						{
							return true;//attaquer(n, paysattaque, paysdefense, team);
						}*/
					}
					else
					{
						armies.get(team).removeSoldiers(1, paysattaque);
						//return true;//attaquer(n, paysattaque, paysdefense, team);
					}
					lastcible = paysdefense;
					lastsource = paysattaque;
					return true;
				}
			}
		}
		else if(lastsource==paysattaque && lastcible==paysdefense && AllPays.pays.get(paysattaque).team==team && AllPays.pays.get(paysdefense).team==team)
		{
			return fortifier(n, paysattaque, paysdefense, team);
		}
		lastsource=-1;
		lastcible=-1;
		return false;
	}
	public boolean fortifier(int n, int paysource, int paysdest, int team)
	{
		if(lastcible<0 || (lastcible==paysdest && lastsource==paysource))
		{
			if(AllPays.pays.get(paysource).team==team && AllPays.pays.get(paysdest).team==team )
			{
				if(AllPays.pays.get(paysource).adjacents.contains(AllPays.pays.get(paysdest)))
				{
					if(AllPays.pays.get(paysource).occupants.size()>1)
					{
						armies.get(team).removeSoldiers(1, paysource);
						armies.get(team).addSoldiersForce(1, paysdest);
						lastcible=paysdest;
						lastsource=paysource;
						return true;
					}
						
				}
				
			}
		}
		return false;
	}
	public void nextPhase()
	{
		phase+=1;
		AllPays.selection=null;
		AllPays.selectiontype=1;
		lastsource=-1;
		lastcible=-1;
		if(phase==3)
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
