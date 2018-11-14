package MerkleTree;

import java.util.Optional;

abstract class MerkleNode<V> {

  private MerkleNode<V> parent;
  private Optional<Long> hash;

  /** Constructor that assigns an empty Optional-object to hash. */
  MerkleNode(){
    this.hash = Optional.empty();
  }

  /** Alternative Constructor that also assignes a parent-node. */
  MerkleNode(MerkleNode<V> parent){
    this.hash = Optional.empty();
    this.parent = parent;
  }

  private void setParent(MerkleNode<V> parent){
    this.parent = parent;
  }

  /**
   * Returns hash-code withoud recalculating it.
   *
   * @return long hash-code
   */
  public long getStoredHash(){
    if(hasHash()){
      return hash.get();
    }
    return -1;
  }

  public boolean hasHash(){
    return hash.isPresent();
  }

  private void setHash(long hashValue){
    hash = Optional.of(hashValue);
  }

  MerkleNode<V> getParent(){
    return parent;
  }

  protected void update(){
    setHash(calculateHash());
    if (parent == null){
      return;
    }
    parent.update();
  }

  /**
   * Abstract method to calculate the node's hash-code.
   *
   * @return long hash-code
   */
  protected abstract long calculateHash();
}
