package agenda;

import cartago.*;
import java.lang.*;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class Agenda41 extends Artifact {
	
	private ArrayList<String> eventos;
	private boolean avisado;
	private TimeUnit time;
	
	void init() {
		avisado = false;
		eventos = new ArrayList<>();
		time = TimeUnit.SECONDS;
		
		//Definicion de eventos
		eventos.add("anhadir la tarea talf");
		eventos.add("recuerda que el gran premio de monaco es este fin de semana");
		eventos.add("recuerda hacer la agenda de si");
		eventos.add("muestra los pilotos del mundial de este a�o");
		eventos.add("muestra la informacion de cesar");
	}
	
	@OPERATION void bucle() {
		if (!eventos.isEmpty()) {
			signal("recordatorio",eventos.remove(0));
			avisado = true;
		}
		else if (eventos.isEmpty() && avisado) {
			signal("recordatorio", "No hay eventos pendientes.\n"+ 
			"Para introducir nuevos eventos escribe 'Agenda: anhade' y tu evento a continuacion.\n"+
			"Puedes programar eventos escribiendo 'Agenda: programa X para dentro de Y, y el evento de producira al cabo de Y segundos.");
			avisado = false;
		}
	}
	
	@OPERATION void mostrar() {
		if (!eventos.isEmpty()) {
			String recordatorios = "\n";
			for(int i = 0; i < eventos.size(); i++) {
				recordatorios += (i+1) +". "+eventos.get(i)+ " \n";
			}
			signal("recordatorio",recordatorios);
		} else {
			signal("recordatorio", "No hay eventos pendientes.\n"+ 
			"Para introducir nuevos eventos escribe 'Agenda: anhade' y tu evento a continuacion.\n"+
			"Puedes programar eventos escribiendo 'Agenda: programa X para dentro de Y, y el evento de producira al cabo de Y segundos.");
		}
	}
	
	@OPERATION void anhadir(String evento) {
		evento = evento.replace("Agenda: anhade", "");
		eventos.add(evento);
		signal("anhadida", evento);
	}
	
	@OPERATION void programar(String evento) {
		int tiempo = 0;
		String temp = evento.replaceAll("[^\\d]", "");
		try {
			tiempo = Integer.valueOf(temp);
		} catch(Exception e){signal("recordatorio","error");}
		evento = evento.replace("Agenda: programa", "");
		evento = evento.replace("para dentro de", "");
		evento = evento.replaceAll("[0-9]+", "");
		try {
			time.sleep(tiempo);
		} catch(Exception e){}
		if (evento.contains("anhade")) anhadir(evento);
		else if (evento.contains("muestra")) mostrar();
		else signal("recordatorio",evento);
	}
}
