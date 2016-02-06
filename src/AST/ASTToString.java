package AST;


public class ASTToString {

    public static String getTreeString(Node n, Boolean AsSEXP){
        if (AsSEXP){
            return getTreeStringSExp(n);
        }
        else{
            return getTreeStringReadable(n);
        }
    }

    private static String getTreeStringReadable(Node n){
        return getTreeStringReadableHelper(n, 0);
    }

    private static String c_spaces(int c){
        String out = "";
        for (int i = 0; i < c; i++){
            out += " ";
        }
        return out;
    }

    private static String getTreeStringReadableHelper(Node n, int level){
        if (n == null){
            int x = 1;
        }
        String out =  n.type();
        for (String attr : n.attr()){
            out += " " + attr;
        }
        level += 4;
        for (Node child : n.children()){
            out += "\n" + c_spaces(level) + getTreeStringReadableHelper(child, level);
        }
        return out;
    }


    private static String getTreeStringSExp(Node n){
        String out =  "(" + n.type();
        for (String attr : n.attr()){
            out += " " + attr;
        }
        for (Node child : n.children()){
            out += " " + getTreeStringSExp(child);
        }
        return out + ")";
    }


}
