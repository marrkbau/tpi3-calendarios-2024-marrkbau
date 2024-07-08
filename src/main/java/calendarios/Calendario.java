package calendarios;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Calendario {

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
            .flatMap(evento -> evento.eventosEntreFechas(inicio, fin).stream())
            .collect(Collectors.toList());
  }

  public List<Evento> eventosSolapadosCon(Evento evento) {
    return eventos.stream()
            .filter(evento1 -> !evento1.equals(evento) && evento1.estaSolapadoCon(evento))
            .toList();
  }
}


