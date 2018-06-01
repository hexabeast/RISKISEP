package com.hexabeast.riskisep;

import com.badlogic.gdx.Screen;
import com.hexabeast.riskisep.ressources.TextureManager;

public class MenuScreen implements Screen{
	int typejoueur = 0;
	

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Main.batch.begin();
		for(int i=0;i<6;i++)
		{
			Main.batch.draw(TextureManager.tex.get("arrowR"), Main.windowWidth/2+80,Main.windowHeight-124-100*i,30,30);
			Main.batch.draw(TextureManager.tex.get("arrowL"), Main.windowWidth/2-80,Main.windowHeight-124-100*i,30,30);
			
			TextureManager.fontMenu.draw(Main.batch, "Joueur "+String.valueOf(i+1),Main.windowWidth/2-200,Main.windowHeight-100-100*i);
			
			String plstr = "Humain";
			String aistr = "IA";
			
			TextureManager.fontMenu.draw(Main.batch, "Humain",Main.windowWidth/2-40,Main.windowHeight-100-100*i);
			
			
		}
		Main.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
