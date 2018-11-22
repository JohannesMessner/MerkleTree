import MerkleTree.MerkleTreeBuilder;

public class Shell {
  public static void main(String[] args) {
    MerkleTreeBuilder<Cuboid> builder = new MerkleTreeBuilder<Cuboid>();
    builder.push(new Cuboid(3,2,1));
    builder.push(new Cuboid(7,8, 9));
    builder.push(new Cuboid(10,11,12));
    System.out.println(builder.toString());
    builder.setHash(2, 5);
    System.out.println(builder.toString());

    }
  }
