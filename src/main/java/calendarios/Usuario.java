package calendarios;

import calendarios.servicios.GugleMapas;
import calendarios.servicios.PositionService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static calendarios.Pending.*;

public class Usuario {

  // TODO implementar estado, comportamiento y/o polimorfismo según sea neceario

  public void agregarCalendario(Calendario calendario) {
    // TODO implementar
    pending();
  }

  public List<Evento> eventosEntreFechas(LocalDateTime inicio, LocalDateTime fin) {
    // TODO implementar
    return pending();
  }

  public boolean llegaATiempoAlProximoEvento()  {
    // TODO implementar
    return pending();
  }

  public boolean tieneCalendario(Calendario calendario) {
    // TODO implementar
    return pending();
  }
}
