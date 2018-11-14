package MerkleTree;

import java.util.List;

public interface Hashtree<V> {

  void setHash(int position, long hash);

  void setValue(int positon, V value);

  void clear();

  boolean isConsistent();

  List<Integer> getMissing();
}
