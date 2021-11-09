package piece;

import position.Position;

public class PieceService {

    private static PieceService instance;

    private PieceService(){}

    public static PieceService getInstance() {
        if (instance == null) {
            instance = new PieceService();
        }
        return instance;
    }

    public void makeAMove(Piece piece, Position newPos) {
        piece.setPosition(newPos);
        piece.setFirstMove(false);
    }

}
