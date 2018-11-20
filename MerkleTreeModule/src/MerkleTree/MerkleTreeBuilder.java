package MerkleTree;

public class MerkleTreeBuilder<V> {

  private MutableMerkleTree<V> tree;

  MerkleTreeBuilder(){
    this.tree = new MutableMerkleTree<>();
  }

  public MerkleTreeBuilder<V> push(V value){
    if (!tree.push(value)){
      expandTree();
      push(value);
    }
    return this;
  }

  private void expandTree(){
    tree.expand();
  }

  public Hashtree<V> build(){
    return (UnmodifiableMerkleTree<V>) tree;
  }

  public void clear(){
    tree.clear();
  }

  //ToDo
  @Override
  public String toString(){
    return "";
  }

}
