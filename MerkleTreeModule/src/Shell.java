import MerkleTree.MerkleTreeBuilder;
import MerkleTree.MutableMerkleTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
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
  private static int CURRENT_MODE;
  private static boolean QUIT = false;
  private static long ROOT_HASH = 0; //Value gets set whenever check-mode is entered,
                                     // so the value 0 is not relevant,
                                     // though required by the compiler
  private static final int START_MODE = 0;
  private static final int BUILD_MODE = 1;
  private static final int CHECK_MODE = 2;

  private static final String CUBOID_REGEX = "Cuboid[(](\\d+),(\\d+),(\\d+)[)]";
  private static final String CYLINDER_REGEX = "Cylinder[(]\\d+,\\d+[)]";


  private static final String INVALID_COMMAND_ERROR = "Error! Not a valid command.";
  private static final String NO_SIZE_ERROR = "Error! You need to specify a tree-size";
  private static final String NO_VALID_SIZE_ERROR = "Error! You need to specify a valid tree-size";
  private static final String WRONG_MODE_ERROR = "Error! This action cannot be performed in this mode";
  private static final String NO_VALUE_ERROR = "Error! You need to specify a value";
  private static final String NO_VALID_VALUE_ERROR = "Error! The specified value is not valid";
  private static final String NO_HASH_ERROR = "Error! You need to specify a hash-value";
  private static final String NO_POSITION_ERROR = "Error! You need to specify a postion in the tree";
  private static final String NO_VALID_POSITION_ERROR = "Error! The specified position is not in the tree";
  private static final String NOT_READY_ERROR = "Error! The tree is not ready for the check";
  private static final String READY_MESSAGE = "READY!";
  private static final String CHEK_PASSED_MESSAGE = "ACK";
  private static final String CHEK_NOT_PASSED_MESSAGE = "REJ";
  private static final String CHECK_HELP_MESSAGE = "Help-message goes here";
  private static final String BUILD_HELP_MESSAGE = "Help-message goes here";
  private static final String GENERAL_HELP_MESSAGE = "Help-message goes here";

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
        QUIT = true;
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
      case "new":
        handleNew(sc);
        break;

      case "PUSH":
      case "push":
        handlePush(sc);
        break;

      case "NEW_CHECK":
      case "new_check":
        handleNewCheck(sc);
        break;

      case "SET_VAL":
      case "set_val":
        handleSetVal(sc);
        break;

      case "SET_HASH":
      case "set_hash":
        handleSetHash(sc);
        break;

      case "READY?":
      case "ready?":
        handleReady();
        break;

      case "CHECK":
      case "check":
        handleCheck();
        break;

      case "CLEAR":
      case "clear":
        handleClear();
        break;

      case "DEBUG":
      case "debug":
        handleDebug();
        break;

      case "HELP":
      case "help":
        handleHelp();
        break;

      case "QUIT":
      case "quit":
        handleQuit(sc);
        break;

      default:
        System.out.println(INVALID_COMMAND_ERROR);
    }

  }

  private static void handleNew(Scanner sc){

    if (sc.hasNextInt()){
      try {
        builder = new MerkleTreeBuilder<Body>(sc.nextInt());
        setMode(BUILD_MODE);
      } catch (IllegalArgumentException e){
        System.out.println(NO_VALID_SIZE_ERROR);
      }
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
    ROOT_HASH = hash;
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
      long rootHash = ROOT_HASH;
      tree.setValue(position, value);
      tree.setHash(0, rootHash);
    }catch (IllegalArgumentException e){
      System.out.println(NO_VALID_POSITION_ERROR);
    }
  }

  private static void handleSetHash(Scanner sc){
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
      ROOT_HASH = hash;
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
    if (!tree.getMissing().isEmpty()){
      System.out.println(NOT_READY_ERROR);
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
    }else if (CURRENT_MODE == BUILD_MODE){
      System.out.println(builder.toString());
    }else{
      System.out.println(WRONG_MODE_ERROR);
    }
  }

  private static void handleClear(){
    if (CURRENT_MODE == CHECK_MODE){
      tree.clear();
      tree.setHash(0, ROOT_HASH);
    }else if (CURRENT_MODE == BUILD_MODE){
      builder.clear();
    }else {
      System.out.println(WRONG_MODE_ERROR);
    }
  }

  private static void handleHelp(){
    if (CURRENT_MODE == CHECK_MODE){
      System.out.println(CHECK_HELP_MESSAGE);
    }else if (CURRENT_MODE == BUILD_MODE){
      System.out.println(BUILD_HELP_MESSAGE);
    }else {
      System.out.println(GENERAL_HELP_MESSAGE);
    }
  }

  private static void handleQuit(Scanner sc){
    sc.close();
    QUIT = true;
  }

  private static String toIndexString(List<Integer> indices){
    Collections.sort(indices);
    StringBuilder strbuilder = new StringBuilder("[");
    for (Integer i : indices){
      strbuilder.append(i);
      strbuilder.append(",");
    }
    strbuilder.deleteCharAt(strbuilder.toString().length() - 1);
    strbuilder.append("]");
    return strbuilder.toString();
  }

  private static void setMode(int mode){
    CURRENT_MODE = mode;
    if (mode == BUILD_MODE){
      PROMPT = "build> ";
    }else {
      PROMPT = "check> ";
    }
  }
  }
