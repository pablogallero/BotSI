!checkingBot.

+!checkingBot <-
	!setupTool("Agenda",BotId).        
	
+!setupTool(Name, Id): true
	<- 	.wait(1000);
		makeArtifact("agenda41","agenda.Agenda41",[],Id);
		focus(Id);
		while(true) {
			.wait(10000);
			bucle;		
		}.
		
+!say(Who,Answer)
	<-	if (.substring("programa",Answer)){programar(Answer);}
	    elif (.substring("muestra",Answer)){mostrar;}
		elif (.substring("anhade",Answer)){anhadir(Answer);}.
		
+recordatorio(Tarea)
	<-  .println("==================================================================");
		.println(Tarea);
		.println("==================================================================");
		if (.substring("anhadir",Tarea) 
			| .substring("muestra",Tarea)
			| .substring("recuerda",Tarea)) {.send(fernandoSainz,achieve,say(agenda,Tarea))}
		-recordatorio(Tarea).
		
+anhadida(Tarea)
	<- 	.println("==================================================================");
		.print("Evento anhadido: ");
		.print(Tarea);
		.println("==================================================================");
		-anhadida(Tarea).
