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
	
	
	
	public static boolean noTransition = true;
	public static boolean fastplay = true;
	public static boolean iaslow = false;
	
	public static int gamestats = 50;
	public static int gamewincounter = 0;
	
	public int[] humanstart = {0,0};
	
	public int[] human = humanstart.clone();
	
	public static Unite[] unitTypes = {new Soldat(-1,-1,-1),new Cheval(-1,-1,-1),new Cannon(-1,-1,-1)};
	public static Color[] teamcol = {new Color(0,0,1,1),new Color(1,0,0,1),new Color(0,1,0,1),new Color(1,1,0,1)};
	
	public static int unitstartnumber = 1; 
	
	public ArrayList<Army> armies = new ArrayList<Army>();
	public ArrayList<IASimple> ias = new ArrayList<IASimple>();
	public Map<String, Unite> soldiersmap = new HashMap<String, Unite>();
	
	public int curid = 0;
	
	public int njoueurs = humanstart.length;
	public int teamactuel = 0;
	public int phase = 0;
	
	public int unitType = 0;
	
	public ArrayList<Unite> selectedUnits = new ArrayList<Unite>();
	public int lastPays = -1;
	
	public boolean gamend = false;
	public boolean gamestart = true;
	public int winner = 0;
	
	public boolean firstturn;
	
	public GameMaster()
	{
	}
	
	public void beginGame()
	{
		human = humanstart.clone();
		firstturn=true;
		gamestart=true;
		IASimple.calculProb(unitTypes);
		for(int i=0;i<njoueurs;i++)
		{
			armies.add(new Army(i));
		}
		
		for(int i=0;i<njoueurs;i++)
		{
			IASimple ia = new IASimple(i);
			ia.randomUnits=true;
			if(i==0)ia.randomai=true;
			ias.add(ia);
			
		}
		this.njoueurs = human.length;
		GameScreen.apays.selection=null;
		
		ArrayList<Pays> rlist = new ArrayList<Pays>();
		rlist.addAll(GameScreen.apays.pays);
		Collections.shuffle(rlist);
		for(int i=0;i<(GameScreen.apays.pays.size()/njoueurs)+1;i++)
		{
			for(int j=0;j<njoueurs;j++)
			{
				if(i*njoueurs+j<GameScreen.apays.pays.size())for(int l=0;l<unitstartnumber;l++)armies.get(j).addSoldierForce(0, rlist.get(i*njoueurs+j).id);
			}
		}
		turnStart();
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
		if(!gamend && !gamestart)
		{
			GameScreen.apays.selectiontype=1;
			
			boolean leftdown = Inputs.instance.leftmousedown && !GameScreen.UIcollision;
			boolean rightdown = Inputs.instance.rightmousedown;
			
			if(human[teamactuel]==1)
			{
				int touche = GameScreen.apays.paysTouched();
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
						GameScreen.apays.selection=GameScreen.apays.pays.get(touche);
						GameScreen.apays.selectiontype=1;
					}
					else
					{
						GameScreen.apays.selection=null;
					}
				}
				else if(phase==1)
				{

					if(selectedUnits.size()>0)
					{
						GameScreen.apays.selection=GameScreen.apays.pays.get(selectedUnits.get(0).pays);
						GameScreen.apays.selectiontype=3;
					}
					else if(touche>=0)
					{
						GameScreen.apays.selection=GameScreen.apays.pays.get(touche);
						GameScreen.apays.selectiontype=3;
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
						if(selectedUnits.size()==0 && unitouche==null && touche>=0 && GameScreen.apays.pays.get(touche).team == teamactuel)
						{
							selectedUnits.addAll(GameScreen.apays.pays.get(touche).getAttaquants());
							touche=-1;
						}
						if(selectedUnits.size()>0 && touche>=0 && GameScreen.apays.pays.get(touche).team != teamactuel)
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
						if(selectedUnits.size()>0 && touche>=0 && GameScreen.apays.pays.get(touche).team == teamactuel)
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
			else if(human[teamactuel]==0)
			{
				if(!fastplay)
				{
					ias.get(teamactuel).play(phase);
				}
				else if (!firstturn)
				{
					while(!gamend)
					{
						//System.out.println(armies.get(teamactuel).soldiers.size());
						ias.get(teamactuel).playf(phase,false);
						checkWinner();
					}
					if(gamestats>0)
					{
						gamestats-=1;
						if(winner==0)gamewincounter+=1;
						if(gamestats==0)System.out.println("Bleu gagne "+String.valueOf(gamewincounter));
						Main.game=new GameScreen();
						Main.game.master.gamestart=false;
					}
				}
			}
			else
			{
				//human = -1 => nextturn
			}
		}
		
		Shaders.setWaveShader();
		Main.batch.draw(TextureManager.tex.get("background"),-326,-60);
		Shaders.setDefaultShader();
		
		
		GameScreen.apays.update(teamactuel);
		
		for(int i=0;i<armies.size();i++)
		{
			armies.get(i).update();
		}
		checkWinner();
		
		if(!gamend && !gamestart && human[teamactuel]==1 && phase==0 && armies.get(teamactuel).newsoldiers>=unitTypes[unitType].cout)
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
		Shaders.setDefaultShader();
		firstturn=false;
	}
	
	public void checkWinner()
	{
		for(int i=0;i<armies.size();i++)
		{
			if(armies.get(i).soldiers.size()==0)
			{
				armies.get(i).newsoldiers=0;
				human[i]=-1;
			}
		}
		int alivenumber = 0;
		int lastalive = 0;
		for(int i=0;i<njoueurs;i++)if(human[i]>=0)
		{
			alivenumber+=1;
			lastalive = i;
		}
		if(alivenumber==1)
		{
			gamend = true;
			winner = lastalive;
		}
	}
	
	public boolean deployer(int paysid, int team, float x, float y, int type)
	{
		if(GameScreen.apays.pays.get(paysid).team==team && armies.get(teamactuel).newsoldiers>0)
		{
			Unite u = armies.get(teamactuel).addSoldier(type, paysid);
			if(u!=null)u.setPos(x,y);
			if(!fastplay)GameScreen.apays.addHighlight(paysid);
			return true;
		}
		return false;
	}
	
	public boolean attaquer(int paysattaque, int paysdefense, int team, ArrayList<Unite> units)
	{
		if(GameScreen.apays.pays.get(paysattaque).team==team && GameScreen.apays.pays.get(paysdefense).team!=team )
		{
			if(GameScreen.apays.pays.get(paysattaque).adjacents.contains(GameScreen.apays.pays.get(paysdefense)))
			{
				if(units.size()<=0 || units.size()>3 || GameScreen.apays.pays.get(paysattaque).occupants.size()<units.size()+1)
				{
					return false;
				}
				ArrayList<Unite> unitsreal = new ArrayList<Unite>();
				
				
				ArrayList<Unite> challengers = GameScreen.apays.pays.get(paysdefense).getChallengers();
				
				boolean troopsok = true;
				for(int i=0;i<units.size();i++)
				{
					Unite unitreal = units.get(i);
					if(unitreal.mvtactuel <= 0 ||unitreal.team != team || unitreal.pays != paysattaque)troopsok = false;
					
					unitreal.scoreactuel=unitreal.puissance+Tools.lancerDe();
					unitsreal.add(unitreal);
				}
				Collections.sort(unitsreal, new Comparator<Unite>() {
			        @Override
			        public int compare(Unite o1, Unite o2) {
			        	int basescore =o2.scoreactuel*1000-o1.scoreactuel*1000;
			        	int departage =o1.att-o2.att;
			            return basescore+departage;
			        }
			    });
				if(troopsok)
				{
					int nbcombats = Math.min(challengers.size(), unitsreal.size());
					//System.out.println("###############");
					for(int i=0;i<nbcombats;i++)
					{
						//System.out.println(challengers.get(i).scoreactuel);
						//System.out.println(unitsreal.get(i).scoreactuel);
						//System.out.println("---");
						if(challengers.get(i).scoreactuel>=unitsreal.get(i).scoreactuel)
						{
							armies.get(teamactuel).removeSoldier(unitsreal.get(i).id);
						}
						else
						{
							armies.get(GameScreen.apays.pays.get(paysdefense).team).removeSoldier(challengers.get(i).id);
						}
					}
					if(GameScreen.apays.pays.get(paysdefense).occupants.size()==0)
					{
						for(int i=0;i<unitsreal.size();i++)armies.get(teamactuel).transferSoldiers(unitsreal.get(i).id, paysdefense);
					}
					
					if(!fastplay)GameScreen.apays.addHighlight(paysattaque);
					if(!fastplay)GameScreen.apays.addHighlight(paysdefense);
					return true;
				}
			}
		}
		return false;
	}
	public boolean fortifier( int paysource, int paysdest, int team, ArrayList<Unite> units)
	{
		if(GameScreen.apays.pays.get(paysource).team==team && GameScreen.apays.pays.get(paysdest).team==team )
		{
			if(GameScreen.apays.pays.get(paysource).adjacents.contains(GameScreen.apays.pays.get(paysdest)))
			{
				
				if(units.size()<=0 || units.size()>3 || GameScreen.apays.pays.get(paysource).occupants.size()<units.size()+1)
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
					if(!fastplay)GameScreen.apays.addHighlight(paysdest);
					if(!fastplay)GameScreen.apays.addHighlight(paysource);
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
		
		phase+=1;
		GameScreen.apays.selection=null;
		GameScreen.apays.selectiontype=1;
		if(phase==2)
		{
			phase=0;
			teamactuel+=1;
			if(teamactuel==njoueurs)teamactuel=0;
			while(teamactuel<njoueurs && armies.get(teamactuel).soldiers.size()==0)teamactuel+=1;
			if(teamactuel==njoueurs)teamactuel=0;
			while(teamactuel<njoueurs && armies.get(teamactuel).soldiers.size()==0)teamactuel+=1;
			
			turnStart();
		}
	}
	
	public void turnStart()
	{
		armies.get(teamactuel).newsoldiers+=(int)(armies.get(teamactuel).getCountries().size()/3);
		armies.get(teamactuel).getRecompenseContinents();
		for(int i=0;i<armies.size();i++)
		{
			for(int j=0;j<armies.get(i).soldiers.size();j++)
			{
				armies.get(i).soldiers.get(j).scoreactuel=0;
				if(i==teamactuel)armies.get(i).soldiers.get(j).mvtactuel=armies.get(i).soldiers.get(j).mvt;
			}
		}
		
	}
	
	

}
