package modelo;

// Define las operaciones necesarias para recibir y consultar calificaciones.
public interface Calificable {
    void recibirCalificacion(int calificacion);

    double obtenerPromedio();
}
