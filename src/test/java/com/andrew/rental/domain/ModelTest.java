package com.andrew.rental.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.andrew.rental.controller.TestUtil;

public class ModelTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Model.class);
        Model model1 = new Model();
        model1.setId(1L);
        Model model2 = new Model();
        model2.setId(model1.getId());
        assertThat(model1).isEqualTo(model2);
        model2.setId(2L);
        assertThat(model1).isNotEqualTo(model2);
        model1.setId(null);
        assertThat(model1).isNotEqualTo(model2);
    }
}
