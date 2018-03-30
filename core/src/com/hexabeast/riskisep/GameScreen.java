package com.hexabeast.riskisep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.riskisep.gameboard.AllPays;
import com.hexabeast.riskisep.gameboard.GameMaster;
import com.hexabeast.riskisep.ressources.Shaders;
import com.hexabeast.riskisep.ressources.TextureManager;

public class GameScreen implements Screen{
	
	public static float gameZoom = 1;
	public static OrthographicCamera camera;
	public SpriteBatch batch = Main.batch;
	public static int camw = 2200;
	public static int camh = 1320;
	int CurFPS;
	public static Vector2 gameMouse;
	
	public static GameMaster master;

	
	private void Clear()
	{
		Gdx.gl.glClearColor(0,0,0,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	public GameScreen() 
	{
		master = new GameMaster();
		master.beginGame(2);
		camera = new OrthographicCamera(camw,camh);
		gameMouse = new Vector2();
		InitializeCamera();
	}
	
	@Override
	public void resize(int width, int height)
	{
		camera.viewportWidth = camw;
		camera.viewportHeight = ((float)height/(float)width)*camw;
	}
	
	@Override
	public void show() 
	{
		
	}
	
	private void InitializeCamera()
	{
		camera.zoom = gameZoom;
		camera.position.set(camera.viewportWidth/2,camera.viewportHeight/2, camera.position.z);	
	}


	private void UpdateCamera()
	{
		camera.update();
	}

	public Vector2 getAbsolutePos(float x, float y)
	{
		Vector3 worldCoord = new Vector3();
		worldCoord.x = x;
		worldCoord.y = y;
		worldCoord.z = 0;
		GameScreen.camera.unproject(worldCoord);
		gameMouse.x = worldCoord.x;
		gameMouse.y = worldCoord.y;
		return gameMouse;
	}
	

	@Override
	public void hide() 
	{

	}

	@Override
	public void pause() 
	{

	}

	@Override
	public void resume() 
	{

	}

	@Override
	public void dispose() 
	{
		
	}
	
	@Override
	public void render(float delta) 
	{	
		Clear();
		UpdateCamera();
		update();
		
		
		if(CurFPS!=Gdx.graphics.getFramesPerSecond())
		{
			Gdx.graphics.setTitle(String.valueOf(Gdx.graphics.getFramesPerSecond())); 
			CurFPS = Gdx.graphics.getFramesPerSecond();
		}
	}
	
	private void update(){
		
		
		batch.setColor(Color.WHITE);
		batch.setProjectionMatrix(camera.combined);
		getAbsolutePos(Inputs.instance.absMouse.x, Inputs.instance.absMouse.y);
		
		batch.begin();
		
		Shaders.setWaveShader();
		batch.draw(TextureManager.tex.get("background"),-90,130);
		Shaders.setDefaultShader();
		Main.batch.setColor(0.25f, 0.2f, 0.4f, 1);
		batch.draw(TextureManager.tex.get("panneau"),-20,-90);
		batch.draw(TextureManager.tex.get("panneauv"),1750,-90);
		Main.batch.setColor(1,1,1,1);
		
		AllPays.update();
		
		master.update();
		
		batch.end();
		
	}
	
}
