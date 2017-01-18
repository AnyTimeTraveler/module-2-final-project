package ss.project.shared.game;

public class Vector3 {

    /**
     * Easy way of writing Vector3(1,1,1).
     * but instead refer to an existing Vector.
     */
    public static final Vector3 ONE = new Vector3(1, 1, 1);

    /**
     * Easy way of writing Vector3(0,0,0).
     * but instead refer to an existing Vector.
     */
    public static final Vector3 ZERO = new Vector3(0, 0, 0);

    /**
     * Easy way of writing Vector3(0,0,1).
     */
    public static final Vector3 UP = new Vector3(0, 0, 1);

    private final int x;
    private final int y;
    private final int z;

    /**
     * Create a Vector3 with x, y, z.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector2 vector2) {
        this.x = vector2.getX();
        this.y = vector2.getY();
        this.z = 0;
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

    /**
     * @return the z coordinate.
     */
    public int getZ() {
        return z;
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

        Vector3 other = (Vector3) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        if (z != other.z) {
            return false;
        }
        return true;
    }

    /**
     * Subtract the argument from this vector3.
     *
     * @param vector3
     * @return
     */
    public Vector3 subtract(Vector3 vector3) {
        return new Vector3(getX() - vector3.getX(), getY() - vector3.getZ(), getZ() - vector3.getZ());
    }

    /**
     * Substract x,y,z from this vector3.
     *
     * @param x the x axis.
     * @param y the y axis.
     * @param z the z axis.
     * @return
     */
    public Vector3 subtract(int x, int y, int z) {
        return new Vector3(getX() - x, getY() - y, getZ() - z);
    }

    /**
     * Add a vector2 to a vector3 (vector3 = vector2 = vector2)
     *
     * @param vector2
     * @return
     */
    public Vector3 add(Vector3 vector3) {
        return new Vector3(getX() + vector3.getX(), getY() + vector3.getY(), getZ() + vector3.getZ());
    }

    /**
     * Return the inverse of the vector3.
     *
     * @return
     */
    public Vector3 inverse() {
        return new Vector3(-getX(), -getY(), -getZ());
    }

    /**
     * Add x,y,z to this vector3.
     *
     * @param x the x axis.
     * @param y the y axis.
     * @param z the z axis.
     * @return
     */
    public Vector3 add(int x, int y, int z) {
        return new Vector3(getX() + x, getY() + y, getZ() + z);
    }

    @Override
    public String toString() {
        return "Vector3 [x=" + x + ", y=" + y + ", z=" + z + "]";
    }
}
