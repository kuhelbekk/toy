package com.starbox.puzzletoy;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class PuzzleScene implements Screen {
	int screenHeight;
	int screenWidth;
	public int realHeight;
	public int realWidth;
//	private int dx, dy; // �������� ����
	private int pfdx, pfdy; // ��������  ����� �� ������
	public Stars stars,fStars;/// ����� ��� ��������� ������
	private Viewport viewport;
	protected SpriteBatch batch;
	protected TextureAtlas textureAtlas;
	protected MainClass game;
	protected PuzzleElement selectedElement;
	protected ArrayList<PuzzleElement> elementList;
	protected FileHandle xmlFile;
	// private FPSLogger fpsLog;
    protected SameElements sameElements;
	protected Button btnDn, btnBack;
	protected Stage stage;
	protected Music mFon;
	protected Sound sGood, sCandy, sStartSound;
	protected ArrayList<Candy> listCandy;	
	protected Image finishRays;
	protected Image puzzleFrame;
	protected Image shelf; //�����
	long timeToFonMusic=0;
	float fonSoundLevel = 1;
	long timeToGood=0;
	
	protected boolean visibleFinishScene;

	protected World world;	
	
	float PIXELS_TO_METERS =31;


	
	protected Button nextLevelButton;
	
	protected abstract void endScene();
	
	public PuzzleScene(MainClass game) {
		super();
		this.game = game;
		this.createScene();
		this.btnBack.setZIndex(150);
	}	
	

	public void createScene() {
		Gdx.app.log("Game", "createScene");
		visibleFinishScene = false;
		elementList = new ArrayList<PuzzleElement>();
		// MouseDown=false;			
		/// ������ �������� ����� � ����
		realWidth = Gdx.graphics.getWidth();
		realHeight = Gdx.graphics.getHeight();
		if ((realHeight > 1200) && (realWidth > 2000)) { // ���������� ����� �  ����������� ����															
			realHeight /= 2;
			realWidth /= 2;
		}
		screenWidth = 1450;
		if ((realHeight <= 800) && (realHeight >= 720)) {
			screenHeight = realHeight;
		} else {
			screenHeight = 800;
		}
	      
		float a = (float) realWidth / screenWidth;
		float b = (float) realHeight / screenHeight;		
		if (a < b)
			screenWidth = (screenHeight * realWidth) / realHeight;
		if (screenWidth < 980)
			screenWidth = 980;
		if (screenWidth > 1450)
			screenWidth = 1450;
		
		Gdx.app.log("start", "realWidth = " + realWidth + " realHeight = "				+ realHeight);
		Gdx.app.log("start", "screenWidth = " + screenWidth				+ " screenHeight = " + screenHeight);
		
		batch = new SpriteBatch();
		viewport = new FitViewport(screenWidth, screenHeight);
		stage = new Stage(viewport,batch) {
			@Override
			public boolean keyDown(int keyCode) {				
				if (keyCode == Keys.BACK) {
					BackClick();
				}				
				return super.keyDown(keyCode);
			}
		};			
		
		world = new World(new Vector2(0, -2.5f), true); ///////box2d//////////////////////////////////////////////////////////////////*		
		
		Image bg = new Image(game.commonAtlas.findRegion("bg"));
		stage.addActor(bg);			
		
		TextureRegion region =game.commonAtlas.findRegion("polka");
		region.setRegionHeight(screenHeight);
		shelf = new Image(region);
		stage.addActor(shelf);
		shelf.setPosition(0,0);		
		
		/// �������� ���
		finishRays = new Image(game.commonAtlas.findRegion("endGameRays"));
		finishRays.setPosition((screenWidth - finishRays.getWidth()) / 2,	(screenHeight - finishRays.getHeight()) / 2);
		finishRays.setVisible(false);		
		stage.addActor(finishRays);
		
		puzzleFrame = new Image(textureAtlas.findRegion("frame"));
		stage.addActor(puzzleFrame);
		
		int dx, dy;
		dx =(int) (((screenWidth-shelf.getWidth()-bg.getWidth()) / 2)+shelf.getWidth());		
		dy =(int) (screenHeight -bg.getHeight())/2;	
		bg.setPosition(dx,dy);
		//bg.setVisible(false);
		//740*720
		pfdx=(int)(((screenWidth -shelf.getWidth()-740)/2)+shelf.getWidth());
		pfdy=(int)(screenHeight - 720)/2; 
		sameElements = new SameElements();
		try {		    
			XmlReader xmlReader = new XmlReader();			
		    XmlReader.Element root = xmlReader.parse(xmlFile); 		    
		    XmlReader.Element framexml = root.getChildByName("frame");		    
		   	int fx =framexml.getInt("x");
		    int fy =framexml.getInt("y");
		    String startSoundName = framexml.get("s");		    
		    if (game.settings.isSound() & game.settings.isVoice() & (!startSoundName.equals(""))) {			    	
				sStartSound = Gdx.audio.newSound(Gdx.files.internal("mfx/"+startSoundName+game.getLangStr()+".mp3"));
			}	    
		    
		    puzzleFrame.setPosition(pfdx+fx,screenHeight- (puzzleFrame.getHeight()+pfdy+fy));
		    puzzleFrame.setName("Frame");	
		    int fz= puzzleFrame.getZIndex();
		    
		    XmlReader.Element elements = root.getChildByName("assets");
		    int ecount = elements.getChildCount();
		    ///// ������� ������ ����
		    XmlReader.Element element = root.getChildByName("bg");
		    int bgElement=0;
		    PuzzleElement peBG = null;
		    if(element!=null){
		    	++ecount;
		    	++bgElement;
		    	int x =element.getInt("x");
		    	int y =element.getInt("y");		    		    		
		    	peBG = new PuzzleElement(this, textureAtlas.findRegion("100"),ecount,pfdx+x, screenHeight - y-pfdy, ecount+ecount+1+fz);		    	
		    	elementList.add(peBG);		    	
		    }
		   
		    int[] randomArray = new int[ecount-bgElement];
		    for (int i=0; i<(ecount-bgElement);i++ )randomArray[i]=i+1;		    
		    for (int i=0; i<(ecount-bgElement);i++ ){		    	
		    		int rnd = (int)(Math.random()*(ecount-bgElement));		                  		           
		            int temp = randomArray[i];
		            randomArray[i] = randomArray[rnd];
		            randomArray[rnd] = temp;		            
		        }		    
		    
		    for (int i=0; i<(ecount-bgElement);i++ ){
		    	/// ������ xml
		    	element = elements.getChild(i);
		    	String nameReg=element.get("id");
		    	int x =element.getInt("x");
		    	int y =element.getInt("y");		    	
		    	int se = element.getInt("se", -1);	
		    	boolean crop = element.getBoolean("crop", false);		    	
		    	String soundName = element.get("s");		    	
		    	/// ������� ��������
		    	PuzzleElement pe;		    	
		    	if (crop) {
		    		pe = new PuzzleElement(this, textureAtlas.findRegion(nameReg),textureAtlas.findRegion(nameReg+"_crop"), randomArray[i],pfdx+x, screenHeight - y-pfdy,i+1+ecount+fz, soundName);
		    	}else {
		    		pe = new PuzzleElement(this, textureAtlas.findRegion(nameReg), randomArray[i],pfdx+x, screenHeight - y-pfdy,i+1+ecount+fz, soundName);
		    	}
		    	elementList.add(pe);
		    	if (se>=0) {
		    		sameElements.put(se,pe);
		    		Gdx.app.log("sameElements", "SE="+se+ "  NE="+ nameReg);
		    	}
		    	pe.image.setName("I"+nameReg);
		    	pe.shadow.setName("S"+nameReg);		    	
		    } 		    
		    
		    
		    puzzleFrame.setZIndex(ecount+fz+1);
		    refreshZindElements();
		    if (bgElement>0) peBG.fixing(false);
		    			    
		} catch (IOException e) {			
			e.printStackTrace();
		}

		if (game.settings.isMusic() & game.settings.isSound()) {
			long rnd = Math.round((Math.random() * 2));
			mFon = Gdx.audio.newMusic(Gdx.files.internal("mfx/s" + rnd + ".mp3"));			
			mFon.setVolume(0.01f);			
			mFon.setLooping(true);	
			mFon.play();
		}

		
		
		if (game.settings.isSound()) {			
			sCandy = Gdx.audio.newSound(Gdx.files.internal("mfx/candy.mp3"));
			
			if (game.settings.isVoice()) {	
				long rnd = Math.round((Math.random() * 3));
				sGood = Gdx.audio.newSound(Gdx.files.internal("mfx/good"+rnd+game.getLangStr()+".mp3"));
			}			
		}

		// /back
		ButtonStyle bs = new ButtonStyle();
		bs.up = game.commonSkin.getDrawable("btn_back_up");
		bs.down = game.commonSkin.getDrawable("btn_back_dn");
		btnBack = new Button(bs);
		btnBack.setPosition(screenWidth-btnBack.getWidth(),screenHeight-btnBack.getHeight());
		stage.addActor(btnBack);
		btnBack.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				BackClick();
			}
		});
		
		/// �������� ������
		stage.addAction(Actions.parallel(
				       Actions.sequence(Actions.scaleTo(1.5f,1.5f),						   			   Actions.scaleTo(1,1, 0.4f,Interpolation.pow2Out)  ),
				       Actions.sequence(Actions.alpha(0),											   Actions.alpha(1, 0.4f)  ),
				       Actions.sequence(Actions.moveTo(-screenWidth/4, -screenHeight/4), Actions.moveTo(0,0, 0.4f,Interpolation.pow2Out)  )
				));
		//stage.addAction(Actions.sequence(Actions.moveTo(stage.getWidth(), 0),Actions.moveTo(0, 0, 0.9f, Interpolation.exp5)));
		
		stars = new Stars(stage,game.commonAtlas,false);
		
		if ((sStartSound != null)) {			
			while (sStartSound.play(1f)==-1){
				Gdx.app.log("sStartSound", "sStartSound -1");
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			timeToFonMusic = TimeUtils.millis();			
		}else{
			timeToFonMusic = TimeUtils.millis()-1000;	
		}
		
		//showCandy();
		
		if (game.settings.isVoice()) {
			fonSoundLevel=0.6f;
		}else{
			fonSoundLevel=1f;
		}

		
	}

	@Override
	public void show() {
		Gdx.app.log("Game", "show Scene");
		
		Gdx.input.setInputProcessor(stage);		
	}

	@Override
	public void render(float delta) {		
		/// ��������� ������� ������ ����� ������� �������
		if ((timeToFonMusic>0) & (mFon!=null)){
			if ((timeToFonMusic+2000)<TimeUtils.millis()) {				
					mFon.setVolume(fonSoundLevel);
					timeToFonMusic=0;				
			}else{
				//Gdx.app.log("Game", "setVolume = "+(((float)(TimeUtils.millis()-timeToFonMusic))/2000f));
				 mFon.setVolume((((float)(TimeUtils.millis()-timeToFonMusic))/2000f)*fonSoundLevel);
			}
		}
		if (timeToGood>0)
			if ((timeToGood+1500)<TimeUtils.millis()) {
				if (sGood != null)
					sGood.play(1f);	
				timeToGood=0;
			}
		
		world.step(1f/60f, 0, 0);  		
		Gdx.gl.glClearColor(1,1,1,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		

		if (visibleFinishScene) {// / ���� �������
			drawFinish();
		}	
		
		
		if (listCandy != null) {
			if (!listCandy.isEmpty()) { // �������� �������� � ��������� �������
				Iterator<Candy> iterator = listCandy.iterator();
				while (iterator.hasNext()) {
					Candy b = (Candy) iterator.next();
					if (b.isFinished()) {
						b.remove();
						iterator.remove();
					}else{
						// box2d	
						b.render(PIXELS_TO_METERS);	
					}
				}
			}				
		}

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// Gdx.app.log("Game", "resize Scene Car");
		stage.getViewport().update(width, height,true);
	}

	@Override
	public void hide() {
		Gdx.app.log("Game", "hide Scene ");
	}

	@Override
	public void pause() {
		Gdx.app.log("Game", "pause Scene ");
	}

	@Override
	public void resume() {
		Gdx.app.log("Game", "resume Scene ");
	}

	@Override
	public void dispose() {	
		Gdx.app.log("Game", "dispose Scene ");
		if (mFon != null){
			mFon.stop();
			mFon.dispose();
		}
		if (sGood != null)
			sGood.dispose();
		if (sCandy != null)
			sCandy.dispose();
		for (PuzzleElement p : elementList) {
			if (p !=null )
				p.dispose();
		}
		// if (sError != null)sError.dispose();
		world.dispose();
		batch.dispose();
		textureAtlas.dispose();
		stage.dispose();
	}
	

	public void elementMounted(PuzzleElement pe) {
		int i = pe.index;
		pe.index = 0;
		refreshZindElements();
		boolean fEndScene = true;
		for (PuzzleElement p : elementList) {
			if (p.index > i) {
				--p.index;
				p.setPosToStartPoint(0);
			}
			if (p.index > 0)
				fEndScene = false;
		}
		if (fEndScene) {
			if (mFon != null)
				if (mFon.isPlaying())
					mFon.stop();			
			visibleFinishScene = true;
		}
	}
	
	
	
	private void refreshZindElements() {		
		for (PuzzleElement p : elementList) {
			p.refreshZindex(elementList.size());
		}
		
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	protected void showCandy() {
		listCandy = new ArrayList<Candy>();				
		for (int c = 0; c < 32; c++) { // ���������� ������
			TextureRegion[] CandyFrames = game.GetAnimFrames("candy"+(int)(Math.random()*4), 188, 134); // �������� ������� ������ ��� ��������
			Animation anim = new Animation(0.04f, CandyFrames); // ������� ��������	 ��������
			AnimationDrawable drawable = new AnimationDrawable(anim); // �������� �����������
			Candy b = new Candy(drawable, sCandy , game.commonAtlas,stage); // / ��������� ������
			listCandy.add(b); // ���� �������
			
			float x = (float)Math.random() * (screenWidth );
			if (x>(screenWidth/2) )
				x+=screenWidth/4;
				else x-=screenWidth/4;
				 
			b.setPosition(x,(float)(-Math.random()*400-100	)); // ��������� �������			
			b.setZIndex(300);
			//b.setScale(0.3f + (float) (Math.random() / 2));
			stage.addActor(b);
			b.addListener(new ClickListener() { // ��������� �� ������
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					//((Candy) (event.getListenerActor())).body.applyLinearImpulse(0f, 0.5f, ((Candy) (event.getListenerActor())).body.getPosition().x,  
					//																	   ((Candy) (event.getListenerActor())).body.getPosition().y,true);
					((Candy) (event.getListenerActor())).clickCandy();
					return true;
				}
			});		

			
			/// box2d			
			BodyDef bodyDef = new BodyDef();
		    bodyDef.type = BodyDef.BodyType.DynamicBody;		     
		    bodyDef.position.set((b.getX() + 188/2) /PIXELS_TO_METERS,
		    					(b.getY() + 134/2) / PIXELS_TO_METERS);		    
		    b.body = world.createBody(bodyDef);
		    PolygonShape shape = new PolygonShape();
		    shape.setAsBox((9) / PIXELS_TO_METERS, (7) / PIXELS_TO_METERS);
		    FixtureDef fixtureDef = new FixtureDef();
	        fixtureDef.shape = shape;	        
	        fixtureDef.density = 0.1f;
	        b.body.createFixture(fixtureDef);
	        shape.dispose();
	        b.body.applyTorque(((float)Math.random()*0.2f)-0.1f, true);
	        
	        b.body.applyLinearImpulse( ((b.getX()-(screenWidth/2))*((float)Math.random())*(-0.01f))/PIXELS_TO_METERS ,(float)Math.random()*0.1f+0.25f, b.body.getPosition().x,  b.body.getPosition().y,true);
	      //  b.body.applyForce(10, 10,b.body.getPosition().x, b.body.getPosition().y, true);
	   
		}
		
		 
	}



	
	public int getPFDx() {
		return pfdx;
	}

	public int getPFDy() {
		return pfdy;
	}
	

	public void showFinishScreen() {
		Gdx.app.log("Game", "showFinishScreen");
		// ����� �����
		puzzleFrame.addAction(Actions.parallel(
				Actions.alpha(0.001f, 1.5f, Interpolation.sine),
				Actions.moveBy((-shelf.getWidth()/2), 0 , 1.5f,Interpolation.sine)
				));		
		// ������� ����
		for(PuzzleElement pe:elementList){
			pe.shadow.addAction(Actions.moveBy((-shelf.getWidth()/2), 0 , 1.5f,Interpolation.sine));
		}
		
		finishRays.setColor(1, 1, 1, 0);	
		finishRays.setVisible(true);
		// ����� ������ 
		finishRays.addAction(Actions.sequence(
				Actions.alpha(0,1.3f),
				Actions.alpha(1, 0.7f,Interpolation.sineOut)
				));
		
		
		
		fStars = new Stars(stage, game.commonAtlas,true);
		fStars.playFinishStar((screenWidth)/2,(screenHeight)/2);
		
		
		
		/// ����� ������
		ButtonStyle bs = new ButtonStyle();
		bs.up = game.commonSkin.getDrawable("btn_next_up");
		bs.down = game.commonSkin.getDrawable("btn_next_dn");
		nextLevelButton = new Button(bs);		
		nextLevelButton.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				endScene();
			}
		});		
		
		
		nextLevelButton.setColor(1, 1, 1, 0.01f);	
		float brSize = 217;
		nextLevelButton.setSize(brSize*1.2f, brSize*1.2f); // *1.2
		nextLevelButton.setPosition(screenWidth/2 - (brSize*0.6f), screenHeight/2 - (brSize*0.6f));
		/// ������  �� ��������
		nextLevelButton.addAction(Actions.sequence(
							Actions.alpha(0,3),
							Actions.parallel(
									Actions.alpha(0.5f,0.15f),
									Actions.sizeTo(brSize*0.9f, brSize*0.9f, 0.15f),
									Actions.moveTo(screenWidth/2 - (brSize*0.45f), screenHeight/2 - (brSize*0.45f) , 0.15f)),
							Actions.parallel(
									Actions.alpha(1,0.05f),
									Actions.sizeTo(brSize, brSize, 0.05f),
									Actions.moveTo(screenWidth/2 - brSize/2, screenHeight/2 - brSize/2 , 0.05f))
							));	
		
		stage.addActor(nextLevelButton);
		timeToGood=TimeUtils.millis();
		
		
		
		
		if (game.settings.isMusic() & game.settings.isSound()) {
			mFon.stop();
			mFon.dispose();			
			mFon = Gdx.audio.newMusic(Gdx.files.internal("mfx/fon_finish.mp3"));			
			mFon.setVolume(0.01f);			
			mFon.setLooping(true);	
			mFon.play();		
			timeToFonMusic = TimeUtils.millis();	
		}
	}

	private void drawFinish() {
		if (nextLevelButton == null) { // / ���� �� ��������				
					showFinishScreen();		
						
		} else { // ///// ���������� �������
			
			if (nextLevelButton.getActions().size==0) {
				if (listCandy==null) {					
					showCandy();	
				}
			} else {
				

			}

		}
	}

	public void BackClick() {
		Gdx.app.log("BtnClick", "exit PuzzleScene");
		game.setScreen(game.menu2d);
		dispose();
		
	
	}	
	
}
