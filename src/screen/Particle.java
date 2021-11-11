package screen;

import engine.DrawManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Particle {
    private Screen screen;
    private BufferedImage backBuffer;
    private Graphics g;
    private DrawManager dm;
    private int x;
    private int y;

    public Particle(DrawManager dm, Screen sc) {
        this.dm = dm;
        this.screen = sc;
        this.backBuffer = dm.getBackBuffer();
        this.g = backBuffer.getGraphics();
        generate_random_position();
    }

    private void generate_random_position() {
        int x_max = screen.getWidth();
        int y_max = screen.getHeight();

        Random random = new Random();
        int x = random.nextInt(x_max);
        int y = random.nextInt(y_max);

        this.x = x;
        this.y = y;
    }

    private Color generate_random_color() {
        Random random = new Random();
        int x = random.nextInt(3);
        if (x == 0)
            return Color.red;
        else if (x==1){
            return Color.blue;
        } else {
            return Color.yellow;
        }
    }

    public void draw(Graphics g) {
        g.setColor(generate_random_color());
        g.drawOval(x, y, 0, 0);
    }
}
