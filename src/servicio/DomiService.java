package servicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import modelo.Bici;
import modelo.Carro;
import modelo.CierreDiario;
import modelo.Cliente;
import modelo.Cotizacion;
import modelo.Moto;
import modelo.Pedido;
import modelo.Repartidor;

// Guarda los datos de Domi y más adelante aplicará las reglas del negocio.
public class DomiService {
    // El mapa permite buscar un cliente usando su cédula.
    private final Map<String, Cliente> clientesPorCedula;
    // El conjunto guarda los barrios con cobertura sin repetirlos.
    private final Set<String> barriosConCobertura;
    // El mapa relaciona cada barrio con su distancia desde el local.
    private final Map<String, Double> distanciasPorBarrio;
    // La lista guarda los repartidores registrados.
    private final List<Repartidor> repartidores;
    // Los pedidos conservan el orden en que fueron creados.
    private final List<Pedido> pedidos;
    // Indica si las cotizaciones deben aplicar las reglas de lluvia.
    private boolean lluviaActiva;

    // Prepara las colecciones y carga los datos de arranque.
    public DomiService() {
        this.clientesPorCedula = new HashMap<>();
        this.barriosConCobertura = new HashSet<>();
        this.distanciasPorBarrio = new HashMap<>();
        this.repartidores = new ArrayList<>();
        this.pedidos = new ArrayList<>();
        this.lluviaActiva = false;
        cargarDatosIniciales();
    }

    // Informa si el modo lluvia está activo.
    public boolean isLluviaActiva() {
        return lluviaActiva;
    }

    // Cambia el modo lluvia y devuelve su nuevo estado.
    public boolean cambiarModoLluvia() {
        lluviaActiva = !lluviaActiva;
        return lluviaActiva;
    }

    // Registra un cliente solo si su cédula es nueva y su barrio tiene cobertura.
    public void registrarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        if (clientesPorCedula.containsKey(cliente.getCedula())) {
            throw new IllegalArgumentException("Ya existe un cliente con esa cédula.");
        }
        if (!barriosConCobertura.contains(cliente.getBarrio())) {
            throw new IllegalArgumentException("El barrio no tiene cobertura.");
        }
        clientesPorCedula.put(cliente.getCedula(), cliente);
    }

    // Busca un cliente por su cédula y devuelve null si no existe.
    public Cliente buscarCliente(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula no puede estar vacía.");
        }
        return clientesPorCedula.get(cedula.trim());
    }

    // Devuelve una lista nueva con los clientes registrados.
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientesPorCedula.values());
    }

    // Edita los datos permitidos de un cliente sin cambiar su cédula.
    public void editarCliente(String cedula, String telefono, String barrio, String direccion) {
        Cliente cliente = buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con esa cédula.");
        }
        if (barrio == null || barrio.trim().isEmpty()) {
            throw new IllegalArgumentException("El barrio no puede estar vacío.");
        }
        if (!barriosConCobertura.contains(barrio.trim())) {
            throw new IllegalArgumentException("El barrio no tiene cobertura.");
        }
        cliente.actualizarDatos(telefono, barrio, direccion);
    }

    // Elimina un cliente existente usando su cédula.
    public void eliminarCliente(String cedula) {
        Cliente cliente = buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con esa cédula.");
        }
        // Un cliente con un pedido en camino debe seguir registrado.
        for (Pedido pedido : pedidos) {
            if (pedido.getCliente() == cliente && pedido.getEstado().equals("EN_CAMINO")) {
                throw new IllegalArgumentException("No se puede eliminar un cliente con un pedido en camino.");
            }
        }
        clientesPorCedula.remove(cliente.getCedula());
    }

    // Registra un repartidor solo si su código todavía no existe.
    public void registrarRepartidor(Repartidor repartidor) {
        if (repartidor == null) {
            throw new IllegalArgumentException("El repartidor no puede ser nulo.");
        }
        if (buscarRepartidor(repartidor.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un repartidor con ese código.");
        }
        repartidores.add(repartidor);
    }

    // Busca un repartidor por código y devuelve null si no existe.
    public Repartidor buscarRepartidor(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código no puede estar vacío.");
        }
        String codigoBuscado = codigo.trim();
        for (Repartidor repartidor : repartidores) {
            if (repartidor.getCodigo().equals(codigoBuscado)) {
                return repartidor;
            }
        }
        return null;
    }

    // Devuelve una lista nueva con los repartidores registrados.
    public List<Repartidor> listarRepartidores() {
        return new ArrayList<>(repartidores);
    }

    // Cambia la disponibilidad y devuelve el nuevo estado del repartidor.
    public boolean cambiarDisponibilidad(String codigo) {
        Repartidor repartidor = buscarRepartidor(codigo);
        if (repartidor == null) {
            throw new IllegalArgumentException("No existe un repartidor con ese código.");
        }
        // La opción manual no puede liberar a quien lleva un pedido activo.
        for (Pedido pedido : pedidos) {
            if (pedido.getRepartidor() == repartidor && pedido.getEstado().equals("EN_CAMINO")) {
                throw new IllegalArgumentException("El repartidor tiene un pedido en camino.");
            }
        }
        boolean nuevaDisponibilidad = !repartidor.isDisponible();
        repartidor.setDisponible(nuevaDisponibilidad);
        return nuevaDisponibilidad;
    }

    // Consulta a cada repartidor disponible y reúne sus cotizaciones.
    public List<Cotizacion> cotizarPedido(String cedula, boolean grande) {
        Cliente cliente = buscarCliente(cedula);
        if (cliente == null) {
            throw new IllegalArgumentException("No existe un cliente con esa cédula.");
        }
        Double kilometros = distanciasPorBarrio.get(cliente.getBarrio());
        if (kilometros == null) {
            throw new IllegalArgumentException("El barrio no tiene cobertura.");
        }

        List<Cotizacion> cotizaciones = new ArrayList<>();
        for (Repartidor repartidor : repartidores) {
            if (!repartidor.isDisponible() || !repartidor.puedeLlevar(kilometros, grande, lluviaActiva)) {
                continue;
            }
            double costo = repartidor.calcularCosto(kilometros);
            if (lluviaActiva) {
                costo *= 1.20;
            }
            double tiempo = repartidor.calcularTiempo(kilometros);
            cotizaciones.add(new Cotizacion(repartidor, costo, tiempo));
        }
        return cotizaciones;
    }

    // Elige el menor costo y usa el menor tiempo cuando hay empate.
    public Pedido crearPedido(String cedula, String descripcion, boolean grande) {
        List<Cotizacion> cotizaciones = cotizarPedido(cedula, grande);
        Cotizacion mejor = null;
        for (Cotizacion cotizacion : cotizaciones) {
            if (mejor == null || cotizacion.getCosto() < mejor.getCosto()
                    || (cotizacion.getCosto() == mejor.getCosto()
                    && cotizacion.getTiempoMinutos() < mejor.getTiempoMinutos())) {
                mejor = cotizacion;
            }
        }

        // Si nadie puede llevarlo no se crea el pedido.
        if (mejor == null) {
            throw new IllegalArgumentException("No hay repartidores disponibles para este pedido.");
        }
        Cliente cliente = buscarCliente(cedula);
        double kilometros = distanciasPorBarrio.get(cliente.getBarrio());
        Pedido pedido = new Pedido(cliente, descripcion, grande, kilometros,
                mejor.getRepartidor(), mejor.getCosto(), mejor.getTiempoMinutos());
        pedidos.add(pedido);
        mejor.getRepartidor().setDisponible(false);
        return pedido;
    }

    // Entrega una copia de la lista para proteger el registro de pedidos.
    public List<Pedido> listarPedidos() {
        return new ArrayList<>(pedidos);
    }

    // Resume los pedidos y cuenta ingresos solo cuando fueron entregados.
    public CierreDiario generarCierreDiario() {
        int pedidosEnCamino = 0;
        int pedidosEntregados = 0;
        int pedidosCancelados = 0;
        double ingresosTotales = 0;
        Map<String, Double> ingresosPorTipo = new LinkedHashMap<>();

        for (Repartidor repartidor : repartidores) {
            ingresosPorTipo.putIfAbsent(repartidor.getTipo(), 0.0);
        }

        for (Pedido pedido : pedidos) {
            if (pedido.getEstado().equals("EN_CAMINO")) {
                pedidosEnCamino++;
            } else if (pedido.getEstado().equals("ENTREGADO")) {
                pedidosEntregados++;
                ingresosTotales += pedido.getCosto();
                String tipo = pedido.getRepartidor().getTipo();
                double ingresos = ingresosPorTipo.getOrDefault(tipo, 0.0);
                ingresosPorTipo.put(tipo, ingresos + pedido.getCosto());
            } else if (pedido.getEstado().equals("CANCELADO")) {
                pedidosCancelados++;
            }
        }

        Repartidor repartidorEstrella = null;
        for (Repartidor repartidor : repartidores) {
            if (repartidor.getCantidadCalificaciones() > 0
                    && (repartidorEstrella == null
                    || repartidor.obtenerPromedio() > repartidorEstrella.obtenerPromedio())) {
                repartidorEstrella = repartidor;
            }
        }

        return new CierreDiario(pedidosEnCamino, pedidosEntregados, pedidosCancelados,
                ingresosTotales, ingresosPorTipo, repartidorEstrella);
    }

    // Busca un pedido por su número y devuelve null si no existe.
    public Pedido buscarPedido(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número del pedido debe ser mayor que cero.");
        }
        for (Pedido pedido : pedidos) {
            if (pedido.getNumero() == numero) {
                return pedido;
            }
        }
        return null;
    }

    // Valida el pedido antes de guardar la calificación y confirmar la entrega.
    public Pedido entregarPedido(int numero, int calificacion) {
        Pedido pedido = buscarPedidoActivo(numero);
        pedido.getRepartidor().recibirCalificacion(calificacion);
        pedido.setEstado("ENTREGADO");
        pedido.getRepartidor().setDisponible(true);
        return pedido;
    }

    // Cancela un pedido en camino y libera al repartidor.
    public Pedido cancelarPedido(int numero) {
        Pedido pedido = buscarPedidoActivo(numero);
        pedido.setEstado("CANCELADO");
        pedido.getRepartidor().setDisponible(true);
        return pedido;
    }

    // Exige que el pedido exista y todavía esté en camino.
    private Pedido buscarPedidoActivo(int numero) {
        Pedido pedido = buscarPedido(numero);
        if (pedido == null) {
            throw new IllegalArgumentException("No existe un pedido con ese número.");
        }
        if (!pedido.getEstado().equals("EN_CAMINO")) {
            throw new IllegalArgumentException("El pedido ya está finalizado.");
        }
        return pedido;
    }

    // Guarda el barrio en la cobertura y registra su distancia.
    private void agregarBarrio(String barrio, double kilometros) {
        barriosConCobertura.add(barrio);
        distanciasPorBarrio.put(barrio, kilometros);
    }

    // Carga los nueve barrios, tres repartidores y dos clientes iniciales.
    private void cargarDatosIniciales() {
        agregarBarrio("Chapinero", 1.5);
        agregarBarrio("Teusaquillo", 3.0);
        agregarBarrio("La Candelaria", 4.5);
        agregarBarrio("Usaquén", 6.0);
        agregarBarrio("Cedritos", 8.0);
        agregarBarrio("Fontibón", 11.0);
        agregarBarrio("Suba", 12.0);
        agregarBarrio("Kennedy", 14.0);
        agregarBarrio("Bosa", 18.0);

        registrarRepartidor(new Bici("R-01", "Camila Torres"));
        registrarRepartidor(new Moto("R-02", "Andrés Mejía"));
        registrarRepartidor(new Carro("R-03", "Jorge Pardo"));

        // Las direcciones son textos de prueba porque la guía no las indica.
        Cliente laura = new Cliente("1020304050", "Laura Gómez", "3001234567",
                "Teusaquillo", "Dirección de prueba en Teusaquillo");
        Cliente mateo = new Cliente("1030405060", "Mateo Rincón", "3109876543",
                "Usaquén", "Dirección de prueba en Usaquén");
        registrarCliente(laura);
        registrarCliente(mateo);
    }
}
