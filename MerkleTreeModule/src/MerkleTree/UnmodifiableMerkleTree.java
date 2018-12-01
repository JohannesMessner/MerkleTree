package MerkleTree;

import java.util.List;

/**
 * Merkle-tree that can't be modified.
 * Wraps a MutableMerkleTree
 * @param <V> Type of Object the tree will hold
 */
public class UnmodifiableMerkleTree<V> implements Hashtree<V> {
  private MutableMerkleTree<V> muTree;

  /**
   * Constructor that sets a MutableMerkleTree that gets wrapped.
   * @param muTree the MutableMerkleTree
   */
  UnmodifiableMerkleTree(MutableMerkleTree<V> muTree){
    this.muTree = muTree;
  }

  /**
   * Always throws an UnsupportedOperationException.
   *
   * @param position BFS-index of the Node
   * @param hash hash-code to be set
   */
  @Override
  public void setHash(int position, long hash) {
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }

  /**
   * Always throws an UnsupportedOperationException.
   *
   * @param position int index of the Leaf
   * @param value V to be set in the Leaf
   */
  @Override
  public void setValue(int position, V value) {
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }

  /**
   * Always throws an UnsupportedOperationException.
   */
  @Override
  public void clear() {
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }

  /**
   * Determines if the tree's hashes are consistent among each other.
   *
   * @return boolean indicating whether the tree's hashes are consistent
   */
  @Override
  public boolean isConsistent() {
    return muTree.isConsistent();
  }

  /**
   * Calculates, which Node's hashes are missing for a consistency-check.
   *
   * @return Indices of missing Nodes
   */
  @Override
  public List<Integer> getMissing() {
    return muTree.getMissing();
  }

  MerkleNode<V> search(int index){
    return muTree.search(index);
  }

  /**
   * Always throws an UnsupportedOperationException.
   *
   * @param value V to be inserted
   * @return boolean true, if value is inserted, false if list is too small
   */
  boolean push(V value){
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }

  /**
   * Returns a string-representation of the tree.
   *
   * @return String representing the tree.
   */
  @Override
  public String toString(){
    return muTree.toString();
  }
}
