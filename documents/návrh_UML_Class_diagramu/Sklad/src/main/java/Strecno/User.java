package Strecno;

public class User {
//toto je meno pouzivatela
    //fdsajhfskdj

    private String username;
    private boolean admin;

    public User(String username, boolean admin) {
        this.username = username;
        this.admin = admin;
    }

    public String getName(){
        return username;
    }

    public boolean isAdmin(){
        return admin;
    }
}
