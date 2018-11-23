import MerkleTree.MerkleTreeBuilder;

public class Shell {
  public static void main(String[] args) {
    MerkleTreeBuilder<Body> builder = new MerkleTreeBuilder<Body>(2);
    builder.push(new Cuboid(3,2,1));
    builder.push(new Cuboid(7,8, 9));
    builder.push(new Cuboid(10,11,12));
    builder.push(new Cuboid(20,30,40));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(1,1,1));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(10,11,12));

    System.out.println(builder.toString());
    //System.out.println(builder.search(22));

    }
  }
