import exceptions.BlackWonException;
import exceptions.IllegalAddToColumnException;
import exceptions.RedWonException;
import org.junit.Test;

public class ConnectNSolverTest {

    @Test
    public void testSolverBreak() throws BlackWonException, IllegalAddToColumnException, RedWonException {
        new ConnectN("30216526011666332");
    }
}
