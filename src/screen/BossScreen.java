package screen;

import engine.*;

import entity.*;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class BossScreen extends Screen {

    private boolean bonusLife;
    private GameSettings gameSettings;
    private int level;
    private int score;
    private int lives;
    private Sound sd;

    private Ship ship;
    private Boss boss;

    /** Number of hits to Boss
     * use it to check whether to go to next stage*/
    private int bulletsShot;
    /** Time from finishing the level to screen change. */
    private Cooldown screenFinishedCooldown;
    /** Milliseconds until the screen accepts user input. */
    private static final int INPUT_DELAY = 6000;
    /** Time from finishing the level to screen change. */
    private static final int SCREEN_CHANGE_INTERVAL = 1500;
    /** Bonus score for each life remaining at the end of the level. */
    private static final int LIFE_SCORE = 200;
    /** Checks if the level is finished. */
    private boolean levelFinished;

    private static final int SEPARATION_LINE_HEIGHT = 40;

    /** Moment the game starts. */
    private long gameStartTime;

    private Set<Bullet> bullets;

    private boolean pausebutton = false;
    private boolean paused = false;
    private boolean isBtnDown = false;
    private static int selectedConfig = 0;
    public static int bright = 0;
    public static int sound = 50;

    private static BufferedImage config;


    public BossScreen(final GameState gameState,
                      final GameSettings gameSettings, final boolean bonusLife,
                      final int width, final int height, final int fps, Sound sd) {
        super(width, height, fps);
        this.bonusLife = bonusLife;
        this.gameSettings = gameSettings;
        this.level = gameState.getLevel();
        this.score = gameState.getScore();
        this.lives = gameState.getLivesRemaining();

        this.boss = new Boss(this.getWidth()/4, this.getWidth()/4, DrawManager.SpriteType.EnemyShipA1, this.gameSettings);
        this.sd = sd;

        File file = new File("src/engine/config.png");
        try{
            this.config = ImageIO.read(file);
        }
        catch(Exception e) {}

        if (this.bonusLife)
            this.lives++;
        this.bulletsShot = 0;
    }

    public final void initialize() {
        super.initialize();

        this.ship = new Ship(this.width / 2, this.height - 30);
        this.screenFinishedCooldown = Core.getCooldown(SCREEN_CHANGE_INTERVAL);
        this.bullets = new HashSet<Bullet>();
        this.gameStartTime = System.currentTimeMillis();
        this.inputDelay = Core.getCooldown(INPUT_DELAY);
        this.inputDelay.reset();
    }

    public final int run() {
        super.run();
        this.score += LIFE_SCORE * (this.lives - 1);
        this.logger.info("Screen cleared with a score of " + this.score);

        return this.returnCode;
    }

    protected final void update() {
        super.update();
        if (this.inputDelay.checkFinished() && !this.levelFinished) {
            if (!this.ship.isDestroyed()) {
                boolean moveRight = inputManager.isKeyDown(KeyEvent.VK_RIGHT)
                        || inputManager.isKeyDown(KeyEvent.VK_D);
                boolean moveLeft = inputManager.isKeyDown(KeyEvent.VK_LEFT)
                        || inputManager.isKeyDown(KeyEvent.VK_A);

                boolean isRightBorder = this.ship.getPositionX()
                        + this.ship.getWidth() + this.ship.getSpeed() > this.width - 1;
                boolean isLeftBorder = this.ship.getPositionX()
                        - this.ship.getSpeed() < 1;

                if (!pausebutton && inputManager.isKeyDown(KeyEvent.VK_ESCAPE)) {
                    pausebutton = true;
                    if (!paused) {
                        paused = true;
                    } else {
                        paused = false;
                    }
                }
                if (pausebutton && inputManager.isKeyUp(KeyEvent.VK_ESCAPE)) {
                    pausebutton = false;
                }

                if (!isBtnDown && paused) {
                    if (inputManager.isKeyDown(KeyEvent.VK_UP)) {
                        isBtnDown = true;
                        previousMenuItem();
                    }
                    if (inputManager.isKeyDown(KeyEvent.VK_DOWN)) {
                        isBtnDown = true;
                        nextMenuItem();
                    }
                    if (inputManager.isKeyDown(KeyEvent.VK_LEFT)) {
                        isBtnDown = true;
                        valueDown();
                    }
                    if (inputManager.isKeyDown(KeyEvent.VK_RIGHT)) {
                        isBtnDown = true;
                        valueUp();
                    }
                }
                if (inputManager.isKeyUp(KeyEvent.VK_UP) && inputManager.isKeyUp(KeyEvent.VK_DOWN)
                        && inputManager.isKeyUp(KeyEvent.VK_LEFT) && inputManager.isKeyUp(KeyEvent.VK_RIGHT)) {
                    isBtnDown = false;
                }

                if (moveRight && !isRightBorder && !paused) {
                    this.ship.moveRight();
                }
                if (moveLeft && !isLeftBorder && !paused) {
                    this.ship.moveLeft();
                }
                if (inputManager.isKeyDown(KeyEvent.VK_SPACE) && !paused)
                    if (this.ship.shoot(this.bullets))
                        this.bulletsShot++;
            }
            if (!paused) {
                this.ship.update();
                this.boss.update();
                this.boss.shoot(this.bullets);
            }
        }


        if (!paused){
            manageCollisions();
            cleanBullets();
            draw();
        }
        else{
            drawConfig();
        }

        if ((this.bulletsShot == 30 || this.lives == 0) /* 게임 종료 조건 체크 */
                && !this.levelFinished) {
            this.levelFinished = true;
            this.screenFinishedCooldown.reset();
        }

        if (this.levelFinished && this.screenFinishedCooldown.checkFinished())
            this.isRunning = false;
    }


    public void previousMenuItem() {
		if (selectedConfig == 0) {
			selectedConfig = 1;
		}
		else if (selectedConfig == 1){
			selectedConfig = 0;
		}
	}

	public void nextMenuItem() {
		if (selectedConfig == 1) {
			selectedConfig = 0;
		}
		else if (selectedConfig == 0) {
			selectedConfig = 1;
		}
	}

	public void valueUp() {
		if (selectedConfig == 0) {
			if (sound >= 0 && sound <= 95) {
				sound += 5;
				sd.setVolume(sound / 100.0f);
			}
		}
		else if (selectedConfig == 1) {
			if (bright >= 0 && bright <= 95) {
				bright += 5;
			}
		}
	}

	public void valueDown() {
		if (selectedConfig == 0) {
			if (sound >= 5 && sound <= 100) {
				if(sound == 0) {
					sound -= 0;
					sd.setVolume(0.0f);
				}
				else {
					sound -= 5;
					sd.setVolume(sound / 100.0f);
				}

			}

		}
		else if (selectedConfig == 1) {
			if (bright >= 5 && bright <= 100) {
				if(bright == 0) {
					bright -= 0;
				}
				else {
					bright-= 5;
				}

			}
		}
	}

    private void drawConfig() {
        drawManager.initDrawing(this);
        drawManager.drawConfigScreen(this,width-100, sound, bright, selectedConfig);
        drawManager.backBlack(this, bright);
        drawManager.completeDrawing(this);
    }

    private void draw() {
        drawManager.initDrawing(this);

        drawManager.drawEntity(this.ship, this.ship.getPositionX(),
                this.ship.getPositionY());

        boss.draw();

        for (Bullet bullet : this.bullets)
            drawManager.drawEntity(bullet, bullet.getPositionX(),
                    bullet.getPositionY());

        // Interface.
        drawManager.drawScore(this, this.score);
        drawManager.drawLives(this, this.lives);
        drawManager.drawHorizontalLine(this, SEPARATION_LINE_HEIGHT - 1);
        drawManager.drawConfig(this, this.config);


        // Countdown to game start.
        if (!this.inputDelay.checkFinished()) {
            int countdown = (int) ((INPUT_DELAY
                    - (System.currentTimeMillis()
                    - this.gameStartTime)) / 1000);
            drawManager.drawBossCountDown(this, this.level, countdown,
                    this.bonusLife);
            drawManager.drawHorizontalLine(this, this.height / 2 - this.height
                    / 12);
            drawManager.drawHorizontalLine(this, this.height / 2 + this.height
                    / 12);
        }

        drawManager.backBlack(this, bright);

        drawManager.completeDrawing(this);
    }

    private void cleanBullets() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets) {
            bullet.update();
            if (bullet.getPositionY() < SEPARATION_LINE_HEIGHT
                    || bullet.getPositionY() > this.height)
                recyclable.add(bullet);
        }
        this.bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    private void manageCollisions() {
        Set<Bullet> recyclable = new HashSet<Bullet>();
        for (Bullet bullet : this.bullets)
            if (bullet.getSpeed() > 0) {
                if (checkCollision(bullet, this.ship) && !this.levelFinished) {
                    recyclable.add(bullet);
                    if (!this.ship.isDestroyed()) {
                        this.ship.destroy();
                        this.lives--;
                        this.logger.info("Hit on player ship, " + this.lives
                                + " lives remaining.");
                    }
                }
            } else {
                if (!boss.isDestroyed() && checkCollision(bullet, boss)) {
                    if (this.bulletsShot == 29) {
                        boss.destroy();
                    }
                    this.score += boss.getPointValue();
                    this.bulletsShot++;
                    boss.update_bulletsShot(this.bulletsShot);
                    recyclable.add(bullet);
                }
            }
        this.bullets.removeAll(recyclable);
        BulletPool.recycle(recyclable);
    }

    private boolean checkCollision(final Entity a, final Entity b) {
        // Calculate center point of the entities in both axis.
        int centerAX = a.getPositionX() + a.getWidth() / 2;
        int centerAY = a.getPositionY() + a.getHeight() / 2;
        int centerBX = b.getPositionX() + b.getWidth() / 2;
        int centerBY = b.getPositionY() + b.getHeight() / 2;
        // Calculate maximum distance without collision.
        int maxDistanceX = a.getWidth() / 2 + b.getWidth() / 2;
        int maxDistanceY = a.getHeight() / 2 + b.getHeight() / 2;
        // Calculates distance.
        int distanceX = Math.abs(centerAX - centerBX);
        int distanceY = Math.abs(centerAY - centerBY);

        return distanceX < maxDistanceX && distanceY < maxDistanceY;
    }

    public GameState getGameState() {
        return new GameState(this.level, this.score, this.lives,
                this.bulletsShot, 30);
    }
}
