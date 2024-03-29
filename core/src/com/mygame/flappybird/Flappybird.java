package com.mygame.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.show;

public class Flappybird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bird;
	Texture bird2;
	Texture background;
	Texture bottomtube;
	Texture toptube;
	Texture birdimg[] = new Texture[2];
	Texture gameover;
	Circle birdCircle;
	ShapeRenderer shapeRenderer;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	int gamestate = 0;
	float gravity = 1;
	float gap = 400;
	float maxgapoffset = 0;
	Random randomGenerator;
	float tubevelocity = 6;
	int numberoftubes = 4;
	int score = 0;
	int scoringtube=0;
	float[] tubeX = new float[numberoftubes];
	float[] tubeoffset = new float[numberoftubes];
	float distancebetweentube;
	Rectangle[] toprectangle;
	Rectangle[] bottomrectangle;
	boolean collision = false;
	BitmapFont font;
	Integer highscore;
	BitmapFont highfont;
	BitmapFont writescore;
	Handler handler;


	//get a preferences instance
	Preferences prefs ;


	@Override
	public void create() {
		batch = new SpriteBatch();
		bird = new Texture("bird.png");
		bird2 = new Texture("bird2.png");
		background = new Texture("bg.png");
		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		gameover = new Texture("GameOver.png");
		birdCircle = new Circle();
		shapeRenderer = new ShapeRenderer();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		highfont = new BitmapFont();
		highfont.setColor(Color.WHITE);
		highfont.getData().setScale(10);
		writescore = new BitmapFont();
		writescore.setColor(Color.WHITE);
		writescore.getData().setScale(5);
		toprectangle = new Rectangle[4];
		bottomrectangle = new Rectangle[4];
		birdimg[0] = bird;
		birdimg[1] = bird2;
		prefs = Gdx.app.getPreferences("Storage");
		highscore = prefs.getInteger("highscore");
		if(highscore == null)
			highscore = 0;

		//gap = new Random().nextInt(1000) + 200;

		maxgapoffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distancebetweentube = Gdx.graphics.getWidth() * (0.6f);

		startGame();
	}
	public void startGame(){
		birdY = Gdx.graphics.getHeight() / 2 - bird.getHeight() / 2;
		for (int i = 0; i < numberoftubes; i++) {
			tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

			// tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distancebetweentube;
			tubeX[i] = Gdx.graphics.getWidth() + i * distancebetweentube;
			toprectangle[i] = new Rectangle();
			bottomrectangle[i] = new Rectangle();
		}
	}
	@Override
	public void render() {
		int i=0;
		//highscore = prefs.getInteger("highscore");
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		if (gamestate == 1) {
			if(tubeX[scoringtube] < Gdx.graphics.getWidth() / 2 - toptube.getWidth()){
				score++;
				Gdx.app.log("score ", String.valueOf(score));
				if(scoringtube < numberoftubes - 1){
					scoringtube++;}
					else{
					scoringtube = 0;
				}
			}
			if (Gdx.input.isTouched() ) {
				velocity = -16;
			}
			for (i = 0; i < numberoftubes; i++) {
				if (tubeX[i] < -toptube.getWidth()) {
					tubeX[i] += numberoftubes * distancebetweentube;
					tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				} else {
					tubeX[i] = tubeX[i] - tubevelocity;
				}
				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);
				toprectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i], toptube.getWidth(), toptube.getHeight());
				bottomrectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i], bottomtube.getWidth(), bottomtube.getHeight());
			}
			if (birdY > 0 && birdY < Gdx.graphics.getHeight()-birdimg[flapstate].getHeight()) {
				//if (velocity < C)
				velocity = velocity + gravity;
				birdY -= velocity;
			}else{
				gamestate = 2;
			}
		}else  if(gamestate == 0){
				if (Gdx.input.justTouched()) {
					gamestate = 1;
				}
			}
			else if(gamestate == 2){
			if(highscore == 0){
				//prefs.putInteger("highscore",score);
				highscore = score;
			}else{
				if(score > highscore)
					//prefs.flush();
					highscore = score;
					//prefs.putInteger("highscore", highscore);
			}
			//int print = prefs.getInteger("highscore");
			Gdx.app.log("highscore is"," "+highscore);

			batch.draw(birdimg[flapstate], Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY);
			batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
			batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);
			batch.draw(gameover,Gdx.graphics.getWidth()/2-gameover.getWidth()/2, Gdx.graphics.getHeight()/2-gameover.getHeight()/2);
			//raw(, String.valueOf(highscore),Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()*0.9f);
			if (Gdx.input.isTouched()) {
				gamestate = 1;
				startGame();
				score = 0;
				scoringtube = 0;
				velocity = 0;
			}
		}
		if (flapstate == 0) {
				flapstate = 1;
			} else {
				flapstate = 0;
			}
			if(gamestate != 2)
			batch.draw(birdimg[flapstate], Gdx.graphics.getWidth() / 2 - bird.getWidth() / 2, birdY);
			font.draw(batch, String.valueOf(score), 100, 200);
			writescore.draw(batch,"High Score",Gdx.graphics.getWidth()-400,300);
			highfont.draw(batch, String.valueOf(highscore), Gdx.graphics.getWidth()-200,200);
			batch.end();
			birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birdimg[flapstate].getHeight() / 2, birdimg[flapstate].getWidth() * 0.4f);
			//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
			//shapeRenderer.setColor(Color.RED);
			//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
			for (int j = 0; j < numberoftubes; j++) {
				//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
				//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());
				if (Intersector.overlaps(birdCircle, toprectangle[j]) || Intersector.overlaps(birdCircle, bottomrectangle[j])) {
					collision = true;

					gamestate = 2;
					
				}

			}
			//shapeRenderer.end();
	}



		@Override
		public void dispose () {
			batch.dispose();
			background.dispose();
		}

	}
