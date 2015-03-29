package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class PuzzleDuck extends PuzzleScene {

	public PuzzleDuck(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/duck.atlas");
		xmlFile = Gdx.files.internal("xml/duck.xml");		
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleDuck");
		game.setScreen(new PuzzleHelicopt(game));
		super.dispose();
	}


}
