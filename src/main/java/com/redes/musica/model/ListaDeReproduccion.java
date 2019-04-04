package com.redes.musica.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListaDeReproduccion {
	private String _id;
	private String _rev;
	private String nombre;
	private String descripcion;
	private List<Cancion> canciones;
}
