package calendarios.servicios;

import calendarios.Ubicacion;
import calendarios.Usuario;

public class PosicionDeUsuario implements AdaptadorDePosicion{



  public PosicionDeUsuario(PositionService positionService) {
    this.positionService = positionService;
  }

  PositionService positionService;
  @Override
  public Ubicacion ubicacionActual(Usuario usuario) {
    return positionService.ubicacionActual(usuario.getMail());
  }
}
