package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleRocket extends PuzzleScene {

	public PuzzleRocket(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/rocket.atlas");
		xmlFile = Gdx.files.internal("xml/rocket.xml");		
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleRocket");		
		game.setScreen(new PuzzlePoezd(game));
		super.dispose();
	}


}
