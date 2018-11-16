package MerkleTree;

public class Tests {
  public static void test(){
    MutableMerkleTree<String> tree = new MutableMerkleTree<String>();

    tree.root.setLeft(new MerkleLeaf<String>(tree.root));
    tree.root.setRight(new MerkleInnerNode<String>(tree.root));
    //tree.root.getLeft().setLeft(new MerkleInnerNode<String>(tree.root.getLeft()));

    System.out.println(tree.search(1, tree.root) instanceof MerkleLeaf);
  }
}
