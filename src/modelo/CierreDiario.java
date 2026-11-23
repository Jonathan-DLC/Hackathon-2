package modelo;

import java.util.LinkedHashMap;
import java.util.Map;

// Reúne los resultados del día para que el operador pueda consultarlos.
public class CierreDiario {
    private final int pedidosEnCamino;
    private final int pedidosEntregados;
    private final int pedidosCancelados;
    private final double ingresosTotales;
    private final Map<String, Double> ingresosPorTipo;
    private final Repartidor repartidorEstrella;

    // Conserva los resultados calculados por el servicio.
    public CierreDiario(int pedidosEnCamino, int pedidosEntregados, int pedidosCancelados,
                        double ingresosTotales, Map<String, Double> ingresosPorTipo,
                        Repartidor repartidorEstrella) {
        this.pedidosEnCamino = pedidosEnCamino;
        this.pedidosEntregados = pedidosEntregados;
        this.pedidosCancelados = pedidosCancelados;
        this.ingresosTotales = ingresosTotales;
        this.ingresosPorTipo = new LinkedHashMap<>(ingresosPorTipo);
        this.repartidorEstrella = repartidorEstrella;
    }

    public int getTotalPedidos() {
        return pedidosEnCamino + pedidosEntregados + pedidosCancelados;
    }

    public int getPedidosEnCamino() {
        return pedidosEnCamino;
    }

    public int getPedidosEntregados() {
        return pedidosEntregados;
    }

    public int getPedidosCancelados() {
        return pedidosCancelados;
    }

    public double getIngresosTotales() {
        return ingresosTotales;
    }

    // Devuelve una copia para proteger los ingresos del resumen.
    public Map<String, Double> getIngresosPorTipo() {
        return new LinkedHashMap<>(ingresosPorTipo);
    }

    public Repartidor getRepartidorEstrella() {
        return repartidorEstrella;
    }
}
