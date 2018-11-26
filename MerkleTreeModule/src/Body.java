public abstract class Body {
  @Override
  public int hashCode(){
    return toString().hashCode();
  }
}
