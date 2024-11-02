package com.example.diskotekee;

public class Usuario {
    // Variable privada estática para almacenar la única instancia de Usuario
    private static Usuario instance;

    // Atributos del usuario
    private long id;
    private String nombre;
    private String apellido;
    private String email;
    private String clave;
    private int matchs;
    private int amistades;

    // Constructor privado para que no pueda ser instanciado desde fuera
    private Usuario() {}

    // Método público para obtener la instancia única de la clase
    public static Usuario getInstance() {
        if (instance == null) {
            instance = new Usuario();
        }
        return instance;
    }

    // Getters y setters para cada atributo
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }

    public int getMatchs() { return matchs; }
    public void setMatchs(int matchs) { this.matchs = matchs; }

    public int getAmistades() { return amistades; }
    public void setAmistades(int amistades) { this.amistades = amistades; }
}
