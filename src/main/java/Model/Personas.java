package Model;

import java.util.Objects;

/**
 * Clase que representa una persona con nombre, apellidos y edad.
 * Esta clase incluye métodos para obtener y establecer los atributos
 * de la persona, así como métodos para comparar instancias de
 * personas por sus atributos
 */
public class Personas{

    /** El nombre de la persona. */
    private String nombre;

    /** Los apellidos de la persona. */
    private String apellidos;

    /** La edad de la persona. */
    private int edad;

    /**
     * Crea una nueva instancia de Persona con el nombre, apellidos y edad especificados.
     *
     * @param nombre El nombre de la persona.
     * @param apellidos Los apellidos de la persona.
     * @param edad La edad de la persona.
     */
    public Personas(String nombre, String apellidos, int edad) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.edad = edad;
    }

    /**
     * Obtiene el nombre de la persona.
     *
     * @return El nombre de la persona.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre de la persona.
     *
     * @param nombre El nuevo nombre de la persona.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene los apellidos de la persona.
     *
     * @return Los apellidos de la persona.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos de la persona.
     *
     * @param apellidos Los nuevos apellidos de la persona.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene la edad de la persona.
     *
     * @return La edad de la persona.
     */
    public int getEdad() {
        return edad;
    }

    /**
     * Establece la edad de la persona.
     *
     * @param edad La nueva edad de la persona.
     */
    public void setEdad(int edad) {
        this.edad = edad;
    }

    /**
     * Compara este objeto con otro para determinar si son iguales.
     * Dos objetos Persona son considerados iguales si tienen el mismo
     * nombre, apellidos y edad.
     *
     * @param o El objeto con el que se comparará este objeto.
     * @return true si los objetos son iguales; false en caso contrario.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personas persona = (Personas) o;
        return edad == persona.edad && Objects.equals(nombre, persona.nombre) && Objects.equals(apellidos, persona.apellidos);
    }

    /**
     * Devuelve un valor hash para el objeto Persona.
     * Este valor se utiliza en estructuras de datos como HashMap.
     *
     * @return Un valor hash calculado a partir de los atributos de la persona.
     */
    @Override
    public int hashCode() {
        return Objects.hash(nombre, apellidos, edad);
    }
}