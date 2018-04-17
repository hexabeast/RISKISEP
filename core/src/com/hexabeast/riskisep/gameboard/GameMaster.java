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
	
	public static Unite[] unitTypes = {new Soldat(-1,-1,-1),new Cheval(-1,-1,-1),new Cannon(-1,-1,-1)};
	
	public int njoueurs = 2;
	public int teamactuel = 0;
	public int phase = 0;
	public int[] human = {1,1};
	public int unitType = 0;
	
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
			{
				for(int l=0;l<5;l++)armies.get(j).addSoldierForce(0, rlist.get(i*2+j).id);
			}
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
		
		AllPays.selectiontype=1;
		
		boolean leftdown = Inputs.instance.leftmousedown && !GameScreen.UIcollision;
		boolean rightdown = Inputs.instance.rightmousedown;
		
		if(human[teamactuel]==1)
		{
			int touche = AllPays.paysTouched();
			Unite unitouche = unitTouched();
			if(phase==0)
			{
				if(armies.get(teamactuel).newsoldiers<unitTypes[unitType].cout)
				{
					unitType+=1;
					if(unitType>=unitTypes.length)unitType=0;
				}
				if(rightdown)
				{
					unitType+=1;
					if(unitType>=unitTypes.length)unitType=0;
				}
				
				if(touche>=0)
				{
					if(leftdown)
					{
						deployer(touche,teamactuel,GameScreen.gameMouse.x, GameScreen.gameMouse.y,unitType);
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
				if(Inputs.instance.rightmousedown)
				{
					selectedUnits.clear();
				}
				if(leftdown)
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
						if(attaquer(selectedUnits.get(0).pays,touche,teamactuel,selectedUnits))
						{
							for(int i=selectedUnits.size()-1; i>-1;i--)
							{
								if(selectedUnits.get(i).dead)selectedUnits.remove(i);
								else if(selectedUnits.get(i).mvtactuel<=0)selectedUnits.remove(i);
							}
						}
					}
					if(selectedUnits.size()>0 && touche>=0 && AllPays.pays.get(touche).team == teamactuel)
					{
						if(selectedUnits.get(0).pays == touche)
						{
							for(int i=0; i<selectedUnits.size();i++)
							{
								selectedUnits.get(i).running=true;
								selectedUnits.get(i).fx=(float) (GameScreen.gameMouse.x+(10*Math.random()-5));
								selectedUnits.get(i).fy=(float) (GameScreen.gameMouse.y+(10*Math.random()-5));
							}
						}
						else if(fortifier(selectedUnits.get(0).pays,touche,teamactuel,selectedUnits))
						{
							for(int i=selectedUnits.size()-1; i>-1;i--)
							{
								if(selectedUnits.get(i).mvtactuel<=0)selectedUnits.remove(i);
							}
						}
					}
				}
			}
		}
		else
		{
			ias.get(teamactuel).play(phase);
		}
		
		
		Shaders.setWaveShader();
		Main.batch.draw(TextureManager.tex.get("background"),-326,-60);
		Shaders.setDefaultShader();
		
		
		AllPays.update(teamactuel);
		
		for(int i=0;i<armies.size();i++)
		{
			armies.get(i).update();
		}
		if(phase==0 && armies.get(teamactuel).newsoldiers>=unitTypes[unitType].cout)
		{
			unitTypes[unitType].team=teamactuel;
			unitTypes[unitType].x=GameScreen.gameMouse.x;
			unitTypes[unitType].y=GameScreen.gameMouse.y;
			unitTypes[unitType].transparency=0.6f;
			unitTypes[unitType].update(-1);
		}
		for(int i=0;i<selectedUnits.size();i++)
		{
			selectedUnits.get(i).updatehighlight();
		}
		
	}
	
	public boolean deployer(int paysid, int team, float x, float y, int type)
	{
		if(AllPays.pays.get(paysid).team==team && armies.get(teamactuel).newsoldiers>0)
		{
			Unite u = armies.get(teamactuel).addSoldier(type, paysid);
			if(u!=null)u.setPos(x,y);
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
		unitType=0;
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
