package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;

@Controller
@RequestMapping("note")
public class NoteController {
    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private NoteService noteService;
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private EncryptionService encryptionService;

    public NoteController(FileService fileService, UserService userService
            , CredentialService credentialService
            ,EncryptionService encryptionService
            , NoteService noteService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService=credentialService;
        this.encryptionService = encryptionService;
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


    private Long getUserId(Authentication authentication) {
        String userName = authentication.getName();
        return userService.getUserIdByUsername(userName);
    }

    @PostMapping("add-note")
    public String addNote(Authentication authentication, @ModelAttribute("newNote") NoteForm addNote,
                          @ModelAttribute("newFile") FileForm File,
                          @ModelAttribute("newCredential") CredentialForm newCredential,
                          Model model) {
        String userName = authentication.getName();
        String newTitle = addNote.getTitle();
        Integer noteId = addNote.getNoteId();
        String newDescription = addNote.getDescription();
        Note [] notes = noteService.getAllNotes(getUserId(authentication));
        boolean noteExists = false;
        for (int i = 0; i < notes.length; i++) {
            if (notes[i].getNoteTitle().equals(newTitle)) {
                noteExists = true;
                break;
            }
        }
        if (!noteExists) {
            if(newTitle.length() > 20){
                model.addAttribute("result", "error");
                model.addAttribute("message", "Note Title cannot exceed 20 characters.");
            }else if(newDescription.length() > 1000){
                model.addAttribute("result", "error");
                model.addAttribute("message", "Note Description cannot exceed 1000 characters.");
            }else {
                if (noteId == null)
                    noteService.addNote(newTitle, newDescription, userName);
                else {
                    Note existingNote = getNote(noteId);
                    noteService.updateNote(existingNote.getNoteId(), newTitle, newDescription);
                }
                model.addAttribute("result", "success");
            }
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "There is a note with the same title.");
        }
        model.addAttribute("credentials", credentialService.getAllCredentials(getUserId(authentication)));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("notes", noteService.getAllNotes(getUserId(authentication)));
        model.addAttribute("files", this.fileService.getFilesForUser(authentication.getName()));
        model.addAttribute("encryptionService", encryptionService);
        return "result";
    }

    @GetMapping("/get-note/{noteId}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNote(noteId);
    }

    @GetMapping("/delete-note/{noteId}")
    public String deleteNote(
            Authentication authentication, @PathVariable Integer noteId,
            @ModelAttribute("newNote") NoteForm addNote,
            @ModelAttribute("newFile") FileForm File,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        noteService.deleteNote(noteId);

        model.addAttribute("credentials", credentialService.getAllCredentials(getUserId(authentication)));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("notes", noteService.getAllNotes(getUserId(authentication)));
        model.addAttribute("files", this.fileService.getFilesForUser(authentication.getName()));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");
        return "result";
    }
}
