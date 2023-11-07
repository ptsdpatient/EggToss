package com.egg.toss;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class EggToss extends ApplicationAdapter {
	SpriteBatch batch;
	Sprite player;
	Array<Nest> nestArray=new Array<Nest>();
	Rectangle playerBounds;
	Texture playerTexture,nestTexture,bgTexture;
	boolean jump=false,fall=false,nextLevel=false;
	float acceleration=0;
	int targetNest=0;

	public class Nest{
		private Sprite nest = new Sprite(nestTexture);

		private Rectangle nestBounds;
		private boolean rightDirection;
		private float speed;
		public Nest(float x,float y,float speed,boolean rightDirection){
			this.rightDirection=rightDirection;
			this.speed=speed;
			nest.setPosition(x,y);
			nest.setOrigin(nest.getWidth()/2,nest.getHeight()/2);

		}
		public void render(SpriteBatch batch){
			nest.draw(batch);

			if(!nextLevel){
				if ((rightDirection)) {
					nest.setPosition(nest.getX() + 2f*speed, nest.getY());
					if(nest.getX()>Gdx.graphics.getWidth()-nest.getWidth())rightDirection=false;
				} else {
					nest.setPosition(nest.getX()-2f*speed, nest.getY());
					if(nest.getX()<0)rightDirection=true;
				}
			} else {nest.setPosition(nest.getX(),nest.getY()-2f);
		}}
		public Rectangle getRectangle(){
			this.nestBounds= nest.getBoundingRectangle();
			return nestBounds;
		}
	}


	@Override
	public void create () {
		//Gdx.graphics.setWindowedMode(360,480);
		Gdx.input.setInputProcessor(inputProcessor);
		Gdx.graphics.setResizable(false);
		batch = new SpriteBatch();
		bgTexture=new Texture("bg.png");
		nestTexture=new Texture("nest.png");
		playerTexture = new Texture("player.png");
		player=new Sprite(playerTexture);
		player.setOrigin(player.getWidth()/2,player.getHeight()/2);
		Nest nest1=new Nest(50,25,1,true);
		Nest nest2 = new Nest(50,25+100* MathUtils.random(1,3),0.5f,false);
		Nest nest3=new Nest(50,25+100* MathUtils.random(3,5),0.5f,false);
		nestArray.add(nest1,nest2,nest3);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);

		if(!jump) player.setPosition(nestArray.get(targetNest).nest.getX()+nestArray.get(targetNest).nest.getWidth()/4,nestArray.get(targetNest).nest.getY()+nestArray.get(targetNest).nest.getHeight()/2 );
		else if(!nextLevel){
			player.setPosition(player.getX(), player.getY()+acceleration);
			player.setRotation(player.getRotation()+15f);
			if(acceleration>-10)acceleration+=(!fall)?0.5f: -0.5f;
			if(player.getY()>(float)Gdx.graphics.getHeight()/3.8) {fall=true;}
		}

		if(nextLevel){
			if(nestArray.get(targetNest).nest.getY()<=25){
				nextLevel=false;
				nestArray.removeIndex(targetNest-1);
				targetNest=0;
				Gdx.app.log("",""+targetNest+" " + nestArray);
		}
		}

			playerBounds=player.getBoundingRectangle();

		if(fall){
			for(Nest nest :nestArray){
				if(acceleration<10 && playerBounds.overlaps(nest.getRectangle())){
					targetNest= nestArray.indexOf(nest,true);
					player.setRotation(0);
					jump=false;
					fall=false;
					nextLevel=true;
					nestArray.add(new Nest(MathUtils.random(0,Gdx.graphics.getWidth()-nest.nest.getWidth()),Gdx.graphics.getHeight()+25*MathUtils.random(0,2),MathUtils.random(0.1f,2.2f),false));
					Gdx.app.log("","next level : " + nextLevel);
				}
			}
		if(player.getY()<-20){
			player.setRotation(0);
			jump=false;
			fall=false;
			nextLevel=false;
		}
		}
		batch.begin();
		batch.draw(bgTexture,0,0);
		player.draw(batch);
		for(Nest nest : nestArray){
			nest.render(batch);
		}
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		playerTexture.dispose();
		nestTexture.dispose();
		bgTexture.dispose();
	}


	InputProcessor inputProcessor = new InputProcessor() {
		@Override
		public boolean keyDown(int keycode) {
			if((keycode== Input.Keys.UP || keycode==Input.Keys.SPACE)&& !jump && !nextLevel) {jump=true;fall=false;acceleration=2;}
			return false;
		}

		@Override
		public boolean keyUp(int keycode) {
			return false;
		}

		@Override
		public boolean keyTyped(char character) {
			return false;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchUp(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
			return false;
		}

		@Override
		public boolean touchDragged(int screenX, int screenY, int pointer) {
			return false;
		}

		@Override
		public boolean mouseMoved(int screenX, int screenY) {
			return false;
		}

		@Override
		public boolean scrolled(float amountX, float amountY) {
			return false;
		}
	};
}
