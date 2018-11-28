import MerkleTree.MerkleTreeBuilder;
import MerkleTree.MutableMerkleTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;

/**
 * Text-based UI-class for the interaction with a Merkle-Tree.
 * Can be used to build and to check a tree.
 */
public class Shell {

  private static MerkleTreeBuilder<Body> builder;
  private static MutableMerkleTree<Body> tree;

  private static String PROMPT = "merkle> ";
  private static boolean CURRENT_MODE;
  private static boolean QUIT = false;
  private static final boolean BUILD_MODE = true;
  private static final boolean CHECK_MODE = false;

  private static final String CUBOID_REGEX = "Cuboid[(](\\d+),(\\d+),(\\d+)[)]";
  private static final String CYLINDER_REGEX = "Cylinder[(]\\d+,\\d+[)]";


  private static final String INVALID_COMMAND_ERROR = "Error! Not a valid command.";
  private static final String NO_SIZE_ERROR = "Error! You need to specify a tree-size";
  private static final String WRONG_MODE_ERROR = "Error! This action cannot be performed in this mode";
  private static final String NO_VALUE_ERROR = "Error! You need to specify a value";
  private static final String NO_VALID_VALUE_ERROR = "Error! The specified value is not valid";
  private static final String NO_HASH_ERROR = "Error! You need to specify a hash-value";
  private static final String NO_POSITION_ERROR = "Error! You need to specify a postion in the tree";
  private static final String NO_VALID_POSITION_ERROR = "Error! The specified position is not in the tree";
  private static final String READY_MESSAGE = "READY!";
  private static final String CHEK_PASSED_MESSAGE = "ACK";
  private static final String CHEK_NOT_PASSED_MESSAGE = "REJ";
  private static final String CHECK_HELP_MESSAGE = "Help-message goes here";
  private static final String BUILD_HELP_MESSAGE = "Help-message goes here";

  /**
   *
   *
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException{

    BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

    while(!QUIT){
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
        handleReady();
        break;

      case "CHECK":
        handleCheck();
        break;

      case "CLEAR":
        handleClear();
        break;

      case "DEBUG":
        handleDebug();
        break;

      case "HELP":
        handleHelp();
        break;

      case "QUIT":
        handleQuit();
        break;

      default:
        System.out.println(INVALID_COMMAND_ERROR);
    }

  }

  private static void handleNew(Scanner sc){

    if (sc.hasNextInt()){
      builder = new MerkleTreeBuilder<Body>( sc.nextInt());
      setMode(BUILD_MODE);
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

    String numbersOnly = value.replaceAll("[^\\d]", " ");
    Scanner bodyScanner = new Scanner(numbersOnly);

    if (value.matches(CUBOID_REGEX)){
      int length = bodyScanner.nextInt();
      int width = bodyScanner.nextInt();
      int height = bodyScanner.nextInt();
      bodyScanner.close();
      return new Cuboid(length, width, height);
    }
    if (value.matches(CYLINDER_REGEX)){
      int radius = bodyScanner.nextInt();
      int height = bodyScanner.nextInt();
      bodyScanner.close();
      return new Cylinder(radius, height);
    }
    bodyScanner.close();
    return null;
  }

  private static void handleNewCheck(Scanner sc){
    setMode(CHECK_MODE);
    int treeSize;
    long hash  = 0;
    if (sc.hasNextInt()){
      treeSize = sc.nextInt();
    }else {
      System.out.println(NO_SIZE_ERROR);
      return;
    }

    if (sc.hasNextLong()){
      hash = sc.nextLong();
    }else {
      System.out.println(NO_HASH_ERROR);
      return;
    }
    tree = new MutableMerkleTree<Body>(treeSize);
    tree.setHash(0, hash);
  }

  private static void handleSetVal(Scanner sc){
    if (CURRENT_MODE != CHECK_MODE){
      System.out.println(WRONG_MODE_ERROR);
      return;
    }

    int position;
    Body value;
    if (sc.hasNextInt()){
      position = sc.nextInt();
    }else{
      System.out.println(NO_POSITION_ERROR);
      return;
    }
    if (sc.hasNext()){
      value = getBody(sc.next());
      if (value == null){
        System.out.println(NO_VALID_VALUE_ERROR);
        return;
      }
    }else {
      System.out.println(NO_VALUE_ERROR);
      return;
    }

    try{
      tree.setValue(position, value);
    }catch (IndexOutOfBoundsException e){
      System.out.println(NO_VALID_POSITION_ERROR);
    }
  }

  private static void handeleSetHash(Scanner sc){
    if (CURRENT_MODE != CHECK_MODE){
      System.out.println(WRONG_MODE_ERROR);
      return;
    }

    int position;
    long hash;
    if (sc.hasNextInt()){
      position = sc.nextInt();
    }else{
      System.out.println(NO_POSITION_ERROR);
      return;
    }
    if (sc.hasNextLong()){
      hash = sc.nextLong();
    }else {
      System.out.println(NO_HASH_ERROR);
      return;
    }

    try{
      tree.setHash(position, hash);
    }catch (IndexOutOfBoundsException e){
      System.out.println(NO_VALID_POSITION_ERROR);
    }
  }

  private static void handleReady(){
    if (CURRENT_MODE != CHECK_MODE){
      System.out.println(WRONG_MODE_ERROR);
      return;
    }

    List<Integer> missingNodes = tree.getMissing();
    if (missingNodes.isEmpty()){
      System.out.println(READY_MESSAGE);
    }else {
      System.out.println(toIndexString(missingNodes));
    }
  }

  private static void handleCheck(){
    if (CURRENT_MODE != CHECK_MODE){
      System.out.println(WRONG_MODE_ERROR);
      return;
    }

    boolean isConsistent = tree.isConsistent();
    if (isConsistent){
      System.out.println(CHEK_PASSED_MESSAGE);
    }else {
      System.out.println(CHEK_NOT_PASSED_MESSAGE);
    }
  }

  private static void handleDebug(){
    if (CURRENT_MODE == CHECK_MODE){
      System.out.println(tree.toString());
    }else{
      System.out.println(builder.toString());
    }
  }

  private static void handleClear(){
    if (CURRENT_MODE == CHECK_MODE){
      tree.clear();
    }else{
      builder.clear();
    }
  }

  private static void handleHelp(){
    if (CURRENT_MODE == CHECK_MODE){
      System.out.println(CHECK_HELP_MESSAGE);
    }else {
      System.out.println(BUILD_HELP_MESSAGE);
    }
  }

  private static void handleQuit(){
    QUIT = true;
  }

  private static String toIndexString(List<Integer> indices){
    StringBuilder strbuilder = new StringBuilder("[");
    for (Integer i : indices){
      strbuilder.append(i);
      strbuilder.append(",");
    }
    strbuilder.append("]");
    return strbuilder.toString();
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
