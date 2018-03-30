package com.hexabeast.riskisep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexabeast.riskisep.gameboard.AllPays;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public class Main extends Game {
	
	public static int windowWidth = 1600;
	public static int windowHeight = 900;
	public static SpriteBatch batch;
	public static ShapeRenderer shapebatch;
	public static GameScreen game;
	public static float delta;
	public static float time = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Shaders.loadShaders();
		shapebatch = new ShapeRenderer();
		game = new GameScreen();
		TextureManager.load();
		AllPays.loadPays();
		Inputs.instance = new Inputs();
		Gdx.input.setInputProcessor(Inputs.instance);
	}
	
	@Override
	public void pause () {
		super.pause();
	}
	
	@Override
	public void resume () {
		super.resume();
	}
	
	@Override
	public void resize (int width, int height) {
		Gdx.graphics.setWindowedMode(16*height/9, height);
		super.resize(width, height);
	}

	@Override
	public void render () {
		delta=Gdx.graphics.getDeltaTime();
		time+=delta;
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Inputs.instance.update();
		
		setScreen(game);
		
		Inputs.instance.updateLate();
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapebatch.dispose();
		TextureManager.dispose();
	}
}
