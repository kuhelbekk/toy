package com.starbox.puzzletoy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;


public class PuzzleYula extends PuzzleScene {

	public PuzzleYula(MainClass game) {
		super(game);
	}

	public void createScene() {
		textureAtlas = new TextureAtlas("data/yula.atlas");
		xmlFile = Gdx.files.internal("xml/yula.xml");		
		super.createScene();	
	}

	protected void endScene() {
		Gdx.app.log("EndScene", "EndPuzzleYula");		
		game.setScreen(new PuzzleRocket(game));
		super.dispose();
	}


}
