package merkletree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Class implementing HashTree that is fully mutable. Stores values in it's leaves and calculates
 * hashes. Can evaluate if it is consistent.
 *
 * @param <V> Type of Object the tree will hold
 */
public class MutableMerkleTree<V> implements Hashtree<V> {
  private static final boolean left = true;
  private static final boolean right = false;

  private MerkleInnerNode<V> root;
  private int numberOfLeafs;

  /**
   * Constructor creating enough Nodes to accomodate a certain amount of list-items.
   *
   * @param numberOfLeafs int representing the # of Leafs (= # of list items)
   */
  public MutableMerkleTree(int numberOfLeafs) {
    if (numberOfLeafs < 2) {
      throw new IllegalArgumentException();
    }
    numberOfLeafs = toNextPowerOfTwo(numberOfLeafs);
    this.root = new MerkleInnerNode<V>();
    this.numberOfLeafs = numberOfLeafs;
    createNodeStructure(numberOfLeafs, root);
  }

  /**
   * Creates the needed Nodes below a given Node with a minimum number of leaves.
   *
   * @param numberOfLeafs int representing the minimum number of leaves
   * @param startingNode MerkleInnerNode starting point of the node-creation
   */
  private void createNodeStructure(int numberOfLeafs, MerkleInnerNode<V> startingNode) {
    int treeDepth = log2(numberOfLeafs);
    startingNode.createNodeStructure(treeDepth);
  }

  /** Expands the tree to accomondate twice as many objects. */
  protected void expand() {
    this.root.setParent(new MerkleInnerNode<V>());
    this.root.getParent().setLeft(this.root);
    this.root
        .getParent()
        .setRight(new MerkleInnerNode<V>((MerkleInnerNode<V>) this.root.getParent()));
    this.root = (MerkleInnerNode<V>) this.root.getParent();
    createNodeStructure(this.numberOfLeafs, (MerkleInnerNode<V>) root.getRight());
    this.numberOfLeafs *= 2;
  }

  private int log2(int n) {
    int exponent = 0;
    while (Math.pow(2, exponent) != n) {
      exponent++;
    }
    return exponent;
  }

  private boolean isPowerOfTwo(int n) {
    return n > 1 && ((n & (n - 1)) == 0);
  }

  /**
   * Returns the next bigger power of 2, or n if n is a power of 2.
   *
   * @param n number whose next number of 2 is wanted
   * @return next bigger power of 2, or n if n is a power of 2
   */
  private int toNextPowerOfTwo(int n) {

    while (!isPowerOfTwo(n)) {
      n++;
    }
    return n;
  }

  /**
   * Serches for a Node with a given Index. Indexing follows a breath-first approach.
   *
   * @param index Index of the wanted Node
   * @return MerkleNode that has been found
   */
  MerkleNode<V> search(int index) {
    return search(index, root);
  }

  private MerkleNode<V> search(int index, MerkleInnerNode<V> startingNode) {
    List<Boolean> pathToNode = calculatePathToNode(index);
    MerkleInnerNode<V> currentNode = startingNode;

    for (Boolean direction : pathToNode) {

      if (direction.equals(right) && currentNode.getRight() instanceof MerkleInnerNode) {
        currentNode = (MerkleInnerNode<V>) currentNode.getRight();
      } else if (direction.equals(right) && currentNode.getRight() instanceof MerkleLeaf) {
        return currentNode.getRight();
      } else if (direction.equals(left) && currentNode.getLeft() instanceof MerkleInnerNode) {
        currentNode = (MerkleInnerNode<V>) currentNode.getLeft();
      } else if (direction.equals(left) && currentNode.getLeft() instanceof MerkleLeaf) {
        return currentNode.getLeft();
      }
    }

    return currentNode;
  }

  /**
   * Calculates the path from the root-Node to a given Node.
   *
   * @param index Index of the target-Node
   * @return LinkedList with information about the path to the target-Node.
   */
  private LinkedList<Boolean> calculatePathToNode(int index) {
    return calculatePathToNode(index, new LinkedList<Boolean>());
  }

  private LinkedList<Boolean> calculatePathToNode(int index, LinkedList<Boolean> currentPath) {

    if (index == 0) {
      return currentPath;
    }

    if (index % 2 == 0) {
      currentPath.add(0, right);
      currentPath = calculatePathToNode((index - 2) / 2, currentPath);
    } else {
      currentPath.add(0, left);
      currentPath = calculatePathToNode((index - 1) / 2, currentPath);
    }

    if (currentPath.size() > log2(this.numberOfLeafs)) {
      throw new IndexOutOfBoundsException();
    }

    return currentPath;
  }

  /**
   * Sets a hash for a Node with a given position.
   *
   * @param position BFS-index of the Node
   * @param hash hash-code to be set
   */
  @Override
  public void setHash(int position, long hash) {
    search(position).setHash(hash);
  }

  /**
   * Sets a value for a Leaf with a given index.
   *
   * @param position int index of the Leaf
   * @param value V to be set in the Leaf
   */
  @Override
  public void setValue(int position, V value) {
    int bfsIndex = toBfsIndex(position);
    MerkleNode<V> targetNode = search(bfsIndex);
    if (targetNode instanceof MerkleLeaf) {
      ((MerkleLeaf<V>) targetNode).setValue(value);
    } else {
      throw new IllegalArgumentException("Node ist not a Leaf-Node.");
    }
  }

  /**
   * Transforms a Leaf-position to a BFS-index.
   *
   * @param leafPositon position among Leaves from left to right
   * @return BFS-index for the Leaf
   */
  private int toBfsIndex(int leafPositon) {
    return this.numberOfLeafs - 1 + leafPositon;
  }

  /** Clears the tree from all the hashes ans values. */
  @Override
  public void clear() {
    root.clear();
  }

  /**
   * Pushes a value to the list.
   *
   * @param value V to be inserted
   * @return boolean true, if value is inserted, false if list is too small
   */
  boolean push(V value) {
    return root.push(value);
  }

  /**
   * Determines if the tree's hashes are consistent among each other.
   *
   * @return boolean indicating whether the tree's hashes are consistent
   */
  @Override
  public boolean isConsistent() {
    if (!getMissing().isEmpty()) {
      return false;
    }
    return root.isConsistent();
  }

  /**
   * Calculates, which Node's hashes are missing for a consistency-check.
   *
   * @return Indices of missing Nodes
   */
  @Override
  public List<Integer> getMissing() {
    return root.getMissing(new ArrayList<Integer>(), 0);
  }

  /**
   * Returns a string-representation of the tree.
   *
   * @return String representing the tree.
   */
  @Override
  public String toString() {
    return root.toString();
  }
}
