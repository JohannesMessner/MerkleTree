package MerkleTree;

import java.util.Optional;

abstract class MerkleNode<V> {

  private MerkleNode<V> parent;
  private MerkleNode<V> left;
  private MerkleNode<V> right;
  private Optional<Long> hash;

  /** Constructor that assigns an empty Optional-object to hash. */
  MerkleNode(){
    this.hash = Optional.empty();
  }

  /** Alternative Constructor that also assignes a parent-node. */
  MerkleNode(MerkleInnerNode<V> parent){
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
    //hässlich, besser wäre über ifPresent
    if(hasHash()){
      return hash.get();
    }
    return -1;
  }

  public boolean hasHash(){
    return hash.isPresent();
  }

  protected void setHash(long hashValue){
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

  protected boolean setLeft(MerkleNode<V> left){
    this.left = left;
    return true;
  }

  protected boolean setRight(MerkleNode<V> right){
    this.right = right;
    return true;
  }

  public MerkleNode<V> getLeft() {
    return left;
  }

  public MerkleNode<V> getRight() {
    return right;
  }
}
