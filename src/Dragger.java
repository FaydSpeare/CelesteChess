import java.util.ArrayList;
import java.util.List;

public class Dragger {

    public static int dragStart = -1;
    public static String dragType = "";
    public static List<Move> dragOptions = new ArrayList<>();

    public static void reset(){
        dragStart = -1;
        dragOptions.clear();
        dragType = "";
    }
}
