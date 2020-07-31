/* *****************************************************************************
 *  Name:    Alan Turing
 *  NetID:   aturing
 *  Precept: P00
 *
 *  Description:  Prints 'Hello, World' to the terminal window.
 *                By tradition, this is everyone's first program.
 *                Prof. Brian Kernighan initiated this tradition in 1974.
 *
 **************************************************************************** */

class Curtain {
    private int width;
    private int length;
    private String color;
    private String material;

    public Curtain(int w, int l, String c, String m) {
        this.width = w;
        this.length = l;
        this.color = c;
        this.material = m;
    }

    // Getter (Accessor) - retrieve attributes of the class
    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }

    public String getColor() {
        return color;
    }

    public String getMaterial() {
        return material;
    }

    public String getStringRepresentation() {
        return width + " " + length + " " + color + " " + material;
    }

    // Setter (Modifier) - modify internal attributes of the class
    public void setWidth(int w) {
        width = w;
    }

    public void setLength(int l) {
        length = l;
    }

    public void setColor(String c) {
        color = c;
    }

    public void setMaterial(String m) {
        material = m;
    }
}

public class HelloWorld {
    public static void main(String[] args) {
        Curtain phucCurtain = new Curtain(1, 3, "Cream", "Silk");
        phucCurtain.setMaterial("Polyester");
        System.out.println(phucCurtain.getStringRepresentation());
    }
}
