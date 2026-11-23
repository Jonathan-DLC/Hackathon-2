package modelo;

import java.util.Set;

// Guarda los datos y el estado de una entrega asignada.
public class Pedido {
    // El número aumenta cada vez que se crea un pedido válido.
    private static int siguienteNumero = 1;
    private static final Set<String> ESTADOS_VALIDOS = Set.of("EN_CAMINO", "ENTREGADO", "CANCELADO");

    private final int numero;
    private final Cliente cliente;
    private final String descripcion;
    private final boolean grande;
    private final double kilometros;
    private final Repartidor repartidor;
    private final double costo;
    private final double tiempoMinutos;
    private String estado;

    // Recibe la cotización elegida y empieza el pedido en camino.
    public Pedido(Cliente cliente, String descripcion, boolean grande, double kilometros,
                  Repartidor repartidor, double costo, double tiempoMinutos) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        if (!Double.isFinite(kilometros) || kilometros <= 0) {
            throw new IllegalArgumentException("La distancia debe ser mayor que cero.");
        }
        if (!Double.isFinite(costo) || costo < 0) {
            throw new IllegalArgumentException("El costo no puede ser negativo.");
        }
        if (!Double.isFinite(tiempoMinutos) || tiempoMinutos <= 0) {
            throw new IllegalArgumentException("El tiempo debe ser mayor que cero.");
        }

        this.cliente = cliente;
        this.descripcion = descripcion.trim();
        this.grande = grande;
        this.kilometros = kilometros;
        this.repartidor = repartidor;
        this.costo = costo;
        this.tiempoMinutos = tiempoMinutos;
        setEstado("EN_CAMINO");
        this.numero = siguienteNumero++;
    }

    // Permite consultar los datos del pedido sin cambiar la cotización.
    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isGrande() {
        return grande;
    }

    public double getKilometros() {
        return kilometros;
    }

    public Repartidor getRepartidor() {
        return repartidor;
    }

    public double getCosto() {
        return costo;
    }

    public double getTiempoMinutos() {
        return tiempoMinutos;
    }

    public String getEstado() {
        return estado;
    }

    // Solo permite pasar de en camino a un estado final una vez.
    public void setEstado(String nuevoEstado) {
        if (nuevoEstado == null || !ESTADOS_VALIDOS.contains(nuevoEstado)) {
            throw new IllegalArgumentException("El estado del pedido no es válido.");
        }
        if (estado == null) {
            if (!nuevoEstado.equals("EN_CAMINO")) {
                throw new IllegalArgumentException("El pedido debe empezar en camino.");
            }
        } else if (!estado.equals("EN_CAMINO")) {
            throw new IllegalArgumentException("El pedido ya está finalizado.");
        } else if (nuevoEstado.equals("EN_CAMINO")) {
            throw new IllegalArgumentException("El pedido ya está en camino.");
        }
        this.estado = nuevoEstado;
    }
}
