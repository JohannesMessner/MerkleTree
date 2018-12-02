

/** Class representing a 3D-cuboid. */
public class Cuboid extends Body {
  private final int length;
  private final int width;
  private final int height;

  /**
   * Constructor that sets length, width and height of the Cuboid.
   *
   * @param length length of the Cuboid
   * @param width width of the Cuboid
   * @param height height of the Cuboid
   */
  public Cuboid(int length, int width, int height) {
    this.height = height;
    this.length = length;
    this.width = width;
  }

  @Override
  public String toString() {
    return "Cuboid(" + this.length + "," + this.width + "," + this.height + ")";
  }
}
