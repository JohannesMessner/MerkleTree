package MerkleTree;

import java.util.Optional;

class MerkleInnerNode<V> extends MerkleNode<V> {

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

  /**
   * Calcultes the Nodes hash-value based on the children's hashes.
   *
   * @return long hash-value
   */
  @Override
  protected long calculateHash() {
    return left.getStoredHash() * right.getStoredHash();
  }

  @Override
  public void clear(){
    super.clear();
    left.clear();
    right.clear();
  }
}
