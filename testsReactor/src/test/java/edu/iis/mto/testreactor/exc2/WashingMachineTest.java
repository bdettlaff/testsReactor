package edu.iis.mto.testreactor.exc2;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hamcrest.Matchers;
import org.junit.Assert;
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
    public void shouldReturnTrueIfLaundryIsTooHeavy(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                    .withWeightKg(10)
                    .withType(Material.WOOL)
                    .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                            .withProgram(Program.SHORT)
                            .withSpin(true)
                            .build();

        assertThat(washingMachine.start(laundryBatch,programConfiguration).getErrorCode(),is(ErrorCode.TOO_HEAVY));
    }

    @Test
    public void shouldReturnTrueIfProgramIsFinished(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.WOOL)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();

        assertThat(washingMachine.start(laundryBatch,programConfiguration).getResult(),is(Result.SUCCESS));
    }

    @Test
    public void shouldReturnTrueIfSpinEngineInvokesOnce(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.WOOL)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();
        washingMachine.start(laundryBatch,programConfiguration);
        verify(engine,times(1)).spin();
    }

    @Test
    public void shouldReturnTrueIfPourInWaterPumpInvokesOnce(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.WOOL)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();
        washingMachine.start(laundryBatch,programConfiguration);
        verify(waterPump,times(1)).pour(laundryBatch.getWeightKg());
    }

    @Test
    public void shouldReturnTrueIfReleaseInWaterPumpInvokesOnce(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.WOOL)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();
        washingMachine.start(laundryBatch,programConfiguration);
        verify(waterPump,times(1)).release();
    }

    @Test
    public void shouldReturnTrueIfProgramIsLong(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.WOOL)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.AUTODETECT)
                                                                        .build();
        Percentage percentage = new Percentage(41.0d);

        when(dirtDetector.detectDirtDegree(laundryBatch)).thenReturn(percentage);
        washingMachine.start(laundryBatch,programConfiguration);
        assertEquals(washingMachine.start(laundryBatch, programConfiguration).getRunnedProgram(), Program.LONG);
    }

    @Test
    public void shouldReturnTrueIfProgramIsShort(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.WOOL)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .build();

        washingMachine.start(laundryBatch,programConfiguration);
        assertEquals(washingMachine.start(laundryBatch, programConfiguration).getRunnedProgram(), Program.SHORT);

    }

    @Test
    public void shouldReturnTrueIfSpinNeverInvoke(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.DELICATE)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.SHORT)
                                                                        .withSpin(true)
                                                                        .build();
        washingMachine.start(laundryBatch,programConfiguration);
        verify(engine,times(0)).spin();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfValueIsOutOfRange(){
        LaundryBatch laundryBatch = LaundryBatch.builder()
                                                .withWeightKg(6)
                                                .withType(Material.WOOL)
                                                .build();
        ProgramConfiguration programConfiguration = ProgramConfiguration.builder()
                                                                        .withProgram(Program.AUTODETECT)
                                                                        .build();
        Percentage percentage = new Percentage(120.0d);

        when(dirtDetector.detectDirtDegree(laundryBatch)).thenReturn(percentage);
        washingMachine.start(laundryBatch,programConfiguration);
    }

    @Test
    public void itCompiles() {
        assertThat(true, Matchers.equalTo(true));
    }

}
