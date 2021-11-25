package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CaracteristiqueTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caracteristique.class);
        Caracteristique caracteristique1 = new Caracteristique();
        caracteristique1.setId(1L);
        Caracteristique caracteristique2 = new Caracteristique();
        caracteristique2.setId(caracteristique1.getId());
        assertThat(caracteristique1).isEqualTo(caracteristique2);
        caracteristique2.setId(2L);
        assertThat(caracteristique1).isNotEqualTo(caracteristique2);
        caracteristique1.setId(null);
        assertThat(caracteristique1).isNotEqualTo(caracteristique2);
    }
}
