
public class MazeData{

    public static final int ROW_COUNT    = 21;
    public static final int COLUMN_COUNT = 19;

    public static final String[] CLASSIC_MAP = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XX XXX X XXX XX X",
            "X X               X",
            "X X XX XXXXX XX X X",
            "X      X   X      X",
            "X    XX X X XX    X",
            "X  X    X X    X  X",
            "OOOX X       X XOOO",
            "XXXX X HHGHH X XXXX",
            "O      bpro       O",
            "XXXX X HHHHH X XXXX",
            "OOOX X       X XOOO",
            "X  X    X X    X  X",
            "X    XX X X XX    X",
            "X      X   X      X",
            "X X XX XXXXX XX X X",
            "X X               X",
            "X XX XXX X XXX XX X",
            "X   P    X        X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    public static final String[] SPIRAL_MAP = {
            "XXXXXXXXXXXXXXXXXXX",
            "X                 X",
            "X XXXXXXXXX XXXXX X",
            "X X       X X   X X",
            "X X XXXXX X X X X X",
            "X X X   X X X X X X",
            "X X X X X X X X X X",
            "X   X X   X   X   X",
            "OOOX X       X XOOO",
            "XXXX X HHGHH X XXXX",
            "O      bpro       O",
            "XXXX X HHHHH X XXXX",
            "OOOX X       X XOOO",
            "X   X X   X   X   X",
            "X X X X X X X X X X",
            "X X X   X X X X X X",
            "X X XXXXX X X X X X",
            "X X       X X   X X",
            "X XXXXXXXXX XXXXX X",
            "X   P             X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    public static final String[] CROSS_MAP = {
            "XXXXXXXXXXXXXXXXXXX",
            "X        X        X",
            "X XXXXXX X XXXXXX X",
            "X X    X X X    X X",
            "X X XX X   X XX X X",
            "X X  X XXXXX X  X X",
            "X XX X X   X X XX X",
            "X    X X   X X    X",
            "OOOX X       X XOOO",
            "XXXX X HHGHH X XXXX",
            "O      bpro       O",
            "XXXX X HHHHH X XXXX",
            "OOOX X       X XOOO",
            "X    X X   X X    X",
            "X XX X X   X X XX X",
            "X X  X XXXXX X  X X",
            "X X XX X   X XX X X",
            "X X    X X X    X X",
            "X XXXXXX X XXXXXX X",
            "X   P    X        X",
            "XXXXXXXXXXXXXXXXXXX"
    };

    public static final String[][] MAZE_MAPS = { CLASSIC_MAP, SPIRAL_MAP, CROSS_MAP };
    public static final String[]   MAZE_NAMES = { "Classic", "Spiral", "Cross" };

    private MazeData() {}
}
