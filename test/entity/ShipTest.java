package entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship ship;

    @BeforeEach
    void setUp() {
        ship = new Ship(0,0);
    }

    @Test
    void getSpeed() {
        int initial_speed = ship.getSpeed();
        ship.setSpeed(6);
        int final_speed = ship.getSpeed();
        assertNotEquals(initial_speed, final_speed);
    }

    @Test
    void getBulletSpeed() {
        int initial_bulletspeed = ship.getBulletSpeed();
        ship.setbullet_Speed(-10);
        int final_bulletspeed = ship.getBulletSpeed();
        assertNotEquals(initial_bulletspeed, final_bulletspeed);
    }
}