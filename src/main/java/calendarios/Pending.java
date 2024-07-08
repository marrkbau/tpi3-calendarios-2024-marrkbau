package calendarios;

/**
 * Esta clase existe sólo para hacer mas claros
 * los "huecos" en la implementación. Se puede borrar en la entrega final
 * una vez que el ejercicio esté terminado
 */
public class Pending {

  public static <T> T pending(String message) {
    throw new UnsupportedOperationException(message);
  }

  public static <T> T pending() {
    return pending("Not implemented yet");
  }
}
