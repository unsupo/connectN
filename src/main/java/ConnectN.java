import exceptions.BlackWonException;
import exceptions.IllegalAddToColumnException;
import exceptions.RedWonException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * This class will create the Connect N object
 * This is based on Connect 4, but this should
 * be able to handle arbitrary sizes and rules
 */
public class ConnectN {

    public static void main(String[] args) throws BlackWonException, IllegalAddToColumnException, RedWonException, InterruptedException {
        /*
        |-|-|R|-|-|-|-|
        |-|-|b|-|-|-|-|
        |-|-|b|-|-|R|-|
        |-|R|b|R|b|b|-|
        |R|R|R|b|R|b|R|
        |b|b|b|R|R|b|R|

        30616205231212422545543-2147483647
         */
//        System.out.println(ConnectNSolver.minimax(new ConnectN("20211533130562641")));
//        System.out.println(ConnectNSolver.minimax(new ConnectN("30216526011666332")));
//        ConnectN c = new ConnectN("32210010160");
//        System.out.println(c);
//        System.out.println(c);
//        System.out.println(ConnectNSolver.minimax(c));
//        System.out.println(c.getHeuristic());
//        playVsComputer();
//        ConnectN c = new ConnectN();
//        System.out.println(ConnectNSolver.minimax(c));
//        randomPlay();
//        ConnectN c = new ConnectN("0535440512505610115016031463343344");
////        ConnectN c = new ConnectN("010100");
//        System.out.println(c);
//        System.out.println(c.getHeuristic());
//        try {
//            c.addColorToColumn(5);
////            for(char cc : "06023616623160316444241365122065".toCharArray())
////                c.addColorToColumn(Integer.parseInt(cc+""));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        ConnectN c = new ConnectN();
//        System.out.println(c);
//        int maxDepth = 7;
//        while (true){
//            if(c.colorTurn==RED)
//                maxDepth = 4;
//            else maxDepth = 6;
//            c.addColorToColumn(new ConnectNSolver(maxDepth).minimax(c));
//            System.out.println(c);
//        }

        playVsComputer();
    }

    public static void randomPlay(){
        List<Integer> moves = new ArrayList<>();
        ConnectN c = new ConnectN();
        try {
            Random r = new Random();
            System.out.println(c);
            while (true) {
                try {
                    int m = r.nextInt(c.columns);
                    c.addColorToColumn(m);
                    moves.add(m);
                    System.out.println(c);
                }catch (IllegalAddToColumnException e){
                    continue;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(c);
            moves.forEach(System.out::print);
        }
    }

    public static void playTwoOnTwo() throws BlackWonException, IllegalAddToColumnException, RedWonException {
        ConnectN c = new ConnectN();
        Scanner s = new Scanner(System.in);
        System.out.println(c);
        while (true) {
            c.addColorToColumn(s.nextInt());
            System.out.println(c);
        }
    }

    public static void playVsComputer() throws BlackWonException, IllegalAddToColumnException, RedWonException {
        ConnectN c = new ConnectN();
        ConnectNSolver solver = new ConnectNSolver();
        Scanner s = new Scanner(System.in);
        System.out.println(c);
        while (true) {
            try {
                if(c.colorTurn==RED)
                    c.addColorToColumn(s.nextInt());
                else
                    c.addColorToColumn(solver.minimax(c));
            }catch (Exception e){
                c.moves.forEach(System.out::print);
                throw e;
            }
            System.out.println(c);
        }
    }
    List<Integer> moves = new ArrayList<>();
    public static final int BLACK = 2, RED = 1, EMPTY = 0;

    Column[] columnRows;
    int winCount = 4, rows = 6, columns = 7, colorTurn = 1;

    public ConnectN(Column[] columnRows, int winCount, int rows, int columns, int colorTurn) {
        this.columnRows = columnRows;
        this.winCount = winCount;
        this.rows = rows;
        this.columns = columns;
        this.colorTurn = colorTurn;
    }

    public ConnectN() {
        init();
    }
    public ConnectN(int rows, int columns){
        this.rows = rows;
        this.columns = columns;
        init();
    }
    public ConnectN(String s) throws BlackWonException, IllegalAddToColumnException, RedWonException {
        this();
        for(char c : s.toCharArray())
            addColorToColumn(Integer.parseInt(c+""));
    }
    private void init(){
        columnRows=new Column[columns];
        for (int i = 0; i < columns; i++)
            columnRows[i] = new Column(rows,winCount);
    }
    public int addColorToColumn(int column) throws BlackWonException, IllegalAddToColumnException, RedWonException {
        return addColorToColumn(colorTurn,column);
    }

    public int addColorToColumn(int color, int column) throws BlackWonException, IllegalAddToColumnException, RedWonException {
        // this handles up and down or vertically
        moves.add(column);
        int c = -1, colorCountHor = 0, colorCountNegDiag = 0, colorCountPosDiag = 0;
        try {
            c = columnRows[column].addColor(color);
        } catch (BlackWonException e){
            throw new BlackWonException(String.format("Column: %s", column));
        } catch (RedWonException e){
            throw new RedWonException(String.format("Column: %s", column));
        }
        for (int i = column - winCount < 0 ? 0 : column - winCount;
             i < (column + winCount > columnRows.length ? columnRows.length : column + winCount); i++) {
            int sD = i-column+c, sU = -i+column+c;
            // check left and right or horizontally
            if(columnRows[i].column[c]==color)
                colorCountHor++;
            else colorCountHor=0;
            // finally check diagonally negative (or check left up right down) and positive (or left down right up) diagonals
            // negative diagonal
            if(sD >=0 && sD < rows && columnRows[i].column[sD]==color)
                colorCountNegDiag++;
            else colorCountNegDiag=0;
            // positive diagonal
            if(sU >=0 && sU < rows && columnRows[i].column[sU]==color)
                colorCountPosDiag++;
            else colorCountPosDiag=0;
            if(colorCountHor>=winCount || colorCountNegDiag>=winCount || colorCountPosDiag>=winCount)
                if(color==1)
                    throw new RedWonException(String.format("%s: %s, col: %s, neg diag: %s, pos diag: %s, hor: %s", "row", c, column, colorCountNegDiag, colorCountPosDiag, colorCountHor));
                else throw new BlackWonException(String.format("%s: %s, col: %s, neg diag: %s, pos diag: %s, hor: %s", "row", c, column, colorCountNegDiag, colorCountPosDiag, colorCountHor));
        }
        colorTurn=colorTurn==1?2:1;
        return c;
    }

    public void removeColorFromColumn(int column){
        columnRows[column].pop();
        colorTurn=colorTurn==1?2:1;
    }

    @Override
    public String toString() {
        return getStringRepresentation();
    }

    private String getStringRepresentation() {
        StringBuilder[] builders = new StringBuilder[columns];
        for (int i = 0; i < columns; i++)
            builders[i] = new StringBuilder();
        for(Column c : columnRows)
            for (int i = 0; i < c.column.length; i++)
                builders[i].append(String.format("|%s", c.column[i]==1?"R":c.column[i]==2?"b":"-"));
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < columns; i++)
            builder.append(builders[columns-i-1].toString()+"|\n");
        return builder.toString();
    }

    public boolean hasMovesLeft(){
        for(Column c : columnRows)
            if(c.canHaveMorePieces())
                return true;
        return false;
    }

    /**
     * Positive means Red is closer to winning, negative means black is closer to winning
     * high value to 3 in a row with unblocked 4th, lower to 2 in a row with unblocked 3 and 4
     * and since only one move per turn zero for single pieces,
     * count disjoint sets as well x xx is still 3 in a row if not blocked, x x is still two in a row ect
     *
     * TODO check if next turn win if opponent doesn't block   xx x vs xx x/nyy y
     * TODO don't add to heuristic if you can't make that move yet
     *
     * TODO don't loop over everything, just remaining moves, simulate each remaining move and victory is Double.MAX_VALUE maybe simulate opponent move too...
     * @return
     */
    public int getHeuristic(){
        int heuristic = 0;
        // loop over all columns, and subsequently all available moves.  Add the move, check the value, then remove the move
        for (int column = 0; column < columnRows.length; column++) {
            try{
                int row = addColorToColumn(column), colorCountHor = 0, colorCountNegDiag = 0, colorCountPosDiag = 0;
                // since we made it here, this color hasn't won by that move, so check for
                for (int i = column - winCount < 0 ? 0 : column - winCount;
                     i < (column + winCount > columnRows.length ? columnRows.length : column + winCount) - 1; i++) {
                    int sD = i - column + row, sU = -i + column + row;
                    // check left and right or horizontally
                    if (columnRows[i].column[row] == colorTurn || columnRows[i].column[row] == EMPTY)
                        colorCountHor++;
                    else colorCountHor = 0;
                    // finally check diagonally negative (or check left up right down) and positive (or left down right up) diagonals
                    // negative diagonal
                    if (sD >= 0 && sD < rows && (columnRows[i].column[sD] == colorTurn || columnRows[i].column[sD] == EMPTY))
                        colorCountNegDiag++;
                    else colorCountNegDiag = 0;
                    // positive diagonal
                    if (sU >= 0 && sU < rows && (columnRows[i].column[sU] == colorTurn || columnRows[i].column[sU] == EMPTY))
                        colorCountPosDiag++;
                    else colorCountPosDiag = 0;
                }
                heuristic+=colorCountHor+colorCountNegDiag+colorCountPosDiag;
            }catch (RedWonException | BlackWonException e){
                return Integer.MAX_VALUE;
            } catch (IllegalAddToColumnException e) {
                /* DO NOTHING */
                continue;
            }
            removeColorFromColumn(column);
        }
        return heuristic;
    }

    @Override public ConnectN clone(){
        Column[] cr = new Column[columnRows.length];
        for (int i = 0; i < columnRows.length; i++)
            cr[i]=columnRows[i].clone();
        return new ConnectN(cr, winCount, rows, columns, colorTurn);
    }
}
