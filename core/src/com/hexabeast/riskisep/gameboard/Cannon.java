package com.hexabeast.riskisep.gameboard;

import com.hexabeast.riskisep.ressources.TextureManager;

public class Cannon extends Unite {

	public Cannon(int pays, int team) {
		super(pays, team);
		type=2;
		cout=7;
		puissance=4;
		att=3;
		def=2;
		mvt=1;
		tex = TextureManager.tex.get("cannon");
		init();
	}

}
