package com.redes.musica.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.redes.musica.model.ListaDeReproduccion;
import com.redes.musica.service.MusicaService;

@RestController
public class MusicaController {
	
	@Autowired
	MusicaService service;
	@RequestMapping("/holaMundo")
	public String holaMundo() {
		return "hola mundo";
	}
	@GetMapping("/songs")
	public List<ListaDeReproduccion> getCanciones() {
		return service.getAll();
	}
}
