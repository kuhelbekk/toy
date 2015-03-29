package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleHelicopt extends PuzzleScene {

	public PuzzleHelicopt(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/helicopt.atlas");
		xmlFile = Gdx.files.internal("xml/helicopt.xml");		
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleHelicopt");
		game.setScreen(new PuzzleShip(game));
		super.dispose();
	}


}
