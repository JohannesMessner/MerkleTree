package MerkleTree;

import java.util.Optional;

class MerkleLeaf<V> extends MerkleNode<V> {

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

  protected void setValue(V value){
    this.value = Optional.of(value);
    update();
  }

  /** Calculates the hash-code. */
  @Override
  protected long calculateHash(){
    return value.hashCode();
  }

  @Override
  protected boolean setLeft(MerkleNode left) {
    return false;
  }

  @Override
  protected boolean setRight(MerkleNode right) {
    return false;
  }

  @Override
  public MerkleNode<V> getLeft() {
    return null;
  }

  @Override
  public MerkleNode<V> getRight() {
    return null;
  }

  @Override
  public void clear(){
    super.clear();
    value = Optional.empty();
  }

  @Override
  protected boolean push(V value) {
    this.setValue(value);
    return true;
  }

  @Override
  protected String addValue(String str){
    if (this.value.isPresent()){
      return str + "\"" + this.value.get().toString()+ "\"";
    }
    return str + "*";
  }
}
