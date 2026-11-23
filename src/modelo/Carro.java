package modelo;

// Representa a un repartidor que hace entregas en carro.
public class Carro extends Repartidor {
    // Entrega el código y el nombre al constructor de Repartidor.
    public Carro(String codigo, String nombre) {
        super(codigo, nombre);
    }

    // Identifica el tipo de repartidor al mostrarlo en el sistema.
    @Override
    public String getTipo() {
        return "Carro";
    }

    // El carro cobra una tarifa base y un valor por kilómetro.
    @Override
    public double calcularCosto(double kilometros) {
        validarKilometros(kilometros);
        return 5000 + kilometros * 1200;
    }

    // El carro recorre veinticinco kilómetros por hora.
    @Override
    public double calcularTiempo(double kilometros) {
        validarKilometros(kilometros);
        return kilometros / 25 * 60;
    }

    // El carro acepta pedidos grandes y no tiene distancia máxima.
    @Override
    public boolean puedeLlevar(double kilometros, boolean pedidoGrande, boolean lluvia) {
        validarKilometros(kilometros);
        return true;
    }
}
