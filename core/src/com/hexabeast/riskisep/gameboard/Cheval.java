package com.hexabeast.riskisep.gameboard;

import com.hexabeast.riskisep.ressources.TextureManager;

public class Cheval extends Unite {

	public Cheval(int pays, int team) {
		super(pays, team);
		type=1;
		cout=3;
		puissance=2;
		att=1;
		def=3;
		mvt=3;
		tex = TextureManager.tex.get("cheval");
		init();
	}
}
