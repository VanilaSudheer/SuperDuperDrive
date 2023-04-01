package com.udacity.jwdnd.course1.cloudstorage.model;

public class CredentialForm {
    private String url, username, password;
    private Integer credentialid;

    public Integer getCredentialId() {
        return credentialid;
    }

    public void setCredentialId(Integer credentialid) {
        this.credentialid = credentialid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
