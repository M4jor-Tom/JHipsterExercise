package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SubFamilyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SubFamily.class);
        SubFamily subFamily1 = new SubFamily();
        subFamily1.setId(1L);
        SubFamily subFamily2 = new SubFamily();
        subFamily2.setId(subFamily1.getId());
        assertThat(subFamily1).isEqualTo(subFamily2);
        subFamily2.setId(2L);
        assertThat(subFamily1).isNotEqualTo(subFamily2);
        subFamily1.setId(null);
        assertThat(subFamily1).isNotEqualTo(subFamily2);
    }
}
