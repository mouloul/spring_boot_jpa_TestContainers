package fr.mha.testContainer;

import fr.mha.testContainer.utilisateur.Utilisateur;
import fr.mha.testContainer.utilisateur.repository.UtilisateurRepository;
import org.junit.jupiter.api.*;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.Assert;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // deactivate the default behaviour
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UtilisateurRepositoryTest {

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
        // repository.deleteAll();
    }

    @Test
    @Order(1)
    public void should_save_utilisateur_and_equal_given_nom_and_saved_utilisateur_nom() {
        Utilisateur utilisateur = generateUser();
        var savedUser = repository.save(utilisateur);

        Assertions.assertThat(savedUser.getNom()).isEqualTo(utilisateur.getNom());
        Assert.notNull(savedUser.getId(), "Id de l'utilisateur ne peut être null !");
    }

    @Test
    @Order(2)
    public void should_get_utilisateur_by_nom_and_equal_given_nom_and_saved_utilisateur_nom() {
        Utilisateur utilisateur = generateUser();
        var savedUser = repository.save(utilisateur);

        var users = repository.getUserByName(savedUser.getNom());

        // org.assertj.core.api
        Assertions.assertThat(users.size()).isEqualTo(1);
        Assertions.assertThat(users.get(0).getNom()).isEqualTo(utilisateur.getNom());
    }
    private Utilisateur generateUser() {
        return Utilisateur.builder().nom("Doe").prenom("john").build();
    }
}