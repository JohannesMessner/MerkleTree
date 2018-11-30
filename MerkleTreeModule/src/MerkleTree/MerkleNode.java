package MerkleTree;

import java.util.List;
import java.util.Optional;

abstract class MerkleNode<V> {

  private MerkleNode<V> parent;
  private MerkleNode<V> left;
  private MerkleNode<V> right;
  private Optional<Long> hash;

  /** Constructor that assigns an empty Optional-object to hash. */
  MerkleNode(){
    this.hash = Optional.empty();
  }

  /** Alternative Constructor that also assigns a parent-node. */
  MerkleNode(MerkleInnerNode<V> parent){
    this.hash = Optional.empty();
    this.parent = parent;
  }

  void setParent(MerkleNode<V> parent){
    if (this.parent == null) {
      this.parent = parent;
    }
  }

  /**
   * Checks, if the stored hash of the Node is consistent with the expected hash.
   *
   * @return boolean that indicates if the hash is consistent.
   */
  protected boolean isConsistent(){
    return hash.get() == calculateHash();
  }

  /**
   * Returns hash-code without recalculating it.
   *
   * @return long hash-code
   */
  public long getStoredHash(){
    //hässlich, besser wäre über ifPresent
    if(hasHash()){
      return hash.get();
    }
    return 0;
  }

  /**
   * Return whether a hash-code is stored in the Node.
   *
   * @return boolean indicating whether hash-code is present.
   */
  public boolean hasHash(){
    return hash.isPresent();
  }

  protected void setHash(long hashValue){
    hash = Optional.of(hashValue);
  }

  protected MerkleNode<V> getParent(){
    return parent;
  }

  /**
   * Recalculates the hash-code for itself and all nodes above itself.
   */
  protected abstract void update();

  /**
   * Abstract method to calculate the node's hash-code.
   *
   * @return long hash-code
   */
  protected abstract long calculateHash();

  protected boolean setLeft(MerkleNode<V> left){
    this.left = left;
    return true;
  }

  protected boolean setRight(MerkleNode<V> right){
    this.right = right;
    return true;
  }

  /**
   * Deletes the stored hash-code.
   */
  public void clear(){
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
  protected boolean push(V value){
    if (this.getLeft() == null && this.getRight() == null){
      return false;
    }

    if (!this.getLeft().hasHash()){
      return this.getLeft().push(value);
    }else if (!this.getRight().hasHash()){
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
  public String toString(){
    String outputString = "";
    outputString = addOpenBracket(outputString);
    outputString = addValue(outputString);
    //outputString = addSpace(outputString);
    outputString = addLeftSubtree(outputString);
    //outputString = addSpace(outputString);
    outputString = addRightSubree(outputString);
    outputString = addClosingBracket(outputString);
    return outputString;
  }

  private String addOpenBracket(String str){
    return "(" + str;
  }

  protected abstract String addValue(String str);

  private String addSpace(String str){
    return str + " ";
  }

  private String addLeftSubtree(String str){
    StringBuilder builder = new StringBuilder(str);

    if (left != null){
      builder.append(left.toString());
      builder.append(" ");
    }

    return builder.toString();
  }

  private String addRightSubree(String str){
    StringBuilder builder = new StringBuilder(str);

    if (right != null){
      builder.append(right.toString());
    }

    return builder.toString();
  }

  private String addClosingBracket(String str){
    return str + ")";
  }

  List<Integer> getMissing(List<Integer> currentlyMissing, int index){

    if (hasNoHashesUnderneath() && !hasHash() && (siblingHasHashesUnderneath(index) || parentIsRoot())){
      currentlyMissing.add(index);
      return currentlyMissing;
    }else{
      if (getRight() != null && getLeft() != null) {
        getLeft().getMissing(currentlyMissing, 2 * index + 1);
        getRight().getMissing(currentlyMissing, 2 * index + 2);
      }
      return currentlyMissing;
    }
  }

  private boolean parentIsRoot(){

    if (parent == null){
      return false;
    }else {
      return parent.getParent() == null;
    }
  }

  private boolean siblingHasHashesUnderneath(int index){
    MerkleNode<V> sibling;
    if (parent != null){
      if (index % 2 != 0){
        sibling = parent.getRight();
      }else {
        sibling = parent.getLeft();
      }
    }else{
      return true;
    }
    return !sibling.hasNoHashesUnderneath() || sibling.hasHash();
  }

  private boolean hasNoHashesUnderneath(){
    if (getLeft() == null || getRight() == null){
      return true;
    }
    if (left.hasHash() || right.hasHash()){
      return false;
    }
    return left.hasNoHashesUnderneath() && right.hasNoHashesUnderneath();
  }

//  void addIndices(List<Integer> currentlyMissing, int index){
//    if (parent == null){
//      return;
//    }
//    MerkleNode<V> sibling;
//    int parentIndex;
//    if (index % 2 == 0){
//      parentIndex = (index - 2)/2;
//      sibling = parent.getLeft();
//      if (!sibling.getLeft().hasHash() && !sibling.getRight().hasHash()){
//        currentlyMissing.add(index - 1);
//      }
//    }else {
//      parentIndex = (index - 1)/2;
//      sibling = parent.getRight();
//      boolean siblingCantBeCalculated =
//      if (siblingCantBeCalculated){
//        currentlyMissing.add(index + 1);
//      }
//    }
//    parent.addIndices(currentlyMissing, parentIndex);
//  }

  //abstract boolean isMissing();

//  List<Integer> getMissing(List<Integer> currentlyMissing){
//    if (getParent() != null){
//      if (!getParent().hasHash() && ( getParent().getLeft().hasHash() )
//    }
//  }
}
