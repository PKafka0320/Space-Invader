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
    private int[] pos;

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

        int[] arr = new int[2];
        arr[0] = x; arr[1] = y;
        this.pos = arr;
    }

    private Color generate_random_color() {
        Random random = new Random();
        int x = random.nextInt(2);
        if (x == 0)
            return Color.red;
        else {
            return Color.blue;
        }
    }

    public void draw(Graphics g) {
        g.setColor(generate_random_color());
        g.drawOval(pos[0], pos[1], 6, 6);
    }
}
