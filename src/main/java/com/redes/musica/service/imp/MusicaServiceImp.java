package com.redes.musica.service.imp;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cloudant.client.api.ClientBuilder;
import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.redes.musica.model.Cancion;
import com.redes.musica.model.ListaDeReproduccion;
import com.redes.musica.service.MusicaService;
import com.redes.musica.service.VCAPHelper;
@Service
public class MusicaServiceImp implements MusicaService {
	private Database db = null;
	private static final String databaseName = "musica";

	public MusicaServiceImp() {
		CloudantClient cloudant = createClient();
		if (cloudant != null) {
			db = cloudant.database(databaseName, true);
		}
	}

	private static CloudantClient createClient() {

		String url;

		if (System.getenv("VCAP_SERVICES") != null) {
			// When running in IBM Cloud, the VCAP_SERVICES env var will have the
			// credentials for all bound/connected services
			// Parse the VCAP JSON structure looking for cloudant.
			JsonObject cloudantCredentials = VCAPHelper.getCloudCredentials("cloudant");
			if (cloudantCredentials == null) {
				System.out.println("No cloudant database service bound to this application");
				return null;
			}
			url = cloudantCredentials.get("url").getAsString();
		} else if (System.getenv("CLOUDANT_URL") != null) {
			url = System.getenv("CLOUDANT_URL");
		} else {
			System.out.println("Running locally. Looking for credentials in cloudant.properties");
			url = VCAPHelper.getLocalProperties("cloudant.properties").getProperty("cloudant_url");
			if (url == null || url.length() == 0) {
				System.out.println("To use a database, set the Cloudant url in src/main/resources/cloudant.properties");
				return null;
			}
		}

		try {
			System.out.println("Connecting to Cloudant");
			CloudantClient client = ClientBuilder.url(new URL(url)).build();
			return client;
		} catch (Exception e) {
			System.out.println("Unable to connect to database");
			// e.printStackTrace();
			return null;
		}
	}

	@Override
	public Database getDB() {
		// TODO Auto-generated method stub
		return db;
	}

	@Override
	public List<ListaDeReproduccion> getAll() {
		// TODO Auto-generated method stub
		List<ListaDeReproduccion> respuesta = new ArrayList<ListaDeReproduccion>();
		try {
			List<HashMap> allDocs = db.getAllDocsRequestBuilder().includeDocs(true).build().getResponse().getDocsAs(HashMap.class);
			
			for (HashMap doc : allDocs) {
				HashMap<String, Object> obj = db.find(HashMap.class, doc.get("_id") + "");
				ListaDeReproduccion lista = new ListaDeReproduccion();
				LinkedTreeMap<String, Object> attachments = (LinkedTreeMap<String, Object>) obj.get("_attachments");
					
				if (attachments != null && attachments.size() > 0) {
					List<Cancion> attachmentList = getAttachmentList(attachments, obj.get("_id") + "");
					lista.set_id(obj.get("_id").toString());
					lista.setNombre(obj.get("name").toString());
					lista.setDescripcion(obj.get("value").toString());
			
					//jsonObject.add("attachements", attachmentList);
					lista.setCanciones(attachmentList);
				} else {
					lista.set_id("");
					lista.setNombre("");
					lista.setDescripcion("");
				}
				//aqui va el add
				respuesta.add(lista);
			}
		} catch (IOException e) {
			return null;
		}
		return respuesta;
	}
	private List<Cancion> getAttachmentList(LinkedTreeMap<String, Object> attachmentList, String docID) {
		JsonArray attachmentArray = new JsonArray();
		List<Cancion> response = new ArrayList<Cancion>();
		for (Object key : attachmentList.keySet()) {
			LinkedTreeMap<String, Object> attach = (LinkedTreeMap<String, Object>) attachmentList.get(key);
			Cancion cancion = new Cancion();
			JsonObject attachedObject = new JsonObject();
			// set the content type of the attachment
			attachedObject.addProperty("content_type", attach.get("content_type").toString());
			// append the document id and attachment key to the URL
			attachedObject.addProperty("url", "attach?id=" + docID + "&key=" + key);
			// set the key of the attachment
			attachedObject.addProperty("key", key + "");
			cancion.setNombre(docID);
			File f = new File("attach?id=" + docID + "&key=" + key);
			cancion.setCancion(f);
		
			// add the attachment object to the array
			attachmentArray.add(attachedObject);
			response.add(cancion);
		}

		return response;
	}
	@Override
	public ListaDeReproduccion get(String id) {
		// TODO Auto-generated method stub
		return null;
	}

}
