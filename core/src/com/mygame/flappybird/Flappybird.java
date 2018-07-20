package com.mygame.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class Flappybird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bird;
	Texture bird2;
	Texture background;
	Texture bottomtube;
	Texture toptube;
	Texture birdimg[] = new Texture[2];
	Circle birdCircle;
	ShapeRenderer shapeRenderer;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	int gamestate = 0;
	float gravity = 2;
	float gap = 600;
	float maxgapoffset=0;
	Random randomGenerator;
	float tubevelocity = 4;
	int numberoftubes = 4;
	float[] tubeX = new float[numberoftubes];
	float[] tubeoffset = new float[numberoftubes];
	float distancebetweentube;
	Rectangle[] toprectangle;
	Rectangle[] bottomrectangle;

	@Override
	public void create () {
		batch = new SpriteBatch();
		bird = new Texture("bird.png");
		bird2 = new Texture("bird2.png");
		background = new Texture("bg.png");
		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		birdCircle = new Circle();
		shapeRenderer = new ShapeRenderer();
		toprectangle = new Rectangle[4];
		bottomrectangle = new Rectangle[4];
		birdimg[0] = bird;
		birdimg[1] = bird2;
		birdY = Gdx.graphics.getHeight()/2-bird.getHeight()/2;
		maxgapoffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distancebetweentube = Gdx.graphics.getWidth() * 3/4;
		for(int i = 0;i < numberoftubes;i++){
			tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-gap-200);

			    tubeX[i] = Gdx.graphics.getWidth() / 2 - toptube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distancebetweentube;
 			//tubeX[i] = Gdx.graphics.getWidth()/2-toptube.getWidth()/2 + i * distancebetweentube;
			toprectangle[i] = new Rectangle();
			bottomrectangle[i] = new Rectangle();

		}
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0 , 0 , Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gamestate !=0){
			if(Gdx.input.justTouched()){
				velocity = -30;

			}
			for(int i=0;i<numberoftubes;i++) {
				if(tubeX[i] < -toptube.getWidth()){
					tubeX[i] += numberoftubes*distancebetweentube;
					tubeoffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight()-gap-200);
				}else {
					tubeX[i] = tubeX[i] - tubevelocity;
				}

				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i]);
				toprectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
				bottomrectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());

			}


            if(birdY > 0 || velocity < 0){

				velocity = velocity+gravity;

				birdY -= velocity;}
		}else{

			if(Gdx.input.justTouched()){
				gamestate = 1;
			}
		}
		if(flapstate==0){
			flapstate=1;}
		else{
			flapstate=0;}
		batch.draw(birdimg[flapstate],Gdx.graphics.getWidth()/2-bird.getWidth()/2,birdY);
		batch.end();
		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birdimg[flapstate].getHeight()/2,birdimg[flapstate].getWidth()/2);
		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for(int i = 0; i < numberoftubes; i++){
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeoffset[i],toptube.getWidth(),toptube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeoffset[i],bottomtube.getWidth(),bottomtube.getHeight());

			if(Intersector.overlaps(birdCircle,toprectangle[i]) || Intersector.overlaps(birdCircle,bottomrectangle[i])){
				Gdx.app.log("Collision","MC");
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
