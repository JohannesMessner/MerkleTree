package MerkleTree;

import java.util.Optional;

class MerkleLeaf<V> extends MerkleNode {

  private MerkleInnerNode<V> parent;
  private Optional<V> value;
  //private Optional<Long> hash;

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

  private void setHash(){
//    Implementation mit ifPresent wäre schöner.
//    Man kann hier in of() jeden Datentyp übergeben. Warum? dürfte laut Vererbung nicht funktioniern!

    if(value.isPresent()){
      hash = Optional.of(value.get().hashCode());
    }
  }
}
