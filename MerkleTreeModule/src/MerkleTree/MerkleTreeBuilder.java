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
