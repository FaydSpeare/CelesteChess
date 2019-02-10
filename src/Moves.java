import java.util.ArrayList;
public class Moves {

    private static long BottomLeftCastle = 7L<<4;
    private static long BottomRightCastle = 3L<<1;
    private static long TopRightCastle = 3L<<57;
    private static long TopLeftCastle = 7L<<60;

    private static long FILE_A=72340172838076673L;
    private static long FILE_H=-9187201950435737472L;
    private static long KNIGHT_SPAN=43234889994L;
    private static long KING_SPAN = 460039L;
    private static long FILE_AB=217020518514230019L;
    private static long FILE_GH=-4557430888798830400L;

    private static long RankMask[] =
            {
                    0xFFL, 0xFF00L, 0xFF0000L, 0xFF000000L, 0xFF00000000L, 0xFF0000000000L, 0xFF000000000000L, 0xFF00000000000000L
            };
    private static long FileMask[] =
            {
                    0x101010101010101L, 0x202020202020202L, 0x404040404040404L, 0x808080808080808L,
                    0x1010101010101010L, 0x2020202020202020L, 0x4040404040404040L, 0x8080808080808080L
            };
    private static long DiagonalMask[] =
            {
                    0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L,
                    0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L,
                    0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L
            };
    private static long AntiDiagonalMask[] =
            {
                    0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L,
                    0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L,
                    0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L
            };

    public static long HVMoves(int s, long o) {
        long shift = 1L << s;
        long hMoves = (((o&RankMask[s/8])-2*shift)^Long.reverse((Long.reverse(o&RankMask[s/8])-2*Long.reverse(shift))))&RankMask[s/8];
        long vMoves = (((o&FileMask[s%8])-2*shift)^Long.reverse((Long.reverse(o&FileMask[s%8])-2*Long.reverse(shift))))&FileMask[s%8];
        return hMoves|vMoves;
    }

    public static  long DMoves(int s, long o) {
        long shift = 1L << s;
        long dMoves = ((o&DiagonalMask[(s/8)+(s%8)])-(2*shift))^Long.reverse(Long.reverse(o&DiagonalMask[(s/8)+(s%8)])-(2*Long.reverse(shift)));
        long adMoves = ((o&AntiDiagonalMask[(s/8)+7-(s%8)])-(2*shift))^Long.reverse(Long.reverse(o&AntiDiagonalMask[(s/8)+7-(s%8)])-(2*Long.reverse(shift)));
        return (dMoves&DiagonalMask[(s/8)+(s%8)])|(adMoves&AntiDiagonalMask[(s/8)+7-(s%8)]);
    }

    private static String getWhiteMoves(boolean Left, boolean Right, long EP, long Queens, long King, long Rooks, long Bishops, long Knights, long Pawns, long o, long w, long b){
        String moves = "";

        //knights
        int m = Long.numberOfTrailingZeros(Knights);

        while(Knights!=0L) {
            long bb;
            if (m>18)
            {
                bb =KNIGHT_SPAN<<(m-18);
            }
            else {
                bb =KNIGHT_SPAN>>(18-m);
            }
            if (m % 8 < 4)
            {
                bb &=~FILE_GH&~w;
            }
            else {
                bb &=~FILE_AB&~w;
            }

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Knights &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Knights);
        }
        //bishop queen
        Bishops |= Queens;
        m = Long.numberOfTrailingZeros(Bishops);
        while(Bishops!=0L) {
            long bb = DMoves(m,o)&~w;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Bishops &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Bishops);
        }
        //rook queen
        Rooks |= Queens;
        m = Long.numberOfTrailingZeros(Rooks);

        while(Rooks!=0) {
            long bb = HVMoves(m,o)&~w;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Rooks &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Rooks);
        }

        //king
        m = Long.numberOfTrailingZeros(King);

        while(King!=0L) {
            long bb = 0L;
            if (m>9)
            {
                bb=KING_SPAN<<(m-9);
            }
            else {
                bb=KING_SPAN>>(9-m);
            }
            if (m%8<4)
            {
                bb &=~FILE_GH&~w;
            }
            else {
                bb &=~FILE_AB&~w;
            }

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            King &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(King);
        }
        if(Right && (BottomRightCastle&o)==0L) {
            moves += ((63-3)+" "+(99)+" ");
        }
        if(Left && (BottomLeftCastle&o)==0L) {
            moves += ((63-3)+" "+(98)+" ");
        }
        //pawns

        m = Long.numberOfTrailingZeros(Pawns);
        while(Pawns!=0L) {
            long bb = 0L;

            bb|= (1L<<(m+8))&~(o);
            if(m/8 == 1 && bb != 0L) {
                bb|= ((1L<<(m+16))&~o);
            }
            if(m%8 > 0) {
                bb |= (1L<<(m+7))&(b);
                if(m > 31) {
                    bb |= (1L<<(m+7))&EP; //en passant
                }
            }
            if(m%8 < 7) {
                bb |= (1L<<(m+9))&(b);
                if(m > 31) {
                    bb |= (1L<<(m+9))&EP; //en passant
                }
            }
            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                if(k > 55) {
                    moves+= (63-m)+" 10"+(63-k)+" " +(63-m)+" 20"+(63-k)+" " +(63-m)+" 30"+(63-k)+" " +(63-m)+" 40"+(63-k)+" ";
                }
                else {
                    moves+= ((63-m)+" "+(63-k)+" ");
                }
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Pawns &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Pawns);
        }
        return moves;
    }

    private static String getBlackMoves(boolean Left, boolean Right, long EP, long Queens, long King, long Rooks, long Bishops, long Knights, long Pawns, long o, long w, long b) {
        String moves = "";
        //knights
        int m = Long.numberOfTrailingZeros(Knights);

        while(Knights!=0L) {
            long bb = 0L;
            if (m>18)
            {
                bb =KNIGHT_SPAN<<(m-18);
            }
            else {
                bb =KNIGHT_SPAN>>(18-m);
            }
            if (m%8<4)
            {
                bb &=~FILE_GH&~b;
            }
            else {
                bb &=~FILE_AB&~b;
            }

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Knights &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Knights);
        }
        //bishop queen
        Bishops |= Queens;
        m = Long.numberOfTrailingZeros(Bishops);
        while(Bishops!=0L) {
            long bb = DMoves(m,o)&~b;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Bishops &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Bishops);
        }
        //rook queen
        Rooks |= Queens;
        m = Long.numberOfTrailingZeros(Rooks);

        while(Rooks!=0) {
            long bb = HVMoves(m,o)&~b;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Rooks &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Rooks);
        }

        //king
        //printBitBoard(King);
        m = Long.numberOfTrailingZeros(King);

        while(King!=0L) {
            long bb = 0L;
            if (m>9)
            {
                bb=KING_SPAN<<(m-9);
            }
            else {
                bb=KING_SPAN>>(9-m);
            }
            if (m%8<4){
                bb &=~FILE_GH&~b;
            }
            else {
                bb &=~FILE_AB&~b;
            }

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            King &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(King);
        }
        if(Right && (TopRightCastle&o)==0L) {
            moves += ((63-59)+" "+(97)+" ");
        }
        if(Left && (TopLeftCastle&o)==0L) {
            moves += ((63-59)+" "+(96)+" ");
        }
        m = Long.numberOfTrailingZeros(Pawns);

        while(Pawns!=0L) {
            long bb = 0L;

            bb|= (1L<<(m-8))&~(o);
            if(m/8 == 6 && bb != 0L) {
                bb|= ((1L<<(m-16))&~o);
            }
            if(m%8 < 7) {
                bb |= (1L<<(m-7))&(w);
                if(m <32) {
                    bb |= (1L<<(m-7))&EP;  // en passant
                }
            }
            if(m%8 > 0) {
                bb |= (1L<<(m-9))&(w);
                if(m <32) {
                    bb |= (1L<<(m-9))&EP;  // en passant
                }
            }

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                if(k < 8) {
                    moves+= (63-m)+" 5"+(63-k)+" " +(63-m)+" 6"+(63-k)+" " +(63-m)+" 7"+(63-k)+" " +(63-m)+" 8"+(63-k)+" ";
                }
                else {
                    moves+= ((63-m)+" "+(63-k)+" ");
                }
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Pawns &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Pawns);
        }
        return moves;
    }

    public static ArrayList<Move> getAllMoves(boolean TLeft, boolean TRight, boolean BLeft, boolean BRight, long EP, long WQ, long WK, long WR, long WB, long WN, long WP, long BQ, long BK, long BR, long BB, long BN, long BP, long O, long W, long B, boolean turn) {
        ArrayList<Move> M = new ArrayList<Move>();
        long piecebb, movebb;
        String Smoves;
        if(turn) {
            Smoves = getWhiteMoves(BLeft, BRight, EP,WQ,WK,WR,WB,WN,WP,O,W,B);
            String[] SmoveA = Smoves.split(" ");

            for(int i = 0; i < SmoveA.length/2; i++) {
                int piece = Integer.parseInt(SmoveA[2*i]);
                int move = Integer.parseInt(SmoveA[2*i + 1]);

                if(move > 64) {
                    if(move <100) {
                        long WQt = WQ, WKt = WK, WRt = WR, WBt = WB, WNt = WN, WPt = WP;
                        long BQt = BQ, BKt = BK, BRt = BR, BBt = BB, BNt = BN, BPt = BP;
                        long Wt = 0L;
                        long Bt = 0L;
                        if(move == 99) {

                            WKt = 7L<<1;
                            WRt |= 1L<<2;
                            WRt &= ~(1L);
                            //printBitBoard(WKt);
                        }
                        if(move == 98) {
                            WKt = 7L<<3;
                            WRt |= 1L<<4;
                            WRt &= ~(1L<<7);
                        }
                        Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                        Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                        long Ot = Bt|Wt;

                        if(UnSafeWhite(BQt, BKt, BBt, BNt, BRt, BPt, Ot, WKt)) {
                            M.add(new Move(piece, move));
                        }
                        continue;
                    }
                    long BBt = ~(1L<<(63-move%100))&BB; long BQt = ~(1L<<(63-move%100))&BQ; long BNt = ~(1L<<(63-move%100))&BN; long BRt = ~(1L<<(63-move%100))&BR;
                    long WBt = ~(1L<<(63-move%100))&WB; long WQt = ~(1L<<(63-move%100))&WQ; long WNt = ~(1L<<(63-move%100))&WN; long WRt = ~(1L<<(63-move%100))&WR;
                    long BKt = BK, WKt = WK;
                    long WPt = WP; long BPt = BP;
                    if(move > 800) {
                        BQt |= 1L<<(63-(move-800));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 700) {
                        BRt |= 1L<<(63-(move%100));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 600) {
                        BBt |= 1L<<(63-(move%100));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 500) {
                        BNt |= 1L<<(63-(move%100));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 400) {
                        WQt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 300) {
                        WRt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 200) {
                        WBt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 100) {
                        WNt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                    long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                    long Ot = Bt|Wt;
                    if(UnSafeWhite(BQt, BKt, BBt, BNt, BRt, BPt, Ot, WKt)) {
                        M.add(new Move(piece, move));
                    }
                    continue;
                }

                piecebb = 1L<<(63-piece);
                movebb = 1L<<(63-move);




                long WQt = MakeMove(WQ, piecebb, movebb);
                long WKt = MakeMove(WK, piecebb, movebb);
                long WRt = MakeMove(WR, piecebb, movebb);
                long WBt = MakeMove(WB, piecebb, movebb);
                long WNt = MakeMove(WN, piecebb, movebb);
                long WPt = MakeMove(WP, piecebb, movebb);

                long BQt = MakeMove(BQ, piecebb, movebb);
                long BKt = MakeMove(BK, piecebb, movebb);
                long BRt = MakeMove(BR, piecebb, movebb);
                long BBt = MakeMove(BB, piecebb, movebb);
                long BNt = MakeMove(BN, piecebb, movebb);
                long BPt = MakeMove(BP, piecebb, movebb);

                if(EP == movebb) {
                    if((piecebb&WP) != 0) {
                        BPt &= ~(1L<<(63-move)-8);
                    }
                    else if((piecebb&BP) != 0) {
                        WPt &= ~(1L<<(63-move)+8);
                    }
                }

                long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                long Ot = Bt|Wt;

                if(UnSafeWhite(BQt, BKt, BBt, BNt, BRt, BPt, Ot, WKt)) {
                    M.add(new Move(piece, move));
                }
            }
            return M;

        }
        else {
            Smoves = getBlackMoves(TLeft, TRight, EP,BQ,BK,BR,BB,BN,BP,O,W,B);
            String[] SmoveB = Smoves.split(" ");

            for(int i = 0; i < SmoveB.length/2; i++) {
                int piece2 = Integer.parseInt(SmoveB[2*i]);
                int move2 = Integer.parseInt(SmoveB[2*i + 1]);

                if(move2 > 64) {
                    if(move2 <100) {
                        long WQt = WQ, WKt = WK, WRt = WR, WBt = WB, WNt = WN, WPt = WP;
                        long BQt = BQ, BKt = BK, BRt = BR, BBt = BB, BNt = BN, BPt = BP;
                        long Wt = 0L;
                        long Bt = 0L;
                        if(move2 == 97) {
                            BKt = 7L<<57;
                            BRt |= 1L<<2;
                            BRt &= ~(1L<<56);
                        }
                        if(move2 == 96) {
                            BKt = 7L<<59;
                            BRt |= 1L<<60;
                            BRt &= ~(1L<<63);
                        }
                        Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                        Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                        long Ot = Bt|Wt;
                        if(UnSafeBlack(WQt, WKt, WBt, WNt, WRt, WPt, Ot, BKt)) {
                            M.add(new Move(piece2, move2));
                        }
                        continue;
                    }
                    long BBt = ~(1L<<(63-move2%100))&BB; long BQt = ~(1L<<(63-move2%100))&BQ; long BNt = ~(1L<<(63-move2%100))&BN; long BRt = ~(1L<<(63-move2%100))&BR;
                    long WBt = ~(1L<<(63-move2%100))&WB; long WQt = ~(1L<<(63-move2%100))&WQ; long WNt = ~(1L<<(63-move2%100))&WN; long WRt = ~(1L<<(63-move2%100))&WR;
                    long BKt = BK, WKt = WK;
                    long WPt = WP; long BPt = BP;
                    if(move2 > 800) {
                        BQt |= 1L<<(63-(move2-800));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 700) {
                        BRt |= 1L<<(63-(move2-700));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 600) {
                        BBt |= 1L<<(63-(move2-600));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 500) {
                        BNt |= 1L<<(63-(move2-500));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 400) {
                        WQt |= 1L<<(63-(move2-400));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 300) {
                        WRt |= 1L<<(63-(move2-300));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 200) {
                        WBt |= 1L<<(63-(move2-200));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 100) {
                        WNt |= 1L<<(63-(move2-100));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                    long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                    long Ot = Bt|Wt;
                    if(UnSafeBlack(WQt, WKt, WBt, WNt, WRt, WPt, Ot, BKt)) {
                        M.add(new Move(piece2, move2));
                    }
                    continue;
                }
                piecebb = 1L<<(63-piece2);
                movebb = 1L<<(63-move2);




                long WQt = MakeMove(WQ, piecebb, movebb);
                long WKt = MakeMove(WK, piecebb, movebb);
                long WRt = MakeMove(WR, piecebb, movebb);
                long WBt = MakeMove(WB, piecebb, movebb);
                long WNt = MakeMove(WN, piecebb, movebb);
                long WPt = MakeMove(WP, piecebb, movebb);

                long BQt = MakeMove(BQ, piecebb, movebb);
                long BKt = MakeMove(BK, piecebb, movebb);
                long BRt = MakeMove(BR, piecebb, movebb);
                long BBt = MakeMove(BB, piecebb, movebb);
                long BNt = MakeMove(BN, piecebb, movebb);
                long BPt = MakeMove(BP, piecebb, movebb);

                if(EP == movebb) {

                    if((piecebb&WP) != 0) {
                        BPt &= ~(1L<<(63-move2)-8);
                    }
                    else if((piecebb&BP) != 0) {
                        WPt &= ~(1L<<(63-move2)+8);
                    }

                }

                long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                long Ot = Bt|Wt;



                if(UnSafeBlack(WQt, WKt, WBt, WNt, WRt, WPt, Ot, BKt)) {
                    M.add(new Move(piece2, move2));
                }
            }
            return M;
        }

    }

    public static long MakeMove(long board, long piece, long move) {

        if((board&piece) != 0L){
            board &= ~piece;
            board |= move;
        }
        else if((board&move) != 0L) {
            board &= ~move;
        }
        return board;
    }

    public static void printBitBoard(long num) {
        String bin = Long.toBinaryString(num);
        while(bin.length() < 64) {
            bin = "0" + bin;
        }
        char[] a = bin.toCharArray();
        System.out.print("[");
        for(int i = 0; i < a.length; i++) {
            //System.out.print(i);
            if(i % 8 != 0 || i == 0) {
                System.out.print(a[i]);
                if((i-7)%8 != 0) {
                    System.out.print(", ");
                }
            }
            else if(i % 8 == 0 && i !=0){
                if(i != 63) {
                    System.out.print("]\n[" + a[i] + ", ");
                }
                else {
                    System.out.print("]" +a[i]+", ");
                }
            }
        }
        System.out.print("]");


    }

    public static boolean UnSafeBlack(long WQ, long WK, long WB, long WN, long WR, long WP, long O, long BK) {
        long unsafe = 0L;
        long possibility;

        //bishop/queen
        long QB=WQ|WB;
        long i=QB&~(QB-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=DMoves(iLocation,O);
            unsafe |= possibility;
            QB&=~i;
            i=QB&~(QB-1);
        }

        //rook/queen
        long QR=WQ|WR;
        i=QR&~(QR-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=HVMoves(iLocation,O);
            unsafe |= possibility;
            QR&=~i;
            i=QR&~(QR-1);
        }

        //knight
        long A = WN;
        i=A&~(A-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            if (iLocation>18)
            {
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if (iLocation%8<4)
            {
                possibility &=~FILE_GH;
            }
            else {
                possibility &=~FILE_AB;
            }
            unsafe |= possibility;
            A&=~i;
            i=A&~(A-1);
        }
        if((unsafe&BK) != 0L) {
            return false;
        }

        //pawn
        unsafe=((WP<<9)&~FILE_A);//pawn capture right
        unsafe|=((WP<<7)&~FILE_H);//pawn capture left

        //king
        long WK2 = WK;
        int iLocation=Long.numberOfTrailingZeros(WK2);
        if (iLocation>9)
        {
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        if (iLocation%8<4)
        {
            possibility &=~FILE_GH;
        }
        else {
            possibility &=~FILE_AB;
        }
        unsafe |= possibility;
        if((unsafe&BK) != 0L) {
            return false;
        }
        return true;
    }

    public static boolean UnSafeWhite(long BQ, long BK, long BB, long BN, long BR, long BP, long O, long WK) {
        long unsafe = 0L;
        long possibility;

        //bishop/queen
        long QB=BQ|BB;
        long i=QB&~(QB-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=DMoves(iLocation,O);
            unsafe |= possibility;
            QB&=~i;
            i=QB&~(QB-1);
        }

        //rook/queen
        long QR=BQ|BR;
        i=QR&~(QR-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            possibility=HVMoves(iLocation,O);
            unsafe |= possibility;
            QR&=~i;
            i=QR&~(QR-1);
        }


        //knight
        long A = BN;
        i=A&~(A-1);
        while(i != 0)
        {
            int iLocation=Long.numberOfTrailingZeros(i);
            if (iLocation>18)
            {
                possibility=KNIGHT_SPAN<<(iLocation-18);
            }
            else {
                possibility=KNIGHT_SPAN>>(18-iLocation);
            }
            if (iLocation%8<4)
            {
                possibility &=~FILE_GH;
            }
            else {
                possibility &=~FILE_AB;
            }
            unsafe |= possibility;
            A&=~i;
            i=A&~(A-1);
        }
        if((unsafe&WK) != 0L) {
            return false;
        }
        //pawn
        unsafe=((BP>>>9)&~FILE_H);//pawn capture right
        unsafe|=((BP>>>7)&~FILE_A);//pawn capture left

        //king
        long WK2 = BK;
        int iLocation=Long.numberOfTrailingZeros(WK2);
        if (iLocation>9)
        {
            possibility=KING_SPAN<<(iLocation-9);
        }
        else {
            possibility=KING_SPAN>>(9-iLocation);
        }
        if (iLocation%8<4)
        {
            possibility &=~FILE_GH;
        }
        else {
            possibility &=~FILE_AB;
        }
        unsafe |= possibility;
        if((unsafe&WK) != 0L) {
            return false;
        }
        return true;
    }

    public static int[] cbb(long bb) {

        int[] a = new int[Long.bitCount(bb)];
        int m = Long.numberOfTrailingZeros(bb);
        int i = 0;
        while(bb!=0L) {

            a[i] = 63-m;
            bb &= ~(1L<<m);
            i++;
            m = Long.numberOfTrailingZeros(bb);

        }
        return a;
    }

    private static String getWhiteCaptures(long EP, long Queens, long Rooks, long Bishops, long Knights, long Pawns, long o, long w, long b){
        String moves = "";
        //knights
        int m = Long.numberOfTrailingZeros(Knights);

        while(Knights!=0L) {
            long bb = 0L;
            if (m>18)
            {
                bb =KNIGHT_SPAN<<(m-18);
            }
            else {
                bb =KNIGHT_SPAN>>(18-m);
            }
            if (m%8<4)
            {
                bb &=~FILE_GH&~w;
            }
            else {
                bb &=~FILE_AB&~w;
            }
            bb &= b;
            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Knights &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Knights);
        }
        //bishop queen
        Bishops |= Queens;
        m = Long.numberOfTrailingZeros(Bishops);
        while(Bishops!=0L) {
            long bb = DMoves(m,o)&b;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Bishops &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Bishops);
        }
        //rook queen
        Rooks |= Queens;
        m = Long.numberOfTrailingZeros(Rooks);

        while(Rooks!=0) {
            long bb = HVMoves(m,o)&b;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Rooks &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Rooks);
        }

        //pawns

        m = Long.numberOfTrailingZeros(Pawns);
        while(Pawns!=0L) {
            long bb = 0L;

            if(m%8 > 0) {
                bb |= (1L<<(m+7))&(b);
                if(m > 31) {
                    bb |= (1L<<(m+7))&EP; //en passant
                }
            }
            if(m%8 < 7) {
                bb |= (1L<<(m+9))&(b);
                if(m > 31) {
                    bb |= (1L<<(m+9))&EP; //en passant
                }
            }
            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                if(k > 55) {
                    moves+= (63-m)+" 10"+(63-k)+" " +(63-m)+" 20"+(63-k)+" " +(63-m)+" 30"+(63-k)+" " +(63-m)+" 40"+(63-k)+" ";
                }
                else {
                    moves+= ((63-m)+" "+(63-k)+" ");
                }
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Pawns &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Pawns);
        }
        return moves;
    }

    private static String getBlackCaptures(long EP, long Queens, long Rooks, long Bishops, long Knights, long Pawns, long o, long w, long b) {
        String moves = "";
        //knights
        int m = Long.numberOfTrailingZeros(Knights);

        while(Knights!=0L) {
            long bb = 0L;
            if (m>18)
            {
                bb =KNIGHT_SPAN<<(m-18);
            }
            else {
                bb =KNIGHT_SPAN>>(18-m);
            }
            if (m%8<4)
            {
                bb &=~FILE_GH&~b;
            }
            else {
                bb &=~FILE_AB&~b;
            }
            bb &= w;
            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Knights &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Knights);
        }
        //bishop queen
        Bishops |= Queens;
        m = Long.numberOfTrailingZeros(Bishops);
        while(Bishops!=0L) {
            long bb = DMoves(m,o)&w;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Bishops &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Bishops);
        }
        //rook queen
        Rooks |= Queens;
        m = Long.numberOfTrailingZeros(Rooks);

        while(Rooks!=0) {
            long bb = HVMoves(m,o)&w;

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                moves+= ((63-m)+" "+(63-k)+" ");
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }

            Rooks &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Rooks);
        }


        m = Long.numberOfTrailingZeros(Pawns);

        while(Pawns!=0L) {
            long bb = 0L;

            if(m%8 < 7) {
                bb |= (1L<<(m-7))&(w);
                if(m <32) {
                    bb |= (1L<<(m-7))&EP;  // en passant
                }
            }
            if(m%8 > 0) {
                bb |= (1L<<(m-9))&(w);
                if(m <32) {
                    bb |= (1L<<(m-9))&EP;  // en passant
                }
            }

            int k = Long.numberOfTrailingZeros(bb);
            while(bb!=0L) {
                if(k < 9) {
                    moves+= (63-m)+" 5"+(63-k)+" " +(63-m)+" 6"+(63-k)+" " +(63-m)+" 7"+(63-k)+" " +(63-m)+" 8"+(63-k)+" ";
                }
                else {
                    moves+= ((63-m)+" "+(63-k)+" ");
                }
                bb &= ~(1L<<k);
                k = Long.numberOfTrailingZeros(bb);
            }
            Pawns &= ~(1L<<m);
            m = Long.numberOfTrailingZeros(Pawns);
        }
        return moves;
    }

    public static ArrayList<Move> getAllCaptures(long EP, long WQ, long WK, long WR, long WB, long WN, long WP, long BQ, long BK, long BR, long BB, long BN, long BP, long O, long W, long B, boolean turn) {
        ArrayList<Move> M = new ArrayList<>();
        long piecebb, movebb;
        String Smoves;
        if(turn) {
            Smoves = getWhiteCaptures(EP,WQ,WR,WB,WN,WP,O,W,B);
            String[] SmoveA = Smoves.split(" ");

            for(int i = 0; i < SmoveA.length/2; i++) {
                int piece = Integer.parseInt(SmoveA[2*i]);
                int move = Integer.parseInt(SmoveA[2*i + 1]);

                if(move > 64) {
                    long BBt = ~(1L<<(63-move%100))&BB; long BQt = ~(1L<<(63-move%100))&BQ; long BNt = ~(1L<<(63-move%100))&BN; long BRt = ~(1L<<(63-move%100))&BR;
                    long WBt = ~(1L<<(63-move%100))&WB; long WQt = ~(1L<<(63-move%100))&WQ; long WNt = ~(1L<<(63-move%100))&WN; long WRt = ~(1L<<(63-move%100))&WR;
                    long BKt = BK, WKt = WK;
                    long WPt = WP; long BPt = BP;
                    if(move > 800) {
                        BQt |= 1L<<(63-(move%100));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 700) {
                        BRt |= 1L<<(63-(move%100));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 600) {
                        BBt |= 1L<<(63-(move%100));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 500) {
                        BNt |= 1L<<(63-(move%100));
                        BPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 400) {
                        WQt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 300) {
                        WRt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 200) {
                        WBt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    else if(move > 100) {
                        WNt |= 1L<<(63-(move%100));
                        WPt &= ~(1L<<(63-piece));
                    }
                    long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                    long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                    long Ot = Bt|Wt;
                    if(UnSafeWhite(BQt, BKt, BBt, BNt, BRt, BPt, Ot, WKt)) {
                        M.add(new Move(piece, move));
                    }
                    continue;
                }

                piecebb = 1L<<(63-piece);
                movebb = 1L<<(63-move);




                long WQt = MakeMove(WQ, piecebb, movebb);
                long WKt = MakeMove(WK, piecebb, movebb);
                long WRt = MakeMove(WR, piecebb, movebb);
                long WBt = MakeMove(WB, piecebb, movebb);
                long WNt = MakeMove(WN, piecebb, movebb);
                long WPt = MakeMove(WP, piecebb, movebb);

                long BQt = MakeMove(BQ, piecebb, movebb);
                long BKt = MakeMove(BK, piecebb, movebb);
                long BRt = MakeMove(BR, piecebb, movebb);
                long BBt = MakeMove(BB, piecebb, movebb);
                long BNt = MakeMove(BN, piecebb, movebb);
                long BPt = MakeMove(BP, piecebb, movebb);

                if(EP == movebb) {
                    if((piecebb&WP) != 0) {
                        BPt &= ~(1L<<(63-move)-8);
                    }
                    else if((piecebb&BP) != 0) {
                        WPt &= ~(1L<<(63-move)+8);
                    }
                }

                long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                long Ot = Bt|Wt;

                if(UnSafeWhite(BQt, BKt, BBt, BNt, BRt, BPt, Ot, WKt)) {
                    M.add(new Move(piece, move));
                }
            }
            return M;

        }
        else {
            Smoves = getBlackCaptures(EP,BQ,BR,BB,BN,BP,O,W,B);
            String[] SmoveB = Smoves.split(" ");

            for(int i = 0; i < SmoveB.length/2; i++) {
                int piece2 = Integer.parseInt(SmoveB[2*i]);
                int move2 = Integer.parseInt(SmoveB[2*i + 1]);

                if(move2 > 64) {

                    long BBt = ~(1L<<(63-move2%100))&BB; long BQt = ~(1L<<(63-move2%100))&BQ; long BNt = ~(1L<<(63-move2%100))&BN; long BRt = ~(1L<<(63-move2%100))&BR;
                    long WBt = ~(1L<<(63-move2%100))&WB; long WQt = ~(1L<<(63-move2%100))&WQ; long WNt = ~(1L<<(63-move2%100))&WN; long WRt = ~(1L<<(63-move2%100))&WR;
                    long BKt = BK, WKt = WK;
                    long WPt = WP; long BPt = BP;
                    if(move2 > 800) {
                        BQt |= 1L<<(63-(move2-800));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 700) {
                        BRt |= 1L<<(63-(move2-700));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 600) {
                        BBt |= 1L<<(63-(move2-600));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 500) {
                        BNt |= 1L<<(63-(move2-500));
                        BPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 400) {
                        WQt |= 1L<<(63-(move2-400));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 300) {
                        WRt |= 1L<<(63-(move2-300));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 200) {
                        WBt |= 1L<<(63-(move2-200));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    else if(move2 > 100) {
                        WNt |= 1L<<(63-(move2-100));
                        WPt &= ~(1L<<(63-piece2));
                    }
                    long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                    long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                    long Ot = Bt|Wt;
                    if(UnSafeBlack(WQt, WKt, WBt, WNt, WRt, WPt, Ot, BKt)) {
                        M.add(new Move(piece2, move2));
                    }
                    continue;
                }
                piecebb = 1L<<(63-piece2);
                movebb = 1L<<(63-move2);




                long WQt = MakeMove(WQ, piecebb, movebb);
                long WKt = MakeMove(WK, piecebb, movebb);
                long WRt = MakeMove(WR, piecebb, movebb);
                long WBt = MakeMove(WB, piecebb, movebb);
                long WNt = MakeMove(WN, piecebb, movebb);
                long WPt = MakeMove(WP, piecebb, movebb);

                long BQt = MakeMove(BQ, piecebb, movebb);
                long BKt = MakeMove(BK, piecebb, movebb);
                long BRt = MakeMove(BR, piecebb, movebb);
                long BBt = MakeMove(BB, piecebb, movebb);
                long BNt = MakeMove(BN, piecebb, movebb);
                long BPt = MakeMove(BP, piecebb, movebb);

                if(EP == movebb) {

                    if((piecebb&WP) != 0) {
                        BPt &= ~(1L<<(63-move2)-8);
                    }
                    else if((piecebb&BP) != 0) {
                        WPt &= ~(1L<<(63-move2)+8);
                    }

                }

                long Wt = WQt|WKt|WRt|WBt|WNt|WPt;
                long Bt = BQt|BKt|BRt|BBt|BNt|BPt;
                long Ot = Bt|Wt;



                if(UnSafeBlack(WQt, WKt, WBt, WNt, WRt, WPt, Ot, BKt)) {
                    M.add(new Move(piece2, move2));
                }
            }
            return M;
        }

    }


    public static long KnMoves(int s) {
        long bb = 0L;
        if (s>18)
        {
            bb =KNIGHT_SPAN<<(s-18);
        }
        else {
            bb =KNIGHT_SPAN>>(18-s);
        }
        if (s%8<4)
        {
            bb &=~FILE_GH;
        }
        else {
            bb &=~FILE_AB;
        }
        return bb;
    }

    public static long KingMoves(int s) {
        long bb = 0L;
        if (s>9)
        {
            bb=KING_SPAN<<(s-9);
        }
        else {
            bb=KING_SPAN>>(9-s);
        }
        if (s%8<4)
        {
            bb &=~FILE_GH;
        }
        else {
            bb &=~FILE_AB;
        }
        return bb;
    }

    static int WHITEPAWN[] = {
            900,900,900,900,900,900,900,900,
            350,350,350,350,350,350,350,350,
            5,  5,  5,  5,  5,  5,  5,  5,
            0,  0,  0, 30, 30,  0,  0,  0,
            5,  5, 30, 30, 30, 15,  5,  5,
            10, 10, 25, 15, 15, 25, 10, 10,
            20, 20, 25, -15,-15, 20, 20, 20,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    static int WHITEPAWNLEFTCASTLE[] = {
            900,900,900,900,900,900,900,900,
            350,350,350,350,350,350,350,350,
            5,  5,  5,  5,  5,  5,  5,  5,
            0,  0,  0, 30, 30, 20, 20, 20,
            0,  0,  0, 30, 30, 30, 30, 30,
            5, -20,  5, 20, 20, 20, 20, 20,
            20, 20, 35,-30,-30, 0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    static int BLACKPAWNLEFTCASTLE[] = {
            0,  0,  0,  0,  0,  0,  0,  0,
            20, 20, 35,-30,-30, 0,  0,  0,
            5, -20,  5, 20, 20, 20, 20, 20,
            0,  0, 0,  30, 30, 30, 30, 30,
            0,  0, 0,  30,  30, 20, 20, 20,
            5,  5,  5,  5,  5,  5,  5,  5,
            350,350,350,350,350,350,350,350,
            900,900,900,900,900,900,900,900,
    };

    static int WHITEPAWNRIGHTCASTLE[] = {
            900,900,900,900,900,900,900,900,
            350,350,350,350,350,350,350,350,
            5,  5,  5,  5,  5,  5,  5,  5,
            20, 20, 20, 30, 30,  0,  0,  0,
            30, 30, 30, 30, 30,  0,  0,  0,
            20, 20, 20, 20, 20,  5, -20,  5,
            0,  0,  0, -30, -30, 25, 20, 20,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    static int BLACKPAWNRIGHTCASTLE[] = {
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0, -30, -30, 25, 20, 20,
            20, 20, 20, 30, 30,  5, -20,  5,
            30, 30, 30, 30, 30,  0,  0,  0,
            20, 20, 20, 20, 20,  0,  0,  0,
            5,  5,  5,  5,  5,  5,  5,  5,
            350,350,350,350,350,350,350,350,
            900,900,900,900,900,900,900,900,
    };


    static int BLACKPAWN[] = {
            0,  0,  0,  0,  0,  0,  0,  0,
            20, 20, 25, -15,-15, 20, 20, 20,
            10, 10, 25, 15, 15, 25, 10, 10,
            5,  5, 30, 30, 30, 30,  5,  5,
            0,  0,  0, 30, 30,  0,  0,  0,
            50, 50, 50, 50, 50, 50, 50, 50,
            350,350,350,350,350,350,350,350,
            900,900,900,900,900,900,900,900,
    };

    static int BLACKPAWNEND[] = {
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0,
            5,  5,  5,  5,  5,  5,  5,  5,
            20, 20, 20, 20, 20, 20, 20, 20,
            50, 50, 50, 50, 50, 50, 50, 50,
            200,200,200,200,200,200, 200, 200,
            350,350,350,350,350,350,350,350,
            900,900,900,900,900,900,900,900,
    };

    static int WHITEPAWNEND[] = {
            900,900,900,900,900,900,900,900,
            350,350,350,350,350,350,350,350,
            200,200,200,200,200,200, 200, 200,
            50, 50, 50, 50, 50, 50, 50, 50,
            20, 20, 20, 20, 20, 20, 20, 20,
            5,  5,  5,  5,  5,  5,  5,  5,
            0,  0,  0,  0,  0,  0,  0,  0,
            0,  0,  0,  0,  0,  0,  0,  0
    };

    static int BISHOP[] = {
            -15,-10,-10,-10,-10,-10,-10,-15,
            -10, 10,  5, 10,  10,  5, 10,-10,
            -10,  5, 10,  5,  5, 10,  5,-10,
            -10,  5, 10, 10, 10, 10,  5,-10,
            -10,  5, 10, 10, 10, 10,  5,-10,
            -10,  5, 10,  5,  5, 10,  5,-10,
            -10, 10,  5, 10, 10,  5, 10,-10,
            -15,-10,-10,-10,-10,-10,-10,-15
    };

    static int KNIGHT[] = {
            -25,-15,-10,-10,-10,-10,-15,-25,
            -15,-20,  0,  7,  7,  0,-20,-15,
            -10,  0,  8, 10, 10,  8,  0,-10,
            -10,  5, 10,  5,  5, 10,  5,-10,
            -10,  5, 10,  5,  5, 10,  5,-10,
            -10,  0,  8, 10, 10,  8,  0,-10,
            -15,-20,  0,  7,  7,  0,-20,-15,
            -25,-15,-10,-10,-10,-10,-15, -25
    };

    static int KINGEND[] = {
            -25,-15,-10,-10,-10,-10,-15,-25,
            -15,-20,  0,  0,  0,  0,-20,-15,
            -10,  5,  8, 10, 10,  8,  5,-10,
            -10,  5, 10, 20, 20, 10,  5,-10,
            -10,  5, 10, 20, 20, 10,  5,-10,
            -10,  5,  8, 10, 10,  8,  5,-10,
            -15,-20,  0,  0,  0,  0,-20,-15,
            -25,-15,-10,-10,-10,-10,-15, -25
    };

    static int WHITEKING[] = {
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            20, 20, 10,  0,  0, 10, 20, 20,
            50, 60, 50,  0,  0, 0,  60, 50
    };

    static int BLACKKING[] = {
            50, 60, 50,  0,  0, 0 , 60, 50,
            20, 20, 10,  0,  0, 10, 20, 20,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
            -10,-10,-20,-30,-30,-20,-10,-10,
    };

    static int SafetyTable[] = {
            0,  0,   1,   2,   3,   5,   7,   9,  12,  15,
            18,  22,  26,  30,  35,  39,  44,  50,  56,  62,
            68,  75,  82,  85,  89,  97, 105, 113, 122, 131,
            140, 150, 169, 180, 191, 202, 213, 225, 237, 248,
            260, 272, 283, 295, 307, 319, 330, 342, 354, 366,
            377, 389, 401, 412, 424, 436, 448, 459, 471, 483,
            494, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
            500, 500, 500, 500, 500, 500, 500, 500, 500, 500
    };

    static long PIF_WR = 30182672128L;
    static long PIF_WL = 30182672128L<<5;
    static long PIF_BR = 30182672128L<<16;
    static long PIF_BL = 30182672128L<<21;
    static long file = 72340172838076673L;

}
