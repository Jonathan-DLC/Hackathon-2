package modelo;

// Define los datos comunes que comparten todos los repartidores.
public abstract class Repartidor implements Calificable {
    // El código identifica al repartidor y no cambia después de crearlo.
    private final String codigo;
    private String nombre;
    private boolean disponible;
    // La suma y la cantidad permiten calcular el promedio sin guardar una lista.
    private int sumaCalificaciones;
    private int cantidadCalificaciones;

    // Los repartidores comienzan disponibles para recibir pedidos.
    protected Repartidor(String codigo, String nombre) {
        this.codigo = validarTexto(codigo, "El código");
        setNombre(nombre);
        this.disponible = true;
        this.sumaCalificaciones = 0;
        this.cantidadCalificaciones = 0;
    }

    // Rechaza textos vacíos y elimina los espacios sobrantes.
    private static String validarTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException(campo + " no puede estar vacío.");
        }
        return valor.trim();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    // El nombre se valida antes de guardarlo.
    public void setNombre(String nombre) {
        this.nombre = validarTexto(nombre, "El nombre");
    }

    public boolean isDisponible() {
        return disponible;
    }

    // Permite ocupar o liberar al repartidor según el estado del pedido.
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Acepta solo calificaciones entre uno y cinco.
    @Override
    public void recibirCalificacion(int calificacion) {
        if (calificacion < 1 || calificacion > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5.");
        }
        sumaCalificaciones += calificacion;
        cantidadCalificaciones++;
    }

    // Devuelve cero cuando todavía no se ha recibido ninguna calificación.
    @Override
    public double obtenerPromedio() {
        if (cantidadCalificaciones == 0) {
            return 0;
        }
        return (double) sumaCalificaciones / cantidadCalificaciones;
    }

    // Permite saber si el repartidor puede participar como estrella del día.
    public int getCantidadCalificaciones() {
        return cantidadCalificaciones;
    }

    // Cada clase concreta indicará qué tipo de repartidor representa.
    public abstract String getTipo();

    // Rechaza distancias que no sirven para una entrega.
    protected void validarKilometros(double kilometros) {
        if (!Double.isFinite(kilometros) || kilometros <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor que cero.");
        }
    }

    // Cada vehículo calcula el costo con su propia tarifa.
    public abstract double calcularCosto(double kilometros);

    // Cada vehículo calcula el tiempo con su propia velocidad.
    public abstract double calcularTiempo(double kilometros);

    // Cada vehículo decide si puede llevar el pedido según sus límites.
    public abstract boolean puedeLlevar(double kilometros, boolean pedidoGrande, boolean lluvia);
}
