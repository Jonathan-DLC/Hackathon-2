package app;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import modelo.Bici;
import modelo.Carro;
import modelo.CierreDiario;
import modelo.Cliente;
import modelo.Cotizacion;
import modelo.Dron;
import modelo.Moto;
import modelo.Pedido;
import modelo.Repartidor;
import servicio.DomiService;

// Muestra el menú y comunica las opciones del operador con el servicio.
public class Main {
    // Usa separadores colombianos para mostrar costos y tiempos.
    private static final Locale FORMATO_COLOMBIA = Locale.forLanguageTag("es-CO");

    public static void main(String[] args) {
        // El servicio se crea una sola vez para conservar los datos durante la sesión.
        DomiService servicio = new DomiService();
        Scanner scanner = new Scanner(System.in);
        boolean enEjecucion = true;

        while (enEjecucion) {
            mostrarMenu(servicio);
            System.out.print("Elige una opción. ");
            if (!scanner.hasNextLine()) {
                break;
            }
            // Lee la línea completa para evitar problemas con el teclado.
            String entrada = scanner.nextLine();

            try {
                int opcion = Integer.parseInt(entrada.trim());
                switch (opcion) {
                    case 1:
                        registrarCliente(scanner, servicio);
                        break;
                    case 2:
                        mostrarClientes(servicio);
                        break;
                    case 3:
                        editarCliente(scanner, servicio);
                        break;
                    case 4:
                        eliminarCliente(scanner, servicio);
                        break;
                    case 5:
                        registrarRepartidor(scanner, servicio);
                        break;
                    case 6:
                        mostrarRepartidores(servicio);
                        break;
                    case 7:
                        cambiarDisponibilidad(scanner, servicio);
                        break;
                    case 8:
                        crearPedido(scanner, servicio);
                        break;
                    case 9:
                        entregarPedido(scanner, servicio);
                        break;
                    case 10:
                        cancelarPedido(scanner, servicio);
                        break;
                    case 11:
                        mostrarPedidos(servicio);
                        break;
                    case 12:
                        cambiarModoLluvia(servicio);
                        break;
                    case 13:
                        mostrarCierreDiario(servicio);
                        break;
                    case 0:
                        enEjecucion = false;
                        System.out.println("Hasta luego.");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException error) {
                System.out.println("Escribe un número de opción.");
            } catch (IllegalArgumentException error) {
                System.out.println("Error. " + error.getMessage());
            }

            if (enEjecucion && !pausarAntesDelMenu(scanner)) {
                break;
            }
        }

        scanner.close();
    }

    // Agrupa las opciones para facilitar su lectura.
    private static void mostrarMenu(DomiService servicio) {
        System.out.println();
        System.out.println("========================================");
        System.out.println("            DOMI. OPERADOR");
        System.out.println("========================================");
        if (servicio.isLluviaActiva()) {
            System.out.println("MODO LLUVIA ACTIVO");
        }
        System.out.println();
        System.out.println("CLIENTES");
        System.out.println("  1. Registrar cliente");
        System.out.println("  2. Listar clientes");
        System.out.println("  3. Editar cliente");
        System.out.println("  4. Eliminar cliente");
        System.out.println();
        System.out.println("REPARTIDORES");
        System.out.println("  5. Registrar repartidor");
        System.out.println("  6. Listar repartidores");
        System.out.println("  7. Cambiar disponibilidad");
        System.out.println();
        System.out.println("PEDIDOS");
        System.out.println("  8. Crear pedido");
        System.out.println("  9. Entregar pedido");
        System.out.println(" 10. Cancelar pedido");
        System.out.println(" 11. Listar pedidos");
        System.out.println();
        System.out.println("OPERACIÓN");
        System.out.println(" 12. Activar o desactivar lluvia");
        System.out.println(" 13. Cierre diario");
        System.out.println("----------------------------------------");
        System.out.println("  0. Salir");
    }

    // Espera a que el operador termine de leer el resultado.
    private static boolean pausarAntesDelMenu(Scanner scanner) {
        System.out.println();
        System.out.print("Pulsa Enter para volver al menú. ");
        if (!scanner.hasNextLine()) {
            return false;
        }
        scanner.nextLine();
        return true;
    }

    // Activa o desactiva la lluvia para las siguientes cotizaciones.
    private static void cambiarModoLluvia(DomiService servicio) {
        boolean activa = servicio.cambiarModoLluvia();
        if (activa) {
            System.out.println("Modo lluvia activado.");
        } else {
            System.out.println("Modo lluvia desactivado.");
        }
    }

    // Reúne los datos y deja las validaciones al modelo y al servicio.
    private static void registrarCliente(Scanner scanner, DomiService servicio) {
        String cedula = pedirDato(scanner, "Cédula. ");
        String nombre = pedirDato(scanner, "Nombre. ");
        String telefono = pedirDato(scanner, "Teléfono. ");
        String barrio = pedirDato(scanner, "Barrio. ");
        String direccion = pedirDato(scanner, "Dirección. ");
        Cliente cliente = new Cliente(cedula, nombre, telefono, barrio, direccion);
        servicio.registrarCliente(cliente);
        System.out.println("Cliente registrado correctamente.");
    }

    // Pide solo los datos que el operador puede cambiar.
    private static void editarCliente(Scanner scanner, DomiService servicio) {
        String cedula = pedirDato(scanner, "Cédula del cliente. ");
        String telefono = pedirDato(scanner, "Nuevo teléfono. ");
        String barrio = pedirDato(scanner, "Nuevo barrio. ");
        String direccion = pedirDato(scanner, "Nueva dirección. ");
        servicio.editarCliente(cedula, telefono, barrio, direccion);
        System.out.println("Cliente actualizado correctamente.");
    }

    // Pide la cédula y deja al servicio la eliminación del cliente.
    private static void eliminarCliente(Scanner scanner, DomiService servicio) {
        String cedula = pedirDato(scanner, "Cédula del cliente. ");
        servicio.eliminarCliente(cedula);
        System.out.println("Cliente eliminado correctamente.");
    }

    // Crea el tipo elegido y deja al servicio comprobar el código único.
    private static void registrarRepartidor(Scanner scanner, DomiService servicio) {
        String codigo = pedirDato(scanner, "Código. ");
        String nombre = pedirDato(scanner, "Nombre. ");
        String tipo = pedirDato(scanner, "Tipo, Bici, Moto, Carro o Dron. ");

        Repartidor repartidor;
        if (tipo.trim().equalsIgnoreCase("Bici")) {
            repartidor = new Bici(codigo, nombre);
        } else if (tipo.trim().equalsIgnoreCase("Moto")) {
            repartidor = new Moto(codigo, nombre);
        } else if (tipo.trim().equalsIgnoreCase("Carro")) {
            repartidor = new Carro(codigo, nombre);
        } else if (tipo.trim().equalsIgnoreCase("Dron")) {
            repartidor = new Dron(codigo, nombre);
        } else {
            throw new IllegalArgumentException("El tipo debe ser Bici, Moto, Carro o Dron.");
        }

        servicio.registrarRepartidor(repartidor);
        System.out.println("Repartidor registrado correctamente.");
    }

    // Pide el código y muestra el nuevo estado que devuelve el servicio.
    private static void cambiarDisponibilidad(Scanner scanner, DomiService servicio) {
        String codigo = pedirDato(scanner, "Código del repartidor. ");
        boolean disponible = servicio.cambiarDisponibilidad(codigo);
        if (disponible) {
            System.out.println("El repartidor quedó disponible.");
        } else {
            System.out.println("El repartidor quedó no disponible.");
        }
    }

    // Pide el pedido y muestra las cotizaciones antes de anunciar al elegido.
    private static void crearPedido(Scanner scanner, DomiService servicio) {
        String cedula = pedirDato(scanner, "Cédula del cliente. ");
        String descripcion = pedirDato(scanner, "Descripción. ");
        boolean grande = pedirSiNo(scanner, "Pedido grande, sí o no. ");

        List<Cotizacion> cotizaciones = servicio.cotizarPedido(cedula, grande);
        Pedido pedido = servicio.crearPedido(cedula, descripcion, grande);
        System.out.println("Cotizaciones para " + pedido.getCliente().getNombre() + ", "
                + pedido.getCliente().getBarrio() + ", "
                + String.format(FORMATO_COLOMBIA, "%.1f", pedido.getKilometros()) + " km.");
        for (Cotizacion cotizacion : cotizaciones) {
            Repartidor repartidor = cotizacion.getRepartidor();
            System.out.println(repartidor.getCodigo() + " | " + repartidor.getNombre() + " | "
                    + repartidor.getTipo() + " | $" + formatearCosto(cotizacion.getCosto()) + " | "
                    + formatearTiempo(cotizacion.getTiempoMinutos()) + " min.");
        }
        System.out.println("Pedido " + pedido.getNumero() + " asignado a "
                + pedido.getRepartidor().getNombre() + ", " + pedido.getEstado() + ".");
    }

    // Pide el número y anuncia la entrega confirmada por el servicio.
    private static void entregarPedido(Scanner scanner, DomiService servicio) {
        int numero = pedirNumeroPedido(scanner);
        int calificacion = pedirCalificacion(scanner);
        Pedido pedido = servicio.entregarPedido(numero, calificacion);
        System.out.println("Pedido " + pedido.getNumero() + " entregado. "
                + pedido.getRepartidor().getNombre() + " recibió " + calificacion
                + " y quedó disponible.");
    }

    // Pide el número y anuncia la cancelación confirmada por el servicio.
    private static void cancelarPedido(Scanner scanner, DomiService servicio) {
        int numero = pedirNumeroPedido(scanner);
        Pedido pedido = servicio.cancelarPedido(numero);
        System.out.println("Pedido " + pedido.getNumero() + " cancelado. "
                + pedido.getRepartidor().getNombre() + " quedó disponible.");
    }

    // Convierte el número escrito para buscar el pedido correspondiente.
    private static int pedirNumeroPedido(Scanner scanner) {
        String texto = pedirDato(scanner, "Número del pedido. ");
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("El número del pedido debe ser numérico.");
        }
    }

    // Convierte la calificación escrita antes de enviarla al servicio.
    private static int pedirCalificacion(Scanner scanner) {
        String texto = pedirDato(scanner, "Calificación de 1 a 5. ");
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("La calificación debe ser numérica.");
        }
    }

    // Convierte la respuesta del operador en un valor verdadero o falso.
    private static boolean pedirSiNo(Scanner scanner, String mensaje) {
        String respuesta = pedirDato(scanner, mensaje).trim();
        if (respuesta.equalsIgnoreCase("si") || respuesta.equalsIgnoreCase("sí")) {
            return true;
        }
        if (respuesta.equalsIgnoreCase("no")) {
            return false;
        }
        throw new IllegalArgumentException("Responde sí o no.");
    }

    // Presenta los costos en pesos con separador de miles.
    private static String formatearCosto(double costo) {
        return String.format(FORMATO_COLOMBIA, "%,.0f", costo);
    }

    // Presenta los tiempos con un decimal para evitar cifras largas.
    private static String formatearTiempo(double tiempo) {
        return String.format(FORMATO_COLOMBIA, "%.1f", tiempo);
    }

    // Lee un dato completo y avisa si se cerró la entrada.
    private static String pedirDato(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        if (!scanner.hasNextLine()) {
            throw new IllegalArgumentException("No se recibieron todos los datos.");
        }
        return scanner.nextLine();
    }

    // El servicio entrega los clientes y Main los muestra.
    private static void mostrarClientes(DomiService servicio) {
        List<Cliente> clientes = servicio.listarClientes();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente.getCedula() + " | " + cliente.getNombre() + " | "
                    + cliente.getTelefono() + " | " + cliente.getBarrio() + " | "
                    + cliente.getDireccion());
        }
    }

    // Cada repartidor informa su tipo y Main muestra su disponibilidad.
    private static void mostrarRepartidores(DomiService servicio) {
        List<Repartidor> repartidores = servicio.listarRepartidores();
        if (repartidores.isEmpty()) {
            System.out.println("No hay repartidores registrados.");
            return;
        }
        for (Repartidor repartidor : repartidores) {
            String estado = "No disponible";
            if (repartidor.isDisponible()) {
                estado = "Disponible";
            }
            String promedio = String.format(FORMATO_COLOMBIA, "%.1f", repartidor.obtenerPromedio());
            System.out.println(repartidor.getCodigo() + " | " + repartidor.getNombre() + " | "
                    + repartidor.getTipo() + " | " + estado + " | Promedio " + promedio);
        }
    }

    // Muestra cada pedido con su repartidor, costo, tiempo y estado.
    private static void mostrarPedidos(DomiService servicio) {
        List<Pedido> pedidos = servicio.listarPedidos();
        if (pedidos.isEmpty()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        for (Pedido pedido : pedidos) {
            Repartidor repartidor = pedido.getRepartidor();
            System.out.println("Pedido " + pedido.getNumero() + " | " + pedido.getCliente().getNombre()
                    + " | " + repartidor.getNombre() + ", " + repartidor.getTipo()
                    + " | $" + formatearCosto(pedido.getCosto()) + " | "
                    + formatearTiempo(pedido.getTiempoMinutos()) + " min | " + pedido.getEstado());
        }
    }

    // Presenta el resumen calculado por el servicio.
    private static void mostrarCierreDiario(DomiService servicio) {
        CierreDiario cierre = servicio.generarCierreDiario();
        System.out.println("CIERRE DIARIO");
        System.out.println("Pedidos totales. " + cierre.getTotalPedidos());
        System.out.println("En camino. " + cierre.getPedidosEnCamino());
        System.out.println("Entregados. " + cierre.getPedidosEntregados());
        System.out.println("Cancelados. " + cierre.getPedidosCancelados());
        System.out.println("Ingresos totales. $" + formatearCosto(cierre.getIngresosTotales()));
        System.out.println("Ingresos por tipo de vehículo.");
        for (Map.Entry<String, Double> ingreso : cierre.getIngresosPorTipo().entrySet()) {
            System.out.println(ingreso.getKey() + ". $" + formatearCosto(ingreso.getValue()));
        }
        Repartidor estrella = cierre.getRepartidorEstrella();
        if (estrella == null) {
            System.out.println("Repartidor estrella. Sin calificaciones.");
        } else {
            System.out.println("Repartidor estrella. " + estrella.getCodigo() + " | "
                    + estrella.getNombre() + " | " + estrella.getTipo() + " | Promedio "
                    + String.format(FORMATO_COLOMBIA, "%.1f", estrella.obtenerPromedio()));
        }
    }
}
