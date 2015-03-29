package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleShip extends PuzzleScene {

	public PuzzleShip(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/ship.atlas");
		xmlFile = Gdx.files.internal("xml/ship.xml");		
		super.createScene();
		
		

	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleShip");
		game.setScreen(new PuzzleFish(game));
		super.dispose();
	}


}
