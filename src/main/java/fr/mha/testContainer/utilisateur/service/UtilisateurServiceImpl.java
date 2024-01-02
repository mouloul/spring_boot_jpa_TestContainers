package fr.mha.testContainer.utilisateur.service;

import fr.mha.testContainer.utilisateur.Utilisateur;
import fr.mha.testContainer.utilisateur.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

@Service

public class UtilisateurServiceImpl implements UtilisateurService{
    private final UtilisateurRepository userRepository;

    public UtilisateurServiceImpl(UtilisateurRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Utilisateur saveUtilisateur(Utilisateur user) {
        return userRepository.save(user);
    }
    @Override
    public Utilisateur findUtilisateurById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
    }
}