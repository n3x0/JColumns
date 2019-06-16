import java.awt.*;

public class Board {

    public static final int ANCHO = 6;
    public static final int ALTO = 13;

    private static final Color OUTSIDE = Color.WHITE;
    private static final Color MARKED = Color.MAGENTA;
    public static final Color BLANK = Color.BLACK;

    private Color[][] colors;
    private Boolean[][] marked;


    public void setColor(Color color, int x, int y) {
        this.colors[x][y] = color;
    }

    public boolean compare(Point one, Point two) {
        boolean ret = false;
        Color colorOne = getColor(one.x, one.y);
        Color colorTwo = getColor(two.x, two.y);
        if (colorOne != BLANK && colorTwo != BLANK && colorOne != OUTSIDE && colorTwo != OUTSIDE) {
            if (colorOne.getRed() == colorTwo.getRed() &&
                    colorOne.getGreen() == colorTwo.getGreen() &&
                    colorOne.getBlue() == colorTwo.getBlue()) {
                ret = true;
            }
        }
        return ret;
    }

    public Color getColor(int x, int y) {
        Color ret;
        if (x < 0 || x >= ANCHO || y < 0 || y >= ALTO) {
            ret = OUTSIDE;
        } else {
            Color color = colors[x][y];
            if (isMarked(x, y)) {
                ret = new Color(color.getRed(), color.getGreen(), color.getBlue(), 128);
            } else {
                ret = color;
            }
        }
        return ret;
    }

    public Boolean isMarked(int x, int y) {
        return marked[x][y];
    }

    public void setMarked(int x, int y) {
        marked[x][y] = Boolean.TRUE;
    }

    public Board() {
        colors = new Color[ANCHO][ALTO];
        marked = new Boolean[ANCHO][ALTO];
        for (int i = 0; i < Board.ANCHO; i++) {
            for (int j = 0; j < Board.ALTO; j++) {
                colors[i][j] = Color.BLACK;
                marked[i][j] = Boolean.FALSE;
            }
        }
    }

    public void delete(int i, int j) {
        colors[i][j] = BLANK;
        marked[i][j] = Boolean.FALSE;
    }
}
