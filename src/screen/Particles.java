package screen;

import java.awt.*;
import java.util.ArrayList;

public class Particles extends ArrayList<Particle> {

    @Override
    public boolean add(Particle p) {
        boolean res = super.add(p);
        return res;
    }

    public void draw(Graphics g) {
        for (Particle p : this) {
            p.draw(g);
        }
    }
}
