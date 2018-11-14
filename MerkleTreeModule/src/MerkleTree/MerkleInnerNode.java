package MerkleTree;

import java.util.Optional;

class MerkleInnerNode<V> extends MerkleNode {

  private MerkleNode<V> left;
  private MerkleNode<V> right;
  private MerkleInnerNode<V> parent;
  private Optional<Long> hash;

  public MerkleInnerNode (){
    super();
  }

  /**
   * Constructor that sets a Parent-Node
   *
   * @param parent MerkleTree.MerkleInnerNode that is the Nodes parent
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

    private void update(){
    //ToDo
  }
}
