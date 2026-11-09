package br.com.vscgabriel.baozistore.controller;

import br.com.vscgabriel.baozistore.entity.ClientEntity;
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

class ClientControllerIT extends IntegrationTestSupport {
    @Test
    void shouldCreateClientAndPersistItInPostgres() throws Exception {
        String content = mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("clients/create/request.json")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        JsonNode response = jsonResponse(content);
        assertJsonEqualIgnoringIds(response, "clients/create/response.json");
        ClientEntity client = clientRepository.findById(response.path("id").asLong()).orElseThrow();
        assertThat(client.getName()).isEqualTo("Gabriel");
        assertThat(client.getDocumentNumber()).isEqualTo("12345678910");
        assertThat(client.getAreaPhoneCode()).isEqualTo(12);
        assertThat(client.getPhoneNumber()).isEqualTo("912345678");
        assertThat(client.isActive()).isTrue();
        assertThat(client.getCreationDate()).isNotNull();
        assertThat(client.getUpdatedDate()).isNotNull();
        assertThat(client.getCreationUser()).isEqualTo("SYSTEM");
        assertThat(client.getLastUpdatedUser()).isEqualTo("SYSTEM");
    }

    @Test
    void shouldReturnValidationMessagesWhenCreatingInvalidClient() throws Exception {
        String content = mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("clients/create/invalid-request.json")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqual(jsonResponse(content), "clients/create/invalid-response.json");
    }

    @Test
    void shouldListClientsWithExpectedJson() throws Exception {
        createClient();
        String content = mockMvc.perform(get("/client"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertJsonEqualIgnoringIds(jsonResponse(content), "clients/list/response.json");
    }

    @Test
    void shouldFindClientByIdWithExpectedJson() throws Exception {
        long id = createClient();
        String content = mockMvc.perform(get("/client/{id}", id))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertJsonEqualIgnoringIds(jsonResponse(content), "clients/find-by-id/response.json");
    }

    @Test
    void shouldFindClientByDocumentNumber() throws Exception {
        createClient();
        String content = mockMvc.perform(get("/client/document-number/{documentNumber}", "12345678910"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqualIgnoringIds(jsonResponse(content), "clients/find-by-id/response.json");
    }

    @Test
    void shouldUpdatePhoneNumberByDocumentNumberAndPersistItInPostgres() throws Exception {
        long id = createClient();

        String content = mockMvc.perform(put("/client/document-number/{documentNumber}/phone-number", "12345678910")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("clients/update-phone-number/request.json")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqualIgnoringIds(jsonResponse(content), "clients/update-phone-number/response.json");
        assertThat(clientRepository.findById(id)).hasValueSatisfying(client -> {
            assertThat(client.getPhoneNumber()).isEqualTo("998765432");
            assertThat(client.getAreaPhoneCode()).isEqualTo(11);
        });
    }

    @Test
    void shouldReturnValidationMessagesWhenUpdatingInvalidPhoneNumber() throws Exception {
        String content = mockMvc.perform(put("/client/document-number/{documentNumber}/phone-number", "12345678910")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("clients/update-phone-number/invalid-request.json")))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        assertJsonEqual(jsonResponse(content), "clients/update-phone-number/invalid-response.json");
    }

    @Test
    void shouldLogicallyDeleteClient() throws Exception {
        long id = createClient();
        mockMvc.perform(delete("/client/{id}", id)).andExpect(status().isNoContent());
        assertThat(clientRepository.findById(id)).hasValueSatisfying(client -> assertThat(client.isActive()).isFalse());
        assertThat(clientRepository.findByIdAndActiveTrue(id)).isEmpty();
    }

    @Test
    void shouldRejectDuplicateDocumentNumber() throws Exception {
        createClient();

        mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("clients/create/request.json")))
                .andExpect(status().isConflict());

        assertThat(clientRepository.count()).isEqualTo(1);
    }

    private long createClient() throws Exception {
        String content = mockMvc.perform(post("/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fixtureText("clients/create/request.json")))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return jsonResponse(content).path("id").asLong();
    }
}
