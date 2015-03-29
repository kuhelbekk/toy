package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleRobot extends PuzzleScene {

	public PuzzleRobot(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/robot.atlas");
		xmlFile = Gdx.files.internal("xml/robot.xml");		
		super.createScene();	
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleRobot");
		game.setScreen(new PuzzleCube(game));
		super.dispose();
	}


}
