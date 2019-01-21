import exceptions.BlackWonException;
import exceptions.IllegalAddToColumnException;
import exceptions.RedWonException;

public class Column{
    //maxValue <= 0 results in infinite column size
    //// TODO: 2019-01-15 Why have only 2 colors, change the colors to an array
    int blackInRow = 0, redInRow = 0, maxValue = 6, colCount = 0, winCount = 4;
    int[] column; // red is 1, black is 2 0 is empty

    public Column(int maxValue, int winCount) {
        this.maxValue = maxValue;
        this.winCount = winCount;
        column = new int[maxValue];
    }

    public Column(int blackInRow, int redInRow, int maxValue, int colCount, int winCount, int[] column) {
        this.blackInRow = blackInRow;
        this.redInRow = redInRow;
        this.maxValue = maxValue;
        this.colCount = colCount;
        this.winCount = winCount;
        this.column = column;
    }

    /**
     * Adds a color to the this row.  This will add it to the bottom of the array
     * and return the position it added it to.
     * Throws RedWonException when red has won, BlackWonException when black has one
     * and IllegalAddToColumnException when this column is full
     * @param color
     * @return
     * @throws RedWonException
     * @throws BlackWonException
     * @throws IllegalAddToColumnException
     */
    public int addColor(int color) throws RedWonException, BlackWonException, IllegalAddToColumnException {
        if(colCount+1>maxValue && maxValue>0)
            throw new IllegalAddToColumnException();
        if(colCount!=0 && column[colCount-1]!=color) {
            redInRow=0;
            blackInRow=0;
        }
        if (color == 1) redInRow++;
        else blackInRow++;
        column[colCount]=color;
        colCount++;
        if(redInRow >= winCount)
            throw new RedWonException();
        if(blackInRow >= winCount)
            throw new BlackWonException();
        return colCount-1;
    }

    public void pop() {
        if(colCount--==0) return;
        if(column[colCount]==1)
            redInRow--;
        else if(column[colCount]==2)
            blackInRow--;
        column[colCount]=0;
    }

    /**
     * This method determines if you can play a piece in this column.
     * If the column is full it will return false otherwise true.
     * If maxvalue for the column is less than or equal 0 then i'll
     * assume you want inifinite row height and thus no limit to be set
     * This is if you want to check a row without adding a value because the
     * addColor method will simply throw an exception if you can't add
     * a value there.
     * @return
     */
    public boolean canHaveMorePieces(){
        return !(colCount>=maxValue && maxValue >0);
    }

    @Override public Column clone(){
        return new Column(blackInRow, redInRow, maxValue, colCount, winCount, column.clone());
    }
}