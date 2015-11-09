package IR;

public class TempVar extends Var {
    public static int num = 0;
    public int id;

    public TempVar(){
        id = num++;
    }

    public String toString(){
        return "$t" + id;
    }
}
