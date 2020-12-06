import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import listener.IListenProfModules;
import listener.Listener;
import org.junit.*;
import org.junit.runner.RunWith;

import java.util.List;

/**
 * @author Melton Smith
 * @since 05.12.2020
 */
@RunWith(JUnitParamsRunner.class)
public class ListenerTest2 extends Assert {

    private IListenProfModules listener;

    public static List<TestDataWrapper> someData() {
        return List.of(ListenerTest.testCase1());
    }

//    @Test(expected = NullPointerException.class)
//    @Parameters(method = "someData")
//    public void test(TestDataWrapper testDataWrapper){
//        throw new NullPointerException();
//    }

    @Test()
    @Parameters(method = "someData")
    public void test1(TestDataWrapper testDataWrapper){
        assertThrows(NullPointerException.class, () -> listener.onEvent(testDataWrapper.getProfModules(), testDataWrapper.getRegistryElements(), testDataWrapper.getMainBonds(), testDataWrapper.getPart2PartBonds()));
    }

    @Before
    public void getListener(){
        listener = Listener.INSTANCE;
    }

    @After
    public void endTest() {
        listener = null;
    }
}
