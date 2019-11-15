package com.wlminus.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.wlminus.web.rest.TestUtil;

public class AppConstTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppConst.class);
        AppConst appConst1 = new AppConst();
        appConst1.setId(1L);
        AppConst appConst2 = new AppConst();
        appConst2.setId(appConst1.getId());
        assertThat(appConst1).isEqualTo(appConst2);
        appConst2.setId(2L);
        assertThat(appConst1).isNotEqualTo(appConst2);
        appConst1.setId(null);
        assertThat(appConst1).isNotEqualTo(appConst2);
    }
}
