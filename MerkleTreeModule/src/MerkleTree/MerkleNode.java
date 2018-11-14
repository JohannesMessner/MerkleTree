package MerkleTree;

import java.util.Optional;

abstract class MerkleNode<V> {

  private MerkleNode<V> parent;
  Optional<Long> hash;

  MerkleNode(){
    this.hash = Optional.empty();
  }
}
