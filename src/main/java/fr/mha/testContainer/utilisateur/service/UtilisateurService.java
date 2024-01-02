package fr.mha.testContainer.utilisateur.service;

import fr.mha.testContainer.utilisateur.Utilisateur;

public interface UtilisateurService {
    Utilisateur saveUtilisateur(Utilisateur utilisateur);
    Utilisateur findUtilisateurById(String id);
}