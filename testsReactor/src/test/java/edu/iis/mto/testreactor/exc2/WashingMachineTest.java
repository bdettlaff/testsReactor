package edu.iis.mto.testreactor.exc2;

import static org.junit.Assert.assertThat;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class WashingMachineTest {
    private WaterPump waterPump;
    private DirtDetector dirtDetector;
    private Engine engine;

    private WashingMachine washingMachine;

    @Before
    public void setUp(){
        waterPump = Mockito.mock(WaterPump.class);
        dirtDetector = Mockito.mock(DirtDetector.class);
        engine = Mockito.mock(Engine.class);

        washingMachine = new WashingMachine(dirtDetector,engine,waterPump);
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

}
