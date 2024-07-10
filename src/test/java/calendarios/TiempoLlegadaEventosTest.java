package calendarios;

import calendarios.servicios.CalculadorDeTiempo;
import calendarios.servicios.GugleMapas;
import calendarios.servicios.PosicionDeUsuario;
import calendarios.servicios.PositionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TiempoLlegadaEventosTest {

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

    // 8. Permitir saber si le usuarie llega al evento más próximo a tiempo, tomando en cuenta la ubicación actual de le usuarie y destino.

    @Test
    void llegaATiempoAlProximoEventoCuandoNoHayEventos() {
        Usuario feli = crearUsuario("feli@gugle.com.ar");
        assertTrue(feli.llegaTiempoAlProximoEvento());
    }

    @Test
    void llegaATiempoAlProximoEventoCuandoHayUnEventoCercano() {
        Usuario feli = crearUsuario("feli@gugle.com.ar");
        Calendario calendario = crearCalendarioVacio();
        feli.agregarCalendario(calendario);

        when(positionService.ubicacionActual("feli@gugle.com.ar")).thenReturn(utnMedrano);

        when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnMedrano)).thenReturn(Duration.ZERO);


        calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

        assertTrue(feli.llegaTiempoAlProximoEvento());
    }

    @Test
    void noLlegaATiempoAlProximoEventoCuandoHayUnEventoFisicamenteLejano() {
        Usuario feli = crearUsuario("feli@gugle.com.ar");
        Calendario calendario = crearCalendarioVacio();
        feli.agregarCalendario(calendario);

        when(positionService.ubicacionActual("feli@gugle.com.ar")).thenReturn(utnCampus);
        when(gugleMapas.tiempoEstimadoHasta(utnCampus, utnMedrano)).thenReturn(Duration.ofMinutes(40));

        calendario.agendar(crearEventoSimpleEnMedrano("Parcial", LocalDateTime.now().plusMinutes(30), Duration.of(2, HOURS)));

        assertFalse(feli.llegaTiempoAlProximoEvento());
    }



    @Test
    void llegaATiempoAlProximoEventoCuandoHayUnEventoCercanoAunqueAlSiguienteNoLlegue() {
        Usuario feli = crearUsuario("feli@gugle.com.ar");
        Calendario calendario = crearCalendarioVacio();
        feli.agregarCalendario(calendario);

        when(positionService.ubicacionActual("feli@gugle.com.ar")).thenReturn(utnMedrano);
        when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnMedrano)).thenReturn(Duration.ofMinutes(0));
        when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnCampus)).thenReturn(Duration.ofMinutes(90));

        calendario.agendar(crearEventoSimpleEnMedrano("Final", LocalDateTime.now().plusMinutes(30), Duration.of(3, HOURS)));
        calendario.agendar(crearEventoSimpleEnCampus("Parcial", LocalDateTime.now().plusMinutes(45), Duration.of(1, HOURS)));

        assertTrue(feli.llegaTiempoAlProximoEvento());
    }

    @Test
    void llegaATiempoAEventoRecurrente() {
        Usuario feli = crearUsuario("feli@gugle.com.ar");
        Calendario calendario = crearCalendarioVacio();
        feli.agregarCalendario(calendario);
        calendario.agendar(crearEventoRecurrenteEnMedrano("clase", LocalDateTime.now().plusMinutes(30), Duration.of(3, HOURS), ChronoUnit.WEEKS, 8));

        when(positionService.ubicacionActual("feli@gugle.com.ar")).thenReturn(utnMedrano);
        when(gugleMapas.tiempoEstimadoHasta(utnMedrano, utnMedrano)).thenReturn(Duration.ofMinutes(0));

        assertTrue(feli.llegaTiempoAlProximoEvento());
    }



    /**
     * @return une usuarie con el mail dado
     */
    Usuario crearUsuario(String email) {
        return new Usuario(email, posicionDeUsuario, calculadorDeTiempo);
    }

    /*
     * @return Un calendario sin ningún evento agendado aún
     */
    Calendario crearCalendarioVacio() {
        return new Calendario();
    }

    Evento crearEventoSimpleEnMedrano(String nombre, LocalDateTime inicio, Duration duracion) {
        return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnMedrano, Collections.emptyList());
    }

    Evento crearEventoSimpleEnCampus(String nombre, LocalDateTime inicio, Duration duracion) {
        return crearEventoSimple("Seguimiento de TPA", inicio, inicio.plus(duracion), utnCampus, Collections.emptyList());
    }

    EventoRecurrente crearEventoRecurrenteEnMedrano(String nombre, LocalDateTime inicio, Duration duracion, ChronoUnit unidad, Integer frecuencia) {
        return crearEventoRecurrente(nombre, inicio, inicio.plus(duracion), utnMedrano, null, unidad, frecuencia);
    }

    /**
     * @return un evento sin invtades que no se repite, que tenga el nombre, fecha de inicio y fin, ubicación dados
     */
    Evento crearEventoSimple(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> usuarios) {
        return new Evento(nombre, ubicacion, inicio, fin, usuarios, null);
    }

    EventoRecurrente crearEventoRecurrente(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> usuarios, ChronoUnit unidad, Integer frecuencia) {
        return new EventoRecurrente(nombre, ubicacion, inicio, fin, usuarios, unidad, frecuencia, null);
    }

}
