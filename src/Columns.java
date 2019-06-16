import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Columns extends JPanel implements KeyListener {
    private static int TILESIZE = 64;
    private static int TOP_BAR_OFFSET = 25;
    private Board board;

    private Point cursor = new Point();
    private Pieza pieza;
    Thread game;
    private boolean animating;


    public void initialize() {

        JFrame f = new JFrame("Columns");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(TILESIZE * Board.ANCHO + 6, TILESIZE * Board.ALTO + 30);
        f.setVisible(true);
        f.setResizable(false);

        this.init();
        f.add(this);
        f.addKeyListener(this);

        game = new Thread() {


            @Override
            public void run() {
                while (!animating) {
                    try {
                        Thread.sleep(500);
                        moveDown();
                        paint();
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
    }

    private void paint() {
        repaint();
        if (pieza != null) {
            System.out.println("(" + cursor.x + "," + cursor.y + ")");
        }
    }

    private void dropBottom() {

    }

    private void moveDown() {
        if (!collides(0, 1)) {
            cursor.y++;
            paint();
        } else {
            fixToWell();
        }
    }

    private void moveLeft() {
        if (!collides(-1, 0)) {
            cursor.x--;
            paint();
        }
    }

    private void moveRight() {
        if (!collides(1, 0)) {
            cursor.x++;
            paint();
        }

    }

    private boolean collides(int xDir, int yDir) {
        if (cursor.x + xDir < 0 || cursor.x + xDir >= Board.ANCHO || cursor.y + yDir < 0 || cursor.y + yDir >= Board.ALTO) {
            return true;
        }
        for (int i = 0; i < Board.ANCHO; i++) {
            for (int j = 0; j < Board.ALTO; j++) {
                if (board.getColor(cursor.x + xDir, cursor.y + yDir) != Board.BLANK) {
                    return true;
                }
            }
        }
        return false;
    }


    private void init() {
        board = new Board();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.fillRect(0, 0, TILESIZE * Board.ANCHO, TILESIZE * Board.ALTO);
        for (int i = 0; i < Board.ANCHO; i++) {
            for (int j = 0; j < Board.ALTO; j++) {
                g.setColor(board.getColor(i, j));
                g.fillRect(TILESIZE * i, TILESIZE * j, TILESIZE - 1, TILESIZE - 1);
            }
        }
        g.setColor(Color.WHITE);
//        g.drawString("Score : " + score, 19 * 12, 25);

        drawPiece(g);
    }

    private void drawPiece(Graphics g) {
        if (pieza != null) {
            for (int i = 0; i < Pieza.SIZE; i++) {
                g.setColor(pieza.getTrozo(i));
                g.fillRect(cursor.x * TILESIZE,
                        (cursor.y - i) * TILESIZE,
                        TILESIZE, TILESIZE);
            }
        }
    }

    public void fixToWell() {
        boolean change = true;
        int iterations = 0;
        if (pieza != null) {
            fixPieces();
        }
        while (change) {
            System.out.println("\nCadena " + ++iterations);
            change = checkColumns(cursor.x, cursor.y);
            removeColumns();
            paint();
            newPiece();
        }

    }

    private void removeColumns() {
        for (int i = 0; i < Board.ANCHO; i++) {
            for (int j = 0; j < Board.ALTO; j++) {
                if (board.isMarked(i, j)) {
                    board.delete(i, j);
                }
            }
        }
        for (int i = Board.ANCHO; i > 0; i--) {
            for (int j = Board.ALTO; j > 0; j--) {
                if (falls(i, j)) {
                    Color color = board.getColor(i, j);
                    board.setColor(Board.BLANK, i, j);
                    board.setColor(color, i, j + 1);
                }
            }
        }
    }

    private boolean falls(int i, int j) {
        if (board.getColor(i, j + 1) == Board.BLANK) {
            return true;
        } else {
            return false;
        }
    }

    private void fixPieces() {
        for (int i = 0; i < Pieza.SIZE; i++) {
            Color color = pieza.getTrozo(i);
            board.setColor(color, cursor.x, cursor.y - i);
        }
    }

    private boolean checkColumns(int e, int r) {
        Color color;
        boolean change = false;
        for (int t = 0; t < 1; t++) {
            for (int i = Board.ANCHO; i >= 0; i--) {
                for (int j = Board.ALTO; j >= 0; j--) {
                    color = pieza.getTrozo(t);
                    Point startingPoint = new Point(i, j);
                    //derecha
                    if (board.compare(startingPoint, new Point(i + 1, j)) &&
                            board.compare(startingPoint, new Point(i + 2, j))) {
                        System.out.println("Columna de 3");
                        board.setMarked(i, j);
                        board.setMarked(i + 1, j);
                        board.setMarked(i + 2, j);
                        change = true;
                        if (board.compare(startingPoint, new Point(i + 3, j))) {
                            System.out.println("Columna de 4");
                            board.setMarked(i + 3, j);
                            if (board.compare(startingPoint, new Point(i + 4, j))) {
                                System.out.println("Columna de 5");
                                board.setMarked(i + 4, j);
                            }
                        }
                    }
                    //abajo
                    if (board.compare(startingPoint, new Point(i, j + 1)) &&
                            board.compare(startingPoint, new Point(i, j + 2))) {
                        System.out.println("Columna de 3");
                        board.setMarked(i, j);
                        board.setMarked(i, j + 1);
                        board.setMarked(i, j + 2);
                        change = true;
                        if (board.compare(startingPoint, new Point(i, j + 3))) {
                            System.out.println("Columna de 4");
                            board.setMarked(i, j + 3);
                            if (board.compare(startingPoint, new Point(i, j + 4))) {
                                System.out.println("Columna de 5");
                                board.setMarked(i, j + 4);
                            }
                        }
                    }
                    //abajo-derecha
                    if (board.compare(startingPoint, new Point(i + 1, j + 1)) &&
                            board.compare(startingPoint, new Point(i + 2, j + 2))) {
                        System.out.println("Columna de 3");
                        board.setMarked(i, j);
                        board.setMarked(i + 1, j + 1);
                        board.setMarked(i + 2, j + 2);
                        change = true;
                        if (board.compare(startingPoint, new Point(i + 3, j + 3))) {
                            System.out.println("Columna de 4");
                            board.setMarked(i + 3, j + 3);
                            if (board.compare(startingPoint, new Point(i + 4, j + 4))) {
                                System.out.println("Columna de 5");
                                board.setMarked(i + 4, j + 4);
                            }
                        }
                    }
                    //arriba-derecha
                    if (board.compare(startingPoint, new Point(i + 1, j - 1)) &&
                            board.compare(startingPoint, new Point(i + 2, j - 2))) {
                        System.out.println("Columna de 3");
                        board.setMarked(i, j);
                        board.setMarked(i + 1, j - 1);
                        board.setMarked(i + 2, j - 2);
                        change = true;
                        if (board.compare(startingPoint, new Point(i + 3, j - 3))) {
                            System.out.println("Columna de 4");
                            board.setMarked(i + 3, j - 3);
                            if (board.compare(startingPoint, new Point(i + 4, j - 4))) {
                                System.out.println("Columna de 5");
                                board.setMarked(i + 4, j - 4);
                            }
                        }
                    }
                }
            }
        }
        return change;
    }

    private void newPiece() {
        Random ran = new Random();
        pieza = new Pieza(ran);
        cursor.x = 3;
        cursor.y = 0;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case (KeyEvent.VK_LEFT):
                moveLeft();
                break;
            case (KeyEvent.VK_RIGHT):
                moveRight();
                break;
            case (KeyEvent.VK_SPACE):
                rotate();
                break;
            case (KeyEvent.VK_UP):
                dropBottom();
                break;
            case (KeyEvent.VK_DOWN):
                moveDown();
                break;
            case KeyEvent.VK_F:
                fixToWell();
                break;
            case KeyEvent.VK_BACK_SPACE:
                start();
                break;
        }
    }

    private void start() {
        if (!game.isAlive()) {
            newPiece();
            game.start();
        }
    }


    private void rotate() {
        Color aux = pieza.getTrozo(0);
        pieza.setTrozo(0, pieza.getTrozo(1));
        pieza.setTrozo(1, pieza.getTrozo(2));
        pieza.setTrozo(2, aux);
        paint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
