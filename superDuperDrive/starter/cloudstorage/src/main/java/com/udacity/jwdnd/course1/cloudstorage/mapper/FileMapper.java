package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.FileEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
@Mapper
public interface FileMapper {
    @Select("SELECT filename FROM FILES WHERE userid = #{userId}")
    List<String> getFileNamesByUserId(@Param("userId") Long userId);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES " +
            "(#{filename}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    Long insertFile(FileEntity fileEntity);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    FileEntity getFileById(@Param("fileId") Long fileId);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFileById(@Param("fileId") Long fileId);
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    @Results({
            @Result(property = "fileId", column = "fileId"),
            @Result(property = "filename", column = "filename"),
            @Result(property = "contentType", column = "contenttype"),
            @Result(property = "fileSize", column = "filesize"),
            @Result(property = "userId", column = "userid"),
            @Result(property = "fileData", column = "filedata")
    })
    List<FileEntity> findByUserId(@Param("userId") Long userId);
}
