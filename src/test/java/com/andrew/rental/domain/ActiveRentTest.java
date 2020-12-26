package com.andrew.rental.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.andrew.rental.controller.TestUtil;

public class ActiveRentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActiveRent.class);
        ActiveRent activeRent1 = new ActiveRent();
        activeRent1.setId(1L);
        ActiveRent activeRent2 = new ActiveRent();
        activeRent2.setId(activeRent1.getId());
        assertThat(activeRent1).isEqualTo(activeRent2);
        activeRent2.setId(2L);
        assertThat(activeRent1).isNotEqualTo(activeRent2);
        activeRent1.setId(null);
        assertThat(activeRent1).isNotEqualTo(activeRent2);
    }
}
