package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionTest {
    @Test
    void mapFrequencyToHuman() {
        assertEquals("3 semaines et 4 jours", Transaction.mapFrequencyToHuman(25));
        assertEquals("3 semaines", Transaction.mapFrequencyToHuman(21));
        assertEquals("4 jours", Transaction.mapFrequencyToHuman(4));
        assertEquals("1 semaine et 1 jour", Transaction.mapFrequencyToHuman(8));


    }


}