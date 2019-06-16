import java.awt.*;
import java.util.Random;

public class Pieza {
    public final Color[] Colores = {Color.cyan, Color.RED, Color.PINK, Color.yellow};

    public static final int SIZE = 3;
    private Color[] trozos;

    public Pieza(Random ran) {
        trozos = new Color[3];
        for (int i = 0; i < trozos.length; i++) {
            trozos[i] = Colores[Math.abs(ran.nextInt()) % Colores.length];
        }
    }

    public Color getTrozo(int i) {
        return trozos[i];
    }

    public void setTrozo(int i, Color color) {
        trozos[i] = color;
    }
}
