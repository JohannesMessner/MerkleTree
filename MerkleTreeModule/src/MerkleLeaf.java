import java.util.Optional;

public class MerkleLeaf<V> extends MerkleNode {

  private MerkleInnerNode<V> parent;
  private Optional<V> value;

  MerkleLeaf(){
    super();
    this.value = Optional.empty();
  }

  MerkleLeaf(MerkleInnerNode<V> parent){
    this.parent = parent;
    this.value = Optional.empty();
  }

  private void setValue(V value){
    this.value = Optional.of(value);
    this.hash = Optional.of(value.hashCode());
  }
}
