public class Cylinder extends Body {

  private final int radius;
  private final int height;

  public Cylinder(int radius, int height){
    this.height = height;
    this.radius = radius;
  }

  @Override
  public String toString(){
    return "Cuboid(" + this.radius + "," + this.height + ")";
  }
}
