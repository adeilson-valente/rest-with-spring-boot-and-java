package br.com.demo.integrationtests.dto;

import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@XmlRootElement
public class TokenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;

    public TokenDTO() {
    }

    public TokenDTO(String username, Boolean authenticated, Date created, Date expiration, String accessToken, String refreshToken) {
        this.username = username;
        this.authenticated = authenticated;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TokenDTO tokenTDO = (TokenDTO) o;
        return Objects.equals(getUsername(), tokenTDO.getUsername()) && Objects.equals(getAuthenticated(), tokenTDO.getAuthenticated()) && Objects.equals(getCreated(), tokenTDO.getCreated()) && Objects.equals(getExpiration(), tokenTDO.getExpiration()) && Objects.equals(getAccessToken(), tokenTDO.getAccessToken()) && Objects.equals(getRefreshToken(), tokenTDO.getRefreshToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getAuthenticated(), getCreated(), getExpiration(), getAccessToken(), getRefreshToken());
    }
}
