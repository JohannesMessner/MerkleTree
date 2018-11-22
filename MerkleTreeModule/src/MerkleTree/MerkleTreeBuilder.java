package MerkleTree;

public class MerkleTreeBuilder<V> {

  private MutableMerkleTree<V> tree;

  public MerkleTreeBuilder(){
    this.tree = new MutableMerkleTree<>();
  }

  public MerkleTreeBuilder(int nuberOfLeaves){
    this.tree = new MutableMerkleTree<>(nuberOfLeaves);
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

  @Override
  public String toString(){
    return this.tree.toString();
  }

  public void setHash(int position, long hash){
    tree.setHash(position, hash);
  }

}
