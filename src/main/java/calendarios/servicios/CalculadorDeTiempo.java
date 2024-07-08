package calendarios.servicios;

import calendarios.Ubicacion;

import java.time.Duration;

public class CalculadorDeTiempo implements AdapterMaps {

  public CalculadorDeTiempo(GugleMapas gugleMapas) {
    this.gugleMapas = gugleMapas;
  }

  GugleMapas gugleMapas;

  @Override
  public Duration tiempoEstimadoHasta(Ubicacion ubicacion, Ubicacion otraUbicacion) {
    return gugleMapas.tiempoEstimadoHasta(ubicacion, otraUbicacion);
  }
}
