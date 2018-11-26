public class Cylinder extends Body {

  private final int radius;
  private final int height;

  /**
   * Contructor that sets the radius and height of the Cylinder.
   *
   * @param radius radius of the Cylinder
   * @param height height of the Cylinder
   */
  public Cylinder(int radius, int height){
    this.height = height;
    this.radius = radius;
  }

  @Override
  public String toString(){
    return "Cuboid(" + this.radius + "," + this.height + ")";
  }
}
