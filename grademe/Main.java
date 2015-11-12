import java.io.File;

// Main file... not much to explain
// Checks to make sure commandline arguments are valid
// and specified tiger file is okay
// then calls compiler

public class Main {

    public static void main(String[] args){
        if (Config.DEBUG){
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
        }
        if (args.length != 1){
            System.out.println("Provide exactly one input tiger file for compilation");
            System.out.println("USAGE: java Main input.tiger");
        } else if (!Util.getFileExtension(args[0]).equals("tiger")){
            System.out.println("Input file must have .tiger extension");
        } else if (!(new File(args[0]).exists())) {
                System.out.println(args[0] + " does not exist");
        } else {
            Compiler compiler = new Compiler();
            compiler.compile(args[0]);
        }
    }
}
