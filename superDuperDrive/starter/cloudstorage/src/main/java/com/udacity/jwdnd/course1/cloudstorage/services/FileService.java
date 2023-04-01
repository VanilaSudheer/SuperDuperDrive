package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.AppUser;
import com.udacity.jwdnd.course1.cloudstorage.model.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService{
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserService userService;

    public List<FileEntity> getFilesForUser(String username) {
        AppUser user = userService.getUser(username);
        Long userId = user.getUserId();
        List<FileEntity> files = fileMapper.findByUserId(userId);
//        List<String> fileNames = new ArrayList<>();
//        for (FileEntity fileEntity : files) {
//            fileNames.add(fileEntity.getFilename());
//        }
        return files;
    }
    public void saveFile(Long userId, MultipartFile file) throws IOException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        FileEntity fileEntity = new FileEntity();
        fileEntity.setUserId(userId);
        fileEntity.setFilename(filename);
        fileEntity.setFileSize(String.valueOf(file.getSize()));
        fileEntity.setContentType(file.getContentType());
        fileEntity.setFileData(file.getBytes());
        fileMapper.insertFile(fileEntity);

    }
    public FileEntity getFile(Long fileId){

        return fileMapper.getFileById(fileId);
    }
    public void deleteFile(Long fileId){
          fileMapper.deleteFileById(fileId);
    }


}



