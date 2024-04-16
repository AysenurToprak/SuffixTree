import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class Node {
    char value;
    Map<Character, Node> children;

    public Node(char value) {
        this.value = value;
        this.children = new HashMap<>();
    }
}

class SuffixTree {
    Node root;

    public SuffixTree() {
        this.root = new Node('\0');
    }

    public void addSuffix(String suffix) {
        Node node = this.root;
        for (char c : suffix.toCharArray()) {
            if (!node.children.containsKey(c)) {
                node.children.put(c, new Node(c));
            }
            node = node.children.get(c);
        }
    }

    public void buildSuffixTree(String s) {
        for (int i = 0; i < s.length(); i++) {
            addSuffix(s.substring(i));
        }
    }

    public boolean hasSubstring(String p) {
        Node node = this.root;
        for (char c : p.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return false;
            }
            node = node.children.get(c);
        }
        return true;
    }

    public int findSubstringPosition(String p) {
        Node node = this.root;
        for (char c : p.toCharArray()) {
            if (!node.children.containsKey(c)) {
                return -1;
            }
            node = node.children.get(c);
        }
        return node.value == p.charAt(0) ? 0 : findSubstringPositionHelper(node, p);
    }

    private int findSubstringPositionHelper(Node node, String p) {
        if (node == null) {
            return -1;
        }

        if (node.value == p.charAt(0)) {
            return 0;
        }

        int position = 0;
        for (Node child : node.children.values()) {
            position++;
            int result = findSubstringPositionHelper(child, p);
            if (result != -1) {
                return position + result;
            }
        }

        return position;
    }



    public String findLongestRepeatedSubstring() {
        return findLongestRepeatedSubstringHelper(root, "");
    }

    private String findLongestRepeatedSubstringHelper(Node node, String currentSubstring) {
        if (node.children.isEmpty()) {
            return currentSubstring;
        }
        String longestRepeatedSubstring = "";
        for (Node child : node.children.values()) {
            String childSubstring = findLongestRepeatedSubstringHelper(child, currentSubstring + child.value);
            if (childSubstring.length() > longestRepeatedSubstring.length()) {
                longestRepeatedSubstring = childSubstring;
            }
        }
        return longestRepeatedSubstring;
    }



    public String findMostRepeatedSubstring() {
        return findMostRepeatedSubstringHelper(root, "");
    }

    private String findMostRepeatedSubstringHelper(Node node, String currentSubstring) {
        if (node.children.isEmpty()) {
            return currentSubstring;
        }

        String mostRepeatedSubstring = currentSubstring; // mostRepeatedSubstring'i başlangıçta currentSubstring ile eşleştir
        for (Node child : node.children.values()) {
            String childSubstring = findMostRepeatedSubstringHelper(child, currentSubstring + child.value);
            if (childSubstring.length() > mostRepeatedSubstring.length()) {
                mostRepeatedSubstring = childSubstring;
            }
        }

        int count = countOccurrences(root, mostRepeatedSubstring);
        return count > 1 ? mostRepeatedSubstring : "";
    }

    private int countOccurrences(Node node, String substring) {
        int count = 0;

        if (node.children.isEmpty()) {
            return 0;
        }

        if (substring.isEmpty()) {
            return 1;
        }

        char firstChar = substring.charAt(0);
        if (node.children.containsKey(firstChar)) {
            Node child = node.children.get(firstChar);
            count += countOccurrences(child, substring.substring(1));
        }

        // Alt dizeyi içeren düğümleri say
        for (Node child : node.children.values()) {
            if (substring.startsWith(Character.toString(child.value))) {
                count += countOccurrences(child, substring.substring(1));
            }
        }

        return count;
    }




}


public class Main {
    public static void main(String[] args) {
        String fileName = "input.txt"; // Dosya adı
        String word = readWordFromFile(fileName); // Dosyadan kelimeyi oku
        if (word != null) {
            System.out.println("Word read from file: " + word);
            SuffixTree suffixTree = new SuffixTree();
            suffixTree.buildSuffixTree(word);
            System.out.println("Suffix Tree built.");

            String p = "li";
            System.out.println("Does the string contain substring '" + p + "'? " + suffixTree.hasSubstring(p));
            System.out.println("Position of substring '" + p + "': " + suffixTree.findSubstringPosition(p));

            System.out.println("Longest repeated substring: " + suffixTree.findLongestRepeatedSubstring());
            System.out.println("Most repeated substring: " + suffixTree.findMostRepeatedSubstring());
        } else {
            System.out.println("Failed to read word from file.");
        }
    }

    public static String readWordFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            return reader.readLine(); // Dosyanın ilk satırını oku (bir kelime bekleniyor)
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
