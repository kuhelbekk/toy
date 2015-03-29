package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleBall extends PuzzleScene {

	public PuzzleBall(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/ball.atlas");
		xmlFile = Gdx.files.internal("xml/ball.xml");		
		super.createScene();	
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleBall");
		game.setScreen(new PuzzleCar(game));
		super.dispose();
	}


}
