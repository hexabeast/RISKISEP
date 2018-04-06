package com.hexabeast.riskisep.ia;

import com.hexabeast.riskisep.GameScreen;
import com.hexabeast.riskisep.Inputs;
import com.hexabeast.riskisep.gameboard.AllPays;

public class IASimple {
	public int team;
	int postcible;
	int postsource;
	
	public IASimple(int team)
	{
		this.team=team;
	}
	
	public void play(int phase)
	{
		if(phase==0)
		{
			/*if(touche>=0)
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
			}*/
		}
		else if(phase==1)
		{
			/*if(touche>=0)
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
							if(attaquer(1,AllPays.selection.id,touche,teamactuel))
							{
								postcible = touche;
								postsource = AllPays.selection.id;
								atk=true;
							}
							else
							{
								postsource=-1;
								postcible=-1;
							}
						}while(Inputs.instance.rightmousedown && postcible!=-1);
					}
					if(!atk && AllPays.pays.get(touche).team==teamactuel)
					{
						AllPays.selection=AllPays.pays.get(touche);
						AllPays.selectiontype=2;
					}
				}
			}*/
		}
		else if(phase==2)
		{
			/*if(touche>=0)
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
								postcible=touche;
							}
							else fo=false; 
						}while(fo && Inputs.instance.rightmousedown);
					}
					if(postcible<0 && AllPays.pays.get(touche).team==teamactuel)
					{
						AllPays.selection=AllPays.pays.get(touche);
						AllPays.selectiontype=3;
					}
				}
			}*/
		}
	}
}
