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

public class GameScreen implements Screen{
	
	public static float gameZoom = 1;
	public static OrthographicCamera camera;
	public SpriteBatch batch = Main.batch;
	int CurFPS;
	Vector2 gameMouse;

	
	private void Clear()
	{
		Gdx.gl.glClearColor(0,0,0,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	public GameScreen() 
	{
	}
	
	@Override
	public void resize(int width, int height)
	{
		camera.viewportWidth = 1280;
		camera.viewportHeight = ((float)height/(float)width)*1280;
	}
	
	@Override
	public void show() 
	{
		camera = new OrthographicCamera(1280,720);
		gameMouse = new Vector2();
		InitializeCamera();
	}
	
	private void InitializeCamera()
	{
		camera.zoom = gameZoom;
		camera.position.set(0,0, camera.position.z);	
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
		
		Texture bad = TextureManager.tex.get("badlogic");
		batch.draw(bad,-bad.getWidth()/2,-bad.getHeight()/2);
		
		batch.end();
		
	}
	
}
