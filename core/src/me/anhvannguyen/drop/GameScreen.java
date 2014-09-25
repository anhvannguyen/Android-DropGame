package me.anhvannguyen.drop;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	final Drop game;
	final int SCREEN_WIDTH = 800;
	final int SCREEN_HEIGHT = 480;
	final int IMG_WIDTH = 64;
	final int IMG_HEIGHT = 64;

	private Texture dropImage;
	private Texture redDropImage;
	private Texture bucketImage;
	private Sound dropSound;
	private Music rainMusic;
	private OrthographicCamera camera;
	private Rectangle bucket;
	private Array<Rectangle> raindrops;
	private Array<Rectangle> reddrops;
	private long lastDropTime;
	private long lastRedDropTime;
	private int dropsGathered;

	public GameScreen(final Drop game) {
		this.game = game;


		// Load the images
		dropImage = new Texture(Gdx.files.internal("images/droplet.png"));
		redDropImage = new Texture(Gdx.files.internal("images/reddrop.png"));
		bucketImage = new Texture(Gdx.files.internal("images/bucket.png"));

		// Load the sound and background music
		dropSound = Gdx.audio.newSound(Gdx.files.internal("sounds/waterdrop.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sounds/rain.mp3"));
		rainMusic.setLooping(true);

		// create the camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

		// create a rectangle to represent the bucket
		bucket = new Rectangle();
		bucket.x = SCREEN_WIDTH / 2 - IMG_WIDTH / 2; // center the bucket horizontally
		bucket.y = 20; // set the bucket 20px above the bottom edge of the screen;
		bucket.width = IMG_WIDTH; // Image should be 64px x 64px
		bucket.height = IMG_HEIGHT;

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();
		
		// create the red drops array then spawn the first red drop
		reddrops = new Array<Rectangle>();
		spawnReddrop();
	}

	@Override
	public void render(float delta) {
		// clear the screen with a blue color
		// arguments to glClearColor are (Red, Green, Blue, Alpha)
		// in the range [0, 1]
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // update the camera matrices
        camera.update();
        
        // tell the SpriteBatch to render in the coordinate system
        // specified by the camera
        game.batch.setProjectionMatrix(camera.combined);

        // draw the bucket and all the drops
        game.batch.begin();
        game.font.draw(game.batch, "Score: " + dropsGathered, 0, SCREEN_HEIGHT);
        game.batch.draw(bucketImage, bucket.x, bucket.y);
        for (Rectangle raindrop : raindrops) {
        	game.batch.draw(dropImage, raindrop.x, raindrop.y);
        }
        for (Rectangle reddrop : reddrops) {
        	game.batch.draw(redDropImage, reddrop.x, reddrop.y);
        }
        game.batch.end();
        
        // get and process user touch/click input
        if (Gdx.input.isTouched()) {
        	Vector3 touchPosition = new Vector3();
        	touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        	camera.unproject(touchPosition);	// camera.unproject takes a Vector3 parameter
        	bucket.x = touchPosition.x - IMG_WIDTH / 2;	// center the bucket to the touch position
        }
        // process keyboard input
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            bucket.x -= 200 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            bucket.x += 200 * Gdx.graphics.getDeltaTime();
        
        // keep the bucket within the screen bounds
        if (bucket.x < 0)
            bucket.x = 0;
        if (bucket.x > SCREEN_WIDTH - IMG_WIDTH)
            bucket.x = SCREEN_WIDTH - IMG_WIDTH;
        
        // create a new drop every 1 second
        if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
            spawnRaindrop();
        
        // create a new drop every 1.2 second
        if (TimeUtils.nanoTime() - lastRedDropTime > 1200000000)
            spawnReddrop();
            
        // Iterate through the list of raindrops
        Iterator<Rectangle> iter = raindrops.iterator();
        while(iter.hasNext()) {
        	Rectangle raindrop = iter.next();
        	// makes the rain drops fall at 200px per second
        	raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
        	// remove the raindrop when it falls off screen
        	if (raindrop.y + IMG_HEIGHT < 0)
                iter.remove();
        	// score and play sound when the drop hits the bucket
        	if (raindrop.overlaps(bucket)) {
        		dropsGathered++;
        		dropSound.play();
        		iter.remove();
        	}
        }
        
        // Iterate through the list of raindrops
        Iterator<Rectangle> iter2 = reddrops.iterator();
        while (iter2.hasNext()) {
        	Rectangle reddrop = iter2.next();
        	// makes the rain drops fall at 180px per second
        	reddrop.y -= 180 * Gdx.graphics.getDeltaTime();
        	// remove the drop when it falls off screen
        	if (reddrop.y + IMG_HEIGHT < 0)
                iter2.remove();
        	// go to game over screen when play hit a red drop
        	if (reddrop.overlaps(bucket)) {
        		game.setScreen(new GameOverScreen(game, dropsGathered));
        		dispose();
        	}
        }
	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, SCREEN_WIDTH - IMG_WIDTH);
		raindrop.y = SCREEN_HEIGHT;
		raindrop.width = IMG_WIDTH;
		raindrop.height = IMG_HEIGHT;
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}
	
	private void spawnReddrop() {
		Rectangle reddrop = new Rectangle();
		reddrop.x = MathUtils.random(0, SCREEN_WIDTH - IMG_WIDTH);
		reddrop.y = SCREEN_HEIGHT;
		reddrop.width = IMG_WIDTH;
		reddrop.height = IMG_HEIGHT;
		reddrops.add(reddrop);
		lastRedDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		// start the music when the game screen is shown
		rainMusic.play();
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
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}
