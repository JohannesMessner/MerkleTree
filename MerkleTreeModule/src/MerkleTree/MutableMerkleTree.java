package MerkleTree;

import java.util.LinkedList;
import java.util.List;

public class MutableMerkleTree<V> implements Hashtree {
  private static final boolean left = true;
  private static final boolean right = false;

  private MerkleInnerNode<V> parent;

  MutableMerkleTree(){
    parent = new MerkleInnerNode<V>();
  }

  public MerkleNode<V> search(int index, MerkleInnerNode<V> startingNode){
    List<Boolean> pathToNode = calculatePathToNode(index);
    MerkleInnerNode currentNode = startingNode;

    for (Boolean direction : pathToNode){

      if (direction.equals(right) && currentNode.getRight() instanceof MerkleInnerNode) {
        currentNode = (MerkleInnerNode) currentNode.getRight();
      }else if (direction.equals(right) && currentNode.getRight() instanceof  MerkleLeaf){
        currentNode = (MerkleInnerNode) currentNode.getRight();
        break;
      }else if (direction.equals(left) && currentNode.getLeft() instanceof MerkleInnerNode){
        currentNode = (MerkleInnerNode) currentNode.getLeft();
      }else if (direction.equals(left) && currentNode.getLeft() instanceof MerkleLeaf){
        currentNode = (MerkleInnerNode) currentNode.getLeft();
        break;
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
      calculatePathToNode((index - 2) / 2 , currentPath);
    }else{
      currentPath.add(0, left);
      calculatePathToNode((index - 1) / 2 , currentPath);
    }

    return currentPath;
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
