package calendarios.servicios;

import calendarios.Ubicacion;
import java.time.Duration;

public class CalculadorDeTiempo implements AdapterMaps {

  GugleMapas gugleMapas;

  public CalculadorDeTiempo(GugleMapas gugleMapas) {
    this.gugleMapas = gugleMapas;
  }

  @Override
  public Duration tiempoEstimadoHasta(Ubicacion ubicacion, Ubicacion otraUbicacion) {
    return gugleMapas.tiempoEstimadoHasta(ubicacion, otraUbicacion);
  }
}
