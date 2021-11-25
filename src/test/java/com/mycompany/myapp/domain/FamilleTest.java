package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Famille.class);
        Famille famille1 = new Famille();
        famille1.setId(1L);
        Famille famille2 = new Famille();
        famille2.setId(famille1.getId());
        assertThat(famille1).isEqualTo(famille2);
        famille2.setId(2L);
        assertThat(famille1).isNotEqualTo(famille2);
        famille1.setId(null);
        assertThat(famille1).isNotEqualTo(famille2);
    }
}
