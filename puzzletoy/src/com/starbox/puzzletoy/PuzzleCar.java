package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleCar extends PuzzleScene {

	public PuzzleCar(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/car.atlas");
		xmlFile = Gdx.files.internal("xml/car.xml");		
		super.createScene();	
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleCar");
		game.setScreen(new PuzzleDuck(game));
		super.dispose();
	}


}
