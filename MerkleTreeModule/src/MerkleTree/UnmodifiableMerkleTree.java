package MerkleTree;

import java.util.List;

public class UnmodifiableMerkleTree<V> implements Hashtree<V> {
  private MutableMerkleTree<V> muTree;
  UnmodifiableMerkleTree(MutableMerkleTree<V> muTree){
    this.muTree = muTree;
  }
  @Override
  public void setHash(int position, long hash) {
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }

  @Override
  public void setValue(int positon, V value) {
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }

  @Override
  public void clear() {
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }

  @Override
  public boolean isConsistent() {
    return muTree.isConsistent();
  }

  @Override
  public List<Integer> getMissing() {
    return muTree.getMissing();
  }

  MerkleNode<V> search(int index){
    return muTree.search(index);
  }

  boolean push(V value){
    throw new UnsupportedOperationException("This tree is unmodifiable");
  }
}
