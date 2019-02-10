public class Move {

    private int from, to;

    public Move(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public int getPiece() {
        return from;
    }

    public int getMove() {
        return to;
    }

}
