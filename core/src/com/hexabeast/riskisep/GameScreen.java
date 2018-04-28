package com.hexabeast.riskisep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.hexabeast.riskisep.gameboard.GameMaster;
import com.hexabeast.riskisep.ui.AllUI;

public class GameScreen implements Screen{
	
	public static float gameZoom = 1;
	public static float maxZoom = 1;
	public static float minZoom = 0.4f;
	public static OrthographicCamera camera;
	public SpriteBatch batch = Main.batch;
	public static int camw = 2200;
	public static int camh = (int) (((float)Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth())*camw);;
	int CurFPS;
	public static Vector2 gameMouse;
	
	public static GameMaster master;
	
	public static Vector2 panPos;
	public static boolean panning = false;
	
	public static boolean UIcollision = false;

	
	private void Clear()
	{
		Gdx.gl.glClearColor(0,0,0,0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}
	
	public GameScreen() 
	{
		master = new GameMaster();
		master.beginGame();
		AllUI.loadUI();
		camera = new OrthographicCamera(camw,camh);
		gameMouse = new Vector2();
		panPos = new Vector2();
		InitializeCamera();
	}
	
	@Override
	public void resize(int width, int height)
	{
		camera.viewportWidth = camw;
		camera.viewportHeight = ((float)height/(float)width)*camw;
		camh = (int) camera.viewportHeight;
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
		getAbsolutePos(Inputs.instance.absMouse.x, Inputs.instance.absMouse.y);
		
		if(Inputs.instance.rightmousedown || Inputs.instance.middlePressed)
		{
			panPos.x=gameMouse.x;
			panPos.y=gameMouse.y;
			panning = true;
		}
		if(Inputs.instance.rightmouseup || Inputs.instance.middleUp)
		{
			panning = false;
		}
		
		if(panning)
		{
			setCamPos(camera.position.x+panPos.x-gameMouse.x,camera.position.y+panPos.y-gameMouse.y);
		}
		camera.update();
		getAbsolutePos(Inputs.instance.absMouse.x, Inputs.instance.absMouse.y);
		if(panning)
		{
			panPos.x=gameMouse.x;
			panPos.y=gameMouse.y;
		}
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
	
	public static void zoom(int amount)
	{
		float oldzoom = gameZoom;
		gameZoom+=(float)amount/10;
		if(gameZoom>maxZoom)gameZoom=maxZoom;
		if(gameZoom<minZoom)gameZoom=minZoom;
		camera.zoom=gameZoom;
		float zoomdiff = (oldzoom-gameZoom)/gameZoom;
		setCamPos((camera.position.x+gameMouse.x*zoomdiff)/(1+zoomdiff),(camera.position.y+gameMouse.y*zoomdiff)/(1+zoomdiff));
	}
	
	public static void setCamPos(float x, float y)
	{
		camera.position.x=x;
		camera.position.y=y;
		if(camera.position.x+camera.viewportWidth*camera.zoom/2>camw)camera.position.x=camw-camera.viewportWidth*camera.zoom/2;
		if(camera.position.y+camera.viewportHeight*camera.zoom/2>camh)camera.position.y=camh-camera.viewportHeight*camera.zoom/2;
		
		if(camera.position.x-camera.viewportWidth*camera.zoom/2<0)camera.position.x=camera.viewportWidth*camera.zoom/2;
		if(camera.position.y-camera.viewportHeight*camera.zoom/2<0)camera.position.y=camera.viewportHeight*camera.zoom/2;
	}
	
	public void camUI()
	{
		
	}
	
	private void update(){
		
		
		batch.setColor(Color.WHITE);
		batch.setProjectionMatrix(camera.combined);
		
		batch.begin();
		
		//////////////////////
		Vector3 campostemp = new Vector3(camera.position);
		float zoomtemp = camera.zoom;
		camera.position.x=camera.viewportWidth/2;
		camera.position.y=camera.viewportHeight/2;
		camera.zoom=1;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		getAbsolutePos(Inputs.instance.absMouse.x, Inputs.instance.absMouse.y);
		
		UIcollision = AllUI.UIpretest();
		
		camera.position.x=campostemp.x;
		camera.position.y=campostemp.y;
		camera.zoom=zoomtemp;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		getAbsolutePos(Inputs.instance.absMouse.x, Inputs.instance.absMouse.y);
		///////////////////////
		
		master.update();
		
		//////////////////////
		camera.position.x=camera.viewportWidth/2;
		camera.position.y=camera.viewportHeight/2;
		camera.zoom=1;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		getAbsolutePos(Inputs.instance.absMouse.x, Inputs.instance.absMouse.y);
		
		AllUI.update();
		
		camera.position.x=campostemp.x;
		camera.position.y=campostemp.y;
		camera.zoom=zoomtemp;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		getAbsolutePos(Inputs.instance.absMouse.x, Inputs.instance.absMouse.y);
		///////////////////////
		batch.end();
		
	}
	
}
