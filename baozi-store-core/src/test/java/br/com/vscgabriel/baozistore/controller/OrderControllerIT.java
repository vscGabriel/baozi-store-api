package br.com.vscgabriel.baozistore.controller;

import br.com.vscgabriel.baozistore.entity.OrderEntity;
import br.com.vscgabriel.baozistore.support.IntegrationTestSupport;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderControllerIT extends IntegrationTestSupport {
    @Test
    void shouldCreateOrderAndPersistItInPostgres() throws Exception {
        String content = createOrder();
        JsonNode response = jsonResponse(content);
        assertJsonEqualIgnoringIds(response, "orders/create/response.json");
        OrderEntity order = orderRepository.findById(response.path("id").asLong()).orElseThrow();
        assertThat(order.getQuantity()).isEqualTo(6);
        assertThat(order.getStatus()).hasToString("REGISTRADO");
        assertThat(order.getClient().getId()).isEqualTo(response.path("clientId").asLong());
        assertThat(order.getProduct().getId()).isEqualTo(response.path("productId").asLong());
        assertThat(order.isActive()).isTrue();
        assertThat(order.getCreationDate()).isNotNull();
        assertThat(order.getUpdatedDate()).isNotNull();
    }

    @Test
    void shouldReturnValidationMessagesWhenCreatingInvalidOrder() throws Exception {
        String content = mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("orders/create/invalid-request.json")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqual(jsonResponse(content), "orders/create/invalid-response.json");
    }

    @Test
    void shouldListOrdersWithExpectedJson() throws Exception {
        createOrder();
        String content = mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertJsonEqualIgnoringIds(jsonResponse(content), "orders/list/response.json");
    }

    @Test
    void shouldFindOrderByIdWithExpectedJson() throws Exception {
        long id = jsonResponse(createOrder()).path("id").asLong();
        String content = mockMvc.perform(get("/order/{id}", id))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertJsonEqualIgnoringIds(jsonResponse(content), "orders/find-by-id/response.json");
    }

    @Test
    void shouldLogicallyDeleteOrder() throws Exception {
        long id = jsonResponse(createOrder()).path("id").asLong();
        mockMvc.perform(delete("/order/{id}", id)).andExpect(status().isNoContent());
        assertThat(orderRepository.findById(id)).hasValueSatisfying(order -> assertThat(order.isActive()).isFalse());
        assertThat(orderRepository.findByIdAndActiveTrue(id)).isEmpty();
    }

    private String createOrder() throws Exception {
        long clientId = createClient();
        long productId = createProduct();
        String request = fixtureText("orders/create/request.json")
                .replace("${clientId}", Long.toString(clientId))
                .replace("${productId}", Long.toString(productId));
        return mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
    }

    private long createClient() throws Exception {
        String content = mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("clients/create/request.json")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return jsonResponse(content).path("id").asLong();
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
