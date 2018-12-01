package Bodies;

/**
 * Abstract class of geometrical 3D-bodies.
 */
public abstract class Body {
  @Override
  public int hashCode(){
    return toString().hashCode();
  }
}
