# Domi, Hackathon 2

Aplicación de consola en Java para registrar clientes y repartidores, cotizar entregas, asignar pedidos y consultar el cierre del día.

## Equipo y video

- Equipo: trabajo individual.
- Integrante y rol: Jonathan David Lozano Calderón, desarrollo y pruebas del proyecto.
- Video demo: [ver grabación](https://drive.google.com/file/d/10McwjsV-TNyYeSm6e0u9m854JTP82Jid/view?usp=sharing).

## Ejecutar en IntelliJ IDEA

1. Abrir esta carpeta como proyecto en IntelliJ IDEA.
2. Configurar un JDK 17 o superior para el proyecto.
3. Abrir `src/app/Main.java` y ejecutar el método `main`.
4. Elegir una opción del menú. Después de cada operación, pulsar Enter para volver al menú.

Al iniciar ya están registrados Laura Gómez, cédula `1020304050`, y Mateo Rincón, cédula `1030405060`. También están disponibles Camila Torres en bici, Andrés Mejía en moto y Jorge Pardo en carro.

## Organización y decisiones

- `modelo` contiene clientes, pedidos, repartidores y sus tipos de vehículo. `Repartidor` es abstracta porque cada vehículo tiene su propia tarifa, velocidad y capacidad.
- `servicio` contiene `DomiService`, que guarda los datos y decide a quién asignar cada pedido.
- `app` contiene `Main`, que lee el teclado y muestra los resultados.
- Los clientes están en un `HashMap` por cédula para buscarlos directamente y evitar claves repetidas.
- Los barrios con cobertura están en un `HashSet` para que no se repitan y sea fácil comprobar si se atienden.
- Las distancias están en un `HashMap` por barrio. El operador no escribe kilómetros porque el servicio toma la distancia registrada y evita errores de captura.
- Los repartidores están en un `ArrayList` para recorrerlos al cotizar. Los pedidos están en otro `ArrayList` para conservar su historial y se buscan por número recorriendo esa lista.
- Los repartidores guardan la suma y la cantidad de calificaciones para calcular el promedio sin almacenar cada nota.

## Lluvia, dron y cierre

`DomiService` guarda si la lluvia está activa y aumenta los costos un 20 %. Cada vehículo decide si puede llevar el pedido. La bici y el dron no trabajan con lluvia.

Para agregar el dron se tocaron dos archivos, `src/modelo/Dron.java` y `src/app/Main.java`. Su clase define tarifa, velocidad y límites, y el menú permite registrarlo. La lógica de cotización y asignación del servicio no necesitó cambios.

El cierre cuenta los pedidos por estado y suma ingresos solo de los entregados. También muestra los ingresos por tipo de repartidor y la mejor calificación promedio entre quienes recibieron al menos una nota.

## Datos al cerrar el programa

Los datos se guardan solo en memoria. Al cerrar y abrir de nuevo se pierden los pedidos y se vuelven a cargar los clientes y repartidores iniciales. Para compartir datos entre dos sedes y operadores haría falta una base de datos común.
