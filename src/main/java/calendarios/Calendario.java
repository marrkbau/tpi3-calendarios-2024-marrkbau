package calendarios;

import java.time.LocalDateTime;
import java.util.List;

import static calendarios.Pending.pending;

public class Calendario {

  // TODO implementar estado, comportamiento y/o polimorfismo según sea neceario

  public void agendar(Evento evento) {
    // TODO implementar
    pending();
  }

  public boolean estaAgendado(Evento evento) {
    // TODO implementar
    return pending();
  }

  public List<Evento> eventosEntreFechas(LocalDateTime initio, LocalDateTime fin) {
    // TODO implementar
    return pending();
  }

  public List<Evento> eventosSolapadosCon(Evento evento) {
    // TODO implementar
    return pending();
  }
}
