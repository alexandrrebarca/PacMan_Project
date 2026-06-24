import java.awt.Color;


public class Skin {
    public final String name;
    public final Color color;

    public Skin(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public static final Skin[] ALL = {
            new Skin("Classic Yellow", new Color(255, 222, 0)),
            new Skin("Cherry Red",     new Color(230, 50, 50)),
            new Skin("Mint Green",     new Color(60, 220, 150)),
            new Skin("Cyber Blue",     new Color(60, 160, 255)),
            new Skin("Royal Purple",   new Color(170, 80, 220)),
    };
}
