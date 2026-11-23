package modelo;

// Representa a un repartidor que hace entregas en bicicleta.
public class Bici extends Repartidor {
    // Entrega el código y el nombre al constructor de Repartidor.
    public Bici(String codigo, String nombre) {
        super(codigo, nombre);
    }

    // Identifica el tipo de repartidor al mostrarlo en el sistema.
    @Override
    public String getTipo() {
        return "Bici";
    }

    // La bici cobra una tarifa base y un valor por kilómetro.
    @Override
    public double calcularCosto(double kilometros) {
        validarKilometros(kilometros);
        return 2000 + kilometros * 800;
    }

    // La bici recorre quince kilómetros por hora.
    @Override
    public double calcularTiempo(double kilometros) {
        validarKilometros(kilometros);
        return kilometros / 15 * 60;
    }

    // La bici solo lleva pedidos pequeños hasta cinco kilómetros y sin lluvia.
    @Override
    public boolean puedeLlevar(double kilometros, boolean pedidoGrande, boolean lluvia) {
        validarKilometros(kilometros);
        return !pedidoGrande && !lluvia && kilometros <= 5;
    }
}
