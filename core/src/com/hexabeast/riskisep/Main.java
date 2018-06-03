package com.hexabeast.riskisep;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hexabeast.riskisep.gameboard.AllPays;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public class Main extends Game {
	
	public static int windowWidth = 1600;
	public static int windowHeight = 900;
	public static int cwindowWidth = windowWidth;
	public static int cwindowHeight = windowHeight;
	public static float zoomratio = 1;
	
	public static SpriteBatch batch;
	public static ShapeRenderer shapebatch;
	public static GameScreen game;
	public static MenuScreen menu;
	public static float delta;
	public static float time = 0;
	
	public static InputMultiplexer inputMultiplexer;
	
	public static int gamestate = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		Shaders.loadShaders();
		shapebatch = new ShapeRenderer();
		TextureManager.load();
		game = new GameScreen();
		Inputs.instance = new Inputs();
		inputMultiplexer= new InputMultiplexer();
		inputMultiplexer.addProcessor(Inputs.instance);
		Gdx.input.setInputProcessor(inputMultiplexer);
		menu = new MenuScreen();
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
		cwindowWidth=16*height/9;
		cwindowHeight=height;
		
		zoomratio=(float)cwindowWidth/(float)windowWidth;
		super.resize(16*height/9, height);
	}

	@Override
	public void render () {
		delta=Gdx.graphics.getDeltaTime();
		if(delta>0.05)delta=0.05f;
		time+=delta;
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Inputs.instance.update();
		
		if(gamestate==0)setScreen(menu);
		else if(gamestate==1)setScreen(game);
		
		Inputs.instance.updateLate();
		super.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		shapebatch.dispose();
		TextureManager.dispose();
	}
	
	public static void drawRectangle(float x, float y, float w, float h, Color col)
	{
		Color old = batch.getColor();
		batch.setColor(col);
		batch.draw(TextureManager.tex.get("blank"), x, y, w, h);
		batch.setColor(old);
	}
	
	public static void drawfontCenter(BitmapFont font, float x, float y, String text)
	{
		TextureManager.fontlayout.setText(font,text);
		float fw = TextureManager.fontlayout.width;
		float fh = TextureManager.fontlayout.height;
		
		font.draw(Main.batch,text, x-fw/2,y+fh/2);
	}
}
