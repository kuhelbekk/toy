package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleFish extends PuzzleScene {

	public PuzzleFish(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/fish.atlas");
		xmlFile = Gdx.files.internal("xml/fish.xml");		
		super.createScene();	
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleFish");
		game.setScreen(new PuzzleSamolet(game));
		super.dispose();
	}


}
