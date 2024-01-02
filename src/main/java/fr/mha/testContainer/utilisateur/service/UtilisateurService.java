package fr.mha.testContainer.utilisateur.service;

import fr.mha.testContainer.utilisateur.Utilisateur;

public interface UtilisateurService {
    Utilisateur saveUser(Utilisateur user);

    Utilisateur findUserById(String id);
}