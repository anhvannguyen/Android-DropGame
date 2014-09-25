package me.anhvannguyen.drop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.TimeUtils;

public class GameOverScreen implements Screen {
	final Drop game;
	final int SCREEN_WIDTH = 800;
	final int SCREEN_HEIGHT = 480;
	
	int score;
	
	long clickDelayTime;
	
	OrthographicCamera camera;
	
	public GameOverScreen(final Drop game, int score) {
		this.game = game;
		this.score = score;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		clickDelayTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);			// Set the clear color to blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	// Clear the screen
		
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.font.draw(game.batch, "Game Over", 100, 150);
        game.font.draw(game.batch, "You Scored " + score + " points!", 100, 100);
        game.font.draw(game.batch, "Tap anywhere to try again", 100, 50);
        game.batch.end();
        
        // delay the touch by 1 second because of accidental clicks from the fast screen change
        if (Gdx.input.isTouched() && TimeUtils.nanoTime() - clickDelayTime > 1000000000) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
