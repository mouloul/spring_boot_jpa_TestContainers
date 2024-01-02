package fr.mha.testContainer;

import fr.mha.testContainer.utilisateur.Utilisateur;
import fr.mha.testContainer.utilisateur.repository.UtilisateurRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.springframework.boot.test.web.client.TestRestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UtilisateurControllerTest {

    @Container
    static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15")
            .withDatabaseName("test")
            .withUsername("root")
            .withPassword("root");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }

    @Autowired
    private UtilisateurRepository repository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String id;

    @BeforeAll
    static void beforeAll() {
        postgresqlContainer.start();

    }

    @AfterAll
    static void afterAll() {
        postgresqlContainer.stop();
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    @Order(1)
    public void should_save_utilisateur_and_return_created_status_code() {

        Utilisateur utilisateur = Utilisateur.builder().nom("Doe").prenom("john").build();

        ResponseEntity<Utilisateur> createResponse = restTemplate.postForEntity("/api/utilisateurs", utilisateur, Utilisateur.class);
        Utilisateur savedUser = createResponse.getBody();

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertEquals(utilisateur.getNom(), savedUser.getNom());
        assertEquals(utilisateur.getPrenom(), savedUser.getPrenom());
        assertNotNull(savedUser);
    }

    @Test
    @Order(2)
    public void should_get_utilisateur_by_id_and_return_ok_status_code() {
        var utilisateur = Utilisateur.builder().nom("Doe").prenom("john").build();
        repository.save(utilisateur);

        String url = String.format("/api/utilisateurs/%s", utilisateur.getId());

        ResponseEntity<Utilisateur> findUserByIdResponse = restTemplate.getForEntity(url, Utilisateur.class);
        Utilisateur existUser = findUserByIdResponse.getBody();

        assertEquals(HttpStatus.OK, findUserByIdResponse.getStatusCode());
        assertNotNull(existUser);

    }
}