package IR;

public class FloatImmediate extends Immediate {

    public float val;

    public FloatImmediate(float val){
        this.val = val;
    }
    public String toString(){
        return Float.toString(val);
    }
}
