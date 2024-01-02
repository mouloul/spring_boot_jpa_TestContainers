package fr.mha.testContainer.utilisateur.repository;

import fr.mha.testContainer.utilisateur.Utilisateur;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilisateurRepository extends CrudRepository<Utilisateur, String> {

    @Query("FROM Utilisateur u WHERE u.nom = ?1")
    List<Utilisateur> getUserByNom(@Param("nom") String nom);
}