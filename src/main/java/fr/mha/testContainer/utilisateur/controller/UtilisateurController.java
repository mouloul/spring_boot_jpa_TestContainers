package fr.mha.testContainer.utilisateur.controller;

import fr.mha.testContainer.utilisateur.Utilisateur;
import fr.mha.testContainer.utilisateur.service.UtilisateurService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {
    private final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }
    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Utilisateur saveUser(@RequestBody Utilisateur user) {
        return utilisateurService.saveUtilisateur(user);
    }
    @GetMapping(path = "/{id}")
    public Utilisateur findUserById(@PathVariable String id) {
        return utilisateurService.findUtilisateurById(id);
    }
}