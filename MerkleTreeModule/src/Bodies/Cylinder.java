package Bodies;

/** Class representing a 3D-cylinder. */
public class Cylinder extends Body {

  private final int radius;
  private final int height;

  /**
   * Contructor that sets the radius and height of the Bodies.Cylinder.
   *
   * @param radius radius of the Bodies.Cylinder
   * @param height height of the Bodies.Cylinder
   */
  public Cylinder(int radius, int height) {
    this.height = height;
    this.radius = radius;
  }

  @Override
  public String toString() {
    return "Bodies.Cylinder(" + this.radius + "," + this.height + ")";
  }
}
