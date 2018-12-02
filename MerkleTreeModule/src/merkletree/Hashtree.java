package merkletree;

import java.util.List;

/**
 * Interface for a Hash-tree. Stores values in it's leaves and calculates
 * hashes. Can evaluate if it is consistent.
 *
 * @param <V>
 */
public interface Hashtree<V> {

  void setHash(int position, long hash);

  void setValue(int positon, V value);

  void clear();

  boolean isConsistent();

  List<Integer> getMissing();
}
