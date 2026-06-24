import java.awt.Color;
import java.awt.Graphics;

public class PacmanRenderer {

    private PacmanRenderer() {

    }

    public static void draw(Graphics g, int x, int y, int w, int h, char dir, Color color, int mouthAngle) {
        int startAngle;
        switch (dir) {
            case 'R': startAngle = mouthAngle / 2; break;
            case 'L': startAngle = 180 + mouthAngle / 2; break;
            case 'U': startAngle = 90 + mouthAngle / 2; break;
            case 'D': startAngle = 270 + mouthAngle / 2; break;
            default:  startAngle = mouthAngle / 2;
        }
        int arcExtent = 360 - mouthAngle;
        g.setColor(color);
        g.fillArc(x, y, w, h, startAngle, arcExtent);
    }
}
