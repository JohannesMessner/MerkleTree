public class Cuboid extends Body{
  private final int length;
  private final int width;
  private final int height;

  public Cuboid(int length, int width, int height){
    this.height = height;
    this.length = length;
    this.width = width;
  }

  @Override
  public String toString(){
    return "Cuboid(" + this.length + "," + this.width + "," + this.height + ")";
  }
}
