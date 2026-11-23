package modelo;

// Guarda el costo y el tiempo que ofrece un repartidor para un pedido.
public class Cotizacion {
    private final Repartidor repartidor;
    private final double costo;
    private final double tiempoMinutos;

    // Recibe los valores calculados por el tipo de repartidor.
    public Cotizacion(Repartidor repartidor, double costo, double tiempoMinutos) {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo.");
        }
        if (!Double.isFinite(costo) || costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }
        if (!Double.isFinite(tiempoMinutos) || tiempoMinutos <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor que cero.");
        }
        this.repartidor = repartidor;
        this.costo = costo;
        this.tiempoMinutos = tiempoMinutos;
    }

    // Permite consultar la cotización sin cambiar sus valores.
    public Repartidor getRepartidor() {
        return repartidor;
    }

    public double getCosto() {
        return costo;
    }

    public double getTiempoMinutos() {
        return tiempoMinutos;
    }
}
