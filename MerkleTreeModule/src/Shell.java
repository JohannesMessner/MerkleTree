import MerkleTree.MerkleTreeBuilder;
import MerkleTree.MutableMerkleTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
  private static final String TOO_MANY_ARGUMENTS_ERROR = "Error! You specified too many arguments";
  private static final String READY_MESSAGE = "READY!";
  private static final String CHEK_PASSED_MESSAGE = "ACK";
  private static final String CHEK_NOT_PASSED_MESSAGE = "REJ";
  private static final String CHECK_HELP_MESSAGE = "You are currently in check-mode.\n" +
          "You cane use the following commands:\n\n" +
          "'NEW_CHECK n h': creates a new tree with n leaves and a root-hash of h\n" +
          "'SET_VAL p v': sets a value v for a leaf at position p\n" +
          "'SET_HASH p h': sets a hash h for a node at index p\n" +
          "'READY?': tells you if the tree is ready for a consistency-check, or which nodes are still missing\n" +
          "'CHECK': checks the tree for consistency\n" +
          "'CLEAR:' deletes all values and hashes\n" +
          "'DEBUG': prints a string-representation of the tree\n" +
          "'HELP': prints this message\n" +
          "'QUIT': quits the program";
  private static final String BUILD_HELP_MESSAGE = "You are currently in build-mode.\n" +
          "You cane use the following commands:\n\n" +
          "'NEW: n ': creates a new tree with n leaves\n" +
          "'PUSH v': adds a value v to the tree\n" +
          "'CLEAR:' deletes all values and hashes\n" +
          "'DEBUG': prints a string-representation of the tree\n" +
          "'HELP': prints this message\n" +
          "'QUIT': quits the program";
  private static final String GENERAL_HELP_MESSAGE = "You are currently in merkle-mode.\n" +
          "This application allows you to build merkle-trees or to check trees for their consistency.\n\n" +
          "You can use the following command:\n\n" +
          "'NEW: n ': launches build-mode with a new tree with n leaves\n" +
          "'NEW_CHECK n h': launches check-mode with a new tree with n leaves and a root-hash of h\n";

  /**
   * Takes user-input and calls the corresponding tree-commands.
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

  /**
   * Transforms the input into actual methods to be called.
   *
   * @param inputLine String representing the user input
   */
  private static void handleCommand(String inputLine){
    Scanner sc = new Scanner(inputLine);
    sc.useDelimiter("\\s+");

    String command = "";
    if (sc.hasNext()) {
      command = sc.next();
    }

    try{
    switch (command) {
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
        handleReady(sc);
        break;


      case "CHECK":
      case "check":
        handleCheck(sc);
        break;

      case "CLEAR":
      case "clear":
        handleClear(sc);
        break;

      case "DEBUG":
      case "debug":
        handleDebug(sc);
        break;

      case "HELP":
      case "help":
        handleHelp(sc);
        break;

      case "QUIT":
      case "quit":
        handleQuit(sc);
        break;

      default:
        System.out.println(INVALID_COMMAND_ERROR);

      }
    } catch (IllegalStateException e){
      System.out.println("Error! " + e.toString());
    }
    sc.close();
  }

  private static void handleNew(Scanner sc){

    int treeSize = 0;
    if (sc.hasNextInt()){
      treeSize = sc.nextInt();
    }else{
      System.out.println(NO_SIZE_ERROR);
      return;
    }
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }

    try {
      builder = new MerkleTreeBuilder<Body>(treeSize);
      setMode(BUILD_MODE);
    } catch (IllegalArgumentException e){
      System.out.println(NO_VALID_SIZE_ERROR);
    }
  }

  private static void handlePush(Scanner sc){
    if (CURRENT_MODE != BUILD_MODE){
      throw new IllegalStateException(WRONG_MODE_ERROR);
    }

    Body bdy = null;
    if(sc.hasNext()){
      bdy = getBody(sc.next());
    }else {
      System.out.println(NO_VALUE_ERROR);
    }
    if(sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }

    if (bdy != null) {
      builder.push(bdy);
    }else{
      System.out.println(NO_VALID_VALUE_ERROR);
    }
  }

  /**
   * Returns a Body-object for a given String.
   *
   * @param value String-representation of a body
   * @return Body-object, null if String not syntactically correct
   */
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
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }

    setMode(CHECK_MODE);
    tree = new MutableMerkleTree<Body>(treeSize);
    ROOT_HASH = hash;
    tree.setHash(0, hash);
  }

  private static void handleSetVal(Scanner sc){
    if (CURRENT_MODE != CHECK_MODE){
      throw new IllegalStateException(WRONG_MODE_ERROR);
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
      throw new IllegalStateException(WRONG_MODE_ERROR);
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
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }

    try{
      tree.setHash(position, hash);
      ROOT_HASH = hash;
    }catch (IndexOutOfBoundsException e){
      System.out.println(NO_VALID_POSITION_ERROR);
    }
  }

  private static void handleReady(Scanner sc){
    if (CURRENT_MODE != CHECK_MODE){
      throw new IllegalStateException(WRONG_MODE_ERROR);
    }
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }

    List<Integer> missingNodes = tree.getMissing();
    if (missingNodes.isEmpty()){
      System.out.println(READY_MESSAGE);
    }else {
      System.out.println(toIndexString(missingNodes));
    }
  }

  private static void handleCheck(Scanner sc){
    if (CURRENT_MODE != CHECK_MODE){
      throw new IllegalStateException(WRONG_MODE_ERROR);
    }
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
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

  private static void handleDebug(Scanner sc){
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }
    if (CURRENT_MODE == CHECK_MODE){
      System.out.println(tree.toString());
    }else if (CURRENT_MODE == BUILD_MODE){
      System.out.println(builder.toString());
    }else{
      System.out.println(WRONG_MODE_ERROR);
    }
  }

  private static void handleClear(Scanner sc){
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }
    if (CURRENT_MODE == CHECK_MODE){
      tree.clear();
      tree.setHash(0, ROOT_HASH);
    }else if (CURRENT_MODE == BUILD_MODE){
      builder.clear();
    }else {
      throw new IllegalStateException(WRONG_MODE_ERROR);
    }
  }

  private static void handleHelp(Scanner sc){
    if (sc.hasNext()){
      System.out.println(TOO_MANY_ARGUMENTS_ERROR);
      return;
    }
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

  /**
   * Deletes all white-spaces from the String-representation of a List.
   *
   * @param indices List to be transformed
   * @return String-representation without white-spaces
   */
  private static String toIndexString(List<Integer> indices){
    Collections.sort(indices);
    return indices.toString().replaceAll(" ", "");

  }

  /**
   * Switches between the different shell-modes.
   *
   * @param mode to be switched to
   */
  private static void setMode(int mode){
    CURRENT_MODE = mode;
    if (mode == BUILD_MODE){
      PROMPT = "build> ";
    }else {
      PROMPT = "check> ";
    }
  }
  }
