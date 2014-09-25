package me.anhvannguyen.drop;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameOverScreen implements Screen {
	final Drop game;
	final int SCREEN_WIDTH = 800;
	final int SCREEN_HEIGHT = 480;
	
	private int score;
	
	// menu items
	private Stage stage;
	private Table table;
	private Skin skin;
	private TextButton replayButton;
	private TextButton exitButton;
	private Label titleLabel;
	private Label scoreLabel;
	
	
	OrthographicCamera camera;
	
	public GameOverScreen(final Drop game, int score) {
		this.game = game;
		this.score = score;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
		
		stage = new Stage();
		table = new Table();
		
		skin = new Skin(Gdx.files.internal("skins/menuSkin.json"), new TextureAtlas("skins/menuSkin.pack"));
		replayButton = new TextButton("Replay", skin);
		exitButton = new TextButton("Exit", skin);
		titleLabel = new Label("Game Over", skin);
		scoreLabel = new Label("Score: " + score, skin);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);			// Set the clear color to blue
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);	// Clear the screen
		
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        
        stage.act();
        stage.draw();
                
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// add click listeners to the buttons
		replayButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game));
				dispose();
			}
		});
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		
		//The elements are displayed in the order you add them.
        //The first appear on top, the last at the bottom.
		table.add(titleLabel).padBottom(10).row();
		table.add(scoreLabel).padBottom(20).row();
        table.add(replayButton).size(250, 60).padBottom(20).row();
        table.add(exitButton).size(250, 60).padBottom(20).row();
        
        table.setFillParent(true);
        stage.addActor(table);
        Gdx.input.setInputProcessor(stage);

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
		skin.dispose();
		stage.dispose();
	}

}
