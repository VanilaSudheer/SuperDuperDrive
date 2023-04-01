package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileEntity;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
//import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Controller
@RequestMapping("/home")
//@RequestMapping("/home")
public class HomeController {
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

    public HomeController(FileService fileService, UserService userService
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


    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @PostMapping("/upload")
    public String newFile(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
                          @ModelAttribute("newNote") NoteForm addNote,
                          @ModelAttribute("newCredential") CredentialForm newCredential,
                          Model model) {
        if(newFile.getFile().isEmpty()){
            model.addAttribute("result", "error");
            model.addAttribute("message", "There is nothing to upload! Please choose a file to upload");
            return "result";
        }
        String userName = authentication.getName();
        Long userId = userService.getUserIdByUsername(userName);
        MultipartFile multipartFile = newFile.getFile();
        List<FileEntity> files = fileService.getFilesForUser(userName);

        String fileName = multipartFile.getOriginalFilename();
        boolean fileExists = false;
        for (int i = 0; i < files.size(); i++)
            if(files.get(i).getFilename().equals(fileName)){
                fileExists = true;
                break;
            }
        if (!fileExists) {
            try {
                fileService.saveFile(userId, multipartFile);

            } catch (IOException e) {
                //e.printStackTrace();
                model.addAttribute("result", "error");
                model.addAttribute("message", "There was an error uploading file.");
                return "redirect:/result";
            }
             catch (MaxUploadSizeExceededException e){
                model.addAttribute("result", "error");
                model.addAttribute("message", "File size is greater than the limit of 10MB");
                 return "redirect:/result";
            }
            model.addAttribute("result", "success");
        } else {
            model.addAttribute("result", "error");
            model.addAttribute("message", "This is a duplicate file.");
        }
        model.addAttribute("files", fileService.getFilesForUser(userName));
        model.addAttribute("notes", noteService.getAllNotes(userId));
        model.addAttribute("credentials", credentialService.getAllCredentials(userId));
        model.addAttribute("encryptionService", encryptionService);

        return "result";
    }


    @GetMapping(value = "/view-file/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public @ResponseBody
    byte[] getFile(@PathVariable Long fileId) {
        return fileService.getFile(fileId).getFileData();
    }

    @GetMapping("/delete-file/{fileId}")
    public String deleteFile(Authentication authentication, @PathVariable Long fileId,@ModelAttribute("newFile") FileForm File,
                             @ModelAttribute("newNote") NoteForm addNote,
                             @ModelAttribute("newCredential") CredentialForm newCredential,
                             Model model) {
        fileService.deleteFile(fileId);
        String userName = authentication.getName();
        Long userId = userService.getUserIdByUsername(userName);
        model.addAttribute("files", fileService.getFilesForUser(userName));
        model.addAttribute("notes", noteService.getAllNotes(userId));
        model.addAttribute("credentials", credentialService.getAllCredentials(userId));
        model.addAttribute("encryptionService", encryptionService);
        model.addAttribute("result", "success");

        return "result";
    }

}




