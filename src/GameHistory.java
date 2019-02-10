public class GameHistory {

    public double searchDuration;
    public double depth;
    public ChessGame position;

    public GameHistory(ChessGame pos, double depth, double searchDuration){
        this.position = pos;
        this.searchDuration = searchDuration;
        this.depth = depth;
    }

    public double difficultyIndex(){
        return depth / searchDuration;
    }
}
