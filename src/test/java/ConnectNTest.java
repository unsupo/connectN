import exceptions.BlackWonException;
import exceptions.IllegalAddToColumnException;
import exceptions.RedWonException;
import org.junit.Test;

public class ConnectNTest {
    @Test
    public void testNegativeDiaganol() {
        try {
            ConnectN c = new ConnectN("3221001016");
            c.addColorToColumn(0);
        } catch (RedWonException e) {
            assert true;
        } catch (BlackWonException | IllegalAddToColumnException e) {
            /* Do Nothing */
        }
        assert false;
    }
}
