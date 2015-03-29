package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleBalloons extends PuzzleScene {

	public PuzzleBalloons(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/balloons.atlas");
		xmlFile = Gdx.files.internal("xml/balloons.xml");		
		super.createScene();
		
		

	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleBalloons");
		game.setScreen(new PuzzleHorse(game));
		super.dispose();
	}


}
