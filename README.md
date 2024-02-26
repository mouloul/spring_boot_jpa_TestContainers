# spring_boot_jpa_TestContainers
---
author: Mouloud Haouili <mouloud.haouili@ouidou.fr>
title: Intégration des tests Spring Boot avec Testcontainers (PostgreSQL)
categories:
  - back
tags:
  - java
  - spring boot
  - tests unitaires
  - testcontainers
  - postgres
description: "Cet article explique comment intégrer Testcontainers dans des tests Spring Boot, avec un focus sur PostgreSQL."
preview_size: 2
publish_on: 2024-01-03
---

# Intégration des tests Spring Boot avec Testcontainers (PostgreSQL)

Testcontainers est une bibliothèque de test qui fournit des interfaces simples et légères pour exécuter des tests d'intégration avec des services réels encapsulés dans des conteneurs Docker. Cette solution permet d'écrire des tests interagissant avec les mêmes types de services déployés en production, éliminant ainsi le besoin de simulations ou de services en mémoire.

## Un peu de contexte avant de commencer

Spring Boot est actuellement le framework le plus populaire pour le développement d'applications rapides dans le secteur du logiciel. Il permet une intégration facile de différentes bases de données, systèmes de messagerie, fournisseurs de cache, et de nombreux autres services tiers. L'un des services les plus répandus est Spring Data JPA, qui fournit une méthode directe pour élaborer des requêtes de base de données et les évaluer. Dans cet article, nous allons créer une petite application CRUD Spring Boot et mettre en place un environnement de test basé sur Testcontainers.

### Le problème / Cas d'usage

Pour tester nos repositories Spring Data, nous pouvons simuler les données et tester les API, ou utiliser une base de données en mémoire (comme la base H2 que Spring Boot intègre facilement). Cependant, celle-ci pourrait ne pas correspondre à la base de données utilisée en production. Nos tests d'intégration ne garantiraient pas alors que notre code fonctionne correctement dans d'autres environnements. Monter une base de données de test distincte engendre des coûts opérationnels et financiers supplémentaires.

### Testcontainers à la rescousse

Testcontainers, un projet open-source lancé en 2015, a gagné en popularité pour sa capacité à simplifier les tests d'intégration en fournissant des environnements de test légers et éphémères via des conteneurs Docker. Testcontainers évite de mocker des services ou de configurer des environnements compliqués, permettant de définir les dépendances nécessaires pour nos tests directement dans le code.

### Prérequis
- Java: 17+
- Spring Boot: 3.1.x
- Un environnement Docker supporté par Testcontainers (https://java.testcontainers.org/supported_docker_environment/)

Nous allons créer une application Spring Boot en utilisant "Spring Initializr" et sélectionner les dépendances Spring Web, Spring Data JPA, PostgreSQL Driver, et Testcontainers starters. Pour simplifier, nous allons créer un contrôleur, un service et une classe repository pour créer et chercher un utilisateur par son nom.

- pom.xml

![image](https://narcisse.synbioz.com/attachments/89be0919-0a29-45f0-a82a-49052da4ab4c/variants/c2cdc787-974b-435e-bb4d-e3f8dfd8faee/src)

- UtilisateurController

![image](https://narcisse.synbioz.com/attachments/12b36b23-a260-4490-9db7-2b473682d57a/variants/73dc7e41-3cd7-4341-8a3d-239885090c9c/src) 

- UtilisateurService

![image](https://narcisse.synbioz.com/attachments/a093c218-436f-4c38-9ef7-a82455bd7fad/variants/6f1df0ec-01cd-4688-aa14-6d2f97b4af73/src) 

- UtilisateurRepository

![image](https://narcisse.synbioz.com/attachments/0d960c10-de23-4df8-9f82-53a784718a87/variants/7c6dda8f-0c84-4fcd-af8f-cda51fb3f9e8/src)

- L'entité Utilisateur

![image](https://narcisse.synbioz.com/attachments/d7bb4c6b-1737-4ec3-9195-f394aef33633/variants/e69c3e88-3325-4bcc-b751-a06eb4147775/src) 

- Le fichier de configuration application.properties

![image](https://narcisse.synbioz.com/attachments/af2a5b27-ce72-4388-921f-8dc9d49c1b5f/variants/6252f5bc-bd6d-4714-ab55-85e116cbd6aa/src)


Pour l'instant rien d'impressionnant, mais passons à l'écriture de nos cas de tests.

- Nous testons d'abord notre repository :
  ![image](https://narcisse.synbioz.com/attachments/2a31d921-1784-4615-823d-cf5cb6e6331f/variants/a29cc0fd-2b86-4c98-9030-613a507a3da5/src)

**@TestContainers :**  
L'extension `@Testcontainers` active le démarrage et l'arrêt automatiques de conteneurs Docker, ici un conteneur PostgreSQL, utilisés dans les tests. Le conteneur PostgreSQL est déclaré comme un champ statique avec l'annotation `@Container`, ce qui signifie qu'il sera partagé entre toutes les méthodes de test de cette classe. Ce conteneur sera démarré une seule fois avant l'exécution de la première méthode de test (`@BeforeAll`) et arrêté après l'exécution de la dernière méthode de test (`@AfterAll`).

**@Container :**  
Dans un contexte de Testcontainers, l'annotation `@Container` est utilisée pour :
- Marquer et identifier les conteneurs Docker dans les classes de test JUnit.
- Gérer automatiquement le démarrage et l'arrêt de ces conteneurs, assurant que chaque test s'exécute dans un environnement isolé et contrôlé.
- Faciliter la configuration et l'utilisation de conteneurs Docker pour les tests d'intégration.

**@DynamicPropertySource :**  
Est utilisée pour intégrer des propriétés dynamiques nécessaires pour configurer la connexion à une base de données PostgreSQL gérée par Testcontainers. Les propriétés ajoutées via `@DynamicPropertySource` sont automatiquement prises en compte par Spring Boot dans notre test.

**@BeforeAll et @AfterAll :**  
`@BeforeAll` est utilisé pour exécuter une méthode **avant tous les tests de la classe**, et `@AfterAll` est utilisé pour exécuter une méthode **après tous les tests de la classe**.

**@BeforeEach :**  
Est utilisé pour signaler que la méthode annotée doit être exécutée **avant chaque test de la classe**.

D'autres annotations comme `@AfterEach`, `@BeforeAll`, `@AfterAll`, `@Test`, `@RepeatedTest`, `@TestFactory`, `@TestTemplate`, etc., existent et peuvent correspondre à des cas d'usage variés. Plus d'informations sont disponibles ici : [JUnit 5 Annotations](https://junit.org/junit5/docs/current/user-guide/#writing-tests-annotations).

Voyons maintenant comment tester la création et la recherche d'un utilisateur :
![image](https://narcisse.synbioz.com/attachments/c0f149c9-447f-4587-ae70-570b9cf9a11e/variants/ded0e66b-b0b3-4bdf-9fc5-eb07d3ced512/src)

Ces exemples illustrent comment Testcontainers facilite la phase de test. Nous avons simplement injecté notre repository, qui interagit avec la base PostgreSQL initiée auparavant, et sauvegarde réellement un utilisateur. Les assertions confirment la réussite des tests.

- Nous testons à présent les méthodes du controller :
  ![image](https://narcisse.synbioz.com/attachments/73df095c-6569-48e2-a7b7-2b08e5ca9d9c/variants/6e851c19-cc3f-4484-936a-ad4ef8ab81b7/src)

`UtilisateurControllerTest` reprend la même logique en créant un conteneur PostgreSQL, puis le démarrant avant les tests et l'arrêtant après.

**@SpringBootTest**  
Peut être spécifiée sur une classe de test qui exécute des tests basés sur Spring Boot.

**@SpringBootTest.WebEnvironment.RANDOM_PORT**  
Indique à Spring Boot de démarrer l'application sur un port disponible choisi aléatoirement.

Nous utilisons **TestRestTemplate** pour tester des API. Plus de détails sont disponibles ici : [TestRestTemplate](https://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/test/web/client/TestRestTemplate.html).

L'implémentation de deux méthodes est présentée :
1. Test de sauvegarde d'un utilisateur (API POST) :
   ![image](https://narcisse.synbioz.com/attachments/697fe5ad-5a8b-4abb-a545-2cf3a77ca11e/variants/bf0cc02c-dec9-4e28-8cb6-f25d980ed0fc/src)
   Nous utilisons `postForEntity` de TestRestTemplate pour créer l'utilisateur et récupérer un `ResponseEntity`.

2. Test de recherche d'un Utilisateur par son Id :
   ![image](https://narcisse.synbioz.com/attachments/14bf28a8-4817-458b-87bf-de732a389ec3/variants/966903c0-c6a5-4bb6-8c77-f46b52942596/src)
   Nous utilisons la méthode `getForEntity` de TestRestTemplate pour rechercher un utilisateur par son Id, puis on verifie que la reponse correspond au resultat attendu.


## **Pour résumer**
Nous avons axé notre présentation sur une démonstration élégante pour les tests d'intégration en encapsulant notre service de base de données dans un conteneur Docker, mais TestContainers ne se limite pas à cela. TestContainers facilite la création d'un environnement de test fidèle à la production en permettant la conteneurisation de plus de 50 modules (pratiquement toutes les technologies qu'on peut dockeriser). Cette fidélité est cruciale pour garantir que les tests d'intégration reflètent le comportement réel de l'application dans un environnement de production.
Cet outil, en éliminant la complexité et les incertitudes liées à la configuration des environnements de test, permet aux développeurs de se concentrer sur ce qui compte vraiment : écrire un code de haute qualité qui fonctionne aussi bien en test qu'en production.


## Sources
- [Getting Started with Testcontainers](https://testcontainers.com/guides/getting-started-with-testcontainers-for-java/)
- [JUnit 5 Annotations](https://junit.org/junit5/docs/current/user-guide/#writing-tests-annotations)
- [Younup](https://www.younup.fr/blog/tests-integration-avec-springboot-docker-testcontainers)
- [IBM Community](https://community.ibm.com/community/user/integration/blogs/aritra-das-bairagya/2023/08/14/testcontainers-in-a-java-spring-boot-application)
- [Baeldung on JUnit 5 Test Order](https://www.baeldung.com/junit-5-test-order)
- [Mkyong on Spring Boot Testcontainers](https://mkyong.com/spring-boot/spring-boot-testcontainers-example/)

#### **Contact**

>Contactez-moi sur [LinkedIn](https://www.linkedin.com/in/mouloud-haouili/) ou par mail à mouloud.haouili@ouidou.fr pour toute question.

>Vous vous demandez qui est Ouidou ? N’hésitez pas à nous contacter via <contact@ouidou.fr> ou visiter notre site [https://ouidou.fr](https://ouidou.fr)
