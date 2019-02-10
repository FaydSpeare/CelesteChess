import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class BoardUI extends Canvas {

    private int size;
    private Image piecesImage;

    private String[] board = {
            "BR","BN","BB","BQ","BK","BB","BN","BR",
            "BP","BP","BP","BP","BP","BP","BP","BP",
            "  ","  ","  ","  ","  ","  ","  ","  ",
            "  ","  ","  ","  ","  ","  ","  ","  ",
            "  ","  ","  ","  ","  ","  ","  ","  ",
            "  ","  ","  ","  ","  ","  ","  ","  ",
            "WP","WP","WP","WP","WP","WP","WP","WP",
            "WR","WN","WB","WQ","WK","WB","WN","WR"
    };

    private String[] board2 = {
            "  ","  ","  ","  ","  ","  ","  ","  ",
            "  ","  ","  ","  ","WP","  ","  ","  ",
            "  ","  ","BK","  ","  ","  ","  ","  ",
            "  ","  ","  ","  ","  ","  ","  ","  ",
            "BQ","  ","  ","  ","  ","  ","  ","  ",
            "  ","  ","  ","  ","  ","  ","  ","  ",
            "  ","  ","  ","  ","  ","WR","  ","  ",
            "  ","  ","  ","  ","WK","  ","  ","  ",
    };

    private ChessGame game;


    BoardUI(int size){
        super(size, size);

        this.size = size;

        game = new ChessGame();
        game.setup(board);

        loadImage();

        renderBoard(Color.WHITE, Color.LIGHTSKYBLUE);
        renderPieces();

        this.setOnMousePressed(this::mousePressed);
        this.setOnMouseDragged(this::mouseDragged);
        this.setOnMouseReleased(this::mouseReleased);

    }

    public void loadImage(){
        piecesImage = new Image("chess-pieces.png");
    }

    private void renderBoard(Color colourOne, Color colourTwo){
        GraphicsContext g = getGraphicsContext2D();
        g.fillRect(0, 0, size, size);

        double tileSize = size / 8;

        int k = 0;

        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){

                if(k % 2 == 0){
                    g.setFill(colourOne);
                } else {
                    g.setFill(colourTwo);
                }
                k++;

                g.fillRect(i*tileSize, j*tileSize, tileSize, tileSize);
            }
            k++;
        }
    }

    private void renderPieces(){
        GraphicsContext g = getGraphicsContext2D();

        double tSize = size / 8;


        for(int i = 0; i < 64; i++){

            if(i == Dragger.dragStart) continue;
            int sy;
            int sx = 0;

            if(board[i].toCharArray()[0] == 'W'){
                sy = 0;

                if(board[i].toCharArray()[1] == 'Q'){
                    sx = 333;
                }
                else if(board[i].toCharArray()[1] == 'B'){
                    sx = 667;
                }
                else if(board[i].toCharArray()[1] == 'N'){
                    sx = 1000;
                }
                else if(board[i].toCharArray()[1] == 'R'){
                    sx = 1333;
                }
                else if(board[i].toCharArray()[1] == 'P'){
                    sx = 1667;
                }
                g.drawImage(piecesImage, sx, sy, 333, 333, (i%8)*tSize, (i/8)*tSize, tSize, tSize);
            }
            else if(board[i].toCharArray()[0] == 'B'){
                sy = 333;

                if(board[i].toCharArray()[1] == 'Q'){
                    sx = 333;
                }
                else if(board[i].toCharArray()[1] == 'B'){
                    sx = 667;
                }
                else if(board[i].toCharArray()[1] == 'N'){
                    sx = 1000;
                }
                else if(board[i].toCharArray()[1] == 'R'){
                    sx = 1333;
                }
                else if(board[i].toCharArray()[1] == 'P'){
                    sx = 1667;
                }
                g.drawImage(piecesImage, sx, sy, 333, 333, (i%8)*tSize, (i/8)*tSize, tSize, tSize);
            }

        }
    }

    private void renderPiece(String piece, double x, double y){

        GraphicsContext g = getGraphicsContext2D();

        double tSize = size / 8;

        int sy;
        int sx = 0;

        if(piece.toCharArray()[0] == 'W'){
            sy = 0;

            if(piece.toCharArray()[1] == 'Q'){
                sx = 333;
            }
            else if(piece.toCharArray()[1] == 'B'){
                sx = 667;
            }
            else if(piece.toCharArray()[1] == 'N'){
                sx = 1000;
            }
            else if(piece.toCharArray()[1] == 'R'){
                sx = 1333;
            }
            else if(piece.toCharArray()[1] == 'P'){
                sx = 1667;
            }
            g.drawImage(piecesImage, sx, sy, 333, 333, x-tSize/2, y-tSize/2, tSize, tSize);
        }
        else if(piece.toCharArray()[0] == 'B'){
            sy = 333;

            if(piece.toCharArray()[1] == 'Q'){
                sx = 333;
            }
            else if(piece.toCharArray()[1] == 'B'){
                sx = 667;
            }
            else if(piece.toCharArray()[1] == 'N'){
                sx = 1000;
            }
            else if(piece.toCharArray()[1] == 'R'){
                sx = 1333;
            }
            else if(piece.toCharArray()[1] == 'P'){
                sx = 1667;
            }
            g.drawImage(piecesImage, sx, sy, 333, 333, x-tSize/2, y-tSize/2, tSize, tSize);
        }
    }

    private void renderMoveOptions(int pos){
        GraphicsContext g = getGraphicsContext2D();
        double tSize = size/8;

        for(Move move: Dragger.dragOptions){
            if(move.getPiece() == pos){
                g.setFill(Color.SEAGREEN);
                g.fillOval(tSize * (move.getMove() % 8) + tSize/4, tSize * (move.getMove() / 8) + tSize/4,
                        tSize/2, tSize/2);
            }
        }

    }

    private void mousePressed(MouseEvent e){
        double tSize = size/8;
        int pos = (int)(Math.floor(e.getY()/tSize)* 8 + Math.floor(e.getX()/tSize));

        setDragPiece(pos);

    }

    private void mouseDragged(MouseEvent e){
        renderBoard(Color.WHITE, Color.LIGHTSKYBLUE);
        renderPieces();
        renderMoveOptions(Dragger.dragStart);
        renderPiece(Dragger.dragType, e.getX(), e.getY());
    }

    private void mouseReleased(MouseEvent e){
        double tSize = size/8;
        int pos = (int)(Math.floor(e.getY()/tSize)* 8 + Math.floor(e.getX()/tSize));

        if(isMoveValid(pos)){
            placePiece(pos);
        }

        Dragger.reset();
        renderBoard(Color.WHITE, Color.LIGHTSKYBLUE);
        renderPieces();

        makeCelesteMove();
    }

    private void makeCelesteMove(){
        new Thread(() -> {
            game.setup(board);
            MoveTriple move = Celeste.iterativeDeepening(5, new ChessGame(game));
            game.makeMove(move.getPiece(), move.getMove());
            updateBoard(game);
            renderBoard(Color.WHITE, Color.LIGHTSKYBLUE);
            renderPieces();
        }).start();
    }

    private void placePiece(int pos){
        game.makeMoveUI(board[Dragger.dragStart], Dragger.dragStart, pos);
        updateBoard(game);
    }

    private boolean isMoveValid(int pos){
        for(Move move: Dragger.dragOptions){
            if(move.getMove() == pos){
                return true;
            }
        }
        return false;
    }

    private void setDragPiece(int pos){
        if(!board[pos].equals(" ")){
            for(Move move: game.getAllMoves(game.turn)){
                if(pos == move.getPiece()){
                    int m = move.getMove();
                    m = ChessGame.getPos(m);
                    Dragger.dragOptions.add(new Move(move.getPiece(), m));
                }
            }
            Dragger.dragStart = pos;
            Dragger.dragType = board[Dragger.dragStart];
            renderMoveOptions(Dragger.dragStart);
        }
    }

    private boolean contains(int[] a, int b) {
        for(int i = 0; i < a.length; i++) {
            if(a[i] == b) {
                return true;
            }
        }
        return false;
    }

    private void updateBoard(ChessGame game) {
        for(int i = 0; i < 64; i++) {
            if(contains(Moves.cbb(game.WQ), i)) {
                board[i] = "WQ";
            }
            else if(contains(Moves.cbb(game.WK), i)) {
                board[i] = "WK";
            }
            else if(contains(Moves.cbb(game.WR), i)) {
                board[i] = "WR";
            }
            else if(contains(Moves.cbb(game.WB), i)) {
                board[i] = "WB";
            }
            else if(contains(Moves.cbb(game.WN), i)) {
                board[i] = "WN";
            }
            else if(contains(Moves.cbb(game.WP), i)) {
                board[i] = "WP";
            }

            else if(contains(Moves.cbb(game.BQ), i)) {
                board[i] = "BQ";
            }
            else if(contains(Moves.cbb(game.BK), i)) {
                board[i] = "BK";
            }
            else if(contains(Moves.cbb(game.BR), i)) {
                board[i] = "BR";
            }
            else if(contains(Moves.cbb(game.BB), i)) {
                board[i] = "BB";
            }
            else if(contains(Moves.cbb(game.BN), i)) {
                board[i] = "BN";
            }
            else if(contains(Moves.cbb(game.BP), i)) {
                board[i] = "BP";
            }
            else {
                board[i] = "  ";
            }
        }
    }

}
