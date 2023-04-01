package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
@RequestMapping("credential")
public class CredentialController {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;
    @Autowired
    private  CredentialService credentialService;
    @Autowired
    private  EncryptionService encryptionService;

    public CredentialController(FileService fileService, UserService userService
            , CredentialService credentialService
            ,EncryptionService encryptionService
            , NoteService noteService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService=credentialService;
        this.encryptionService = encryptionService;
    }

    private Long getUserId(Authentication authentication) {
        String userName = authentication.getName();
        return userService.getUserIdByUsername(userName);
    }

    @GetMapping
    public String getHomePage(Authentication authentication,
                              @ModelAttribute("newNote") NoteForm Note,
                              @ModelAttribute("newFile") FileForm File,
                              @ModelAttribute("newCredential") CredentialForm newCredential,
                              Model model,
                              Principal principal)
    {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        model.addAttribute("notes", noteService.getAllNotes(userId));
        model.addAttribute("files", this.fileService.getFilesForUser(username));
        model.addAttribute("credentials", credentialService.getAllCredentials(userId));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @PostMapping("add-credential")
    public String newCredential(Authentication authentication,
                                @ModelAttribute("newNote") NoteForm addNote,
                                @ModelAttribute("newFile") FileForm File,
                                @ModelAttribute("newCredential") CredentialForm newCredential,
                                Model model) {
        String userName = authentication.getName();
        String newUrl = newCredential.getUrl();
        Integer credentialId = newCredential.getCredentialId();
        String password = newCredential.getPassword();

        //Generating salt
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        Credential [] credentials = credentialService.getAllCredentials(getUserId(authentication));
        boolean credentialExists = false;
        for (int i = 0; i < credentials.length; i++) {
            if (credentials[i].getUserName().equals(userName)) {
                credentialExists = true;
                break;
            }
        }
        if (!credentialExists) {
            if(newUrl.length() > 100){
                model.addAttribute("result", "error");
                model.addAttribute("message", "Credential Url cannot exceed 100 characters.");
            }else if(userName.length() > 30){
                model.addAttribute("result", "error");
                model.addAttribute("message", "Credential Username cannot exceed 30 characters.");
            }else if(password.length() > 30){
                model.addAttribute("result", "error");
                model.addAttribute("message", "Credential password cannot exceed 30 characters.");
            }else {
                if (credentialId == null) {
                    credentialService.addCredential(newUrl, userName, newCredential.getUserName(), encodedKey, encryptedPassword);
                } else {
                    Credential existingCredential = getCredential(credentialId);
                    credentialService.updateCredential(existingCredential.getCredentialid(), newCredential.getUserName(), newUrl, encodedKey, encryptedPassword);
                }
                model.addAttribute("result", "success");
            }
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "There is a Credential with the same username.");
        }
        model.addAttribute("credentials", credentialService.getAllCredentials(getUserId(authentication)));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("notes", noteService.getAllNotes(getUserId(authentication)));
        model.addAttribute("files", this.fileService.getFilesForUser(authentication.getName()));
        model.addAttribute("encryptionService", encryptionService);
        return "result";
    }

    @GetMapping("/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential(Authentication authentication, @PathVariable Integer credentialId,
                                   @ModelAttribute("newNote") NoteForm addNote,
                                   @ModelAttribute("newFile") FileForm File,
                                   @ModelAttribute("newCredential") CredentialForm newCredential,
                                   Model model) {
        model.addAttribute("credentials", credentialService.getAllCredentials(getUserId(authentication)));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("notes", noteService.getAllNotes(getUserId(authentication)));
        model.addAttribute("files", this.fileService.getFilesForUser(authentication.getName()));
        model.addAttribute("encryptionService", encryptionService);
        credentialService.deleteCredential(credentialId);
        model.addAttribute("result", "success");
        return "result";
    }
}
