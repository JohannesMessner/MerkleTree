import MerkleTree.MerkleTreeBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Shell {

  private static MerkleTreeBuilder<Body> builder;

  private static String PROMPT = "merkle> ";
  private static boolean CURRENT_MODE;
  private static final boolean BUILD_MODE = true;
  private static final boolean CHECK_MODE = false;

  private static final String CUBOID_REGEX = "Cuboid(\\d,\\d,\\d)";
  private static final String CYLINDER_REGEX = "Cylinder(\\d,\\d)";

  private static final String INVALID_COMMAND_ERROR = "Error! Not a valid command.";
  private static final String NO_SIZE_ERROR = "Error! You need to specify a tree-sze";
  private static final String WRONG_MODE_ERROR = "Error! This action cannot be performed in this mode";
  private static final String NO_VALUE_ERROR = "Error! You need to specify a value";
  private static final String NO_VALID_VALUE_ERROR = "Error! The specified value is not valid";

  private static final int NO_COMMAND = -1;
  private static final int NEW = 0;
  private static final int PUSH = 1;
  private static final int NEW_CHECK = 2;
  private static final int SET_VAL = 3;
  private static final int SET_HASH = 4;
  private static final int READY = 4;
  private static final int CHECK = 6;
  private static final int CLEAR = 7;
  private static final int DEBUG = 8;
  private static final int HELP = 9;
  private static final int QUIT = 10;

  public static void main(String[] args) throws IOException{
//    MerkleTreeBuilder<Body> builder = new MerkleTreeBuilder<Body>();
//    builder.push(new Cuboid(3,2,1));
//    builder.push(new Cuboid(7,8, 9));
//    builder.push(new Cuboid(10,11,12));
//    builder.push(new Cuboid(20,30,40));
//    System.out.println(builder.toString());
//    Hashtree<Body> tree = builder.build();
//    System.out.println(tree.isConsistent());

    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

    boolean quit = false;
    while(!quit){
      System.out.print(PROMPT);

      String inputLine = stdin.readLine();
      if(inputLine == null) {
        System.out.println("Error!");
      }else {
        handleCommand(inputLine);
      }
    }
    }

  private static void handleCommand(String inputLine){
    Scanner sc = new Scanner(inputLine);
    sc.useDelimiter("\\s+");

    String command = "";
    if (sc.hasNext()) {
      command = sc.next();
    }

//    if (command.equals("NEW")){
//      setMode(BUILD_MODE);
//    }else if (command.equals("NEW_CHECK")){
//      setMode(CHECK_MODE);
//    }

    int commandID = getCommandID(command);
    switch (command){
      case "NEW":
        handleNew(sc);
        break;

      case "PUSH":
        handlePush(sc);
        break;

      case "NEW_CHECK":
        handleNewCheck(sc);
        break;

      case "SET_VAL":
        handleSetVal(sc);
        break;

      case "SET_HASH":
        handeleSetHash(sc);
        break;

      case "READY?":
        handleReady(sc);
        break;

      case "CHECK":
        handleCheck(sc);
        break;

      case "DEBUG":
        handleDebug(sc);
        break;

      case "HELP":
        handleHelp(sc);
        break;

      case "QUIT":
        handleQuit(sc);
        break;

      default:
        System.out.println(INVALID_COMMAND_ERROR);
    }

  }

  private static void handleNew(Scanner sc){
    setMode(BUILD_MODE);

    if (sc.hasNextInt()){
      builder = new MerkleTreeBuilder<Body>( sc.nextInt());
    }else{
      System.out.println(NO_SIZE_ERROR);
    }
  }
  private static int getCommandID(String command){
    return 1;
  }

  private static void handlePush(Scanner sc){
    if (CURRENT_MODE != BUILD_MODE){
      System.out.println(WRONG_MODE_ERROR);
      return;
    }

    if(sc.hasNext()){
      Body bdy = getBody(sc.next());
      if (bdy != null) {
        builder.push(bdy);
      }else{
        System.out.println(NO_VALID_VALUE_ERROR);
      }
    }else {
      System.out.println(NO_VALUE_ERROR);
    }
  }

  private static Body getBody(String value){
    if (value.matches(CUBOID_REGEX)){
      int length = Character.getNumericValue(value.charAt(7));
      int width = Character.getNumericValue(value.charAt(9));
      int height = Character.getNumericValue(value.charAt(11));
      return new Cuboid(length, width, height);
    }
    if (value.matches(CYLINDER_REGEX)){
      int radius = Character.getNumericValue(value.charAt(7));
      int height = Character.getNumericValue(value.charAt(9));
      return new Cylinder(radius, height);
    }
    return null;
  }

  private static void handleNewCheck(Scanner sc){
    setMode(CHECK_MODE);
  }
  private static void setMode(boolean mode){
    CURRENT_MODE = mode;
    if (mode){
      PROMPT = "build> ";
    }else {
      PROMPT = "check> ";
    }
  }
  }
