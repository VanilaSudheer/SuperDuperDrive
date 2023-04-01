package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.FileEntity;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Base64;
import java.util.List;


@Controller
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;


//    @PostMapping
//    public String handleFileUpload(@ModelAttribute("fileForm") FileForm fileForm, Principal principal, Model model) throws IOException {
//       String username = principal.getName();
//       Long userId = userService.getUserIdByUsername(username);
//       MultipartFile file = (MultipartFile) fileForm.getFile();
//       fileService.saveFile(userId, file);
//       List<FileEntity> files = fileService.getFilesForUser(username);
//       //model.addAttribute("files", files);
//       return "redirect:/home";
//        //return "/";
//    }
}
    /*
     @GetMapping
    public String showUploadForm(Model model, Principal principal) {
        model.addAttribute("fileForm", new FileForm());
        String username = principal.getName();
        List<String> files = fileService.getFilesForUser(username);
        model.addAttribute("files", files);
        return "/";
    }

    @PostMapping("/upload")
    public String uploadFile(@ModelAttribute FileEntity fileEntity) throws IOException {
        byte[] fileData = Base64.getDecoder().decode(fileEntity.getFileData());
        String filename = fileEntity.getFilename();
        Path filepath = Paths.get("/path/to/upload/directory", filename);
        Files.write(filepath, fileData);
        return "upload success";
    }


    @PostMapping("/upload")
    public String handleFileUpload(@ModelAttribute("fileForm") FileForm fileForm, Principal principal,Model model) throws IOException {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        FileEntity file = fileForm.getFile();
        fileService.saveFile(userId, file);
        List<FileEntity> files = fileService.getFilesForUser(username);
        model.addAttribute("files", files);
        // return "redirect:/home";
        return "home";
    }
        String username = principal.getName();
        List<String> files = fileService.getFilesForUser(username);
        model.addAttribute("existingFiles", files);
        return "upload-form";



    @PostMapping
    public String handleFileUpload(@ModelAttribute("fileForm") FileForm fileForm, Principal principal) throws IOException {
        String username = principal.getName();
        Long userId = userService.getUserIdByUsername(username);
        MultipartFile file = fileForm.getFile();
        fileService.saveFile(userId, file);
        return "redirect:/upload";
    }
    @PostMapping(value = "/view/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] viewFile(@PathVariable("fileId") Long fileId, HttpServletResponse response) {
        FileEntity file = fileService.getFile(fileId);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
        response.setContentLength(Integer.parseInt(file.getFileSize()));
        return file.getFileData();
    }
    @PostMapping("/deleteFile/{fileId}")
    public String deleteFile(@PathVariable("fileId") Long fileId, Model model) {
        fileService.deleteFile(fileId);
        model.addAttribute("message", "File deleted successfully!");
        return "success";
    }
}

 */


