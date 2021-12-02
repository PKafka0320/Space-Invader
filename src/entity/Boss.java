package entity;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;
import engine.DrawManager;
import engine.GameSettings;

/**
 * Implements a enemy ship, to be destroyed by the player.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Boss extends Entity {

    /** Cooldown between sprite changes. */
    private Cooldown animationCooldown;
    /** Checks if the ship has been hit by a bullet. */
    private boolean isDestroyed;
    /** Values of the Boss, in points, when destroyed. */
    private int pointValue;
    private DrawManager drawManager;

    /** Number of hits to Boss
     * use it to check whether to go to next stage*/
    private int bulletsShot;
    /** Minimum time between shots. */
    private Cooldown shootingCooldown;
    /** Speed of the bullets shot by the members. */
    private static final int BULLET_SPEED = 4;

    /** Time between shots. */
    private int shootingInterval;
    /** Variance in the time between shots. */
    private int shootingVariance;
    private static final double SHOOTING_VARIANCE = .2;

    /**
     * Constructor, establishes the ship's properties.
     *
     * @param positionX
     *            Initial position of the ship in the X axis.
     * @param positionY
     *            Initial position of the ship in the Y axis.
     * @param spriteType
     *            Sprite type, image corresponding to the ship.
     */
    public Boss(final int positionX, final int positionY,
                final SpriteType spriteType, GameSettings gameSettings) {
        super(positionX, positionY, 12 * 20, 8 * 20, Color.WHITE);

        this.drawManager = Core.getDrawManager();
        this.spriteType = spriteType;
        this.animationCooldown = Core.getCooldown(500);
        this.isDestroyed = false;

        this.shootingInterval = gameSettings.getShootingFrecuency();
        this.shootingVariance = (int) (gameSettings.getShootingFrecuency()
                * SHOOTING_VARIANCE);

        this.shootingCooldown = Core.getVariableCooldown(shootingInterval,
                shootingVariance);

        this.bulletsShot = 0;

        this.pointValue = 400;

    }

    /**
     * Getter for the score bonus if this ship is destroyed.
     *
     * @return Value of the ship.
     */
    public final int getPointValue() {
        return this.pointValue;
    }

    /**
     * Updates attributes, mainly used for animation purposes.
     */
    public final void update() {
        if (this.animationCooldown.checkFinished()) {
            this.animationCooldown.reset();

            switch (this.spriteType) {
                case EnemyShipA1:
                    this.spriteType = SpriteType.EnemyShipA2;
                    break;
                case EnemyShipA2:
                    this.spriteType = SpriteType.EnemyShipA1;
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Draws every individual component of the formation.
     */
    public final void draw() {
        drawManager.drawBoss(this, this.getPositionX(), this.getPositionY());
    }

    /**
     * Shoots a bullet downwards.
     *
     * @param bullets
     *            Bullets set to add the bullet being shot.
     */
    public final void shoot(final Set<Bullet> bullets) {
        // For now, only ships in the bottom row are able to shoot.
        if (this.shootingCooldown.checkFinished()) {
            this.shootingCooldown.reset();
            Random random = new Random();
            int x_pos = random.nextInt(448);
            int y_pos = random.nextInt(550);
            bullets.add(BulletPool.getBullet(x_pos, y_pos, BULLET_SPEED));
        }
    }

    public void update_bulletsShot(int bulletsShot) {
        this.bulletsShot = bulletsShot;
    }

    /**
     * Destroys the ship, causing an explosion.
     */
    public final void destroy() {
        this.isDestroyed = true;
        this.spriteType = SpriteType.Explosion;
    }

    /**
     * Checks if the ship has been destroyed.
     *
     * @return True if the ship has been destroyed.
     */
    public final boolean isDestroyed() {
        return this.isDestroyed;
    }
}
