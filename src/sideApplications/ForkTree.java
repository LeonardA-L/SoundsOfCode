/*
##############################################################
## 	                Sounds Of Code	    	    	    ##
##	        A GitHub Data Challenge III Entry	    ##
##			  LeonardA-L			    ##
##############################################################
*/
import java.util.ArrayList;
import java.util.Scanner;

public class ForkTree {
    public static class Node {
        public String id;
        public ArrayList<Node> children = new ArrayList<Node>();

        public void addNode(Node n) {
            children.add(n);
        }
    }

    Node root;
    int depth;

    public ForkTree() {

    }

    public Node searchFor(String forked, Node start) {
        Node n = null;
        if (start.id.equals(forked)) {
            n = start;
        } else {
            for (int i = 0; i < start.children.size(); i++) {
                Node o = searchFor(forked, start.children.get(i));
                if (o != null) {
                    n = o;
                    break;
                }
            }
        }
        return n;
    }

    public void addFork(String forker, String forked) {
        if (root == null) {
            root = new Node();
            root.id = forked;
            Node n = new Node();
            n.id = forker;
            root.addNode(n);
        } else {
            Node n = searchFor(forked, root);
            Node o = new Node();
            o.id = forker;
            n.addNode(o);
        }
    }

    public void display(Node n) {
        //System.out.println(n.id);
        System.out.println(depth + "-" +n.id);
        for (int i = 0; i < n.children.size(); i++) {
            depth++;
            display(n.children.get(i));
            System.out.println(depth + "-" +n.id);
        }
        depth--;
    }
    
    public void addDepth(Node n, ArrayList<Integer> list) {
        list.add(depth);
        for (int i = 0; i < n.children.size(); i++) {
            depth++;
            addDepth(n.children.get(i), list);
            list.add(depth);
        }
        depth--;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ForkTree ft = new ForkTree();
        while (in.hasNextLine()) {
            String s = in.nextLine();
            if (!(s.equals("") || s.equals("}") || s.equals("digraph{"))) {
                String[] ss = s.split(" -> ");
                ft.addFork(ss[1], ss[0]);
            }
        }
        ArrayList<Integer> depths = new ArrayList<Integer>();
        ft.depth = 0;
        ft.addDepth(ft.root,depths);
        
        System.out.println("TreeDepth");
        for(int i=0;i<depths.size();i++){
            System.out.println(depths.get(i));
        }
    }
}
