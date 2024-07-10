package calendarios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Calendario {

  private List<Evento> eventos;

  public Calendario() {
    this.eventos = new ArrayList<>();
  }

  public List<Evento> getEventos() {
    return new ArrayList<>(eventos);
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


