package MerkleTree;

import java.util.LinkedList;
import java.util.List;

public class MutableMerkleTree<V> implements Hashtree<V> {
  private static final boolean left = true;
  private static final boolean right = false;

  private MerkleInnerNode<V> root;
  private int numberOfLeafs;


  MutableMerkleTree(){
    this(0);
  }

  /**
   * Constructor creating enough nodes to accomodate a certain amount of list-items.
   *
   * @param numberOfLeafs int representing the # of Leafs (= # of list items)
   */
  MutableMerkleTree(int numberOfLeafs){
    numberOfLeafs = toNextPowerOfTwo(numberOfLeafs);
    System.out.println("Leaves at creation: " +numberOfLeafs);
    this.root = new MerkleInnerNode<V>();
    this.numberOfLeafs = numberOfLeafs;
    createNodeStructure(numberOfLeafs, root);

  }

  /**
   * Creates the needed Nodes below a given Node with a minimum number of leaves.
   *
   * @param numberOfLeafs int representing the minimum number of leaves
   * @param startingNode MerkleInnerNode<V> starting point of the node-creation
   */
  private void createNodeStructure(int numberOfLeafs, MerkleInnerNode<V> startingNode){
    //numberOfLeafs = toNextPowerOfTwo(numberOfLeafs);
    int treeDepth = log2(numberOfLeafs);
    startingNode.createNodeStructure(treeDepth);

//    MerkleInnerNode<V> currentNode = startingNode;
//    for (int i = 0; i < treeDepth; i++){
//      if (i == treeDepth-1){
//        currentNode.setLeft(new MerkleLeaf<V>(currentNode));
//        currentNode.setRight(new MerkleLeaf<V>(currentNode));
//      }else{
//        currentNode.setLeft(new MerkleInnerNode<V>(currentNode));
//        currentNode.setRight(new MerkleInnerNode<V>(currentNode));
//      }
//    }
  }

  protected void expand(){
    this.root.setParent(new MerkleInnerNode<V>());
    this.root.getParent().setLeft(this.root);
    this.root.getParent().setRight(new MerkleInnerNode<V>((MerkleInnerNode<V>) this.root.getParent()));
    this.root = (MerkleInnerNode<V>) this.root.getParent();
    createNodeStructure(this.numberOfLeafs, (MerkleInnerNode<V>) root.getRight());
    this.numberOfLeafs *= 2;
  }

  private int log2(int n){
    int exponent = 0;
    while (Math.pow(2, exponent) != n){
      exponent++;
    }
    return exponent;
  }

  private boolean isPowerOfTwo(int n){
    return n > 1 && ((n &(n-1)) == 0 );
  }

  private int toNextPowerOfTwo(int n){

    while (!isPowerOfTwo(n)){
      n++;
    }
    return n;
  }

  public MerkleNode<V> search(int index){
    return search(index, root);
  }

  public MerkleNode<V> search(int index, MerkleInnerNode<V> startingNode){
    List<Boolean> pathToNode = calculatePathToNode(index);
    MerkleInnerNode<V> currentNode = startingNode;

    for (Boolean direction : pathToNode){

      if (direction.equals(right) && currentNode.getRight() instanceof MerkleInnerNode) {
        currentNode = (MerkleInnerNode<V>) currentNode.getRight();
      }else if (direction.equals(right) && currentNode.getRight() instanceof  MerkleLeaf){
        return currentNode.getRight();
      }else if (direction.equals(left) && currentNode.getLeft() instanceof MerkleInnerNode){
        currentNode = (MerkleInnerNode<V>) currentNode.getLeft();
      }else if (direction.equals(left) && currentNode.getLeft() instanceof MerkleLeaf){
        return currentNode.getLeft();
      }
    }

    return currentNode;
  }


  public LinkedList<Boolean> calculatePathToNode(int index){
    return calculatePathToNode(index, new LinkedList<Boolean>());
  }

  public LinkedList<Boolean> calculatePathToNode(int index, LinkedList<Boolean> currentPath){

    if (index == 0){
      return currentPath;
    }

    if (index % 2 == 0){
      currentPath.add(0, right);
      currentPath = calculatePathToNode((index - 2) / 2 , currentPath);
    }else{
      currentPath.add(0, left);
      currentPath = calculatePathToNode((index - 1) / 2 , currentPath);
    }

    return currentPath;
  }


  @Override
  public void setHash(int position, long hash) {
    search(position).setHash(hash);
  }

  @Override
  public void setValue(int positon, V value) {
    MerkleNode<V> targetNode = search(positon);
    if (targetNode instanceof MerkleLeaf){
      ((MerkleLeaf<V>) targetNode).setValue(value);
    }
  }

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
  protected boolean push(V value){
    return root.push(value);
  }

  //ToDo
  @Override
  public boolean isConsistent() {
    if (getMissing().isEmpty()){
      return false;
    }
    return root.isConsistent();
  }

  //ToDo
  @Override
  public List<Integer> getMissing() {
    return null;
  }

  @Override
  public String toString(){
    System.out.println(this.numberOfLeafs);
    return root.toString();
  }
}
