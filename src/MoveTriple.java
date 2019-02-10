public class MoveTriple {

    private int piece;
    private int move;
    private int score;

    public MoveTriple(int piece, int move, int score) {
        this.piece = piece;
        this.score = score;
        this.move = move;
    }

    public int getPiece() {
        return piece;
    }

    public int getMove() {
        return move;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof MoveTriple){
            MoveTriple other = (MoveTriple)obj;
            return other.move == this.move && other.piece == this.piece;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return move + 13*piece;
    }
}
