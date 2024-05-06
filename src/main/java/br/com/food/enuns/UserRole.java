package br.com.food.enuns;

public enum UserRole {
    ADMIN("ADMIN"),
    USER("USER"),

    SYSTEM("SYSTEM"),
    CLIENT("CLIENT"),
    RESPONSABLE("RESPONSABLE"),
    MOBILLE("MOBILLE"),
    CONSUMIDOR_FINAL("Consumidor Final");

    private String role;

    UserRole(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }

    public static UserRole fromUserType(UserType userType) {
        switch (userType) {
            case ADMIN:
                return ADMIN;
            case USER:
                return USER;
            case SYSTEM:
                return SYSTEM;
            case CLIENT:
                return CLIENT;
            case RESPONSABLE:
                return RESPONSABLE;
            case MOBILLE:
                return MOBILLE;
            case CONSUMIDOR_FINAL:
                return CONSUMIDOR_FINAL;
            default:
                throw new IllegalArgumentException("Tipo de usuário desconhecido: " + userType);
        }
    }
}
