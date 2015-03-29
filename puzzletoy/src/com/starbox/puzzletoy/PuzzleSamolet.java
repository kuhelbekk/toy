package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleSamolet extends PuzzleScene {

	public PuzzleSamolet(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/samolet.atlas");
		xmlFile = Gdx.files.internal("xml/samolet.xml");
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleSamolet");
		game.setScreen(new PuzzleRobot(game));
		super.dispose();
	}


}
