package MerkleTree;

import java.util.Optional;

class MerkleInnerNode<V> extends MerkleNode<V> {

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
    return getLeft().getStoredHash() * getRight().getStoredHash();
  }

  @Override
  public void clear(){
    super.clear();
    getLeft().clear();
    getRight().clear();
  }
}
