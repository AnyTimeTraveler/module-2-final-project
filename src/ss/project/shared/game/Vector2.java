package ss.project.shared.game;

public class Vector2 {

	private final int	x;
	private final int	y;

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

	@Override
	public String toString() {
		return "Vector2 [x=" + x + ", y=" + y + "]";
	}
}
