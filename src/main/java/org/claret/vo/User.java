package org.claret.vo;

import java.sql.Timestamp;

/**
 * @author samlv
 */
@SuppressWarnings("unused")
public class User {
    private Integer id;
    private String username ;
    private Timestamp lastLogin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", lastLogin=" + lastLogin +
                '}';
    }
}
