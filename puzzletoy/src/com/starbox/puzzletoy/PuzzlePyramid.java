package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PuzzlePyramid extends PuzzleScene {

	public PuzzlePyramid(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/pyramid.atlas");
		xmlFile = Gdx.files.internal("xml/pyramid.xml");
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzlePyramid");
		//BackClick();
		game.setScreen(new PuzzleBall(game));
		super.dispose();
	}


}
