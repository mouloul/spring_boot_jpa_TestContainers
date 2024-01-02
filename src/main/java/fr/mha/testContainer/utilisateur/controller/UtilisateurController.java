package fr.mha.testContainer.utilisateur.controller;

import fr.mha.testContainer.utilisateur.Utilisateur;
import fr.mha.testContainer.utilisateur.service.UtilisateurService;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    private final UtilisateurService userService;

    public UtilisateurController(UtilisateurService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "")
    @ResponseStatus(HttpStatus.CREATED)
    public Utilisateur saveUser(@RequestBody Utilisateur user) {
        return userService.saveUser(user);
    }

    @GetMapping(path = "/{id}")
    public Utilisateur findUserById(@PathVariable String id) {
        return userService.findUserById(id);
    }
}