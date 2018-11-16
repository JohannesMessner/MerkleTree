package MerkleTree;

import java.util.Optional;

class MerkleInnerNode<V> extends MerkleNode {

  private MerkleNode<V> left;
  private MerkleNode<V> right;

  public MerkleInnerNode (){
    super();
  }

  /**
   * Constructor that sets a Parent-Node
   *
   * @param parent MerkleTree.MerkleInnerNode that is the Nodes parent
   */
  public MerkleInnerNode(MerkleInnerNode<V> parent){
    super(parent);
  }

  MerkleNode<V> getLeft() {
    return left;
  }

  MerkleNode<V> getRight() {
    return right;
  }


  /**
   * Calcultes the Nodes hash-value based on the children's hashes.
   *
   * @return long hash-value
   */
  @Override
  protected long calculateHash() {
    return left.getStoredHash() * right.getStoredHash();
  }
}
