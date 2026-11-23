package modelo;

// Representa a un repartidor que hace entregas en moto.
public class Moto extends Repartidor {
    // Entrega el código y el nombre al constructor de Repartidor.
    public Moto(String codigo, String nombre) {
        super(codigo, nombre);
    }

    // Identifica el tipo de repartidor al mostrarlo en el sistema.
    @Override
    public String getTipo() {
        return "Moto";
    }

    // La moto cobra una tarifa base y un valor por kilómetro.
    @Override
    public double calcularCosto(double kilometros) {
        validarKilometros(kilometros);
        return 3000 + kilometros * 1000;
    }

    // La moto recorre treinta kilómetros por hora.
    @Override
    public double calcularTiempo(double kilometros) {
        validarKilometros(kilometros);
        return kilometros / 30 * 60;
    }

    // La moto lleva pedidos pequeños hasta quince kilómetros, incluso con lluvia.
    @Override
    public boolean puedeLlevar(double kilometros, boolean pedidoGrande, boolean lluvia) {
        validarKilometros(kilometros);
        return !pedidoGrande && kilometros <= 15;
    }
}
