import java.util.*;
import tester.Tester;

// A class that defines a new permutation code, as well as methods for encoding
// and decoding of the messages that use this code.
class PermutationCode {
  // The original list of characters to be encoded
  ArrayList<Character> alphabet = new ArrayList<Character>(
      Arrays.asList('a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
          'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'));

  ArrayList<Character> code = new ArrayList<Character>(26);

  // A random number generator
  Random rand = new Random();

  // Create a new instance of the encoder/decoder with a new permutation code
  PermutationCode() {
    this.code = this.initEncoder();
  }

  // Create a new instance of the encoder/decoder with the given code
  PermutationCode(ArrayList<Character> code) {
    this.code = code;
  }

  // Initialize the encoding permutation of the characters
  ArrayList<Character> initEncoder() {
    ArrayList<Character> letters = (ArrayList<Character>) alphabet.clone();
    ArrayList<Character> newCode = new ArrayList<Character>();
    int r;
    for (int i = 26; i > 0; i--) {
      r = rand.nextInt(i);
      newCode.add(letters.get(r));
      letters.remove(r);
    }
    return newCode;
  }

  // produce an encoded String from the given String
  String encode(String source) {
    if (source.isEmpty()) {
      return "";
    }

    if (source.length() > 1) {
      Character c = source.charAt(0);
      if (c.equals(' ')) {
        return " " + encode(source.substring(1));
      }
      else {
        int pos = alphabet.indexOf(source.charAt(0));
        return code.get(pos) + encode(source.substring(1));
      }
    }
    else {
      if (source.equals(" ")) {
        return " ";
      }
      else {
        int pos = alphabet.indexOf(source.charAt(0));
        return Character.toString(code.get(pos));
      }
    }
  }

  // produce a decoded String from the given String
  String decode(String code) {
    if (code.isEmpty()) {
      return "";
    }

    if (code.length() > 1) {
      Character c = code.charAt(0);
      if (c.equals(' ')) {
        return " " + decode(code.substring(1));
      }
      else {
        int pos = this.code.indexOf(code.charAt(0));
        return alphabet.get(pos) + decode(code.substring(1));
      }
    }
    else {
      if (code.equals(" ")) {
        return " ";
      }
      else {
        int pos = this.code.indexOf(code.charAt(0));
        return Character.toString(alphabet.get(pos));
      }
    }

  }
}

class ExamplesPermutationCode {

  ArrayList<Character> shift = new ArrayList<Character>(
      Arrays.asList('b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
          'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'a'));

  PermutationCode shifted = new PermutationCode(shift);

  String mt = "";
  String abc = "abc";
  String space = "hello world";

  // tests initEncoder() by checking that all letters are in p
  boolean testinitEncoder(Tester t) {
    ArrayList<Character> newCode = shifted.initEncoder();
    boolean res = true;
    res &= newCode.contains('a');
    res &= newCode.contains('b');
    res &= newCode.contains('c');
    res &= newCode.contains('d');
    res &= newCode.contains('e');
    res &= newCode.contains('f');
    res &= newCode.contains('g');
    res &= newCode.contains('h');
    res &= newCode.contains('i');
    res &= newCode.contains('j');
    res &= newCode.contains('k');
    res &= newCode.contains('l');
    res &= newCode.contains('m');
    res &= newCode.contains('n');
    res &= newCode.contains('o');
    res &= newCode.contains('p');
    res &= newCode.contains('q');
    res &= newCode.contains('r');
    res &= newCode.contains('s');
    res &= newCode.contains('t');
    res &= newCode.contains('u');
    res &= newCode.contains('v');
    res &= newCode.contains('w');
    res &= newCode.contains('x');
    res &= newCode.contains('y');
    res &= newCode.contains('z');
    return t.checkExpect(res, true);
  }

  // tests the encode method
  boolean testEncode(Tester t) {
    boolean res = true;
    // tests an emptyString
    res &= t.checkExpect(shifted.encode(mt), "");
    // tests a simple string
    res &= t.checkExpect(shifted.encode(abc), "bcd");
    // tests a string with space
    res &= t.checkExpect(shifted.encode(space), "ifmmp xpsme");
    return res;
  }

  // tests the decode method
  boolean testDecode(Tester t) {
    boolean res = true;
    // tests an emptyString
    res &= t.checkExpect(shifted.decode(mt), "");
    // tests a simple string
    res &= t.checkExpect(shifted.decode("bcd"), abc);
    // tests a string with space
    res &= t.checkExpect(shifted.decode("ifmmp xpsme"), space);
    return res;
  }
}
