package com.udacity.jwdnd.course1.cloudstorage.model;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class FileForm {
    MultipartFile file;
    //FileEntity file;
    private List<FileEntity> existingFiles;

    public FileForm() {
        System.out.println("fileform");
    }

//    public FileForm(FileEntity file, List<FileEntity> existingFiles) {
//        this.file = file;
//        this.existingFiles = existingFiles;
//    }

    public MultipartFile getFile() {
        return file;
    }
    //public FileEntity getFile() {        return file;    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
    //public void setFile(FileEntity file) {        this.file = file;    }

    public List<FileEntity> getExistingFiles() {
        return existingFiles;
    }

    public void setExistingFiles(List<FileEntity> existingFiles) {
        this.existingFiles = existingFiles;
    }
/*
    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public MultipartFile getFile() {
        return file;
    }

 */
}
