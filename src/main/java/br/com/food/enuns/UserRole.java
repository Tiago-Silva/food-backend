package br.com.food.enuns;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER"),

    SYSTEM("SYSTEM"),
    CLIENT("CLIENT"),
    RESPONSABLE("RESPONSABLE"),
    MOBILLE("MOBILLE");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
