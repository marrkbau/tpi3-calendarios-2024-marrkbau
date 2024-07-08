package calendarios;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static calendarios.Pending.pending;

public class Calendario {

  // TODO implementar estado, comportamiento y/o polimorfismo según sea neceario
  private List<Evento> eventos;

  public List<Evento> getEventos() {
    return eventos;
  }

  public Calendario() {
    this.eventos = new ArrayList<>();
  }

  public void agendar(Evento evento) {
    eventos.add(evento);
  }

  public boolean estaAgendado(Evento evento) {
    return eventos.contains(evento);
  }

  public List<Evento> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
    return eventos.stream()
        .filter(evento -> evento.getHoraInicio().isAfter(inicio) && evento.getHoraInicio().isBefore(fin))
        .toList();
  }


  public List<Evento> eventosSolapadosCon(Evento evento) {
    return eventos.stream()
        .filter(e -> !e.equals(evento) && e.estaSolapadoCon(evento))
        .toList();
  }
}
