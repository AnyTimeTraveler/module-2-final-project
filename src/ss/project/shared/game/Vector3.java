package ss.project.shared.game;

import lombok.Data;

/**
 * A data class used to pass and calculate with vectors or coordinates.
 * All fields are final, which means no reusing of vectors.
 *
 * @see Vector2
 */
@Data
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
    //@ ensures getX()==x;
    //@ ensures getY()==y;
    //@ ensures getZ()==z;
    public Vector3(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Create a Vector3 from a vector2 with z = 0.
     *
     * @param vector2 The x and y of the vector3.
     */
    //@ ensures getX()==vector2.getX();
    //@ ensures getY()==vector2.getY();
    //@ ensures getZ()==0;
    public Vector3(Vector2 vector2) {
        this.x = vector2.getX();
        this.y = vector2.getY();
        this.z = 0;
    }

    /**
     * Add a vector2 to a vector3 (vector3 = vector2 = vector2).
     *
     * @return
     */
    public Vector3 add(Vector3 vector3) {
        return new Vector3(getX() + vector3.getX(), getY() + vector3.getY(), getZ() + vector3.getZ());
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
}
