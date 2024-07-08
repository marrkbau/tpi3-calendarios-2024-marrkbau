package calendarios.servicios;

import calendarios.Ubicacion;
import calendarios.Usuario;

public interface AdaptadorDePosicion {

  Ubicacion ubicacionActual(Usuario usuario);

}
