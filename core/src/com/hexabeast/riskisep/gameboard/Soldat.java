package com.hexabeast.riskisep.gameboard;

import com.hexabeast.riskisep.ressources.TextureManager;

public class Soldat extends Unite {

	public Soldat(int id, int pays, int team) {
		super(id,pays, team);
		type=0;
		cout=1;
		puissance=1;
		att=2;
		def=1;
		mvt=2;
		tex = TextureManager.tex.get("soldat");
		init();
	}

}
