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

  /**
   * Checks if the stored hash is consistent with the hashes of all successors.
   *
   * @return
   */
  @Override
  protected boolean isConsistent(){
    return super.isConsistent() && getLeft().isConsistent() && getRight().isConsistent();
  }

  @Override
  protected String addValue(String str){
    if (getStoredHash() == 0) {
      return str + "*";
    }
    return  str + this.getStoredHash();
  }
}
