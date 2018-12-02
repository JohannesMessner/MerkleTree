package merkletree;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * Class of Nodes in a MerkleTree.
 *
 * @param <V> Type of Object the Node will hold
 */
abstract class MerkleNode<V> {

  private MerkleNode<V> parent;
  private MerkleNode<V> left;
  private MerkleNode<V> right;
  private Optional<Long> hash;

  /** Constructor that assigns an empty Optional-object to hash. */
  MerkleNode() {
    this.hash = Optional.empty();
  }

  /** Alternative Constructor that also assigns a parent-node. */
  MerkleNode(MerkleInnerNode<V> parent) {
    this.hash = Optional.empty();
    this.parent = parent;
  }

  /**
   * Assignes a parent-Node to the Node.
   *
   * @param parent parent-Node
   */
  void setParent(MerkleNode<V> parent) {
    if (this.parent == null) {
      this.parent = parent;
    }
  }

  /**
   * Checks, if the stored hash of the Node is consistent with the expected hash.
   *
   * @return boolean that indicates if the hash is consistent.
   */
  protected abstract boolean isConsistent();

  /**
   * Returns hash-code without recalculating it.
   *
   * @return long hash-code
   * @throws NoSuchElementException when no hash-code is stored
   */
  long getStoredHash() throws NoSuchElementException {
    return hash.get();
  }

  /**
   * Return whether a hash-code is stored in the Node.
   *
   * @return boolean indicating whether hash-code is present.
   */
  boolean hasHash() {
    return hash.isPresent();
  }

  /**
   * Sets a hash-code.
   *
   * @param hashValue value of the hash-code
   */
  void setHash(long hashValue) {
    hash = Optional.of(hashValue);
  }

  MerkleNode<V> getParent() {
    return parent;
  }

  /** Recalculates the hash-code for itself and all nodes above itself. */
  protected abstract void update();

  /**
   * Abstract method to calculate the node's hash-code.
   *
   * @return long hash-code
   */
  protected abstract long calculateHash();

  protected boolean setLeft(MerkleNode<V> left) {
    this.left = left;
    return true;
  }

  protected boolean setRight(MerkleNode<V> right) {
    this.right = right;
    return true;
  }

  /** Deletes the stored hash-code. */
  public void clear() {
    hash = Optional.empty();
  }

  public MerkleNode<V> getLeft() {
    return left;
  }

  public MerkleNode<V> getRight() {
    return right;
  }

  /**
   * Pushes a value to the list.
   *
   * @param value V to be inserted
   * @return boolean true, if value is inserted, false if list is too small
   */
  protected boolean push(V value) {
    if (this.getLeft() == null && this.getRight() == null) {
      return false;
    }

    if (!this.getLeft().hasHash()) {
      return this.getLeft().push(value);
    } else if (!this.getRight().hasHash()) {
      return this.getRight().push(value);
    }

    return false;
  }

  /**
   * Returns a string-representation of the tree-structure below the Node.
   *
   * @return String represetning the tree-structure below the Node.
   */
  @Override
  public String toString() {
    String outputString = "";
    outputString = addOpenBracket(outputString);
    outputString = addValue(outputString);
    outputString = addLeftSubtree(outputString);
    outputString = addRightSubree(outputString);
    outputString = addClosingBracket(outputString);
    return outputString;
  }

  private String addOpenBracket(String str) {
    return "(" + str;
  }

  protected abstract String addValue(String str);

  private String addLeftSubtree(String str) {
    StringBuilder builder = new StringBuilder(str);

    if (left != null) {
      builder.append(left.toString());
      builder.append(" ");
    }

    return builder.toString();
  }

  private String addRightSubree(String str) {
    StringBuilder builder = new StringBuilder(str);

    if (right != null) {
      builder.append(right.toString());
    }

    return builder.toString();
  }

  private String addClosingBracket(String str) {
    return str + ")";
  }

  /**
   * Returns which hashes below itself are missing for a consistency-check.
   *
   * @param currentlyMissing indices of hashes that are already missing
   * @param index index of the Node itself
   * @return List of the indices of the missing hashes
   */
  List<Integer> getMissing(List<Integer> currentlyMissing, int index) {

    if (hasNoHashesUnderneath()
        && !hasHash()
        && (siblingHasHashesUnderneath(index) || parentIsRoot())) {
      currentlyMissing.add(index);
      return currentlyMissing;
    } else {
      if (getRight() != null && getLeft() != null) {
        getRight().getMissing(currentlyMissing, 2 * index + 2);
        getLeft().getMissing(currentlyMissing, 2 * index + 1);
      }
      return currentlyMissing;
    }
  }

  private boolean parentIsRoot() {

    if (parent == null) {
      return false;
    } else {
      return parent.getParent() == null;
    }
  }

  /**
   * Returns whether it's sibling hsa hashes underneath it.
   *
   * @param index of the Node itsels
   * @return boolean indicating the presence of hashes below the sibling-Node
   */
  private boolean siblingHasHashesUnderneath(int index) {
    MerkleNode<V> sibling;
    if (parent != null) {
      if (index % 2 != 0) {
        sibling = parent.getRight();
      } else {
        sibling = parent.getLeft();
      }
    } else {
      return true;
    }
    return !sibling.hasNoHashesUnderneath() || sibling.hasHash();
  }

  /**
   * Returns whether any of the Nodes below itself contain hashes.
   *
   * @return boolean representing the presence of hashes below itself
   */
  protected boolean hasNoHashesUnderneath() {
    if (getLeft() == null || getRight() == null) {
      return true;
    }
    if (left.hasHash() || right.hasHash()) {
      return false;
    }
    return left.hasNoHashesUnderneath() && right.hasNoHashesUnderneath();
  }

  protected abstract Long calculateHashRecursively();
}
