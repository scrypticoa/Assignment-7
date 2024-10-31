import java.util.function.Predicate;

// represents the connection between nodes and the sentinal of a Deque
abstract class ANode<T> {
  ANode<T> prev;
  ANode<T> next;

  // changes what the the next ANode this ANode is conected too
  public void setPrev(ANode<T> node) {
    this.prev = node;
  }

  //changes what the the previous ANode this ANode is conected too
  public void setNext(ANode<T> node) {
    this.next = node;
  }

  // counts the amount of ANodes in the deque
  public int size() {
    return 1 + next.size();
  }

  // a method to find the first node to pass the predicate
  abstract ANode<T> find(Predicate<T> p);
}

// represents a node in a deque that hold the data of type <T>
class Node<T> extends ANode<T> {
  T data;

  public Node(T data) {
    this.data = data;
  }

  // constructor
  public Node(ANode<T> prev, T data, ANode<T> next) {
    if (prev == null || next == null) {
      throw new IllegalArgumentException("Node<T> Cannot accept null " + "nodes in constructor");
    }

    this.next = next;
    this.next.setPrev(this);

    this.prev = prev;
    this.prev.setNext(this);
  }

  // goes through the nodes to find the first to pass the predicate
  public ANode<T> find(Predicate<T> p) {
    if (p.test(data)) {
      return this;
    }
    return next.find(p);
  }

}

// represents a sentinal/header to the ANode Deque
class Sentinel<T> extends ANode<T> {
  public Sentinel() {
    next = this;
    prev = this;
  }

  //ends the count aroun the deque
  public int size() {
    return 0;
  }

  //starts the count around the deque
  public int getSize() {
    return next.size();
  }

  //starts the find on the first node
  public ANode<T> findHelp(Predicate<T> p) {
    return next.find(p);
  }

  //ends the find method when no passing predicate is found
  public ANode<T> find(Predicate<T> p) {
    return this;
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

  //counts the number of nodes in a list Deque, not including the header node
  public int size() {
    return header.getSize();
  }

  //consumes a value of type T and inserts it at the front of the list
  public void addAtHead(T data) {
    new Node<T>(header, data, header.next);
  }

  //consumes a value of type T and inserts it at the tail of this list
  public void addAtTail(T data) {
    new Node<T>(header.prev, data, header);
  }

  public void removeFromHead() {

  }

  // finds the first node that returns true for p
  public ANode<T> find(Predicate<T> p) {
    return header.findHelp(p);
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
}