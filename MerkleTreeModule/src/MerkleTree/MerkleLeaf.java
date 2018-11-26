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
    if (value.isPresent()) {
      return value.hashCode();
    }
    return 0;
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

  /**
   * Deletes the stored has-code and the stored value-object.
   */
  @Override
  public void clear(){
    super.clear();
    value = Optional.empty();
  }

  /**
   * Sets the value of the Leaf.
   *
   * @param value V to be inserted
   * @return boolean true that the value has been set.
   */
  @Override
  protected boolean push(V value) {
    this.setValue(value);
    return true;
  }

  /**
   * Adds the value to the string-representation of the Node.
   *
   * @param str String the value will be added to.
   * @return String with the value added to it.
   */
  @Override
  protected String addValue(String str){
    if (this.value.isPresent()){
      return str + "\"" + this.value.get().toString()+ "\"";
    }
    return str + "*";
  }

  @Override
  protected void update(){
    setHash(calculateHash());
    if (getParent() == null){
      return;
    }
    getParent().update();
  }
}
