package com.hexabeast.riskisep.ia;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Tools;
import com.hexabeast.riskisep.gameboard.AllPays;
import com.hexabeast.riskisep.gameboard.GameMaster;
import com.hexabeast.riskisep.gameboard.Unite;

public class IASimple {
	public int team;
	int postcible;
	int postsource;
	
	public boolean playing = false;
	
	public static int PROBACCURACY = 1000000;
	
	public boolean randomUnits=false;
	
	float cUnits = 5;
	float cEUnits = 6.05f;
	float cCountries = 12;
	float cContinents = 8;
	float cEContinents = 4;
	float cForce = 0.2f;
	float cFaiblesse = 0.12f;
	
	public static Probabilities probabilities = new Probabilities();
	public static int sleeptime = 10;
	public static int slowtime = 80;
	
	public BoardState state;
	public BoardState beginstate;
	
	public int phase = 0;
	public Thread thread;
	//float c
	
	public IASimple(int team)
	{
		this.team=team;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true)
				{
					while(playing==false)
					{
						try {
							Thread.sleep(sleeptime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					playf(phase, true);
				}
			}
		}).start();
	}
	
	public static void recurProb(int siz, int[] actuel, ArrayList<int[]> stockage, boolean first, int maxize)
	{
		
		for(int i=0; i<=siz;i++)
		{
			int[] tempact;
			
			if(!first)
			{
				tempact = new int[actuel.length+1];
				for(int k=0;k<actuel.length;k++)tempact[k]=actuel[k];
				tempact[actuel.length] = siz;
			}
			else
			{
				tempact = new int[] {};
			}
			
			if(tempact.length==maxize)
			{
				stockage.add(tempact);
				return;
			}
			
			recurProb(siz-i, tempact, stockage,false,maxize);
		}
	}
	
	public static void calculProb(final Unite[] types)
	{
		FileHandle file = Gdx.files.local("probasIA.json");
		
		boolean recalculate = false;
		if(!file.file().exists()) {
			try {
				recalculate = true;
				file.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(recalculate)
		{
			probabilities.p = new Probability[types.length+1][types.length+1][types.length+1][types.length+1][types.length+1];
				
			final ArrayList<int[]> sources = new ArrayList<int[]>();
			recurProb(types.length,new int[] {}, sources,true,3);
			sources.remove(sources.size()-1);
			
			final ArrayList<int[]> dests = new ArrayList<int[]>();
			recurProb(types.length,new int[] {}, dests,true,2);
			dests.remove(dests.size()-1);
			
			for(int i=0; i<sources.size(); i++)
			{
				for(int j=0; j<dests.size(); j++)
				{
					int combats = 1;
					if(sources.get(i)[1] != 0 && dests.get(j)[1]!=0)combats = 2;
					double[] probs = new double[Probability.issues.length];
					for(int k=0; k<PROBACCURACY; k++)
					{
						final int ib = i;
						final int jb = j;
						
						int[][] a = new int[3][2];
						for(int l = 0; l<3; l++)
						{
							a[l][0] = l;
							if(sources.get(i)[l]==0)a[l][1] = 0;
							else a[l][1] = Tools.lancerDe()+types[sources.get(i)[l]-1].puissance;
						}
						
						Arrays.sort(a, new Comparator<int[]>() {
							@Override
							public int compare(int[] o1, int[] o2) {
								int basescore =o2[1]*1000-o1[1]*1000;
					        	int departage = 0;
					        	if(sources.get(ib)[o1[0]]>0 && sources.get(ib)[o2[0]]>0)departage=types[sources.get(ib)[o1[0]]-1].att-types[sources.get(ib)[o2[0]]-1].att;
					            return basescore+departage;
							}
						});
						int[][] b = new int[2][2];
						for(int l = 0; l<2; l++)
						{
							b[l][0] = l;
							if(dests.get(j)[l]==0)b[l][1] = 0;
							else b[l][1] = Tools.lancerDe()+types[dests.get(j)[l]-1].puissance;
						}
						
						Arrays.sort(b, new Comparator<int[]>() {
							@Override
							public int compare(int[] o1, int[] o2) {
								int basescore =o2[1]*1000-o1[1]*1000;
					        	int departage =0;
					        	if(dests.get(jb)[o1[0]]>0 && dests.get(jb)[o2[0]]>0)departage=types[dests.get(jb)[o1[0]]-1].def-types[dests.get(jb)[o2[0]]-1].def;
					            return basescore+departage;
							}
						});
						
						boolean anormal = true;
						
						int[] etat= new int[] {0,0,0,0,0};
						
						if(a[0][1]>b[0][1])etat[3+b[0][0]]=1;
						else etat[a[0][0]]=1;
						if(combats==2)
						{
							if(a[1][1]>b[1][1])etat[3+b[1][0]]=1;
							else etat[a[1][0]]=1;
						}
						for(int l=0; l<Probability.issues.length;l++)
						{
							if(Arrays.equals(etat, Probability.issues[l]))
							{
								anormal=false;
								probs[l]+=1.0/(double)PROBACCURACY;
								break;
							}
						}
						if(anormal)
						{
							System.out.println("ISSUE IMPOSSIBLE");
							System.out.println(Arrays.toString(etat));
						}
						
						if(a[0][1]<b[0][1])etat[3+b[0][0]]=1;
					}
					probabilities.p[sources.get(i)[0]][sources.get(i)[1]][sources.get(i)[2]][dests.get(j)[0]][dests.get(j)[1]] = new Probability(probs);
				}
			}
			String proba = new Json().toJson(probabilities);
			file.writeString(proba, false);
		}
		
		String loadParams = file.readString();
		probabilities = new Json().fromJson(Probabilities.class, loadParams);
		if(probabilities== null)
		{
			System.out.println("FICHIER DE PROBABILITES MANQUANT");
		}
		
		
		System.out.println(Arrays.toString(probabilities.p[1][0][0][3][0].probs));
	}
	
	public float forces()
	{
		float etat = 0;
		int nbcompare = 0;
		for(int i=0; i<GameScreen.apays.pays.size(); i++)
		{
			if(state.pays[i].team==team)
			{
				for(int j=0; j<GameScreen.apays.pays.get(i).adjacents.size(); j++)
				{
					SimplePays ennemi = state.pays[GameScreen.apays.pays.get(i).adjacents.get(j).id];
					if(ennemi.team!=team)
					{
						SimplePays ami = state.pays[i];
						etat+=Math.min(Math.sqrt(Math.max(0, ami.nbsoldats-ennemi.nbsoldats)),5);
						nbcompare+=1;
					}
					else
					{
						etat+=5;
						nbcompare+=1;
					}
				}
			}
		}
		if(nbcompare>0)etat/=(float)nbcompare;
		return etat;
	}
	
	public float faiblesses()
	{
		float etat = 0;
		int nbcompare = 0;
		for(int i=0; i<GameScreen.apays.pays.size(); i++)
		{
			if(state.pays[i].team==team)
			{
				for(int j=0; j<GameScreen.apays.pays.get(i).adjacents.size(); j++)
				{
					SimplePays ennemi = state.pays[GameScreen.apays.pays.get(i).adjacents.get(j).id];
					if(ennemi.team!=team)
					{
						SimplePays ami = state.pays[i];
						etat+=Math.pow(Math.max(0, ennemi.nbsoldats-ami.nbsoldats),2);
						nbcompare+=1;
					}
				}
			}
		}
		if(nbcompare>0)etat/=(float)nbcompare;
		return etat;
	}
	
	public float simuleplace(int id, int type)
	{
		state.pays[id].nbsoldats+=GameMaster.unitTypes[type].cout;
		float score = boardScore();
		state.pays[id].nbsoldats-=GameMaster.unitTypes[type].cout;
		return score;
	}
	
	public float simuldeplace(int id, int id2, int type)
	{
		state.pays[id].nbsoldats-=GameMaster.unitTypes[type].cout;
		state.pays[id2].nbsoldats+=GameMaster.unitTypes[type].cout;
		float score = boardScore();
		state.pays[id].nbsoldats+=GameMaster.unitTypes[type].cout;
		state.pays[id2].nbsoldats-=GameMaster.unitTypes[type].cout;
		return score;
	}
	
	public ArrayList<Unite> getAttaquants(int id)
	{
		return GameScreen.apays.pays.get(id).getAttaquants();
	}
	
	public float simulattaque(int id, int id2)
	{
		ArrayList<Unite> challengers = GameScreen.apays.pays.get(id2).getChallengers();
		Collections.sort(challengers,new Comparator<Unite>() {
			@Override
			public int compare(Unite o1, Unite o2) {
				return o2.type-o1.type;
			}
		});
		
		ArrayList<Unite> attaquants = getAttaquants(id);
		
		int i1 = attaquants.get(0).type+1;
		int i2 = 0;
		if(attaquants.size()>1)i2 = attaquants.get(1).type+1;
		int i3 = 0;
		if(attaquants.size()>2)i3 = attaquants.get(2).type+1;
		
		int i4 = challengers.get(0).type+1;
		int i5 = 0;
		if(challengers.size()>1)i5 = challengers.get(1).type+1;
		double[] probasituation = probabilities.p[i1][i2][i3][i4][i5].probs;
		
		float scoretotal = 0;
		for(int l=0; l<Probability.issues.length;l++)
		{
			if(probasituation[l]>0.000001)
			{
				for(int i=0;i<attaquants.size(); i++)
				{
					if(Probability.issues[l][i]==1)state.pays[id].nbsoldats-=GameMaster.unitTypes[attaquants.get(i).type].cout;
				}
				for(int i=0;i<challengers.size(); i++)
				{
					if(Probability.issues[l][i+3]==1)state.pays[id2].nbsoldats-=GameMaster.unitTypes[challengers.get(i).type].cout;
				}
				if(state.pays[id2].nbsoldats<0)System.out.println("ESTIMATION ERROR");
				if(state.pays[id2].nbsoldats==0)
				{
					for(int i=0;i<attaquants.size(); i++)
					{
						if(Probability.issues[l][i]==0)
						{
							state.pays[id].nbsoldats-=GameMaster.unitTypes[attaquants.get(i).type].cout;
							state.pays[id2].nbsoldats+=GameMaster.unitTypes[attaquants.get(i).type].cout;
						}
					}
					state.pays[id2].team=team;
				}
				scoretotal+=boardScore()*probasituation[l];
				state.pays[id2].team = beginstate.pays[id2].team;
				state.pays[id2].nbsoldats = beginstate.pays[id2].nbsoldats;
				state.pays[id].nbsoldats = beginstate.pays[id].nbsoldats;
			}
		}
		return scoretotal;
	}
	
	public float boardScore()
	{
		float score = 0;
		//Unites alliees
		score+=cUnits*state.compteUnitesTeam(team)*0.05f;
		//Unites ennemies
		score-=cEUnits*state.compteUnitesPasTeam(team)*0.05f;
		//nombre de pays en possession
		score+=cCountries*state.comptePaysTeam(team)*0.05;
		
		score+=cContinents*state.getContinentScore(team);
		
		score-=cEContinents*state.getContinentScoreEnnemy(team);
		//TODO nombre de continents en possession
		//TODO nombre de continents ennemis en possession
		
		//score de forces des pays
		score+=cForce*forces();
		//score de faiblesses des pays
		score-=cFaiblesse*faiblesses();
		
		return score;
	}
	
	/*public Pays[] scoreAttaqueMax()
	{
		float bestscore = -1000000000;
		Pays bestpays = null;
		Pays bestpaysattaque = null;
		for(int i=0; i<AllPays.pays.size(); i++)
		{
			if(AllPays.pays.get(i).team==team)
			{
				int score = 0;
			}
		}
		return new Pays[] {bestpays,bestpays};
	}*/
	
	int bestplaytype = -1;
	int fromcountry = -1;
	int tocountry = -1;
	int unittype = 1;
	boolean readytoplay = false;
	
	public void playf(int phase, boolean threaded)
	{
		this.phase=phase;
		//System.out.println("IA PLAY");
		float basescore = -100000000;
		boolean action = true;
		boolean first = true;
		while(action)
		{
			action = false;
			while(threaded && bestplaytype != -1)
			{
				try {
					Thread.sleep(sleeptime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			beginstate = new BoardState();
			beginstate.loadCurrentState();
			state = new BoardState(beginstate);
			if(!first)basescore = boardScore();
			double bestscore = -1000000000;
			
			if(phase==0 && GameScreen.master.armies.get(team).newsoldiers>0)
			{
				int typ = 0;
				
				if(randomUnits)
				{
					double ran = Math.random();
					if(GameScreen.master.armies.get(team).newsoldiers>=GameMaster.unitTypes[2].cout && ran>0.8)typ=2;
					if(GameScreen.master.armies.get(team).newsoldiers>=GameMaster.unitTypes[1].cout && ran>0.93)typ=1;
				}
				for(int i=0; i<state.pays.length; i++)
				{
					if(state.pays[i].team==team)
					{
						
						double tscore = simuleplace(state.pays[i].id, typ);
						tscore+=(Math.random()-0.5)*0.00000001;
						/*System.out.println("------");
						System.out.println(basescore);
						System.out.println(tscore);
						System.out.println("------");*/
						if(tscore>bestscore)
						{
							bestscore=tscore;
							bestplaytype = 1;
							unittype=typ;
							tocountry = state.pays[i].id;
						}
					}
				}
			}
			else if(phase==1)
			{
				for(int i=0; i<state.pays.length; i++)
				{
					if(state.pays[i].team==team && getAttaquants(state.pays[i].id).size()>0)
					{
						for(int j=0; j<GameScreen.apays.pays.get(state.pays[i].id).adjacents.size(); j++)
						{
							int adjid = GameScreen.apays.pays.get(state.pays[i].id).adjacents.get(j).id;
							if(state.pays[adjid].team==team)
							{
								double tscore = simuldeplace(state.pays[i].id,adjid, getAttaquants(state.pays[i].id).get(0).type);
								tscore+=(Math.random()-0.5)*0.00000001;
								if(tscore>bestscore)
								{
									bestscore=tscore;
									bestplaytype = 3;
									tocountry = adjid;
									fromcountry = state.pays[i].id;
								}
							}
							else 
							{
								double tscore = simulattaque(state.pays[i].id,adjid);
								tscore+=(Math.random()-0.5)*0.00000001;
								/*System.out.println("------");
								System.out.print("Score avant attaque : ");
								System.out.println(basescore);
								System.out.print("Score après attaque : ");
								System.out.println(tscore);
								System.out.println("------");*/
								if(tscore>bestscore)
								{
									bestscore=tscore;
									bestplaytype = 2;
									tocountry = adjid;
									fromcountry = state.pays[i].id;
								}
							}
						}
					}
				}
			}
			if(bestscore>basescore)
			{
				if(GameMaster.iaslow)
				{
					try {
						Thread.sleep(slowtime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				if(threaded)readytoplay = true;
				else jouerCoup();
				
				action=true;
			}
			
			first=false;
		}
		bestplaytype=4;
		if(threaded)readytoplay = true;
		else jouerCoup();
		playing=false;
		//System.out.println(String.valueOf(team)+" "+String.valueOf(phase));
	}
	
	public void play(final int phase)
	{
		this.phase=phase;
		if(!playing && !readytoplay)
		{
			playing=true;
		}
		
		if(readytoplay)
		{
			jouerCoup();
		}
		
	}
	
	public void jouerCoup()
	{
		
		if(bestplaytype==1)
		{
			if(!GameScreen.master.deployer(tocountry, team, -1000, -1000, unittype))System.out.println("IA PROBLEME PLACEMENT");
		}
		else if(bestplaytype==2)
		{
			if(!GameScreen.master.attaquer(fromcountry, tocountry, team, getAttaquants(fromcountry)))
			{
				System.out.println(team);
				System.out.println(GameScreen.apays.pays.get(fromcountry).nom);
				System.out.println(GameScreen.apays.pays.get(tocountry).nom);
				System.out.println(getAttaquants(fromcountry).size());
				System.out.println("IA PROBLEME ATTAQUE");
			}
		}
		else if(bestplaytype==3)
		{
			ArrayList<Unite> fortunits = new ArrayList<Unite>();
			fortunits.add(getAttaquants(fromcountry).get(0));
			if(!GameScreen.master.fortifier(fromcountry, tocountry, team, fortunits))System.out.println("IA PROBLEME DEPLACEMENT");
		}
		else if(bestplaytype==4)
		{
			GameScreen.master.nextPhase();
		}
		
		readytoplay=false;
		fromcountry = -1;
		tocountry = -1;
		bestplaytype = -1;
	}
}
