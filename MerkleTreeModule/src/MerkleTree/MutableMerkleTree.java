package MerkleTree;

import java.util.List;

public class MutableMerkleTree<V> implements Hashtree {

  private MerkleInnerNode<V> parent;

  MutableMerkleTree(){
    parent = new MerkleInnerNode<V>();
  }

  @Override
  public void setHash(int position, long hash) {

  }

  @Override
  public void setValue(int positon, Object value) {

  }

  @Override
  public void clear() {
    parent = new MerkleInnerNode<V>();;
  }

  @Override
  public boolean isConsistent() {
    return false;
  }

  @Override
  public List<Integer> getMissing() {
    return null;
  }


}
