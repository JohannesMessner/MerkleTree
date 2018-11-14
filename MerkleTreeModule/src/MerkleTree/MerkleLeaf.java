package MerkleTree;

import java.util.Optional;

class MerkleLeaf<V> extends MerkleNode {

  private Optional<V> value;
  //protected Optional<Long> hash;

  MerkleLeaf(){
    super();
    this.value = Optional.empty();
  }

  MerkleLeaf(MerkleInnerNode<V> parent){
    super(parent);
    this.value = Optional.empty();
  }

  private void setValue(V value){
    this.value = Optional.of(value);
    super.setHash(calculateHash());
  }

  @Override
  protected long calculateHash(){
    return value.hashCode();
  }
}
