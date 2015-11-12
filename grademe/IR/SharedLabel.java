package IR;

public class SharedLabel extends Label {


    private static int labelNum = 0;
    public int id; // each label should be uniquely identified I think...

    public SharedLabel(String name){
        id = labelNum++;
        this.name = name + "_" + id;
    }
}
