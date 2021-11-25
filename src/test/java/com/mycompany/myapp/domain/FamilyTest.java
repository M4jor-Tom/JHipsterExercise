package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FamilyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Family.class);
        Family family1 = new Family();
        family1.setId(1L);
        Family family2 = new Family();
        family2.setId(family1.getId());
        assertThat(family1).isEqualTo(family2);
        family2.setId(2L);
        assertThat(family1).isNotEqualTo(family2);
        family1.setId(null);
        assertThat(family1).isNotEqualTo(family2);
    }
}
