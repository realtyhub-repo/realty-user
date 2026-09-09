package service.user.repository;

import org.springframework.data.jpa.domain.Specification;
import service.user.entity.RolUsuario;
import service.user.entity.Usuario;

public class UsuarioSpecifications {
    public static Specification<Usuario> tieneRol(RolUsuario rol){
        return (root, query, cb) -> rol==null?null:cb.equal(root.get("rol"),rol);
    }

    public static Specification<Usuario> tieneEstado(Boolean activo){
        return (root,query,cb)-> activo==null?null:cb.equal(root.get("activo"),activo);
    }

}
