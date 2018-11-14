package MerkleTree;

import java.util.Optional;

class MerkleLeaf<V> extends MerkleNode {

  private Optional<V> value;

  /** Constructor assigning an empty Optional-object to value. */
  MerkleLeaf(){
    super();
    this.value = Optional.empty();
  }

  /** Alternative Constructor that also assigns a perent-node. */
  MerkleLeaf(MerkleInnerNode<V> parent){
    super(parent);
    this.value = Optional.empty();
  }

  private void setValue(V value){
    this.value = Optional.of(value);
    update();
  }

  /** Calculates the hash-code. */
  @Override
  protected long calculateHash(){
    return value.hashCode();
  }
}
