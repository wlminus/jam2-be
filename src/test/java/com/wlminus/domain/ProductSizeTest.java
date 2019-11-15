package com.wlminus.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.wlminus.web.rest.TestUtil;

public class ProductSizeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductSize.class);
        ProductSize productSize1 = new ProductSize();
        productSize1.setId(1L);
        ProductSize productSize2 = new ProductSize();
        productSize2.setId(productSize1.getId());
        assertThat(productSize1).isEqualTo(productSize2);
        productSize2.setId(2L);
        assertThat(productSize1).isNotEqualTo(productSize2);
        productSize1.setId(null);
        assertThat(productSize1).isNotEqualTo(productSize2);
    }
}
