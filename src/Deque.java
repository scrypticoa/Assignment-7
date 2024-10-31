import tester.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

// represents the connection between nodes and the sentinal of a Deque
abstract class ANode<T> {
  ANode<T> prev;
  ANode<T> next;
  
  // returns the amount of node between this node and the
  // sentinel, including this node but excluding the sentinel
  public int size() {
    return 1 + next.size();
  }
  
  // removes this element from the deque, and stitches in the hole
  public void remove() {
    this.prev.setNext(this.next);
    this.next.setPrev(this.prev);
  }

  // sets the previous node of a given ANode
  public void setPrev(ANode<T> node) {
    this.prev = node;
  }

  // sets the next node of a given ANode
  public void setNext(ANode<T> node) {
    this.next = node;
  }

  // finds the first node from this node to the sentinel which satisfies p,
  // if no valid node is found, returns the sentinel
  abstract ANode<T> find(Predicate<T> p);
  
  // helper method for testing which flattens this deque into an ArrayList
  public abstract ArrayList<T> doFlatten(ArrayList<T> acc);
}

// represents a node in a deque that hold the data of type <T>
class Node<T> extends ANode<T> {
  T data;

  public Node(T data) {
    this.data = data;
  }

  // convenience constructor which stitches this node between two given nodes
  public Node(ANode<T> prev, T data, ANode<T> next) {
    if (prev == null || next == null) {
      throw new IllegalArgumentException("Node<T> Cannot accept null nodes in constructor");
    }

    this.data = data;
    
    this.next = next;
    this.next.setPrev(this);

    this.prev = prev;
    this.prev.setNext(this);
  }

  // returns this node if it satisfies p, otherwise keeps checking nodes
  public ANode<T> find(Predicate<T> p) {
    if (p.test(this.data)) {
      return this;
    }
    return next.find(p);
  }

  // helper method for testing which flattens this deque into an ArrayList
  public ArrayList<T> doFlatten(ArrayList<T> acc) {
    acc.add(data);
    return this.next.doFlatten(acc);
  }
}

// represents a sentinal/header to the ANode Deque
class Sentinel<T> extends ANode<T> {
  public Sentinel() {
    next = this;
    prev = this;
  }

  // the sentinel doesn't count in the deque's size, so returns 0, breaking the recursion
  @Override
  public int size() {
    return 0;
  }

  // gets the number of other elements linked to this sentinel
  public int getSize() {
    return next.size();
  }
  
  // attempt to remove from an empty deque, throws an error
  @Override
  public void remove() {
    throw new RuntimeException("Attempting to remove from empty deque");
  }
  
  // removes the head of the deque, if empty, throws an error
  public void removeFromHead() {
    this.next.remove();
  }
  
  // removes the tail of the deque, if empty, throws an error
  public void removeFromTail() {
    this.prev.remove();
  }

  // finds and returns the first node in this deque which satisfies p
  // if no such node exists, returns this
  public ANode<T> findHelp(Predicate<T> p) {
    return next.find(p);
  }
  
  // no satisfactory node was found, therefore returns this
  @Override
  public ANode<T> find(Predicate<T> p) {
    return this;
  }
  
  // helper method for testing which flattens this deque into an ArrayList
  public ArrayList<T> flatten()
  {
    return this.next.doFlatten(new ArrayList<T>());
  }
  
  // end of flatten process
  public ArrayList<T> doFlatten(ArrayList<T> acc)
  {
    return acc;
  }
}

// represents a deque with ANodes of type <T>
class Deque<T> {
  Sentinel<T> header;

  // constructor
  public Deque() {
    this.header = new Sentinel<T>();
  }

  // constructor
  public Deque(Sentinel<T> header) {
    this.header = header;
  }
  
  // returns the number of nodes in this deque, excluding the sentinel
  public int size()
  {
    return this.header.getSize();
  }
  
  // adds a node at the head of this deque
  public void addAtHead(T data)
  {
    
    this.header.next = new Node<T>(this.header, data, this.header.next);
  }
  
  // adds a node at the tail of this deque
  public void addAtTail(T data)
  {
    new Node<T>(this.header.prev, data, this.header);
  }
  
  // removes the head node of this deque, if
  // no head exists, throws an error
  public void removeFromHead()
  {
    this.header.removeFromHead();
  }
  
  // removes the tail node of this deque, if
  // no tail exists, throws an error
  public void removeFromTail()
  {
    this.header.removeFromTail();
  }
  
  // returns the first node in this deque which satisfies p,
  // if no such node exists, returns the header
  public ANode<T> find(Predicate<T> p) {
    return header.findHelp(p);
  }
  
  // helper method for testing which flattens this deque into an ArrayList
  public ArrayList<T> flatten() {
    return header.flatten();
  }
}

class ExamplesDeque {
  Deque<String> deque1 = new Deque<String>();

  Sentinel<String> sentinel2 = new Sentinel<String>();
  Node<String> node02 = new Node<String>(sentinel2, "abc", sentinel2);
  Node<String> node12 = new Node<String>(node02, "bcd", sentinel2);
  Node<String> node22 = new Node<String>(node12, "cde", sentinel2);
  Node<String> node32 = new Node<String>(node22, "def", sentinel2);
  Deque<String> deque2 = new Deque<String>(sentinel2);

  Sentinel<String> sentinel3 = new Sentinel<String>();
  Node<String> node03 = new Node<String>(sentinel3, "z", sentinel3);
  Node<String> node13 = new Node<String>(node03, "yo", sentinel3);
  Node<String> node23 = new Node<String>(node13, "Route", sentinel3);
  Node<String> node33 = new Node<String>(node23, "apple", sentinel3);
  Deque<String> deque3 = new Deque<String>(sentinel3);

  Sentinel<Boolean> boolSent = new Sentinel<Boolean>();
  Node<Boolean> singleBool = new Node<Boolean>(boolSent, true, boolSent);
  Deque<Boolean> singleDeque = new Deque<Boolean>(boolSent);
  
  static <T> ArrayList<T> gen(T... elements)
  {
    return new ArrayList<T>(Arrays.asList(elements));
  }
  
  boolean testNodeConstructor(Tester t) {
    boolean res = true;
    
    // proper convenience assignment
    res &= t.checkExpect(node32.prev, node22);
    res &= t.checkExpect(node22.next, node32);
    res &= t.checkExpect(node33.next, sentinel3);
    res &= t.checkExpect(sentinel2.prev, node32);
    
    // null inputs
    res &= t.checkConstructorException(
        new IllegalArgumentException("Node<T> Cannot accept null nodes in constructor"), 
        "Node", 
        null, 5, node02);
    
    return res;
  }
  
  boolean testSize(Tester t) {
    boolean res = true;
    
    // call on deque
    
    // empty case
    res &= t.checkExpect(deque1.size(), 0);
    
    // single case
    res &= t.checkExpect(singleDeque.size(), 1);
    
    // multiple case
    res &= t.checkExpect(deque3.size(), 4);
    
    // call on sentinel
    
    // empty case
    res &= t.checkExpect(deque1.header.getSize(), 0);
    res &= t.checkExpect(deque1.header.size(), 0);
    
    // single case
    res &= t.checkExpect(boolSent.getSize(), 1);
    res &= t.checkExpect(boolSent.size(), 0);
    
    // multiple case
    res &= t.checkExpect(sentinel3.getSize(), 4);
    res &= t.checkExpect(sentinel3.size(), 0);
    
    // call on node
    
    // single case
    res &= t.checkExpect(singleBool.size(), 1);
    
    // multiple case, halfway through
    res &= t.checkExpect(node23.size(), 2);
    
    return res;
  }
  
  boolean testAddAtHead(Tester t)
  {
    boolean res = true;
    
    Deque<String> baseDeque = new Deque<String>();
    
    // call on deque
    
    // first add
    
    Sentinel<String> stringSent = new Sentinel<String>();
    Node<String> singleString = new Node<String>(stringSent, "1", stringSent);
    Deque<String> singleStringDeque = new Deque<String>(stringSent);
    
    baseDeque.addAtHead("1");
    
    t.checkExpect(baseDeque.flatten(), gen("1"));
    
    // second add (differentiate between addAtHead and addAtTail)
    
    baseDeque.addAtHead("0");
    
    t.checkExpect(baseDeque.flatten(), gen("0", "1"));
    
    return res;
  }
  
  boolean testAddAtTail(Tester t)
  {
    boolean res = true;
    
    Deque<String> baseDeque = new Deque<String>();
    
    // call on deque
    
    // first add
    
    Sentinel<String> stringSent = new Sentinel<String>();
    Node<String> singleString = new Node<String>(stringSent, "1", stringSent);
    Deque<String> singleStringDeque = new Deque<String>(stringSent);
    
    baseDeque.addAtTail("1");
    
    t.checkExpect(baseDeque.flatten(), gen("1"));
    
    // second add (differentiate between addAtHead and addAtTail)
    
    baseDeque.addAtTail("0");
    
    t.checkExpect(baseDeque.flatten(), gen("1", "0"));
    
    return res;
  }
  
  boolean testRemoveFromHead(Tester t)
  {
    // calls on deque
    
    boolean res = true;
    
    Deque<Integer> intDeque = new Deque<Integer>();
    intDeque.addAtTail(1);
    intDeque.addAtTail(2);
    intDeque.addAtTail(3);
    intDeque.addAtTail(4);
    
    // generic case
    
    intDeque.removeFromHead();
    
    res &= t.checkExpect(intDeque.flatten(), gen(2, 3, 4));
    
    // last element case
    
    intDeque.removeFromHead();
    intDeque.removeFromHead();
    intDeque.removeFromHead();
    
    res &= t.checkExpect(intDeque, new Deque<Integer>());
    
    // empty error
    
    res &= t.checkException(
        new RuntimeException("Attempting to remove from empty deque"),
        intDeque,
        "removeFromHead");
    
    // calls on header
    
    intDeque = new Deque<Integer>();
    intDeque.addAtTail(1);
    intDeque.addAtTail(2);
    intDeque.addAtTail(3);
    intDeque.addAtTail(4);
    
    // generic case
    
    intDeque.header.removeFromHead();
    
    res &= t.checkExpect(intDeque.flatten(), gen(2, 3, 4));
    
    // last element case
    
    intDeque.header.removeFromHead();
    intDeque.header.removeFromHead();
    intDeque.header.removeFromHead();
    
    res &= t.checkExpect(intDeque, new Deque<Integer>());
    
    // empty error
    
    res &= t.checkException(
        new RuntimeException("Attempting to remove from empty deque"),
        intDeque.header,
        "removeFromHead");
    
    // calls on Node
    
    intDeque = new Deque<Integer>();
    intDeque.addAtTail(1);
    intDeque.addAtTail(2);
    intDeque.addAtTail(3);
    intDeque.addAtTail(4);
    
    // generic case
    
    intDeque.header.next.remove();
    
    res &= t.checkExpect(intDeque.flatten(), gen(2, 3, 4));
    
    // last element case
    
    intDeque.header.next.remove();
    intDeque.header.next.remove();
    intDeque.header.next.remove();
    
    res &= t.checkExpect(intDeque, new Deque<Integer>());
    
    // empty error
    
    res &= t.checkException(
        new RuntimeException("Attempting to remove from empty deque"),
        intDeque.header.next,
        "remove");
    
    return res;
  }
  
  boolean testRemoveFromTail(Tester t)
  {
    boolean res = true;
    
    Deque<Integer> intDeque = new Deque<Integer>();
    intDeque.addAtTail(1);
    intDeque.addAtTail(2);
    intDeque.addAtTail(3);
    intDeque.addAtTail(4);
    
    // generic case
    
    intDeque.removeFromTail();
    
    res &= t.checkExpect(intDeque.flatten(), gen(1, 2, 3));
    
    // last element case
    
    intDeque.removeFromTail();
    intDeque.removeFromTail();
    intDeque.removeFromTail();
    
    res &= t.checkExpect(intDeque, new Deque<Integer>());
    
    // empty error
    
    res &= t.checkException(
        new RuntimeException("Attempting to remove from empty deque"),
        intDeque,
        "removeFromTail");
    
    intDeque = new Deque<Integer>();
    intDeque.addAtTail(1);
    intDeque.addAtTail(2);
    intDeque.addAtTail(3);
    intDeque.addAtTail(4);
    
    // calls on header
    
    // generic case
    
    intDeque.header.removeFromTail();
    
    res &= t.checkExpect(intDeque.flatten(), gen(1, 2, 3));
    
    // last element case
    
    intDeque.header.removeFromTail();
    intDeque.header.removeFromTail();
    intDeque.header.removeFromTail();
    
    res &= t.checkExpect(intDeque, new Deque<Integer>());
    
    // empty error
    
    res &= t.checkException(
        new RuntimeException("Attempting to remove from empty deque"),
        intDeque.header,
        "removeFromTail");
    
    // calls on Node
    
    intDeque = new Deque<Integer>();
    intDeque.addAtTail(1);
    intDeque.addAtTail(2);
    intDeque.addAtTail(3);
    intDeque.addAtTail(4);
    
    // generic case
    
    intDeque.header.prev.remove();
    
    res &= t.checkExpect(intDeque.flatten(), gen(1, 2, 3));
    
    // last element case
    
    intDeque.header.prev.remove();
    intDeque.header.prev.remove();
    intDeque.header.prev.remove();
    
    res &= t.checkExpect(intDeque, new Deque<Integer>());
    
    // empty error
    
    res &= t.checkException(
        new RuntimeException("Attempting to remove from empty deque"),
        intDeque.header.prev,
        "remove");
    
    return res;
  }
  
  boolean testFind(Tester t)
  {
    boolean res = true;
    
    // calls on deque
    
    // generic exists
    
    res &= t.checkExpect(deque2.find((s) -> s == "cde"), node22);
    res &= t.checkExpect(deque2.find((s) -> s.length() == 3), node02);
    
    // generic fails
    
    res &= t.checkExpect(deque2.find((s) -> s == "efg"), sentinel2);
    res &= t.checkExpect(deque2.find((s) -> s.length() == 4), sentinel2);
    
    // empty deque
    
    res &= t.checkExpect(deque1.find((s) -> s.isBlank()), deque1.header);
    
    // calls on header
    
    // generic exists
    
    res &= t.checkExpect(deque2.header.findHelp((s) -> s == "cde"), node22);
    res &= t.checkExpect(deque2.header.findHelp((s) -> s.length() == 3), node02);
    
    // generic fails
    
    res &= t.checkExpect(deque2.header.findHelp((s) -> s == "efg"), sentinel2);
    res &= t.checkExpect(deque2.header.findHelp((s) -> s.length() == 4), sentinel2);
    
    // empty deque
    
    res &= t.checkExpect(deque1.header.findHelp((s) -> s.isBlank()), deque1.header);
    
    // calls on node
    
    // generic exists
    
    res &= t.checkExpect(deque2.header.next.find((s) -> s == "cde"), node22);
    res &= t.checkExpect(deque2.header.next.find((s) -> s.length() == 3), node02);
    
    // generic fails
    
    res &= t.checkExpect(deque2.header.next.find((s) -> s == "efg"), sentinel2);
    res &= t.checkExpect(deque2.header.next.find((s) -> s.length() == 4), sentinel2);
    
    // empty deque
    
    res &= t.checkExpect(deque1.header.next.find((s) -> s.isBlank()), deque1.header);
    
    return res;
  }
}