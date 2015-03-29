package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleHouse extends PuzzleScene {

	public PuzzleHouse(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/house.atlas");
		xmlFile = Gdx.files.internal("xml/house.xml");		
		super.createScene();
		
		

	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleHouse");
		if (!game.isPremium()) {
			game.setScreen(game.menu2d);
			game.menu2d.showPayFrame();
		} else {
			game.setScreen(new PuzzleBalloons(game));
		}
		
		super.dispose();
	}


}
