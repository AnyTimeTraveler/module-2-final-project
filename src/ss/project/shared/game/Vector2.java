package ss.project.shared.game;

/**
 * A data class used to pass and calculate with vectors or coordinates.
 * All fields are final, which means no reusing of vectors.
 *
 * @see Vector3
 */
public class Vector2 {

    /**
     * Easy way of writing Vector2(1,1).
     * but instead refer to an existing Vector.
     */
    public static final Vector2 ONE = new Vector2(1, 1);

    /**
     * Easy way of writing Vector2(0,0).
     * but instead refer to an existing Vector.
     */
    public static final Vector2 ZERO = new Vector2(0, 0);

    private final int x;
    private final int y;

    /**
     * Create a Vector2 with x, y.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y coordinate.
     */
    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int result = 5;
        result = 37 * result + x;
        result = 37 * result + y;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        Vector2 other = (Vector2) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

    /**
     * Add x,y,z to this vector3.
     *
     * @param x the x axis.
     * @param y the y axis.
     * @return
     */
    public Vector2 add(int x, int y) {
        return new Vector2(getX() + x, getY() + y);
    }

    /**
     * Add a vector2 to a vector3 (vector3 = vector2 = vector2)
     *
     * @return
     */
    public Vector2 add(Vector2 vector2) {
        return new Vector2(getX() + vector2.getX(), getY() + vector2.getY());
    }

    @Override
    public String toString() {
        return "Vector2 [x=" + x + ", y=" + y + "]";
    }
}
