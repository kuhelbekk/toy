package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleCube extends PuzzleScene {

	public PuzzleCube(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/cubes.atlas");
		xmlFile = Gdx.files.internal("xml/cubes.xml");		
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleCubes");
		game.setScreen(new PuzzleYula(game));
		super.dispose();
	}


}
