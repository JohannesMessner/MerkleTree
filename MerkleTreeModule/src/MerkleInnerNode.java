import java.util.Optional;

public class MerkleInnerNode<V> extends MerkleNode {

  private MerkleNode<V> left;
  private MerkleNode<V> right;
  private MerkleInnerNode<V> parent;
  private Optional<Long> hash;

  public MerkleInnerNode (){
    hash = Optional.empty();
  }

  /**
   * Constructor that sets a Parent-Node
   *
   * @param parent MerkleInnerNode that is the Nodes parent
   */
  public MerkleInnerNode(MerkleInnerNode<V> parent){
    this.parent = parent;
    this.hash = Optional.empty();
  }

  public MerkleInnerNode<V> getParent() {
    return parent;
  }

  public MerkleNode<V> getLeft() {
    return left;
  }

  public MerkleNode<V> getRight() {
    return right;
  }

  private void setLeft(MerkleNode<V> left){
    this.left = left;
  }

  private void setRight(MerkleNode<V> right){
    this.right = right;
  }


}
