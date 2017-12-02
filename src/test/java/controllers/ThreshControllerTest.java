package controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ThreshControllerTest {
    @Test
    void getThresh() {
        ThreshController.setThresh(0);
        float thresh = ThreshController.getThresh();
        assertEquals(0, thresh);
    }

    @Test
    void setThresh() {
        ThreshController.setThresh(45);
        assertEquals(45, ThreshController.getThresh());
    }

}