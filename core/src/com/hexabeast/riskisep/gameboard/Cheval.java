package com.hexabeast.riskisep.gameboard;

import com.hexabeast.riskisep.ressources.TextureManager;

public class Cheval extends Unite {

	public Cheval(int id,int pays, int team) {
		super(id, pays, team);
		type=1;
		cout=3;
		puissance=2;
		att=1;
		def=3;
		mvt=3;
		h*=1.3f;
		tex = TextureManager.tex.get("cheval");
		init();
	}
}
