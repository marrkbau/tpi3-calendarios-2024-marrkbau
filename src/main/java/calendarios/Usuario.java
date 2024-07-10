package calendarios;

import calendarios.servicios.CalculadorDeTiempo;
import calendarios.servicios.EnviadorDeMails;
import calendarios.servicios.PosicionDeUsuario;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Usuario {

  private String mail;
  private List<Calendario> calendarios;
  private PosicionDeUsuario posicionDeUsuario;
  private CalculadorDeTiempo calculadorDeTiempo;

  public Usuario(String mail,
                 PosicionDeUsuario posicionDeUsuario,
                 CalculadorDeTiempo calculadorDeTiempo) {
    this.calendarios = new ArrayList<>();
    this.mail = mail;
    this.posicionDeUsuario = posicionDeUsuario;
    this.calculadorDeTiempo = calculadorDeTiempo;
  }

  public String getMail() {
    return mail;
  }

  public void agregarCalendario(Calendario calendario) {
    calendarios.add(calendario);
  }

  public List<Evento> eventosEntreFechas(LocalDateTime inicio,
                                         LocalDateTime fin) {
    return calendarios.stream()
        .flatMap(calendario -> calendario
            .eventosEntreFechas(inicio, fin).stream())
        .toList();
  }

  public boolean llegaTiempoAlProximoEvento() {
    Evento proximoEvento = proximoEvento();
    if (proximoEvento == null) {
      return true;
    }

    Ubicacion ubicacionActual = posicionDeUsuario.ubicacionActual(this);
    Ubicacion ubicacionEvento = proximoEvento.getUbicacion();
    Duration tiempoEstimadoViaje = calculadorDeTiempo
        .tiempoEstimadoHasta(ubicacionActual, ubicacionEvento);
    Duration tiempoRestanteEvento = proximoEvento.cuantoFalta();


    return tiempoEstimadoViaje.compareTo(tiempoRestanteEvento) <= 0;
  }

  private Evento proximoEvento() {
    List<Evento> todosEventos = calendarios.stream()
        .flatMap(calendario -> calendario.getEventos().stream())
        .toList();
    Evento eventoMasProximo = null;
    Duration menorTiempoRestante = null;

    for (Evento evento : todosEventos) {
      Duration tiempoRestante = evento.cuantoFalta();
      if (tiempoRestante.compareTo(Duration.ZERO) > 0
          && (menorTiempoRestante == null || tiempoRestante
              .compareTo(menorTiempoRestante) < 0)) {
        eventoMasProximo = evento;
        menorTiempoRestante = tiempoRestante;
      }
    }

    return eventoMasProximo;
  }

  public void enviarRecordatoriosPendientes(EnviadorDeMails enviadorMails) {
    calendarios.forEach(calendario -> calendario.getEventos()
        .forEach(evento -> evento
            .enviarRecordatoriosPendientes(enviadorMails)));
  }

  public boolean tieneCalendario(Calendario calendario) {
    return calendarios.contains(calendario);
  }
}
