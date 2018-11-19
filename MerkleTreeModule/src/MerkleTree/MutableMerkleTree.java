package MerkleTree;

import java.util.LinkedList;
import java.util.List;

public class MutableMerkleTree<V> implements Hashtree {
  private static final boolean left = true;
  private static final boolean right = false;

  public MerkleInnerNode<V> root;

  MutableMerkleTree(){
    root = new MerkleInnerNode<V>();
  }

  public MerkleNode<V> search(int index){
    return search(index, root);
  }

  public MerkleNode<V> search(int index, MerkleInnerNode<V> startingNode){
    List<Boolean> pathToNode = calculatePathToNode(index);
    MerkleInnerNode<V> currentNode = startingNode;

    for (Boolean direction : pathToNode){

      if (direction.equals(right) && currentNode.getRight() instanceof MerkleInnerNode) {
        currentNode = (MerkleInnerNode) currentNode.getRight();
      }else if (direction.equals(right) && currentNode.getRight() instanceof  MerkleLeaf){
        return currentNode.getRight();
      }else if (direction.equals(left) && currentNode.getLeft() instanceof MerkleInnerNode){
        currentNode = (MerkleInnerNode) currentNode.getLeft();
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
      calculatePathToNode((index - 2) / 2 , currentPath);
    }else{
      currentPath.add(0, left);
      calculatePathToNode((index - 1) / 2 , currentPath);
    }

    return currentPath;
  }


  @Override
  public void setHash(int position, long hash) {
    search(position).setHash(hash);
  }

  @Override
  public void setValue(int positon, Object value) {
    MerkleNode<V> targetNode = search(positon);
    if (targetNode instanceof MerkleLeaf){
      ((MerkleLeaf) targetNode).setValue(value);
    }
  }

  @Override
  public void clear() {
    root.clear();
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
