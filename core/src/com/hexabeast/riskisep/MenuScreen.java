package com.hexabeast.riskisep;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Json;
import com.hexabeast.riskisep.gameboard.GameMaster;
import com.hexabeast.riskisep.ia.Probabilities;
import com.hexabeast.riskisep.ressources.TextureManager;
import com.hexabeast.riskisep.ui.AllUI;
import com.hexabeast.riskisep.ui.Button;

public class MenuScreen implements Screen{
	int[] typejoueur = new int[]{0,0,0,0,0,0};
	
	TextField[] txtflds = new TextField[6];
	
	FileHandle file = Gdx.files.local("menusave.json");
	
	int njoueurs = 4;
	
	int offsetY = -120; 
	GlyphLayout layout;
	
	Stage scene;
	
	public MenuScreen()
	{
		
		scene = new Stage();
		Main.inputMultiplexer.addProcessor(scene);
		initscene();
		
		if(!file.file().exists()) {
			try {
				file.file().createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			String loadMenu = file.readString();
			MenuSave msav = new Json().fromJson(MenuSave.class, loadMenu);
			njoueurs = msav.njoueurs;
			typejoueur = msav.types;
			for(int i=0;i<6;i++)
			{
				txtflds[i].setText(msav.noms[i]);
			}
			
		}
		initscene();
		
		layout = new GlyphLayout();
	}
	

	@Override
	public void show() {
		
	}
	
	public void initscene()
	{
		scene.clear();
		for(int i=0;i<6;i++)
		{
			TextFieldStyle tfs = new TextFieldStyle();
			tfs.font = TextureManager.fontMenu2;
			tfs.fontColor = Color.BLACK;
			tfs.selection = new TextureRegionDrawable(new TextureRegion(TextureManager.tex.get("textBoxSurline")));
			tfs.cursor = new TextureRegionDrawable(new TextureRegion(TextureManager.tex.get("textBoxCursor")));
			
			String defaul = "Joueur "+String.valueOf(i+1);
			if(txtflds[i]!=null)defaul=txtflds[i].getText();
			
			txtflds[i] = new TextField(defaul, tfs);
			txtflds[i].setWidth(300);
			txtflds[i].setMaxLength(20);
			
			if(i<njoueurs)scene.addActor(txtflds[i]);
		}
	}

	@Override
	public void render(float delta) {
		Main.batch.begin();
		
		TextureManager.fontMenu.draw(Main.batch, "Nombre de joueurs",Main.windowWidth/2-260,Main.windowHeight-116);
		TextureManager.fontMenu.draw(Main.batch, String.valueOf(njoueurs),Main.windowWidth/2+40,Main.windowHeight-116);
		Button arR = new Button(Main.windowWidth/2+64,Main.windowHeight-140,30,30,TextureManager.tex.get("arrowR"));
		Button arL = new Button(Main.windowWidth/2,Main.windowHeight-140,30,30,TextureManager.tex.get("arrowL"));
		
		arR.update();
		arL.update();
		
		Vector2 mousepos = new Vector2(Gdx.input.getX()/Main.zoomratio,Main.windowHeight-Gdx.input.getY()/Main.zoomratio);
		
		if(arL.isclicked(mousepos))
		{
			njoueurs-=1;
			if(njoueurs<2)njoueurs=2;
			initscene();
		}
		if(arR.isclicked(mousepos))
		{
			njoueurs+=1;
			if(njoueurs>6)njoueurs=6;
			initscene();
		}
		
		for(int i=0;i<njoueurs;i++)
		{
			layout.setText(TextureManager.fontMenu2, txtflds[i].getText());
			txtflds[i].setPosition(Main.windowWidth/2-layout.width-100,Main.windowHeight-120-100*i+offsetY);
		}
			
		
		
		Main.batch.end();
		scene.act();
		scene.draw();
		Main.batch.begin();
		
		for(int i=0;i<njoueurs;i++)
		{
			arR = new Button(Main.windowWidth/2+80,Main.windowHeight-124-100*i+offsetY,30,30,TextureManager.tex.get("arrowR"));
			arL = new Button(Main.windowWidth/2-80,Main.windowHeight-124-100*i+offsetY,30,30,TextureManager.tex.get("arrowL"));
			
			arR.update();
			arL.update();
			
			
			
			if(arL.isclicked(mousepos) || arR.isclicked(mousepos))
			{
				typejoueur[i]=1-typejoueur[i];
				
			}
			//TextureManager.fontMenu.draw(Main.batch, "",Main.windowWidth/2-200,Main.windowHeight-100-100*i+offsetY);
			
			if(typejoueur[i]==0)
			{
				TextureManager.fontMenu.draw(Main.batch, "Humain",Main.windowWidth/2-22,Main.windowHeight-100-100*i+offsetY);
			}
			else TextureManager.fontMenu.draw(Main.batch, " IA",Main.windowWidth/2-20,Main.windowHeight-100-100*i+offsetY);
			
		}
		
		
		Button confirm = new Button(Main.windowWidth/2-100,Main.windowHeight-850,200,45,Color.BLACK,"Confirmer");
		confirm.update(mousepos);
		
		if(confirm.isclicked(mousepos))
		{
			boolean ok = true;
			for(int i=0;i<njoueurs;i++)
			{
				if(txtflds[i].getText().length()==0)ok=false;
			}
			if(ok)
			{
				
				
				for(int i=0;i<6;i++)
				{
					AllUI.teamname[i] = txtflds[i].getText();
				}
				
				String msav = new Json().toJson(new MenuSave(njoueurs,AllUI.teamname,typejoueur));
				file.writeString(msav, false);
				
				GameMaster.humanstart = new int[njoueurs];
				for(int i=0;i<njoueurs;i++)GameMaster.humanstart[i]=1-typejoueur[i];
				Main.gamestate=1;
				GameScreen.master.resetGame();
				scene.clear();
				Main.inputMultiplexer.removeProcessor(scene);
			}
			
		}
		
		Main.batch.end();
	}

	@Override
	public void resize(int width, int height) {
		scene.getViewport().update(width, height);
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
