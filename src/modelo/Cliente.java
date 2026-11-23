package modelo;

// Representa a una persona que puede solicitar pedidos en Domi.
public class Cliente {
    // La cédula identifica al cliente y no cambia después de crearlo.
    private final String cedula;
    private String nombre;
    private String telefono;
    private String barrio;
    private String direccion;

    // El constructor recibe los datos y aplica las validaciones iniciales.
    public Cliente(String cedula, String nombre, String telefono, String barrio, String direccion) {
        this.cedula = validarTexto(cedula, "La cédula");
        setNombre(nombre);
        setTelefono(telefono);
        setBarrio(barrio);
        setDireccion(direccion);
    }

    // Rechaza textos vacíos y quita los espacios sobrantes.
    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    // Permite consultar la cédula sin modificarla.
    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    // El nombre se valida antes de guardarlo.
    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "El nombre");
    }

    public String getTelefono() {
        return telefono;
    }

    // El teléfono se valida antes de guardarlo.
    public void setTelefono(String telefono) {
        this.telefono = validarTelefono(telefono);
    }

    // El teléfono debe tener entre siete y diez dígitos.
    private static String validarTelefono(String telefono) {
        String numero = validarTexto(telefono, "El teléfono");
        if (numero.length() < 7 || numero.length() > 10) {
            throw new IllegalArgumentException("El teléfono debe tener entre 7 y 10 dígitos.");
        }
        // Revisa cada carácter para impedir letras y otros símbolos.
        for (int i = 0; i < numero.length(); i++) {
            if (!Character.isDigit(numero.charAt(i))) {
                throw new IllegalArgumentException("El teléfono solo puede contener números.");
            }
        }
        return numero;
    }

    public String getBarrio() {
        return barrio;
    }

    // El servicio comprobará después si el barrio tiene cobertura.
    public void setBarrio(String barrio) {
        this.barrio = validarTexto(barrio, "El barrio");
    }

    public String getDireccion() {
        return direccion;
    }

    // La dirección no puede quedar vacía.
    public void setDireccion(String direccion) {
        this.direccion = validarTexto(direccion, "La dirección");
    }

    // Valida los tres datos antes de cambiar cualquiera de ellos.
    public void actualizarDatos(String telefono, String barrio, String direccion) {
        String telefonoValidado = validarTelefono(telefono);
        String barrioValidado = validarTexto(barrio, "El barrio");
        String direccionValidada = validarTexto(direccion, "La dirección");
        this.telefono = telefonoValidado;
        this.barrio = barrioValidado;
        this.direccion = direccionValidada;
    }
}
