package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConnexionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Connexion.class);
        Connexion connexion1 = new Connexion();
        connexion1.setId(1L);
        Connexion connexion2 = new Connexion();
        connexion2.setId(connexion1.getId());
        assertThat(connexion1).isEqualTo(connexion2);
        connexion2.setId(2L);
        assertThat(connexion1).isNotEqualTo(connexion2);
        connexion1.setId(null);
        assertThat(connexion1).isNotEqualTo(connexion2);
    }
}
