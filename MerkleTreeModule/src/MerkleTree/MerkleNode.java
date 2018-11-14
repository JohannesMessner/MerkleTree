package MerkleTree;

import java.util.Optional;

abstract class MerkleNode<V> {

  private MerkleNode<V> parent;
  private Optional<Long> hash;

  MerkleNode(){
    this.hash = Optional.empty();
  }

  MerkleNode(MerkleNode<V> parent){
    this.parent = parent;
  }

  private void setParent(MerkleNode<V> parent){
    this.parent = parent;
  }

  public long getStoredHash(){
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

  protected abstract long calculateHash();
}
