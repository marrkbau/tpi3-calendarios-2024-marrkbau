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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class EventosRecurrentesTest {

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


    // 7. Permitir agendar eventos con repeticiones, con una frecuencia diaria, semanal, mensual o anual

    @Test
    void sePuedenAgendarYListarEventosRecurrrentes() {
        Usuario usuario = crearUsuario("rene@gugle.com.ar");

        LocalDateTime inicio = LocalDateTime.of(2020, 9, 8, 19, 0);
        EventoRecurrente claseDds = crearEventoRecurrente("clase DDS",  inicio,
                inicio.plusMinutes(45), utnMedrano, null, ChronoUnit.WEEKS, 1);

        Calendario calendario = new Calendario();
        calendario.agendar(claseDds);
        usuario.agregarCalendario(calendario);

        List<Evento> eventos = usuario.eventosEntreFechas(
                LocalDateTime.of(2020, 9, 14, 9, 0),
                LocalDateTime.of(2020, 9, 28, 21, 0));

        assertEquals(2, eventos.size());
    }

    @Test
    void unEventoRecurrenteSabeCuantoFaltaParaSuProximaRepeticion() {
        LocalDateTime ahora = LocalDateTime.now();
        EventoRecurrente unRecurrente = crearEventoRecurrente("Evento Recurrente",
                ahora.minusHours(1), ahora,  utnCampus, List.of(), ChronoUnit.DAYS, 15);

        assertTrue(unRecurrente.cuantoFalta().compareTo(Duration.of(15, ChronoUnit.DAYS)) <= 0);
        assertTrue(unRecurrente.cuantoFalta().compareTo(Duration.of(14, ChronoUnit.DAYS)) >= 0);
    }

    /**
     * @return une usuarie con el mail dado
     */
    Usuario crearUsuario(String email) {
        return new Usuario(email, posicionDeUsuario, calculadorDeTiempo);
    }

    EventoRecurrente crearEventoRecurrente(String nombre, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion, List<Usuario> usuarios, ChronoUnit unidad, Integer frecuencia) {
        return new EventoRecurrente(nombre, ubicacion, inicio, fin, usuarios, unidad, frecuencia, null);
    }

}
