import java.util.function.Predicate;

abstract class ANode<T> {
  ANode<T> prev;
  ANode<T> next;
  
  public int size() {
    return 1 + next.size();
  }
  
  public void remove() {
    this.prev.setNext(this.next);
    this.next.setPrev(this.prev);
  }

  public void setPrev(ANode<T> node) {
    this.prev = node;
  }

  public void setNext(ANode<T> node) {
    this.next = node;
  }

  abstract ANode<T> find(Predicate<T> p);
}

class Node<T> extends ANode<T> {
  T data;

  public Node(T data) {
    this.data = data;
  }

  public Node(ANode<T> prev, T data, ANode<T> next) {
    if (prev == null || next == null) {
      throw new IllegalArgumentException("Node<T> Cannot accept null " + "nodes in constructor");
    }

    this.next = next;
    this.next.setPrev(this);

    this.prev = prev;
    this.prev.setNext(this);
  }

  public ANode<T> find(Predicate<T> p) {
    if (p.test(data)) {
      return this;
    }
    return next.find(p);
  }

}

class Sentinel<T> extends ANode<T> {
  public Sentinel() {
    next = this;
    prev = this;
  }

  @Override
  public int size() {
    return 0;
  }

  public int getSize() {
    return next.size();
  }
  
  @Override
  public void remove() {
    throw new RuntimeException("Attempting to remove from empty deque");
  }
  
  public void removeFromHead() {
    this.next.remove();
  }
  
  public void removeFromTail() {
    this.prev.remove();
  }

  public ANode<T> findHelp(Predicate<T> p) {
    return next.find(p);
  }
  
  @Override
  public ANode<T> find(Predicate<T> p) {
    return this;
  }
}

class Deque<T> {
  Sentinel<T> header;

  public Deque() {
    this.header = new Sentinel<T>();
  }

  public Deque(Sentinel<T> header) {
    this.header = header;
  }
  
  public int size()
  {
    return this.header.getSize();
  }
  
  public void addAtHead(T data)
  {
    new Node<T>(this.header, data, this.header.next);
  }
  
  public void addAtTail(T data)
  {
    new Node<T>(this.header.prev, data, this.header);
  }
  
  public void removeFromHead()
  {
    this.header.removeFromHead();
  }
  
  public void removeFromTail()
  {
    this.header.removeFromTail();
  }

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