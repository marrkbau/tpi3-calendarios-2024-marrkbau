package calendarios.servicios;

import calendarios.Ubicacion;
import java.time.Duration;

public interface AdapterMaps {

  Duration tiempoEstimadoHasta(Ubicacion ubicacion, Ubicacion otraUbicacion);

}
