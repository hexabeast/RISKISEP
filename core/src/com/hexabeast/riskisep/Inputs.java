package com.hexabeast.riskisep;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class Inputs implements InputProcessor{
	public static Inputs instance;
	public boolean mouseup;
	private boolean tomouseup;
	
	public boolean mousedown;
	private boolean tomousedown;
	
	public boolean leftmousedown;
	private boolean toleftmousedown;
	
	public boolean rightmousedown;
	private boolean torightmousedown;
	
	public boolean leftmouseup;
	private boolean toleftmouseup;
	
	public boolean rightmouseup;
	private boolean torightmouseup;
	
	public boolean middlePressed;
	public boolean tomiddlePressed;
	
	public boolean middleUp;
	public boolean tomiddleUp;
	
	public boolean CTRL = false;
	public boolean Q = false;
	public boolean Z = false;
	public boolean S = false;
	public boolean D = false;
	public boolean shift = false;
	public boolean space = false;
	public boolean spacePressed = false;
	
	public boolean leftpress = false;
	public boolean rightpress = false;
	
	public Vector2 absMouse = new Vector2();
	
	
	///////
	public static final int QWERTY = 0;
	public static final int AZERTY = 1;
	
	private static int kA;
	private static int kW;
	@SuppressWarnings("unused")
	private static int kZ;
	@SuppressWarnings("unused")
	private static int kQ;
	@SuppressWarnings("unused")
	private static int kM;
	
	public static void setKeyboard(int k)
	{
		if(k==QWERTY)
		{
			kA = Input.Keys.A;
			kW = Input.Keys.W;
			kZ = Input.Keys.Z;
			kQ = Input.Keys.Q;
			kM = Input.Keys.M;
		}
		else if(k==AZERTY)
		{
			kA = Input.Keys.Q;
			kW = Input.Keys.Z;
			kZ = Input.Keys.W;
			kQ = Input.Keys.A;
			kM = Input.Keys.SEMICOLON;
		}
	}
	////////
	
	public Inputs()
	{
		setKeyboard(AZERTY);
	}
	
	public void computeAbsMouse()
	{
		absMouse.set(Gdx.input.getX(),Gdx.input.getY());	
	}
	
	@Override
	public boolean keyTyped(char character) {return false;}
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		tomousedown = true;
		
		if (button == Input.Buttons.RIGHT)torightmousedown = true;
		if (button == Input.Buttons.LEFT)toleftmousedown = true;
		
		if(button == Input.Buttons.MIDDLE)tomiddlePressed = true;
		
		return false;
	}


	public void update()
	{
		computeAbsMouse();
		
		leftpress = Gdx.input.isButtonPressed(Input.Buttons.LEFT);
		rightpress = Gdx.input.isButtonPressed(Input.Buttons.RIGHT);
		
		if(tomouseup)
		{
			mouseup = true;
			tomouseup = false;
		}
		else
		{
			mouseup = false;
		}
		
		if(toleftmouseup)
		{
			leftmouseup = true;
			toleftmouseup = false;
		}
		else
		{
			leftmouseup = false;
		}
		
		if(tomiddlePressed)
		{
			middlePressed = true;
			tomiddlePressed = false;
		}
		else
		{
			middlePressed = false;
		}
		
		if(tomiddleUp)
		{
			middleUp = true;
			tomiddleUp = false;
		}
		else
		{
			middleUp = false;
		}
		
		if(torightmouseup)
		{
			rightmouseup = true;
			torightmouseup = false;
		}
		else
		{
			rightmouseup = false;
		}
		
		if(tomousedown)
		{
			mousedown = true;
			tomousedown = false;
		}
		else
		{
			mousedown = false;
		}

		
		if(toleftmousedown)
		{
			leftmousedown = true;
			toleftmousedown = false;
		}
		else
		{
			leftmousedown = false;
		}
		
		if(torightmousedown)
		{
			rightmousedown = true;
			torightmousedown = false;
		}
		else
		{
			rightmousedown = false;
		}
		
	}
	
	public void updateLate()
	{
		if(space)space = false;
	}


	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		tomouseup = true;
		if (button == Input.Buttons.RIGHT)torightmouseup = true;
		if (button == Input.Buttons.LEFT)toleftmouseup = true;
		if (button == Input.Buttons.MIDDLE)tomiddleUp = true;
		return false;}
	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
	@Override
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	@Override
	public boolean scrolled(int amount) 
	{
		GameScreen.zoom(amount);
		return false;
	}
	
	
	@Override
	public boolean keyDown(int keycode) 
	{

		if(keycode == kA)
		{
			Q = true;
		}
		else if(keycode == kW)
		{
			Z = true;
		}
		else if(keycode == Keys.D)
		{
			D = true;
		}
		else if(keycode == Keys.S)
		{
			S = true;
		}
					
		switch(keycode){
		
		case Keys.SPACE:
			space = true;
			spacePressed = true;
			break;
			
		case Keys.SHIFT_LEFT:
			shift = true;
			break;	
			
		case Keys.C:
			break;
			
		case Keys.CONTROL_LEFT:
			CTRL = true;
			break;
			
		case Keys.T:
			break;
			
		case Keys.ENTER:
			break;
		case Keys.NUM_0:	
			break;
		case Keys.NUM_1:
			break;
		case Keys.NUM_2:
			break;
		case Keys.NUM_3:
			break;
		case Keys.NUM_4:
			break;
		case Keys.NUM_5:
			break;
		case Keys.NUM_6:
			break;
		case Keys.NUM_7:
			break;
		case Keys.NUM_8:
			break;
		case Keys.NUM_9:
			break;
		}
			
		return false;
	}
	

	@Override
	public boolean keyUp(int keycode) {
					
		if(keycode == kA)
		{
			Q = false;
		}
		else if(keycode == kW)
		{
			Z = false;
		}
		else if(keycode == Keys.D)
		{
			D = false;
		}
		else if(keycode == Keys.S)
		{
			S = false;
		}
		
		switch(keycode){
		
		case Keys.I:
			break;
		case Keys.SHIFT_LEFT:
			shift = false;
			break;
		case Keys.CONTROL_LEFT:
			CTRL = false;
			break;
		case Keys.SPACE:	
			spacePressed = false;
			break;
		case Keys.H:
			break;
		}
			
		return false;
		
	}
}