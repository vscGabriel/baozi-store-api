package br.com.vscgabriel.baozistore.controller;

import br.com.vscgabriel.baozistore.entity.ProductEntity;
import br.com.vscgabriel.baozistore.support.IntegrationTestSupport;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerIT extends IntegrationTestSupport {
    @Test
    void shouldCreateProductAndPersistItInPostgres() throws Exception {
        String content = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("products/create/request.json")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode response = jsonResponse(content);
        assertJsonEqualIgnoringIds(response, "products/create/response.json");
        ProductEntity product = productRepository.findById(response.path("id").asLong()).orElseThrow();
        assertThat(product.getName()).isEqualTo("Baozi de Cogumelos e Cebolinha");
        assertThat(product.getAvailable()).isTrue();
        assertThat(product.isActive()).isTrue();
        assertThat(product.getCreationDate()).isNotNull();
        assertThat(product.getUpdatedDate()).isNotNull();
        assertThat(product.getCreationUser()).isEqualTo("SYSTEM");
        assertThat(product.getLastUpdatedUser()).isEqualTo("SYSTEM");
    }

    @Test
    void shouldReturnValidationMessagesWhenCreatingInvalidProduct() throws Exception {
        String content = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("products/create/invalid-request.json")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqual(jsonResponse(content), "products/create/invalid-response.json");
    }

    @Test
    void shouldListProductsWithExpectedJson() throws Exception {
        createProduct();
        String content = mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertJsonEqualIgnoringIds(jsonResponse(content), "products/list/response.json");
    }

    @Test
    void shouldFindProductByIdWithExpectedJson() throws Exception {
        long id = createProduct();
        String content = mockMvc.perform(get("/product/{id}", id))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertJsonEqualIgnoringIds(jsonResponse(content), "products/find-by-id/response.json");
    }

    @Test
    void shouldUpdateProductAndPersistItInPostgres() throws Exception {
        long id = createProduct();

        String content = mockMvc.perform(put("/product/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("products/update/request.json")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqualIgnoringIds(jsonResponse(content), "products/update/response.json");
        assertThat(productRepository.findById(id)).hasValueSatisfying(product -> {
            assertThat(product.getName()).isEqualTo("Pork Baozi Premium");
            assertThat(product.getPrice()).isEqualByComparingTo("14.90");
            assertThat(product.getAvailable()).isFalse();
        });
    }

    @Test
    void shouldReturnValidationMessagesWhenUpdatingInvalidProduct() throws Exception {
        String content = mockMvc.perform(put("/product/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("products/update/invalid-request.json")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqual(jsonResponse(content), "products/update/invalid-response.json");
    }

    @Test
    void shouldLogicallyDeleteProduct() throws Exception {
        long id = createProduct();
        mockMvc.perform(delete("/product/{id}", id)).andExpect(status().isNoContent());
        assertThat(productRepository.findById(id)).hasValueSatisfying(product -> assertThat(product.isActive()).isFalse());
        assertThat(productRepository.findByIdAndActiveTrue(id)).isEmpty();
    }

    private long createProduct() throws Exception {
        String content = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("products/create/request.json")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return jsonResponse(content).path("id").asLong();
    }
}
