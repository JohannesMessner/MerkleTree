package MerkleTree;

public class MerkleTreeBuilder<V> {

  private MutableMerkleTree<V> tree;

  public MerkleTreeBuilder<V> push(V value){

  }

  public Hashtree<V> build(){
    return (UnmodifiableMerkleTree) tree;
  }

  public void clear(){
    tree.clear();
  }

  @Override
  public String toString(){

  }

}
