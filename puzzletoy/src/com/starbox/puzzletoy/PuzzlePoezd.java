package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PuzzlePoezd extends PuzzleScene {

	public PuzzlePoezd(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/poezd.atlas");
		xmlFile = Gdx.files.internal("xml/poezd.xml");
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzlePoezd");			
		game.setScreen(new PuzzleHouse(game));
		super.dispose();
		
	}


}
