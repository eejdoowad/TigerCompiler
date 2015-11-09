package IR;

public class IntImmediate extends Immediate {

    public int val;

    public IntImmediate(int val){
        this.val = val;
    }
    public String toString(){
        return Integer.toString(val);
    }
}
