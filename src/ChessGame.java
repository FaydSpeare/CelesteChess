import java.util.ArrayList;

public class ChessGame {

    private static final long bottomRow = 255L;
    private static final long topRow = 255L << 56;


    private int moves;
    private boolean endgame;

    private static final long WhiteKing = 1L<<3, BlackKing = 1L<<59;
    private static final long BottomRight = 1L, BottomLeft = 1L<<7;
    private static final long TopLeft = 1L << 63, TopRight = 1L<<56;

    // Piece Boards
    long WK, WQ, WR, WB, WN, WP;
    long BK, BQ, BR, BB, BN, BP;
    public long O;
    private long W, B;

    public boolean turn = true;

    // En Passant Rights
    private long EP;

    // Castling Rights
    private boolean WhiteShort = true, WhiteLong = true, BlackShort = true, BlackLong = true;
    private boolean WhiteCastled = false, BlackCastled = false;

    private static int i = 0;
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ChessGame){

            ChessGame game = (ChessGame)obj;
            boolean ret = WK == game.WK &&
                    WQ == game.WQ &&
                    WR == game.WR &&
                    WB == game.WB &&
                    WN == game.WN &&
                    WP == game.WP &&
                    BK == game.WK &&
                    BQ == game.BQ &&
                    BR == game.BR &&
                    BB == game.BB &&
                    BN == game.BN &&
                    BP == game.BP &&
                    EP == game.EP &&
                    turn == game.turn &&
                    WhiteShort == game.WhiteShort &&
                    WhiteLong == game.WhiteLong &&
                    BlackShort == game.BlackShort &&
                    BlackLong == game.BlackLong;

            return ret;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int code = 31;

        int bools = 0;
        if(WhiteShort) bools += 1;
        if(WhiteLong) bools += 2;
        if(BlackShort) bools += 4;
        if(BlackLong) bools += 8;
        if(turn) bools += 16;
        code += Integer.hashCode(bools);

        int prime = 7;
        for(long l: new long[]{WK, WQ, WR, WB, WN, WP, BK, BQ, BR, BB, BN, BP, EP}){
            code += prime*(code + Long.bitCount(l)*Long.hashCode(l));
            prime += 7;
        }
        return code;

    }

    public ChessGame(ChessGame copy) {
        moves = copy.moves;
        WK = copy.WK;
        WQ = copy.WQ;
        WR = copy.WR;
        WB = copy.WB;
        WN = copy.WN;
        WP = copy.WP;

        BK = copy.BK;
        BQ = copy.BQ;
        BR = copy.BR;
        BB = copy.BB;
        BN = copy.BN;
        BP = copy.BP;

        EP = copy.EP;
        WhiteShort = copy.WhiteShort;
        WhiteLong = copy.WhiteLong;
        BlackShort = copy.BlackShort;
        BlackLong = copy.BlackLong;
        BlackCastled = copy.BlackCastled;
        WhiteCastled = copy.WhiteCastled;

        W = WQ|WK|WR|WB|WN|WP;
        B = BQ|BK|BR|BB|BN|BP;
        O = B|W;
        endgame = copy.endgame;
        turn = copy.turn;

    }

    public ChessGame(){}

    public void setup(String[] Board) {

        String Wking = "";
        String Wqueen = "";
        String Wrook = "";
        String Wbishop = "";
        String Wknight = "";
        String Wpawn = "";

        String Bking = "";
        String Bqueen = "";
        String Brook = "";
        String Bbishop = "";
        String Bknight = "";
        String Bpawn = "";

        for(int i = 0; i< 64; i++) {
            /// PIECE CALC

            if(Board[i] == "WK") {
                Wking += "1";
            }
            else {
                Wking += "0";
            }
            if(Board[i] == "WQ") {
                Wqueen += "1";
            }
            else {
                Wqueen += "0";
            }
            if(Board[i] == "WR") {
                Wrook += "1";
            }
            else {
                Wrook += "0";
            }
            if(Board[i] == "WN") {
                Wknight += "1";
            }
            else {
                Wknight += "0";
            }
            if(Board[i] == "WB") {
                Wbishop += "1";
            }
            else {
                Wbishop += "0";
            }
            if(Board[i] == "WP") {
                Wpawn += "1";
            }
            else {
                Wpawn += "0";
            }




            if(Board[i] == "BK") {
                Bking += "1";
            }
            else {
                Bking += "0";
            }
            if(Board[i] == "BQ") {
                Bqueen += "1";
            }
            else {
                Bqueen += "0";
            }
            if(Board[i] == "BR") {
                Brook += "1";
            }
            else {
                Brook += "0";
            }
            if(Board[i] == "BN") {
                Bknight += "1";
            }
            else {
                Bknight += "0";
            }
            if(Board[i] == "BB") {
                Bbishop += "1";
            }
            else {
                Bbishop += "0";
            }
            if(Board[i] == "BP") {
                Bpawn += "1";
            }
            else {
                Bpawn += "0";
            }
        }
        long promoteW = Long(Wpawn)& topRow;
        long promoteB = Long(Bpawn)& bottomRow;

        WK = Long(Wking);
        WQ = Long(Wqueen)|promoteW;
        WR = Long(Wrook);
        WB = Long(Wbishop);
        WN = Long(Wknight);
        WP = Long(Wpawn)&~promoteW;

        BK = Long(Bking);
        BQ = Long(Bqueen)|promoteB;
        BR = Long(Brook);
        BB = Long(Bbishop);
        BN = Long(Bknight);
        BP = Long(Bpawn)&~promoteB;

        String Bocc = "";
        for(int i = 0; i< 64; i++) {
            if((Board[i]).charAt(0) == 'B') {
                Bocc += "1";
            }
            else {
                Bocc += "0";
            }
        }

        String Wocc = "";
        for(int i = 0; i< 64; i++) {
            if((Board[i]).charAt(0) == 'W') {
                Wocc += "1";
            }
            else {
                Wocc += "0";
            }
        }

        String Aocc = "";
        for(int i = 0; i< 64; i++) {
            if((Board[i]).charAt(0) == 'W' || (Board[i]).charAt(0) == 'B' ) {
                Aocc += "1";
            }
            else {
                Aocc += "0";
            }
        }

        O = Long(Aocc);
        W = Long(Wocc);
        B = Long(Bocc);

    }

    private long Long(String s) {
        return Long.parseUnsignedLong(s, 2);
    }

    public ArrayList<Move> getAllMoves(boolean b_w){
        return Moves.getAllMoves(BlackLong, BlackShort, WhiteLong, WhiteShort, EP, WQ, WK, WR, WB, WN, WP, BQ, BK, BR, BB, BN, BP, O, W, B, b_w);
    }

    public ArrayList<Move> getAllCaptures(boolean b_w){
        return Moves.getAllCaptures(EP, WQ, WK, WR, WB, WN, WP, BQ, BK, BR, BB, BN, BP, O, W, B, b_w);
    }

    public void makeMove(int piece, int move) {
        moves++;
        turn = !turn;
        if(move > 64) {
            EP = 0L;
            if(move <100) {
                if(move == 99) {
                    WK = 1L<<1;
                    WR |= 1L<<2;
                    WR &= ~(BottomRight);
                    WhiteLong = false;
                    WhiteShort = false;
                    WhiteCastled = true;
                }
                if(move == 98) {
                    WK = 1L<<5;
                    WR |= 1L<<4;
                    WR &= ~(BottomLeft);
                    WhiteLong = false;
                    WhiteShort = false;
                    WhiteCastled = true;
                }
                if(move == 97) {
                    BK = 1L<<57;
                    BR |= 1L<<58;
                    BR &= ~(TopRight);
                    BlackLong = false;
                    BlackShort = false;
                    BlackCastled = true;
                }
                if(move == 96) {
                    BK = 1L<<61;
                    BR |= 1L<<60;
                    BR &= ~(TopLeft);
                    BlackLong = false;
                    BlackShort = false;
                    BlackCastled = true;
                }
                W = WQ|WK|WR|WB|WN|WP;
                B = BQ|BK|BR|BB|BN|BP;
                O = B|W;
                if(!endgame && Long.bitCount((O)&~(WP|BP)) < 7) {
                    endgame = true;
                }
                return;
            }
            BB &= ~(1L<<(63-move%100)); BQ &= ~(1L<<(63-move%100)); BN &= ~(1L<<(63-move%100)); BR &= ~(1L<<(63-move%100));
            WB &= ~(1L<<(63-move%100)); WQ &= ~(1L<<(63-move%100)); WN &= ~(1L<<(63-move%100)); WR &= ~(1L<<(63-move%100));
            if(move > 800) {
                BQ |= 1L<<(63-(move%100));
                BP &= ~(1L<<(63-piece));
            }
            else if(move > 700) {
                BR |= 1L<<(63-(move%100));
                BP &= ~(1L<<(63-piece));
            }
            else if(move > 600) {
                BB |= 1L<<(63-(move%100));
                BP &= ~(1L<<(63-piece));
            }
            else if(move > 500) {
                BN |= 1L<<(63-(move%100));
                BP &= ~(1L<<(63-piece));
            }
            else if(move > 400) {
                WQ |= 1L<<(63-(move%100));
                WP &= ~(1L<<(63-piece));
            }
            else if(move > 300) {
                WR |= 1L<<(63-(move%100));
                WP &= ~(1L<<(63-piece));
            }
            else if(move > 200) {
                WB |= 1L<<(63-(move%100));
                WP &= ~(1L<<(63-piece));
            }
            else if(move > 100) {
                WN |= 1L<<(63-(move%100));
                WP &= ~(1L<<(63-piece));
            }
            W = WQ|WK|WR|WB|WN|WP;
            B = BQ|BK|BR|BB|BN|BP;
            O = B|W;

            // capturing castle when promoting
            if((BlackKing&B) == 0L) {
                BlackLong = false;
                BlackShort = false;
            }
            else if((TopRight&B) == 0L) {
                BlackShort = false;
            }
            if((TopLeft&B) == 0L) {
                BlackLong = false;
            }
            if((WhiteKing&W) == 0L) {
                WhiteLong = false;
                WhiteShort = false;
            }
            else if((BottomLeft&W) == 0L) {
                WhiteLong = false;
            }
            if((BottomRight&W) == 0L) {
                WhiteShort = false;
            }
            if(!endgame && Long.bitCount((O)&~(WP|BP)) < 7) {
                endgame = true;
            }
            return;

        }
        else {
            move = 63-move;
            piece = 63-piece;
            long piecebb = 1L<<(piece);
            long movebb = 1L<<(move);

            if(EP == movebb) {
                if((piecebb&WP) != 0) {
                    BP &= ~(1L<<move-8);
                }
                else if((piecebb&BP) != 0) {
                    WP &= ~(1L<<move+8);
                }
            }

            EP = 0L;

            WQ = Moves.MakeMove(WQ, piecebb, movebb);
            WK = Moves.MakeMove(WK, piecebb, movebb);
            WR = Moves.MakeMove(WR, piecebb, movebb);
            WB = Moves.MakeMove(WB, piecebb, movebb);
            WN = Moves.MakeMove(WN, piecebb, movebb);
            WP = Moves.MakeMove(WP, piecebb, movebb);

            BQ = Moves.MakeMove(BQ, piecebb, movebb);
            BK = Moves.MakeMove(BK, piecebb, movebb);
            BR = Moves.MakeMove(BR, piecebb, movebb);
            BB = Moves.MakeMove(BB, piecebb, movebb);
            BN = Moves.MakeMove(BN, piecebb, movebb);
            BP = Moves.MakeMove(BP, piecebb, movebb);

            W = WQ|WK|WR|WB|WN|WP;
            B = BQ|BK|BR|BB|BN|BP;
            O = B|W;

            if((BlackKing&B) == 0L) {
                BlackLong = false;
                BlackShort = false;
            }
            else if((TopRight&B) == 0L) {
                BlackShort = false;
            }
            if((TopLeft&B) == 0L) {
                BlackLong = false;
            }
            if((WhiteKing&W) == 0L) {
                WhiteLong = false;
                WhiteShort = false;
            }
            else if((BottomLeft&W) == 0L) {
                WhiteLong = false;
            }

            if((BottomRight&W) == 0L) {
                WhiteShort = false;
            }

            if(Math.abs(piece-move)==16 && (WP&movebb)!=0L) {


                if(((1L<<move-1)&BP) !=0L) {
                    EP |= (1L<<move-8);
                }
                if(((1L<<move+1)&BP) !=0L) {

                    EP |= (1L<<move-8);
                }
            }

            else if(Math.abs(piece-move)==16 && (BP&movebb)!=0L) {
                if(((1L<<move-1)&WP) !=0L) {
                    EP |= (1L<<move+8);
                }
                if(((1L<<move+1)&WP) !=0L) {
                    EP |= (1L<<move+8);
                }
            }
        }
        if(!endgame && Long.bitCount((O)&~(WP|BP)) < 7) {
            endgame = true;
        }

    }

    private int Evaluation(int c, boolean turn) {
        int evaluation = 0;
        int count;

        if(c == 1) {
            if(turn) {
                evaluation -= 1000000;
            }
            else {
                evaluation += 1000000;
            }
        }
        count = Long.bitCount(WQ);
        evaluation += count * 900;
        count = Long.bitCount(WR);
        evaluation += count * 500;
        count = Long.bitCount(WB);
        evaluation += count * 300;
        count = Long.bitCount(WN);
        evaluation += count * 300;
        count = Long.bitCount(WP);
        evaluation += count * 100;

        count = Long.bitCount(BQ);
        evaluation -= count * 900;
        count = Long.bitCount(BR);
        evaluation -= count * 500;
        count = Long.bitCount(BB);
        evaluation -= count * 300;
        count = Long.bitCount(BN);
        evaluation -= count * 300;
        count = Long.bitCount(BP);
        evaluation -= count * 100;

        return evaluation;
    }

    public int Evaluation2(int c, boolean turn) {
        int evaluation = 0;

        if(Long.bitCount((O)&~(WP|BP)) > 13 && !WhiteCastled) {
            evaluation -= 45;
        }
        if(Long.bitCount((O)&~(WP|BP)) > 13 && !BlackCastled) {
            evaluation += 45;
        }
        if(moves < 10 && WhiteCastled) {
            evaluation += 55;
        }
        if(moves < 10 && BlackCastled) {
            evaluation -= 55;
        }
        if(moves < 15) {
            if((WP&1L<<10) != 0L) {
                evaluation -= 10;
            }
            if((WP&1L<<11) != 0L) {
                evaluation -= 20;
            }
            if((WP&1L<<12) != 0L) {
                evaluation -= 20;
            }
            if((WB&1L<<2) != 0L) {
                evaluation -= 15;
            }
            if((WB&1L<<5) != 0L) {
                evaluation -= 15;
            }
            if((WN&1L<<1) != 0L) {
                evaluation -= 10;
            }
            if((WN&1L<<6) != 0L) {
                evaluation -= 10;
            }
            if((BP&1L<<50) != 0L) {
                evaluation += 10;
            }
            if((BP&1L<<51) != 0L) {
                evaluation += 20;
            }
            if((BP&1L<<52) != 0L) {
                evaluation += 20;
            }
            if((BB&1L<<58) != 0L) {
                evaluation += 15;
            }
            if((BB&1L<<61) != 0L) {
                evaluation += 15;
            }
            if((BN&1L<<62) != 0L) {
                evaluation += 10;
            }
            if((BN&1L<<57) != 0L) {
                evaluation += 10;
            }
        }
        if(!WhiteCastled && moves < 13) {
            if(!WhiteLong  && !WhiteShort) {
                evaluation -= 30;
            }
        }
        if(!BlackCastled && moves < 13) {
            if(!BlackLong  && !BlackShort) {
                evaluation +=30;
            }
        }
        if(Long.bitCount(B) < 3 && Long.bitCount(B)<Long.bitCount(W)) {
            int k1 = Math.abs(Long.numberOfTrailingZeros(BK)%8 - Long.numberOfTrailingZeros(WK)%8);
            int k2 = Math.abs(Long.numberOfTrailingZeros(BK)/8 - Long.numberOfTrailingZeros(WK)/8);
            evaluation -= 20*(k1+k2);
        }
        if(Long.bitCount(W) < 3 && Long.bitCount(B)>Long.bitCount(W)) {
            int k1 = Math.abs(Long.numberOfTrailingZeros(BK)%8 - Long.numberOfTrailingZeros(WK)%8);
            int k2 = Math.abs(Long.numberOfTrailingZeros(BK)/8 - Long.numberOfTrailingZeros(WK)/8);
            evaluation += 20*(k1+k2);
        }

        if(c == 1) {
            if(turn) {
                if(endgame && Moves.UnSafeWhite(BQ, BK, BB, BN, BR, BP, O, WK)) {
                    if(this.Evaluation(0, turn) > 100) {
                        evaluation -= 500000;
                    }
                    else if(this.Evaluation(0, turn) < -100) {
                        evaluation += 500000;
                    }
                }
                else if(!endgame) {

                    evaluation -= 1000000;
                }


                return evaluation;
            }
            else {
                if(endgame && Moves.UnSafeBlack(WQ, WK, WB, WN, WR, WP, O, BK)) {
                    if(this.Evaluation(0, turn) > 100) {
                        evaluation += 500000;
                    }
                    else if(this.Evaluation(0, turn) < -100) {
                        evaluation -= 500000;
                    }
                }
                else if(!endgame) {
                    evaluation += 1000000;
                }


                return evaluation;
            }
        }

        for(int i: Moves.cbb(WQ)) {
            if(moves > 15 && !WhiteCastled) {
                evaluation -= 70;
            }
            evaluation += 1010;
        }
        for(int i: Moves.cbb(WR)) {
            evaluation += 510;
        }
        for(int i: Moves.cbb(WB)) {
            evaluation += 345 + (Moves.BISHOP[i]);
        }
        for(int i: Moves.cbb(WN)) {
            evaluation += 330 + (Moves.KNIGHT[i]);
        }
        int counter = 0;
        int sumx = 0;
        int sumy = 0;


        for(int i: Moves.cbb(WP)) {

            if(endgame) {
                counter++;
                sumx += i%8;
                sumy += i/8;
                evaluation += 105 +(Moves.WHITEPAWNEND[i]) ;
            }
            else if((WK&1799L) != 0L) {
                evaluation += 95 + (Moves.WHITEPAWNRIGHTCASTLE[i]);
            }
            else if((WK&1799L<<5) != 0L) {
                evaluation += 95 + (Moves.WHITEPAWNLEFTCASTLE[i]);
            }
            else {
                evaluation += 95  +(Moves.WHITEPAWN[i]);
            }

        }
        for(int i: Moves.cbb(WK)) {
            if(endgame) {
                int kingx = i%8;
                int kingy = i/8;
                int diff;
                if(counter != 0) {
                    diff = Math.abs((sumx/counter)-kingx) + Math.abs((sumy/counter)-kingy);
                }
                else {
                    diff = 0;
                }
                evaluation += 300+Moves.KINGEND[i];
                if(diff > 2) {
                    //evaluation -= 3*diff;
                }
            }
            else {
                evaluation += 300 +Moves.WHITEKING[i];
            }
        }


        for(int i: Moves.cbb(BQ)) {
            if(moves > 15 && !BlackCastled) {
                evaluation += 70;
            }
            evaluation -= 1010;
        }
        for(int i: Moves.cbb(BR)) {
            evaluation -= 510;
        }
        for(int i: Moves.cbb(BB)) {
            evaluation -= 340 + (Moves.BISHOP[i]);
        }
        for(int i: Moves.cbb(BN)) {
            evaluation -= 330 + (Moves.KNIGHT[i]);
        }
        counter = 0;
        sumx = 0;
        sumy = 0;


        for(int i: Moves.cbb(BP)) {

            if(endgame) {
                counter++;
                sumx += i%8;
                sumy += i/8;
                evaluation -= 105 + (Moves.BLACKPAWNEND[i]);
            }
            else if((BK&1799L<<48) != 0L) {
                evaluation -= 95 + (Moves.BLACKPAWNRIGHTCASTLE[i]);
            }
            else if((BK&1799L<<53) != 0L) {
                evaluation -= 95 + (Moves.BLACKPAWNLEFTCASTLE[i]);
            }
            else {
                evaluation -= 95 + (Moves.BLACKPAWN[i]);
            }
        }
        for(int i: Moves.cbb(BK)) {
            if(endgame) {
                int kingx = i%8;
                int kingy = i/8;
                int diff;
                if(counter != 0) {
                    diff = Math.abs((sumx/counter)-kingx) + Math.abs((sumy/counter)-kingy);
                }
                else {
                    diff = 0;
                }
                evaluation -= 300+Moves.KINGEND[i];
                if(diff > 2) {
                    //evaluation+= 3*diff;
                }
            }
            else {
                evaluation -= 300 +Moves.BLACKKING[i];
            }
        }

        if((BK&1799L<<48) != 0L) {
            if((Moves.file&(WP)) == 0L) {
                if((Moves.file&(BP)) == 0L) {
                    evaluation += 35;
                }
                evaluation += 20;
            }
            if((Moves.file<<1&(WP)) == 0L) {
                if((Moves.file<<1&(BP)) == 0L) {
                    evaluation += 35;
                }
                evaluation += 20;
            }
            evaluation += KingSafety(2);
        }
        else if((BK&1799L<<53) != 0L) {
            if((Moves.file<<7&(WP)) == 0L) {
                if((Moves.file<<7&(BP)) == 0L) {
                    evaluation += 35;
                }
                evaluation += 20;
            }
            if((Moves.file<<6&(WP)) == 0L) {
                if((Moves.file<<6&(BP)) == 0L) {
                    evaluation += 35;
                }
                evaluation += 20;
            }
            evaluation += KingSafety(1);
        }
        else if((BK&771L<<51) != 0L) {
            evaluation += KingSafety(8);
        }
        else {
            evaluation += KingSafety(98);
        }

        if((WK&1799L) != 0L) {
            if((Moves.file&(BP)) == 0L) {
                if((Moves.file&(WP)) == 0L) {
                    evaluation -= 35;
                }
                evaluation -= 20;
            }
            if((Moves.file<<1&(BP)) == 0L) {
                if((Moves.file<<1&(WP)) == 0L) {
                    evaluation -= 35;
                }
                evaluation -= 20;
            }
            evaluation -= KingSafety(4);
        }
        else if((WK&1799L<<5) != 0L) {
            if((Moves.file<<7&(BP)) == 0L) {
                if((Moves.file<<7&(WP)) == 0L) {
                    evaluation -= 35;
                }
                evaluation -= 20;
            }
            if((Moves.file<<6&(BP)) == 0L) {
                if((Moves.file<<6&(WP)) == 0L) {
                    evaluation -= 35;
                }
                evaluation -= 20;
            }
            evaluation -= KingSafety(3);
        }
        else if((WK&771L<<3) != 0L) {
            evaluation -= KingSafety(9);
        }
        else {
            evaluation -= KingSafety(99);
        }

        for(int i = 0; i<8; i++) {
            if(Long.bitCount((Moves.file<<i)&WP) > 1) {
                evaluation -= 3;
                if(i == 0 || i == 7) {
                    evaluation -= 4;
                }
            }
            if(Long.bitCount((Moves.file<<i)&BP) > 1) {
                evaluation += 3;
                if(i == 0 || i == 7) {
                    evaluation += 4;
                }
            }
        }


        return evaluation;
    }

    public int KingSafety(int king) {
        int attcount = 0;
        // white right

        long bball = 0L;
        long bb = 0L;
        long bb2 = 0L;

        int pawns = 0, rooks = 0, bishs = 0, knights = 0, queens = 0;
        // black king
        if(king == 1 || king == 2) {
            if(king == 2) {
                int[] nums = {40,41,42,48,49,50,56,57,58};
                long o = (O&(~(W)))|(WP&~(Moves.PIF_BR));
                for(int i: nums) {
                    bb = Moves.DMoves(i, o);
                    bb2 = Moves.HVMoves(i, o);
                    bball |= bb2|bb;
                    bishs += Long.bitCount(bb&(WB));
                    rooks += Long.bitCount(bb2&(WR));
                    queens += Long.bitCount((bb|bb2)&(WQ));
                    knights += Long.bitCount(Moves.KnMoves(i)&WN);
                    bball |= Moves.KnMoves(i)&(WN);

                }
                pawns += Long.bitCount(Moves.PIF_BR&WP);
                attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
                if(pawns + Long.bitCount(bball&(WQ|WB|WR|WN)) < 3) {
                    attcount = 0;
                }
            }

            else {
                int[] nums = {45,46,47,53,54,55,61,62,63};
                long o = (O&(~(W)))|(WP&~(Moves.PIF_BL));
                for(int i: nums) {
                    bb = Moves.DMoves(i, o);
                    bb2 = Moves.HVMoves(i, o);
                    bball |= bb2|bb;
                    bishs += Long.bitCount(bb&(WB));
                    rooks += Long.bitCount(bb2&(WR));
                    queens += Long.bitCount((bb|bb2)&(WQ));
                    knights += Long.bitCount(Moves.KnMoves(i)&WN);
                    bball |= Moves.KnMoves(i)&(WN);

                }
                pawns += Long.bitCount(Moves.PIF_BL&WP);
                attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
                if(pawns + Long.bitCount(bball&(WQ|WB|WR|WN)) < 3) {
                    attcount = 0;
                }
            }
        }

        // white king

        else if(king == 3 || king == 4) {
            if(king == 4) {
                int[] nums = {0,1,2,8,9,10,16,17,18};
                long o = (O&(~(B)))|(BP&~(Moves.PIF_WR));
                for(int i: nums) {
                    bb = Moves.DMoves(i, o);
                    bb2 = Moves.HVMoves(i, o);
                    bball |= bb2|bb;
                    bishs += Long.bitCount(bb&(BB));
                    rooks += Long.bitCount(bb2&(BR));
                    queens += Long.bitCount((bb|bb2)&(BQ));
                    knights += Long.bitCount(Moves.KnMoves(i)&BN);
                    bball |= Moves.KnMoves(i)&(BN);

                }
                pawns += Long.bitCount(Moves.PIF_WR&BP);
                attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
                if(pawns + Long.bitCount(bball&(BQ|BB|BR|BN)) < 3) {
                    attcount = 0;
                }
            }
            else {
                int[] nums = {5,6,7,13,14,15,21,22,23};
                long o = (O&(~(B)))|(BP&~(Moves.PIF_WL));
                for(int i: nums) {
                    bb = Moves.DMoves(i, o);
                    bb2 = Moves.HVMoves(i, o);
                    bball |= bb2|bb;
                    bishs += Long.bitCount(bb&(BB));
                    rooks += Long.bitCount(bb2&(BR));
                    queens += Long.bitCount((bb|bb2)&(BQ));
                    knights += Long.bitCount(Moves.KnMoves(i)&BN);
                    bball |= Moves.KnMoves(i)&(BN);

                }
                pawns += Long.bitCount(Moves.PIF_WL&BP);
                attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
                if(pawns + Long.bitCount(bball&(BQ|BB|BR|BN)) < 3) {
                    attcount = 0;
                }
            }


        }
        else if(king == 8) {
            int[] nums = {59,60,51,52,43,44};
            long o = (O&(~(W)))|(WP&~(Moves.PIF_BR));
            for(int i: nums) {
                bb = Moves.DMoves(i, o);
                bb2 = Moves.HVMoves(i, o);
                bball |= bb2|bb;
                bishs += Long.bitCount(bb&(WB));
                rooks += Long.bitCount(bb2&(WR));
                queens += Long.bitCount((bb|bb2)&(WQ));
                knights += Long.bitCount(Moves.KnMoves(i)&WN);
                bball |= Moves.KnMoves(i)&(WN);

            }
            pawns += Long.bitCount(Moves.PIF_BR&WP);
            attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
            if(pawns + Long.bitCount(bball&(WQ|WB|WR|WN)) < 3) {
                attcount = 0;
            }
        }
        else if(king == 9) {
            int[] nums = {3,4,11,12,19,20};
            long o = (O&(~(B)))|(BP&~(Moves.PIF_WR));
            for(int i: nums) {
                bb = Moves.DMoves(i, o);
                bb2 = Moves.HVMoves(i, o);
                bball |= bb2|bb;
                bishs += Long.bitCount(bb&(BB));
                rooks += Long.bitCount(bb2&(BR));
                queens += Long.bitCount((bb|bb2)&(BQ));
                knights += Long.bitCount(Moves.KnMoves(i)&BN);
                bball |= Moves.KnMoves(i)&(BN);

            }
            pawns += Long.bitCount(Moves.PIF_WR&BP);
            attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
            if(pawns + Long.bitCount(bball&(BQ|BB|BR|BN)) < 3) {
                attcount = 0;
            }
        }
        else if(king == 98) {
            int[] nums = Moves.cbb(Moves.KingMoves(Long.numberOfTrailingZeros(BK)));
            long o = (O&(~(W)))|(WP&~(Moves.PIF_BR));
            for(int i: nums) {
                bb = Moves.DMoves(i, o);
                bb2 = Moves.HVMoves(i, o);
                bball |= bb2|bb;
                bishs += Long.bitCount(bb&(WB));
                rooks += Long.bitCount(bb2&(WR));
                queens += Long.bitCount((bb|bb2)&(WQ));
                knights += Long.bitCount(Moves.KnMoves(i)&WN);
                bball |= Moves.KnMoves(i)&(WN);

            }
            pawns += Long.bitCount(Moves.PIF_BR&WP);
            attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
            if(pawns + Long.bitCount(bball&(WQ|WB|WR|WN)) < 3) {
                attcount = 0;
            }
        }
        else if(king == 99) {
            int[] nums = Moves.cbb(Moves.KingMoves(Long.numberOfTrailingZeros(WK)));
            long o = (O&(~(B)))|(BP&~(Moves.PIF_WR));
            for(int i: nums) {
                bb = Moves.DMoves(i, o);
                bb2 = Moves.HVMoves(i, o);
                bball |= bb2|bb;
                bishs += Long.bitCount(bb&(BB));
                rooks += Long.bitCount(bb2&(BR));
                queens += Long.bitCount((bb|bb2)&(BQ));
                knights += Long.bitCount(Moves.KnMoves(i)&BN);
                bball |= Moves.KnMoves(i)&(BN);

            }
            pawns += Long.bitCount(Moves.PIF_WR&BP);
            attcount = 1*pawns + 2*bishs + 2*knights + 3*rooks + 5*queens;
            if(pawns + Long.bitCount(bball&(BQ|BB|BR|BN)) < 3) {
                attcount = 0;
            }
        }
        if(attcount > 99) {
            attcount = 99;
        }
        return Moves.SafetyTable[attcount];
    }

    public void makeMoveUI(String type, int piece, int move){
        moves++;

        if(type.equals("WK")){

            if(piece == 60){

                //White Short Castle
                if(move == 62){
                    makeMove(piece, 99);
                    return;
                }

                //White Long Castle
                if(move == 58){
                    makeMove(piece, 98);
                    return;
                }
            }
        }
        else if(type.equals("BK")){

            if(piece == 4){

                //Black Short Castle
                if(move == 6){
                    makeMove(piece, 97);
                    return;
                }

                //Black Long Castle
                if(move == 2){
                    makeMove(piece, 96);
                    return;
                }
            }
        }

        makeMove(piece, move);
    }

    public static int getPos(int m){
        if(m > 64) {
            if(m == 96) {
                m = 2;
            }
            else if(m == 97) {
                m = 6;
            }
            else if(m == 98) {
                m = 58;
            }
            else if(m == 99) {
                m = 62;
            }
        }
        m %= 100;
        return m;
    }

}
