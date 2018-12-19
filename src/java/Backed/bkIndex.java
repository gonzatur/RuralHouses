/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Backed;

import DAO.AlojamientoJpaController;
import DAO.PoblacionJpaController;
import DAO.ZonaJpaController;
import DTO.Alojamiento;
import DTO.ObjetoBusqueda;
import DTO.Opinion;
import DTO.Poblacion;
import DTO.Zona;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author gonza
 */
public class bkIndex {

    private EntityManagerFactory emf;
    private AlojamientoJpaController controlAlojamientos;
    private PoblacionJpaController controlPoblaciones;
    private ZonaJpaController controlZonas;
    private ArrayList listadoBaratos = null;
    private ArrayList<Alojamiento> listadoValorados = null;
    private Alojamiento alojamiento;
    private List<Alojamiento> listadoAlojamientos = null;
    private ArrayList listadoPersonasDisponibles = null;

    private ArrayList listadoPueblosBuscados = null;

    public bkIndex() {
        emf = Persistence.createEntityManagerFactory("RuralHousesPU");
        controlAlojamientos = new AlojamientoJpaController(emf);
        controlPoblaciones = new PoblacionJpaController(emf);
        controlZonas = new ZonaJpaController(emf);
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public AlojamientoJpaController getControlAlojamientos() {
        return controlAlojamientos;
    }

    public void setControlAlojamientos(AlojamientoJpaController controlAlojamientos) {
        this.controlAlojamientos = controlAlojamientos;
    }

    public PoblacionJpaController getControlPoblaciones() {
        return controlPoblaciones;
    }

    public void setControlPoblaciones(PoblacionJpaController controlPoblaciones) {
        this.controlPoblaciones = controlPoblaciones;
    }

    public ArrayList getListadoBaratos() {
        return listadoBaratos;
    }

    public void setListadoBaratos(ArrayList listadoBaratos) {
        this.listadoBaratos = listadoBaratos;
    }

    public ArrayList<Alojamiento> getListadoValorados() {
        return listadoValorados;
    }

    public void setListadoValorados(ArrayList<Alojamiento> listadoValorados) {
        this.listadoValorados = listadoValorados;
    }

    public Alojamiento getAlojamiento() {
        return alojamiento;
    }

    public void setAlojamiento(Alojamiento alojamiento) {
        this.alojamiento = alojamiento;
    }

    public List<Alojamiento> getListadoAlojamientos() {
        return listadoAlojamientos;
    }

    public void setListadoAlojamientos(List<Alojamiento> listadoAlojamientos) {
        this.listadoAlojamientos = listadoAlojamientos;
    }

    public ZonaJpaController getControlZonas() {
        return controlZonas;
    }

    public void setControlZonas(ZonaJpaController controlZonas) {
        this.controlZonas = controlZonas;
    }

    public ArrayList getListadoPersonasDisponibles() {
        return listadoPersonasDisponibles;
    }

    public void setListadoPersonasDisponibles(ArrayList listadoPersonasDisponibles) {
        this.listadoPersonasDisponibles = listadoPersonasDisponibles;
    }

    public ArrayList getListadoPueblosBuscados() {
        return listadoPueblosBuscados;
    }

    public void setListadoPueblosBuscados(ArrayList listadoPueblosBuscados) {
        this.listadoPueblosBuscados = listadoPueblosBuscados;
    }

    //METODOS PARA OBTENCION DE DATOS
    public List completeTheme(String query) {
        //Lista que se muestra
        List<ObjetoBusqueda>filteredAloj = new ArrayList();

        //Listado de ALOJAMIENTOS, ZONAS Y POBLACIONES
        List<Alojamiento> allAlojamientos = controlAlojamientos.findAlojamientoEntities();
        List<Zona> allZonas = controlZonas.findZonaEntities();
        List<Poblacion> allPoblaciones = controlPoblaciones.findPoblacionEntities();

        for (int i = 0; i < allAlojamientos.size(); i++) {
            Alojamiento aloj = allAlojamientos.get(i);
            if (aloj.getNombre().toLowerCase().startsWith(query)) {
                ObjetoBusqueda alojamientoBSQ = new ObjetoBusqueda(aloj.getNombre(), aloj.getRegistroTurismo(), "alojamiento", aloj.getImagenPrincipal());
                filteredAloj.add(alojamientoBSQ);
            }
        }

        for (int j = 0; j < allZonas.size(); j++) {
            Zona zona = allZonas.get(j);
            if (zona.getNombreZona().toLowerCase().startsWith(query)) {
                int codigoZona = zona.getCodigoZona();
                String codigo = String.valueOf(codigoZona);
                ObjetoBusqueda zonaBSQ = new ObjetoBusqueda(zona.getNombreZona(),codigo,"zona", "zona.png");
                filteredAloj.add(zonaBSQ);
            }
        }

        for (int i = 0; i < allPoblaciones.size(); i++) {
            Poblacion pobla = allPoblaciones.get(i);
            if (pobla.getNombrePoblacion().toLowerCase().startsWith(query)) {
                int codigoPobla = pobla.getCodigoPoblacion();
                String codigo = String.valueOf(codigoPobla);
                ObjetoBusqueda poblacionBSQ = new ObjetoBusqueda(pobla.getNombrePoblacion(),codigo, "poblacion", "pueblo.png");
                filteredAloj.add(poblacionBSQ);
            }
        }
        return filteredAloj;
    }

    public ArrayList getListaPersonasBuscar() {
        if (listadoPersonasDisponibles == null) {
            listadoPersonasDisponibles = new ArrayList();
            List<Alojamiento> listaAloj = controlAlojamientos.findByPlazasMax();

            int mayorNumPersonas = 0;
            if (listaAloj != null) {
                mayorNumPersonas = listaAloj.get(0).getPlazasMax();
            }

            for (int i = 1; i <= mayorNumPersonas; i++) {

                listadoPersonasDisponibles.add(new SelectItem(i, String.valueOf(i)));

            }
        }

        return listadoPersonasDisponibles;
    }


    public List<Alojamiento> listaAlojamientos() {
        if (listadoAlojamientos == null) {
            listadoAlojamientos = new ArrayList<Alojamiento>();
            List<Alojamiento> listaAloj = controlAlojamientos.findAlojamientoEntities();
            for (Alojamiento aloj : listaAloj) {
                listadoAlojamientos.add(aloj);
            }
        }
        return listadoAlojamientos;
    }

    public ArrayList getMasBaratos() {
        if (listadoBaratos == null) {
            listadoBaratos = new ArrayList();
            List<Alojamiento> listaBaratos = controlAlojamientos.findByPrecio();
            int i = 0;
            for (Alojamiento aloj : listaBaratos) {
                if (i < 5) {
                    listadoBaratos.add(aloj);
                }
                i++;
            }
        }
        return listadoBaratos;
    }

    public ArrayList getPueblosMasBuscados() {
        if (listadoPueblosBuscados == null) {
            listadoPueblosBuscados = new ArrayList();
            List<Poblacion> listaPob = controlPoblaciones.findByNumVisitas();
            int i = 0;
            for (Poblacion pob : listaPob) {
                if (i < 10) {
                    listadoPueblosBuscados.add(pob);
                }
                i++;
            }
        }
        return listadoPueblosBuscados;
    }

    public ArrayList getMasValorados() {

        if (listadoValorados == null) {
            listadoValorados = new ArrayList();
            List<Alojamiento> listadoAlojamiento = controlAlojamientos.findAlojamientoEntities();
            ArrayList<Float> listadoNotas = new ArrayList(5);
            float notita = 0;
            listadoNotas.add(notita);
            listadoNotas.add(notita);
            listadoNotas.add(notita);
            listadoNotas.add(notita);
            listadoNotas.add(notita);

            listadoValorados.add(null);
            listadoValorados.add(null);
            listadoValorados.add(null);
            listadoValorados.add(null);
            listadoValorados.add(null);

            for (int i = 0; i < listadoAlojamiento.size(); i++) {

                Alojamiento aloj = listadoAlojamiento.get(i);
                List listaOpinionesAloj = aloj.getOpinionList();

                float nota = 0;
                float notaMedia = 0;

                for (int j = 0; j < listaOpinionesAloj.size(); j++) {

                    Opinion op = (Opinion) listaOpinionesAloj.get(j);

                    nota = nota + op.getPuntuacionMedia();
                }

                notaMedia = nota / listaOpinionesAloj.size();

                float menorResultado = 0;
                if (listadoNotas.get(0) < notaMedia) {
                    menorResultado = listadoNotas.get(0);
                }
                if (listadoNotas.get(1) < notaMedia) {
                    if (menorResultado > listadoNotas.get(1)) {
                        menorResultado = listadoNotas.get(1);
                    }
                }

                if (listadoNotas.get(2) < notaMedia) {
                    if (menorResultado > listadoNotas.get(2)) {
                        menorResultado = listadoNotas.get(2);
                    }
                }
                if (listadoNotas.get(3) < notaMedia) {
                    if (menorResultado > listadoNotas.get(3)) {
                        menorResultado = listadoNotas.get(3);
                    }
                }
                if (listadoNotas.get(4) < notaMedia) {
                    if (menorResultado > listadoNotas.get(4)) {
                        menorResultado = listadoNotas.get(4);
                    }

                }

                for (int k = 0; k < 5; k++) {

                    boolean encontrado = false;

                    if (listadoNotas.get(0) == menorResultado && !encontrado) {
                        listadoNotas.set(0, notaMedia);
                        listadoValorados.set(0, aloj);
                        encontrado = true;
                    }

                    if (listadoNotas.get(1) == menorResultado && !encontrado) {
                        listadoNotas.set(1, notaMedia);
                        listadoValorados.set(1, aloj);
                        encontrado = true;
                    }
                    if (listadoNotas.get(2) == menorResultado && !encontrado) {
                        listadoNotas.set(2, notaMedia);
                        listadoValorados.set(2, aloj);
                        encontrado = true;
                    }
                    if (listadoNotas.get(3) == menorResultado && !encontrado) {
                        listadoNotas.set(3, notaMedia);
                        listadoValorados.set(3, aloj);
                        encontrado = true;

                    }
                    if (listadoNotas.get(4) == menorResultado && !encontrado) {
                        listadoNotas.set(4, notaMedia);
                        listadoValorados.set(4, aloj);
                        encontrado = true;

                    }
                    k = 5;
                }
            }
        }
        return listadoValorados;
    }

}
