package DTO;

public class ObjetoBusqueda {

    private String nombre;
    private String codigo;
    private String tipoObjeto;
    private String imagen;

    public ObjetoBusqueda(String nombre, String codigo, String tipoObjeto, String imagen) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.tipoObjeto = tipoObjeto;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipoObjeto() {
        return tipoObjeto;
    }

    public void setTipoObjeto(String tipoObjeto) {
        this.tipoObjeto = tipoObjeto;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
