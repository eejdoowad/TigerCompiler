
// Nothing to epsilon really
// All you ever do is:
//      1. Check if a Symbol is an instance of Epsilon (see Parser.java)
//      2. Push it onto the parse stack
//      3. Pop it from the parse stack

public class Epsilon extends Symbol {

    public Epsilon(){
        this.symbol = Config.EPSILON;
    }
}
