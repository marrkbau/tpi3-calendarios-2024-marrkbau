package calendarios;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

import calendarios.servicios.CalculadorDeTiempo;
import calendarios.servicios.GugleMapas;
import calendarios.servicios.PosicionDeUsuario;
import calendarios.servicios.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EventosListadosTest {


  private PositionService positionService;
  private GugleMapas gugleMapas;

  private CalculadorDeTiempo calculadorDeTiempo;

  private PosicionDeUsuario posicionDeUsuario;

  Ubicacion utnMedrano = new Ubicacion(-34.5984145, -58.4222096);
  Ubicacion utnCampus = new Ubicacion(-34.6591644,-58.4694862);

  @BeforeEach
  void init() {
    positionService = mock(PositionService.class);
    gugleMapas = mock(GugleMapas.class);
    posicionDeUsuario = new PosicionDeUsuario(positionService);
    calculadorDeTiempo = new CalculadorDeTiempo(gugleMapas);
  }


  // 4. Permitir listar los próximos eventos entre dos fechas


  @Test
  void sePuedeListarUnEventoEntreDosFechasParaUnCalendario() {

    Calendario calendario = new Calendario();
    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2,  HOURS));

    calendario.agendar(tpRedes);

    List<Evento> eventos = calendario.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 4).atStartOfDay());

    assertEquals(Arrays.asList(tpRedes), eventos);
  }


  @Test
  void sePuedeListarUnEventoEntreDosFechasParaUneUsuarie() {
    Usuario rene = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = new Calendario();
    rene.agregarCalendario(calendario);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2,  HOURS));

    calendario.agendar(tpRedes);

    List<Evento> eventos = rene.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 4).atStartOfDay());

    assertEquals(eventos, Arrays.asList(tpRedes));
  }

  @Test
  void noSeListaUnEventoSiNoEstaEntreLasFechasIndicadasParaUneUsuarie() {
    Usuario dani = crearUsuario("dani@gugle.com.ar");
    Calendario calendario = new Calendario();
    dani.agregarCalendario(calendario);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(1, HOURS));

    calendario.agendar(tpRedes);

    List<Evento> eventos = dani.eventosEntreFechas(
        LocalDate.of(2020, 5, 8).atStartOfDay(),
        LocalDate.of(2020, 5, 16).atStartOfDay());

    assertTrue(eventos.isEmpty());
  }


  @Test
  void sePuedenListarMultiplesEventoEntreDosFechasParaUneUsuarieConCoincidenciaParcial() {
    Usuario usuario = crearUsuario("rene@gugle.com.ar");
    Calendario calendario = new Calendario();
    usuario.agregarCalendario(calendario);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2,  HOURS));
    Evento tpDeGestion = crearEventoSimpleEnMedrano("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(2,  HOURS));
    Evento tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(2,  HOURS));

    calendario.agendar(tpRedes);
    calendario.agendar(tpDeGestion);
    calendario.agendar(tpDeDds);

    List<Evento> eventos = usuario.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDate.of(2020, 4, 6).atStartOfDay());

    assertEquals(eventos, Arrays.asList(tpRedes, tpDeGestion));
  }


  @Test
  void sePuedenListarMultiplesEventoEntreDosFechasParaUneUsuarieConCoincidenciaTotal() {
    Usuario juli = crearUsuario("juli@gugle.com.ar");
    Calendario calendario = new Calendario();
    juli.agregarCalendario(calendario);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));
    Evento tpDeGestion = crearEventoSimpleEnMedrano("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(30, MINUTES));
    Evento tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(1, HOURS));

    calendario.agendar(tpRedes);
    calendario.agendar(tpDeGestion);
    calendario.agendar(tpDeDds);

    List<Evento> eventos = juli.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDateTime.of(2020, 4, 12, 21, 0));

    assertEquals(eventos, Arrays.asList(tpRedes, tpDeGestion, tpDeDds));
  }

  @Test
  void sePuedenListarEventosDeMultiplesCalendarios() {
    Usuario juli = crearUsuario("juli@gugle.com.ar");

    Calendario calendarioFacultad = new Calendario();
    juli.agregarCalendario(calendarioFacultad);

    Calendario calendarioLaboral = new Calendario();
    juli.agregarCalendario(calendarioLaboral);

    Evento tpRedes = crearEventoSimpleEnMedrano("TP de Redes", LocalDateTime.of(2020, 4, 3, 16, 0), Duration.of(2, HOURS));
    Evento tpDeGestion = crearEventoSimpleEnMedrano("TP de Gestión", LocalDateTime.of(2020, 4, 5, 18, 30), Duration.of(30, MINUTES));
    Evento tpDeDds = crearEventoSimpleEnMedrano("TP de DDS", LocalDateTime.of(2020, 4, 12, 16, 0), Duration.of(1, HOURS));

    calendarioFacultad.agendar(tpRedes);
    calendarioFacultad.agendar(tpDeGestion);
    calendarioFacultad.agendar(tpDeDds);

    Evento testearCodigoAntesDePushearAProduccion = crearEventoSimple("Test de codigo pre produ", LocalDateTime.of(2020, 4, 3, 10, 0), LocalDateTime.of(2020, 4, 3, 10, 0).plus(Duration.of(2, HOURS)), null, null);

    calendarioLaboral.agendar(testearCodigoAntesDePushearAProduccion);

    List<Evento> eventos = juli.eventosEntreFechas(
        LocalDate.of(2020, 4, 1).atStartOfDay(),
        LocalDateTime.of(2020, 4, 12, 21, 0));

    assertEquals(eventos, Arrays.asList(tpRedes, tpDeGestion, tpDeDds, testearCodigoAntesDePushearAProduccion));
  }



  Usuario crearUsuario(String email) {
    return new Usuario(email, posicionDeUsuario, calculadorDeTiempo);
  }
  Calendario crearCalendarioVacio() {
    return new Calendario();
  }

  Evento crearEventoSimpleEnMedrano(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnMedrano, Collections.emptyList());
  }

  Evento crearEventoSimpleEnCampus(String nombre, LocalDateTime inicio, Duration duracion) {
    return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnCampus, Collections.emptyList());
  }

  /**
   * @return un evento sin invtades que no se repite, que tenga el nombre, fecha de inicio y fin, ubicación dados
   */
  Evento crearEventoSimple(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> usuarios) {
    return new Evento(nombre, ubicacion, inicio, fin, usuarios);
  }


}
