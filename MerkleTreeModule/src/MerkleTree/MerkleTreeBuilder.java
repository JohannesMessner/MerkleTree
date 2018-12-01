package MerkleTree;

/**
 * Class that builds and modifies a HashTree.
 *
 * @param <V> Type of Object the HashTree will hold
 */
public class MerkleTreeBuilder<V> {

  private MutableMerkleTree<V> tree;

  public MerkleTreeBuilder(int numberOfLeaves) {
    this.tree = new MutableMerkleTree<>(numberOfLeaves);
  }

  /**
   * Inserts a value into the tree. Expands the tree if necessary.
   *
   * @param value V to be inserted
   * @return itself
   */
  public MerkleTreeBuilder<V> push(V value) {
    if (!tree.push(value)) {
      expandTree();
      push(value);
    }
    return this;
  }

  /** Doubles the tree in size. */
  private void expandTree() {
    tree.expand();
  }

  /**
   * Returns a unmodifiable version of the tree.
   *
   * @return UnmodifiableMerkleTree equivalent to the mutable tree
   */
  public Hashtree<V> build() {
    return new UnmodifiableMerkleTree<>(tree);
  }

  /** Clears the tree of all of it's hashes and values. */
  public void clear() {
    tree.clear();
  }

  @Override
  public String toString() {
    return this.tree.toString();
  }

  /**
   * Sets the hash-code for a Node at a given Index. Indexing follows a breath-first approach.
   *
   * @param position index of the Node
   * @param hash long representing the hash-code
   */
  public void setHash(int position, long hash) {
    tree.setHash(position, hash);
  }
}
