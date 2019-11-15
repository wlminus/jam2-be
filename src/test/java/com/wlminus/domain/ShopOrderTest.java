package com.wlminus.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.wlminus.web.rest.TestUtil;

public class ShopOrderTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShopOrder.class);
        ShopOrder shopOrder1 = new ShopOrder();
        shopOrder1.setId(1L);
        ShopOrder shopOrder2 = new ShopOrder();
        shopOrder2.setId(shopOrder1.getId());
        assertThat(shopOrder1).isEqualTo(shopOrder2);
        shopOrder2.setId(2L);
        assertThat(shopOrder1).isNotEqualTo(shopOrder2);
        shopOrder1.setId(null);
        assertThat(shopOrder1).isNotEqualTo(shopOrder2);
    }
}
