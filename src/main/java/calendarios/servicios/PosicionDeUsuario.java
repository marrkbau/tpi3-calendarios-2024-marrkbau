package calendarios.servicios;

import calendarios.Ubicacion;
import calendarios.Usuario;

public class PosicionDeUsuario implements AdaptadorDePosicion {


  PositionService positionService;

  public PosicionDeUsuario(PositionService positionService) {
    this.positionService = positionService;
  }

  @Override
  public Ubicacion ubicacionActual(Usuario usuario) {
    return positionService.ubicacionActual(usuario.getMail());
  }
}
