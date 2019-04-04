package com.redes.musica.service;

import java.util.Collection;
import java.util.List;

import com.redes.musica.model.ListaDeReproduccion;


public interface MusicaService {

	    /**
	     * Get the target db object.
	     * 
	     * @return Database.
	     * @throws Exception 
	     */
	    public Object getDB();

	  
	    /**
	     * Gets all Visitors from the store.
	     * 
	     * @return All ListaDeReproduccion.
	     * @throws Exception 
	     */
	    public List<ListaDeReproduccion> getAll();

	    /**
	     * Gets an individual ToDo from the store.
	     * @param id The ID of the ToDo to get.
	     * @return The ToDo.
	     */
	    public ListaDeReproduccion get(String id);

	   
}
