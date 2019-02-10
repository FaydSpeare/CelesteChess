import java.util.ArrayList;
import java.util.List;

public class Celeste {


    public static List<GameHistory> history = new ArrayList<>();
    public static int counter = 0;

    private static MoveTriple abSearch(ChessGame position, int depth, boolean alliance, int alpha, int beta, int quietSearch){

        counter++;
        int bestmove = 0;
        int bestpiece = 0;

        if(depth == 0) {
            return new MoveTriple(bestpiece, bestmove, quietSearch(position, alliance, alpha, beta, quietSearch));
        }
        else {
            if(alliance) {


                ArrayList<Move> moves;
                moves = position.getAllMoves(true);

                if(moves.isEmpty()) {
                    if(Moves.UnSafeWhite(position.BQ, position.BK, position.BB, position.BN,
                            position.BR, position.BP, position.O, position.WK)) {
                        return new MoveTriple(bestpiece, bestmove, -1000000-depth);
                    }
                }

                for(Move MOVE: moves) {
                    int piece = MOVE.getPiece();
                    int move = MOVE.getMove();
                    ChessGame game = new ChessGame(position);
                    game.makeMove(piece,move);

                    MoveTriple result = abSearch(game, depth-1, false, alpha, beta, quietSearch);
                    if(TimeManager.finished()){
                        TimeManager.stop();
                        return null;
                    }

                    if(result.getScore() > alpha) {
                        alpha = result.getScore();
                        bestmove = move;
                        bestpiece = piece;
                        if(alpha >= beta) {
                            break;
                        }
                    }
                }
                return new MoveTriple(bestpiece, bestmove, alpha);
            }
            else {


                ArrayList<Move> moves;
                moves = position.getAllMoves(false);

                if(moves.isEmpty()) {
                    if(Moves.UnSafeBlack(position.WQ, position.WK, position.WB, position.WN, position.WR,
                            position.WP, position.O, position.BK)) {
                        return new MoveTriple(bestpiece, bestmove, 1000000+depth);
                    }
                }

                for(Move MOVE: moves) {
                    int piece = MOVE.getPiece();
                    int move = MOVE.getMove();
                    ChessGame game = new ChessGame(position);
                    game.makeMove(piece,move);

                    MoveTriple result = abSearch(game, depth-1, true, alpha, beta, quietSearch);
                    if(TimeManager.finished()){
                        TimeManager.stop();
                        return null;
                    }

                    if(result.getScore() < beta) {
                        beta = result.getScore();
                        bestmove = move;
                        bestpiece = piece;
                        if(alpha >= beta) {
                            break;
                        }
                    }
                }
                return new MoveTriple(bestpiece, bestmove, beta);

            }

        }
    }

    private static int quietSearch(ChessGame position, boolean alliance, int alpha, int beta, int depth) {
        counter++;
        if(alliance) {
            int stand_pat = position.Evaluation2(0, true);
            if(depth == 0) {
                return stand_pat;
            }
            if(stand_pat >= beta) {
                return beta;
            }
            if(alpha < stand_pat) {
                alpha = stand_pat;
            }
            ArrayList<Move> moves = position.getAllCaptures(true);
            for(Move MOVE: moves) {
                int piece = MOVE.getPiece();
                int move = MOVE.getMove();
                ChessGame game = new ChessGame(position);
                game.makeMove(piece,move);
                int result = quietSearch(game, false, alpha, beta, depth-1);
                if(result > alpha) {
                    alpha = result;
                    if(alpha >= beta) {
                        break;
                    }
                }
            }
            return alpha;
        }
        else {
            int stand_pat = position.Evaluation2(0, false);
            if(depth == 0) {
                return stand_pat;
            }
            if(stand_pat <= alpha) {
                return alpha;
            }
            if(beta > stand_pat) {
                beta = stand_pat;
            }
            ArrayList<Move> moves = position.getAllCaptures(false);

            for(Move MOVE: moves) {
                int piece = MOVE.getPiece();
                int move = MOVE.getMove();
                ChessGame game = new ChessGame(position);
                game.makeMove(piece,move);
                int result = quietSearch(game, true, alpha, beta, depth-1);
                if(result < beta) {
                    beta = result;
                    if(alpha >= beta) {
                        break;
                    }
                }
            }
            return beta;

        }
    }

    private static ArrayList<MoveTriple> getSortedMoves(ChessGame position, List<MoveTriple> moves, boolean alliance,
                                                       int depth){
        if(moves == null){
            return abSearchList2(position, 1, alliance, -9999999, 9999999, 5);
        } else {
            return abSearchList(position, moves, depth, alliance, -9999999, 9999999, 5);
        }

    }

    public static void runPerft(ChessGame game, int depth, boolean b_w) {
        if(depth == 0) {
            counter++;
        }
        else {
            ArrayList<Move> moves = game.getAllMoves(b_w);
            for(Move move: moves ) {
                ChessGame copy = new ChessGame(game);
                copy.makeMove(move.getPiece(),move.getMove());
                runPerft(copy, depth-1, !b_w);
            }

        }
    }

    public static MoveTriple iterativeDeepening(double limit, ChessGame game){
        System.out.println("");
        TimeManager.start();
        double timeAllowed = TimeManager.setTimeAllowed(game);

        int depth = 0;
        double partialDepth = 0;

        List<MoveTriple> sortedMoves = null;
        while(!TimeManager.finished()){


            depth++;
            if(depth == 1){
                sortedMoves = getSortedMoves(game, sortedMoves, game.turn, depth);
                partialDepth = depth;
                System.out.println("Searced Depth: "+depth);
            } else {
                List<MoveTriple> moves = getSortedMoves(game, sortedMoves, game.turn, depth);
                if(moves.size() == sortedMoves.size()){
                    sortedMoves = moves;
                    partialDepth = depth;
                    System.out.println("Searced Depth: "+depth);
                } else {

                    //System.out.println(moves.size() + " / "+sortedMoves.size());
                    partialDepth += ((double)moves.size()/(double)sortedMoves.size());
                    for(int i = 0; i < sortedMoves.size(); i++){
                        MoveTriple move = sortedMoves.get(i);
                        if(!moves.contains(move)){
                            moves.add(move);
                        }
                    }


                    sortedMoves.clear();
                    if(game.turn){
                        for(int i = 0; i < moves.size(); i++){
                            MoveTriple move = moves.get(i);
                            if(sortedMoves.size() == 0){
                                sortedMoves.add(move);
                            } else {
                                for(int j = 0; j < sortedMoves.size(); j++){
                                    MoveTriple other = moves.get(i);
                                    if(move.getScore() > other.getScore()){
                                        sortedMoves.add(j, move);
                                    }
                                }
                            }
                        }
                    } else {
                        for(int i = 0; i < moves.size(); i++){
                            MoveTriple move = moves.get(i);
                            if(sortedMoves.size() == 0){
                                sortedMoves.add(move);
                            } else {
                                for(int j = 0; j < sortedMoves.size(); j++){
                                    MoveTriple other = moves.get(i);
                                    if(move.getScore() < other.getScore()){
                                        sortedMoves.add(j, move);
                                    }
                                }
                            }
                        }
                    }

                }

            }



            ///System.out.println(TimeManager.elapsed());
        }

        System.out.println("Searched Depth: "+partialDepth);
        System.out.println("Evaluation: "+sortedMoves.get(0).getScore());
        System.out.println("Nodes: " + counter);
        System.out.println("Time Left: "+TimeManager.timeLeft);
        counter = 0;

        /*
        for(int i = 0; i < sortedMoves.size(); i++){
            System.out.println(sortedMoves.get(i).getPiece() + " " + sortedMoves.get(i).getMove() + " " + sortedMoves
                    .get(i).getScore());
        }
        */

        history.add(new GameHistory(game, partialDepth, timeAllowed));
        return sortedMoves.get(0);

    }

    private static ArrayList<MoveTriple> abSearchList(ChessGame position, List<MoveTriple> moves, int depth, boolean alliance,
                                                      int alpha, int beta, int quietSearch){
        ArrayList<MoveTriple> SortedMoves = new ArrayList<>();
        ArrayList<Integer> Scores = new ArrayList<>();

        if(alliance) {
            for(MoveTriple MOVE: moves) {
                int piece = MOVE.getPiece();
                int move = MOVE.getMove();
                ChessGame game = new ChessGame(position);
                game.makeMove(piece,move);

                MoveTriple result = abSearch(game, depth-1, false, alpha, beta, quietSearch);
                if(TimeManager.finished()){
                    TimeManager.stop();
                    return SortedMoves;
                }

                if(Scores.size() == 0) {
                    SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                    Scores.add(result.getScore());
                }
                else {
                    int size = Scores.size();
                    for(int i=0; i < size; i++) {
                        if(result.getScore() > Scores.get(i)) {
                            SortedMoves.add(i, new MoveTriple(piece, move, result.getScore()));
                            Scores.add(i, result.getScore());
                            break;
                        }
                        else if(i == Scores.size()-1) {
                            SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                            Scores.add(result.getScore());
                        }
                    }
                }
                if(TimeManager.finished()){
                    TimeManager.stop();
                    return null;
                }
            }
            return SortedMoves;
        }
        else {
            for(MoveTriple MOVE: moves) {
                int piece = MOVE.getPiece();
                int move = MOVE.getMove();
                ChessGame game = new ChessGame(position);
                game.makeMove(piece,move);

                MoveTriple result = abSearch(game, depth-1, true, alpha, beta, quietSearch);
                if(TimeManager.finished()){
                    TimeManager.stop();
                    return SortedMoves;
                }

                if(Scores.size() == 0) {
                    SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                    Scores.add(result.getScore());
                }
                else {
                    int size = Scores.size();
                    for(int i=0; i < size; i++) {
                        if(result.getScore() < Scores.get(i)) {
                            SortedMoves.add(i, new MoveTriple(piece, move, result.getScore()));
                            Scores.add(i, result.getScore());
                            break;
                        }
                        else if(i == Scores.size()-1) {
                            SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                            Scores.add(result.getScore());
                        }
                    }
                }
                if(TimeManager.finished()){
                    TimeManager.stop();
                    return null;
                }
            }
            return SortedMoves;

        }
    }








    public static ArrayList<MoveTriple> getSortedMoves(ChessGame position, boolean alliance){
        return abSearchList2(position, 1, alliance, -9999999, 9999999, 1);
    }

    private static ArrayList<MoveTriple> abSearchList2(ChessGame position, int depth, boolean alliance, int alpha, int
            beta, int quietSearch){
        ArrayList<MoveTriple> SortedMoves = new ArrayList<>();
        ArrayList<Integer> Scores = new ArrayList<>();

        if(alliance) {
            ArrayList<Move> moves = position.getAllMoves(true);

            for(Move MOVE: moves) {
                int piece = MOVE.getPiece();
                int move = MOVE.getMove();
                ChessGame game = new ChessGame(position);
                game.makeMove(piece,move);

                MoveTriple result = abSearch(game, depth-1, false, alpha, beta, quietSearch);
                if(Scores.size() == 0) {
                    SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                    Scores.add(result.getScore());
                }
                else {
                    int size = Scores.size();
                    for(int i=0; i < size; i++) {
                        if(result.getScore() > Scores.get(i)) {
                            SortedMoves.add(i, new MoveTriple(piece, move, result.getScore()));
                            Scores.add(i, result.getScore());
                            break;
                        }
                        else if(i == Scores.size()-1) {
                            SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                            Scores.add(result.getScore());
                        }
                    }
                }
            }
            return SortedMoves;
        }
        else {
            ArrayList<Move> moves = position.getAllMoves(false);

            for(Move MOVE: moves) {
                int piece = MOVE.getPiece();
                int move = MOVE.getMove();
                ChessGame game = new ChessGame(position);
                game.makeMove(piece,move);

                MoveTriple result = abSearch(game, depth-1, true, alpha, beta, quietSearch);
                if(Scores.size() == 0) {
                    SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                    Scores.add(result.getScore());
                }
                else {
                    int size = Scores.size();
                    for(int i=0; i < size; i++) {
                        if(result.getScore() < Scores.get(i)) {
                            SortedMoves.add(i, new MoveTriple(piece, move, result.getScore()));
                            Scores.add(i, result.getScore());
                            break;
                        }
                        else if(i == Scores.size()-1) {
                            SortedMoves.add(new MoveTriple(piece, move, result.getScore()));
                            Scores.add(result.getScore());
                        }
                    }
                }
            }
            return SortedMoves;

        }
    }



}
