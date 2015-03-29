package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleHorse extends PuzzleScene {

	public PuzzleHorse(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/horse.atlas");
		xmlFile = Gdx.files.internal("xml/horse.xml");		
		super.createScene();
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleHorse");
		if (!game.isPremium()) {
			game.setScreen(game.menu2d);
			game.menu2d.showPayFrame();
		} else {
			game.setScreen(new PuzzlePyramid(game));
		}		
		super.dispose();
	}


}
