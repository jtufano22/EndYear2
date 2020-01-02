public class Piece{
    private Block[] blocks = new Block[4];

    public Piece (Block[] b) {
        blocks = b;
    }
    public void rotate() {

    }

    public void moveRight() {
        for(Block block : blocks) {
            block.column++;
        }
    }
    public void moveLeft() {
        for(Block block : blocks) {
            block.column--;
        }
    }
    public void moveDown() {
        for(Block block : blocks) {
            block.row--;
        }
    }
}
