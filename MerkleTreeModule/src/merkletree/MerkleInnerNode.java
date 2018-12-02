package merkletree;

class MerkleInnerNode<V> extends MerkleNode<V> {

  public MerkleInnerNode() {
    super();
  }

  /**
   * Constructor that sets a Parent-Node.
   *
   * @param parent MerkleTree.MerkleInnerNode that is the Nodes parent
   */
  public MerkleInnerNode(MerkleInnerNode<V> parent) {
    super(parent);
  }

  /**
   * Calcultes the Nodes hash-value based on the children's hashes.
   *
   * @return long hash-value
   */
  @Override
  protected long calculateHash() {
    return getRight().getStoredHash() * getLeft().getStoredHash();
    //    if (getLeft().hasHash() && getRight().hasHash()){
    //      return getLeft().getStoredHash() * getRight().getStoredHash();
    //    }else {
    //      return getRight().calculateHash() * getLeft().calculateHash();
    //    }
  }

  /** Deletes the hash-code for itself and all Nodes below itself. */
  @Override
  public void clear() {
    super.clear();
    getLeft().clear();
    getRight().clear();
  }

  /**
   * Checks if the stored hash is consistent with the hashes of all successors.
   *
   * @return boolean indicating whether the hash-codes ar consistent.
   */
  @Override
  protected boolean isConsistent() {
    if (hasNoHashesUnderneath()) {
      return true;
    } else if (!hasHash()) {
      return getLeft().isConsistent() && getRight().isConsistent();
    } else if (getLeft().hasHash() && getRight().hasHash()) {
      return getLeft().isConsistent()
          && getRight().isConsistent()
          && (getStoredHash() == calculateHash());
    } else {
      return getStoredHash() == (long) calculateHashRecursively();
    }
  }

  @Override
  protected Long calculateHashRecursively() {
    if (getRight() == null || getRight() == null) {
      return null;
    }
    if (getRight().hasHash() && getLeft().hasHash()) {
      return getRight().getStoredHash() * getLeft().getStoredHash();
    }
    if (getLeft().hasHash() && !getRight().hasHash()) {
      return getLeft().getStoredHash() * getRight().calculateHashRecursively();
    }
    if (!getLeft().hasHash() && getRight().hasHash()) {
      return getLeft().calculateHashRecursively() * getRight().getStoredHash();
    }
    if (!getLeft().hasHash() && !getRight().hasHash()) {
      return getLeft().calculateHashRecursively() * getRight().calculateHashRecursively();
    }
    return null;
  }

  /**
   * Adds the value to the string-representation of the Node.
   *
   * @param str String the value will be added to.
   * @return String with the value added to it.
   */
  @Override
  protected String addValue(String str) {
    if (!hasHash()) {
      return str + "* ";
    }
    return str + this.getStoredHash() + " ";
  }

  /**
   * Creates a complete binary Node-structure of a given depth below itself.
   *
   * @param treeDepth depth (= height) of the structure that will be created
   */
  void createNodeStructure(int treeDepth) {
    if (treeDepth > 1) {
      this.setLeft(new MerkleInnerNode<V>(this));
      ((MerkleInnerNode<V>) this.getLeft()).createNodeStructure(treeDepth - 1);
      this.setRight(new MerkleInnerNode<V>(this));
      ((MerkleInnerNode<V>) this.getRight()).createNodeStructure(treeDepth - 1);
    } else if (treeDepth == 1) {
      this.setLeft(new MerkleLeaf<V>(this));
      this.setRight(new MerkleLeaf<V>(this));
    }
  }

  /** Recalculates the hashes of itself and all other Nodes above it. */
  @Override
  protected void update() {
    if (!this.getRight().hasHash() || !this.getLeft().hasHash()) {
      return;
    }
    setHash(calculateHash());
    if (getParent() == null) {
      return;
    }
    getParent().update();
  }
}
