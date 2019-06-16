import java.awt.*;

public class Game {
    static Tetris tetris;
    static Columns columns;
    public final Color[] Colores = {Color.cyan, Color.RED, Color.orange, Color.yellow};

    public static void main(String[] args) {
        columns = new Columns();
        columns.initialize();
    }
}