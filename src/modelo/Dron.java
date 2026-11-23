package modelo;

// Representa a un repartidor que hace entregas con dron.
public class Dron extends Repartidor {
    // Entrega el código y el nombre al constructor de Repartidor.
    public Dron(String codigo, String nombre) {
        super(codigo, nombre);
    }

    // Identifica el tipo de repartidor al mostrarlo en el sistema.
    @Override
    public String getTipo() {
        return "Dron";
    }

    // El dron cobra una tarifa base y un valor por kilómetro.
    @Override
    public double calcularCosto(double kilometros) {
        validarKilometros(kilometros);
        return 4000 + kilometros * 600;
    }

    // El dron recorre cincuenta kilómetros por hora.
    @Override
    public double calcularTiempo(double kilometros) {
        validarKilometros(kilometros);
        return kilometros / 50 * 60;
    }

    // El dron solo lleva pedidos pequeños hasta ocho kilómetros y sin lluvia.
    @Override
    public boolean puedeLlevar(double kilometros, boolean pedidoGrande, boolean lluvia) {
        validarKilometros(kilometros);
        return !pedidoGrande && !lluvia && kilometros <= 8;
    }
}
