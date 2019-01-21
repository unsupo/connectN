import exceptions.BlackWonException;
import exceptions.IllegalAddToColumnException;
import exceptions.RedWonException;

import java.util.ArrayList;
import java.util.List;

public class ConnectNSolver {
    // won't use expectimax as it's based on random chance and connect 4 isn't random, it has full information https://en.wikipedia.org/wiki/Expectiminimax
    public ConnectNSolver(){ }
    public ConnectNSolver(int maxDepth){
        this.MAX_DEPTH=maxDepth;
    }

    /*
    //(* Initial call *)
    //minimax(origin, depth, TRUE)
    function minimax(node, depth, maximizingPlayer) is
    if depth = 0 or node is a terminal node then
        return the heuristic value of node
    if maximizingPlayer then
        value := −∞
        for each child of node do
            value := max(value, minimax(child, depth − 1, FALSE))
        return value
    else (* minimizing player *)
        value := +∞
        for each child of node do
            value := min(value, minimax(child, depth − 1, TRUE))
        return value
     */
    private int MAX_DEPTH = 6;
    public int minimax(ConnectN origin){
        return minimax(origin.clone(),MAX_DEPTH,origin.colorTurn==1);
    }
    private int minimax(ConnectN node, int depth, boolean maximizingPlayer){
        if(depth==0 || !node.hasMovesLeft())
            return node.getHeuristic(); //heuristic
        if(maximizingPlayer) {
            List<Integer> values = new ArrayList<>();
            int value = -Integer.MAX_VALUE;
            for (int i = 0; i < node.columnRows.length; i++) {
                ConnectN child = node.clone();
                try {
                    child.addColorToColumn(i);
                } catch (BlackWonException | RedWonException e) {
                    if(depth==MAX_DEPTH)
                        return i;
                    return Integer.MAX_VALUE;
                }catch (IllegalAddToColumnException e) {
                    values.add(null);
                    continue;
                }
                int v = minimax(child, depth - 1, false);
                value = Math.max(value, v);
                values.add(v);
            }
            if(depth==MAX_DEPTH) {
                int best = -Integer.MAX_VALUE, index = -1, defaultIndex = 0;
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i) == null) defaultIndex++;
                    if (values.get(i) != null && values.get(i) == Integer.MAX_VALUE)
                        return i;
                    else if (values.get(i) != null && values.get(i) > best) {
                        best = values.get(i);
                        index = i;
                    }
                }
                return index == -1 ? defaultIndex : index;
            }
            return value;
        }else{
            List<Integer> values = new ArrayList<>();
            int value = Integer.MAX_VALUE;
            for (int i = 0; i < node.columnRows.length; i++) {
                ConnectN child = node.clone();
                try {
                    child.addColorToColumn(i);
                } catch (BlackWonException | RedWonException e) {
                    if(depth==MAX_DEPTH)
                        return i;
                    return -Integer.MAX_VALUE;
                }catch (IllegalAddToColumnException e) {
                    values.add(null);
                    continue;
                }
                int v = minimax(child, depth - 1, true);
                value = Math.min(value, v);
                values.add(v);
            }
            if(depth==MAX_DEPTH) {
                int best = Integer.MAX_VALUE, index = -1, defaultIndex = 0;
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i) == null) defaultIndex++;
                    if (values.get(i) != null && values.get(i) == -Integer.MAX_VALUE)
                        return i;
                    else if (values.get(i) != null && values.get(i) < best) {
                        best = values.get(i);
                        index = i;
                    }
                }
                return index == -1 ? defaultIndex : index;
            }
            return value;
        }
    }
    /*
    // same as minimax, just simplified.  Only works if red heuristic is just the opposite of black heuristic
    function negamax(node, depth, color) is
    if depth = 0 or node is a terminal node then
        return color × the heuristic value of node
    value := −∞
    for each child of node do
        value := max(value, −negamax(child, depth − 1, −color))
    return value
     */
    public static int negamax(ConnectN origin){
        return negamax(origin.clone(),0);
    }
    public static int negamax(ConnectN node, int depth){
        if(depth==0 || !node.hasMovesLeft())
            return node.getHeuristic(); //heuristic
        int value = -Integer.MAX_VALUE;
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < node.columnRows.length; i++) {
            try {
                node.addColorToColumn(i);
            } catch (BlackWonException | RedWonException e) {
                node.removeColorFromColumn(i);
                return -Integer.MAX_VALUE;
            }catch (IllegalAddToColumnException e) {
                values.add(null);
                continue;
            }
            int v = -negamax(node.clone(),depth-1);
            values.add(v);
            value=Math.max(v,value);
            node.removeColorFromColumn(i);
        }
        if(depth==0) {
            int best = Integer.MAX_VALUE, index = -1;
            for (int i = 0; i < values.size(); i++)
                if(values.get(i) != null && values.get(i)==-Integer.MAX_VALUE)
                    return i;
                else if (values.get(i) != null && values.get(i)<best){
                    best=values.get(i);
                    index=i;
                }
            return index;
        }
        return value;
    }

    /*
    function alphabeta(node, depth, α, β, maximizingPlayer) is
    if depth = 0 or node is a terminal node then
        return the heuristic value of node
    if maximizingPlayer then
        value := −∞
        for each child of node do
            value := max(value, alphabeta(child, depth − 1, α, β, FALSE))
            α := max(α, value)
            if α ≥ β then
                break (* β cut-off *)
        return value
    else
        value := +∞
        for each child of node do
            value := min(value, alphabeta(child, depth − 1, α, β, TRUE))
            β := min(β, value)
            if α ≥ β then
                break (* α cut-off *)
        return value
     */
    public static void alphabeta(ConnectN node, int depth, int a, int b, int maximizingPlayer){

    }

    /*
    function pvs(node, depth, α, β, color) is
    if depth = 0 or node is a terminal node then
        return color × the heuristic value of node
    for each child of node do
        if child is first child then
            score := −pvs(child, depth − 1, −β, −α, −color)
        else
            score := −pvs(child, depth − 1, −α − 1, −α, −color) (* search with a null window *)
            if α < score < β then
                score := −pvs(child, depth − 1, −β, −score, −color) (* if it failed high, do a full re-search *)
        α := max(α, score)
        if α ≥ β then
            break (* beta cut-off *)
    return α
     */
    public static int pvs(ConnectN origin){
        return pvs(origin.clone(),0,-Integer.MAX_VALUE,Integer.MAX_VALUE);
    }
    public static int pvs(ConnectN node, int depth, int a, int b){ //principle variation search
        if(depth==0 || !node.hasMovesLeft())
            return node.getHeuristic(); //heuristic
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < node.columnRows.length; i++) {
            ConnectN child = node.clone();
            try {
                child.addColorToColumn(i);
            } catch (BlackWonException | RedWonException e) {
                return -Integer.MAX_VALUE;
            }catch (IllegalAddToColumnException e) {
                values.add(null);
                continue;
            }
            int score;
            if(i==0)
                score = -pvs(child,depth-1,-b,-a);
            else {
                score = -pvs(child,depth-1,-a-1,-a);
                if(a<score && score<b)
                    score=-pvs(child,depth-1,-b,-score);
            }
            a = Math.max(a,score);
            values.add(a);
            if(a >= b)
                break;
        }
        if(depth==0) {
            int best = -Integer.MAX_VALUE, index = -1;
            for (int i = 0; i < values.size(); i++)
                if(values.get(i) != null && values.get(i)==Integer.MAX_VALUE)
                    return i;
                else if (values.get(i) != null && values.get(i)>best){
                    best=values.get(i);
                    index=i;
                }
            return index;
        }
        return a;
    }
}
