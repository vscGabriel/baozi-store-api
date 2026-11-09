package br.com.vscgabriel.baozistore.support;

import br.com.vscgabriel.baozistore.repository.ClientRepository;
import br.com.vscgabriel.baozistore.repository.OrderRepository;
import br.com.vscgabriel.baozistore.repository.ProductRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class IntegrationTestSupport {
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:17-alpine")
            .withDatabaseName("baozi_store")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected OrderRepository orderRepository;
    @Autowired protected ProductRepository productRepository;
    @Autowired protected ClientRepository clientRepository;

    @BeforeEach
    void cleanDatabase() {
        orderRepository.deleteAll();
        productRepository.deleteAll();
        clientRepository.deleteAll();
    }

    protected String fixtureText(String path) {
        try (InputStream input = getClass().getResourceAsStream("/cases/" + path)) {
            assertThat(input).as("fixture %s", path).isNotNull();
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception exception) {
            throw new IllegalStateException("Nao foi possivel ler o fixture " + path, exception);
        }
    }

    protected JsonNode fixtureJson(String path) {
        try {
            return objectMapper.readTree(fixtureText(path));
        } catch (Exception exception) {
            throw new IllegalStateException("Fixture JSON invalido: " + path, exception);
        }
    }

    protected JsonNode jsonResponse(String content) {
        try {
            return objectMapper.readTree(content);
        } catch (Exception exception) {
            throw new IllegalStateException("Resposta JSON invalida", exception);
        }
    }

    protected void assertJsonEqualIgnoringIds(JsonNode actual, String expectedFixture) {
        assertThat(removeIdFields(actual.deepCopy()))
                .usingRecursiveComparison()
                .isEqualTo(removeIdFields(fixtureJson(expectedFixture)));
    }

    protected void assertJsonEqual(JsonNode actual, String expectedFixture) {
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(fixtureJson(expectedFixture));
    }

    private JsonNode removeIdFields(JsonNode node) {
        if (node instanceof ObjectNode objectNode) {
            List<String> fieldsToRemove = new ArrayList<>();
            Iterator<Map.Entry<String, JsonNode>> fields = objectNode.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (field.getKey().matches("(?i).*id")) {
                    fieldsToRemove.add(field.getKey());
                } else {
                    removeIdFields(field.getValue());
                }
            }
            objectNode.remove(fieldsToRemove);
        } else if (node instanceof ArrayNode arrayNode) {
            arrayNode.forEach(this::removeIdFields);
        }
        return node;
    }
}
