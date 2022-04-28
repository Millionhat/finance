package ia2.datagather.finanzas.Model;

import java.io.Serializable;

public class Usuario implements Serializable {
    private String id,username,password;

    public Usuario(String id,String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Usuario() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
