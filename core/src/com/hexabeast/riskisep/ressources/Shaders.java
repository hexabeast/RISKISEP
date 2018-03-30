package com.hexabeast.riskisep.ressources;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.hexabeast.riskisep.Main;

public class Shaders {
	public static ShaderProgram base;
	public static ShaderProgram outline;
	public static ShaderProgram green;
	public static ShaderProgram wave;
	
	public static void loadShaders()
	{
		ShaderProgram.pedantic = false;
		
		base = new ShaderProgram(Gdx.files.internal("shaders/passthroughvertex.fragx"),Gdx.files.internal("shaders/passthrough.fragx"));
		outline = new ShaderProgram(Gdx.files.internal("shaders/passthroughvertex.fragx"),Gdx.files.internal("shaders/outline.fragx"));
		green = new ShaderProgram(Gdx.files.internal("shaders/passthroughvertex.fragx"),Gdx.files.internal("shaders/green.fragx"));
		wave = new ShaderProgram(Gdx.files.internal("shaders/passthroughvertex.fragx"),Gdx.files.internal("shaders/wave.fragx"));
		
		if(!base.isCompiled())System.out.println("base : "+base.getLog());
		if(!outline.isCompiled())System.out.println("outline : "+outline.getLog());
		if(!green.isCompiled())System.out.println("green"+green.getLog());
		if(!wave.isCompiled())System.out.println("wave"+wave.getLog());
		
		setDefaultShader();
	}
	
	public static void setDefaultShader()
	{
		Main.batch.setShader(Shaders.base);
	}
	
	public static void setOutlineShader()
	{
		Main.batch.setShader(Shaders.outline);
	}
	
	public static void setGreenShader()
	{
		Main.batch.setShader(Shaders.green);
	}
	
	public static void setWaveShader()
	{
		
		Main.batch.setShader(Shaders.wave);
		wave.setUniformf("u_time",Main.time,Main.time);
	}
}
