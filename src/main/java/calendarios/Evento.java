package calendarios;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static calendarios.Pending.*;
public class Evento {

  // TODO implementar estado, comportamiento y/o polimorfismo según sea neceario

  private LocalDateTime getInicio() {
    // TODO implementar
    return pending();
  }
  public Duration cuantoFalta() {
    // Este es un ejemplo de cómo se puede obtener una duración
    // Modificar en caso de que sea necesario
    return Duration.ofHours(LocalDateTime.now().until(getInicio(), ChronoUnit.HOURS));
  }

  public boolean estaSolapadoCon(Evento otro) {
    // TODO implementar
    return pending();
  }
}
