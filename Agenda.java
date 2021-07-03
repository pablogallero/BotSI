// Environment code for project prueba.mas2j
package agenda;

import java.util.LinkedList;
import java.util.Queue;

import cartago.*;
import cartago.tools.*;

import java.io.*;
import java.io.File;   
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;               
import java.net.URL;
import java.net.URLEncoder;      

import java.util.*;
import java.util.Locale;
import java.util.logging.Logger; 
import java.util.Properties;                 
                       
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;              
import javax.mail.Session;
import javax.mail.Transport;              
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
                                      
import javax.speech.*;
import javax.speech.Central;
import javax.speech.recognition.*;   
import javax.speech.synthesis.*;
import javax.speech.synthesis.Voice;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;   

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import org.alicebot.ab.utils.IOUtils;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Insets;
 
import org.json.*;
import org.json.JSONArray;   

public class Agenda extends GUIArtifact {
	

    private MyFrame frame;
    private String actualPath = getResourcesPath();

    Queue<String> eventos=new LinkedList();
	
	public void setup() {
		frame = new MyFrame();
		linkActionEventToOp(frame.okButton,"ok");
		linkKeyStrokeToOp(frame.text,"ENTER","updateText");
		linkWindowClosingEventToOp(frame, "closed");
		defineObsProperty("value",getValue());
		frame.setVisible(true);
	}

	@INTERNAL_OPERATION void ok(ActionEvent ev){
		signal("ok");
	}

	@INTERNAL_OPERATION void closed(WindowEvent ev){
		signal("closed");
	}
	
	@INTERNAL_OPERATION void updateText(ActionEvent ev){
		getObsProperty("value").updateValue(getValue());
	}

	/*@INTERNAL_OPERATION void updateText(ActionEvent ev){
		String texto = Integer.toString(getValue());
		getObsProperty("say").updateValue(texto);
		signal("say",texto);
		java.util.Date fecha = new Date();
		String fechaString = fecha+" : ";
				
		frame.setText("");
		
		appendToPane(frame.textArea, fechaString, Color.DARK_GRAY);
		appendToPane(frame.textArea, texto, Color.DARK_GRAY);
		String salto = System.lineSeparator();
		appendToPane(frame.textArea, salto, Color.DARK_GRAY);
	}*/

	@INTERNAL_OPERATION void actualizar(String mensaje){
		java.util.Date fecha = new Date();
		String fechaString = fecha+" : "+mensaje;

		eventos.add(mensaje);
		
		appendToPane(frame.textArea, fechaString, Color.DARK_GRAY);
		String salto = System.lineSeparator();
		//frame.setText(salto);
		appendToPane(frame.textArea, salto, Color.RED);
		appendToPane(frame.textArea, salto, Color.RED);

	}

	@INTERNAL_OPERATION void comprobarAgenda(){
		
		if(eventos.size()!=0){
			String evento = eventos.remove();
			signal("apuntarAgenda",evento);
			try{
				Thread.sleep(5000);
			}catch(Exception e){
			}
			
		}
		
	}

	@OPERATION void crear () {
		try {
			File archivo = new File(actualPath + File.separator + "tareasPendientes");
			BufferedWriter bw;
		
			if(archivo.exists()) {
				System.out.println("El fichero de texto ya estaba creado.");
			} else {
				archivo.createNewFile();              
				System.out.println("Creado el fichero de texto");
			}
			bw = new BufferedWriter(new FileWriter(archivo,true));
			bw.close();
		}
		catch (Exception eLabel) {
			eLabel.printStackTrace();                                                                   
		};   
	}

	@OPERATION void escribir () {    
		try {                
			File archivo = new File(actualPath + File.separator + "tareasPendientes");
			BufferedWriter bw;
		
			if(archivo.exists()) {
				System.out.println("El fichero de texto "+"tareasPendientes"+" ya estaba creado.");
			} else {
				archivo.createNewFile();              
				System.out.println("Acabo de crear el fichero de texto: "+"tareasPendientes");
			}
			bw = new BufferedWriter(new FileWriter(archivo));
			PrintWriter out = new PrintWriter(bw);
			String texto="";
			texto="TAREAS PENDIENTES: \n";
			if(eventos.size()==0){
				texto = texto + "NO QUEDAN TAREAS PENDIENTES";
			}
			for(int i=0; i<eventos.size(); i++){
				String evento=eventos.remove();
				eventos.add(evento);
				texto = texto + "TAREA " + (i+1) + " -> "+evento+"\n";
			}
			out.println(texto); // Writing the text
			out.close();       // Closing the output
			bw.close();        // CLosing the buffer
          } 
		catch (Exception eLabel) {
			eLabel.printStackTrace();
		};   
	}

	@OPERATION void leerFile() {    
		try {                
			File archivo = new File(actualPath + File.separator + "tareasPendientes");
			FileReader fr = new FileReader(archivo);
     		BufferedReader br = new BufferedReader(fr);
		
			String linea;
			String texto="";
        	while((linea=br.readLine())!=null){
            	texto=texto+"\n"+linea+"\n";
          	}
          	appendToPane(frame.textArea, texto, Color.DARK_GRAY);
			String salto = System.lineSeparator();
			//frame.setText(salto);
			appendToPane(frame.textArea, salto, Color.RED);
          	fr.close();
        } 
        catch (Exception eLabel) {
			eLabel.printStackTrace();                                                                   
		};			  
	}

	private static String getResourcesPath() {
		File currDir = new File(".");
		String path = currDir.getAbsolutePath();
		path = path.substring(0, path.length() - 2);
		//System.out.println(path);
		//logger.info(path);
		String resourcesPath = path + File.separator + "src" + File.separator + "resources";
		return resourcesPath;
	}

	@OPERATION void setValue(int value){
		frame.setText(""+value);
		getObsProperty("value").updateValue(getValue());
	}

	private int getValue(){
		return Integer.parseInt(frame.getText());
	}

	private void appendToPane(JTextPane tp, String msg, Color c)
    {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

	class MyFrame extends JFrame {
		
		private JButton okButton;
		private JTextField text;
		private JTextPane textArea;
		private JScrollPane scroll;
		
		public MyFrame(){

    		java.util.Date fecha = new Date();
			
			setTitle("GUI AGENDA");
			setSize(600,400);
			
			Container contenedor = this.getContentPane();
			contenedor.setLayout(new BorderLayout());

			JPanel panel = new JPanel(new FlowLayout());
			setContentPane(contenedor);
			
			okButton = new JButton("ok");
			okButton.setSize(80,50);
			text = new JTextField(10);

			textArea = new JTextPane();
			textArea.setSize(400,200);
			textArea.setMargin(new Insets(5, 5, 5, 5));
			appendToPane(textArea, "/*  ------------------AGENDA------------------  */", Color.BLUE);
			String salto = System.lineSeparator();
			appendToPane(textArea, salto, Color.BLUE);
			appendToPane(textArea, salto, Color.BLUE);
			int tamano = eventos.size();
			for(int i=0; i<tamano; i++){
				String evento=eventos.remove();
				eventos.add(evento);
				String fechaString = fecha+" : "+evento;
				appendToPane(textArea, fechaString, Color.DARK_GRAY);
				appendToPane(textArea, salto, Color.BLUE);
				appendToPane(textArea, salto, Color.BLUE);
			}

			scroll = new JScrollPane(textArea);
			
			text.setText("0");
			text.setEditable(true);
			contenedor.add(scroll, BorderLayout.CENTER);
			contenedor.add(panel, BorderLayout.SOUTH);
		}

		public String getText(){
			return text.getText();
		}
		public void setText(String s){
			text.setText(s);
		}
	}


}
	



