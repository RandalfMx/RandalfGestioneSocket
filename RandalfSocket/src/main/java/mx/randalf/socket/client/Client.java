package mx.randalf.socket.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

import mx.randalf.socket.client.exception.ClientException;

/**
 * Questa classe serve per il colloquio con un server.
 * 
 * @author Randazzo Massimiliano
 * 
 */
public class Client
{

	/**
   * Questa variabile viene utilizzata per loggare l'applicazione
   */
	private static Logger log = Logger.getLogger(Client.class);

	/**
   * Questa variabile viene utilizzata per l'apertura della connessione
   */
	private Socket Connect;

	/**
   * Questa variabile viene utilizzata per la gestione del canale di ricezione
   * dati
   */
	private BufferedReader input;

	private InputStream Inp;

	private InputStreamReader isr;
	
	private OutputStream os;
	/**
   * Questa variabile viene utilizzata per la gestione del canale di
   * trasmissione dati
   */
	private PrintStream output;

	/**
   * Questa variabile viene utilizzata per la segnalazione di un eventuale
   * errore
   */
	private boolean error;

	/**
   * Questo metodo viene utilizzato per l'apertura della connessione IP
   * 
   * @param indi
   *          Indirizzo IP da utilizzare nella connessione
   * @param port
   *          Porta da utilizzare nella connessione
   * @return Viene restituito un valore booleano che indica se la connnesione �
   *         stata effetuata
   */
	public boolean Connect(String indi, int port) throws ClientException
	{
		boolean ris = false;
		// Questa funzione serve per la Connesione con un server
		try
		{
			error = false;
			// Apro la Connesione Con il Server.
			log.debug("Apertura connessione " + indi + ":" + port);
			Connect = new Socket(indi, port);

			Connect.setKeepAlive(true);
			log.debug("KeepAlive: "+Connect.getKeepAlive());
			// Inizializzo le Variabili per il Colloquio con il Server.
			log.debug("Connect.getInputStream()");
			Inp = Connect.getInputStream();
			log.debug("InputStreamReader(Inp)");
			isr = new InputStreamReader(Inp);
			log.debug("BufferedReader(isr)");
			input = new BufferedReader(isr);

			log.debug("Connect.getOutputStream()");
			os = Connect.getOutputStream();
			log.debug("PrintStream(os)");
			output = new PrintStream(os);
			log.debug("fine");
			ris = true;
		}
		catch (Exception e)
		{
			error = true;
			log.error(e);
			throw new ClientException("Problemi di connesione con il server [" + indi
					+ ":" + port + "]");
		}
		return ris;
	}

	/**
   * Questo metodo viene utilizzato per leggerte le informazioni che arrivano in
   * risposta aspettando un CRLF
   * 
   * @return Messaggio ricevuto dal server
   */
	public String Recive() throws ClientException
	{
		// Questa funzione serve per leggere le informazioni che arrivano dal
		// Server.
		String Linea = "";

		try
		{
			log.debug("Recive");
			log.debug("error: "+error);
			if (!error)
			{
				log.debug("Recive");
				// Inizia il Ciclo di Attesa per le informazioni del Server.
				while (Linea.equals(""))
				{
					Linea = input.readLine();
					log.debug("Linea: " + Linea);
				}
			}
			else
			{
				// Nel caso in cui ci sia stato un errore in precedenza non eseguo
				// l'operazione richiesta
				Linea = "";
			}
		}
		catch (SocketException e)
		{
			log.error(e);
			throw new ClientException("000", "Connessione  caduta",
					"Connessione caduta");
		}
		catch (Exception e)
		{
			log.error(e);
			Linea = "";
		}
		return Linea;
	}

	/**
   * Questo metodo viene utilizzato per leggerte le informazioni che arrivano in
   * risposta aspettando un CRLF
   * 
   * 
   */
	public void Recive(OutputStream out) throws ClientException
	{
		// Questa funzione serve per leggere le informazioni che arrivano dal
		// Server.
		String linea = "";
		int x=0;
		byte[] c = new byte[100000];
//		byte[] c2 = null;

		try
		{
			if (!error)
			{
				log.debug("Recive");
				
				// Inizia il Ciclo di Attesa per le informazioni del Server.
				while (((x=Inp.read(c))>-1))
				{
					if (c.length==x)
						out.write(c);
					else
					{
						try
						{
							out.write(c,0,x);
						}
						catch (Exception e)
						{

						}
/*						System.out.println("X: "+x);
						c2 = new byte[x];
						for (int y=0; y<x; y++)
						{
							c2[y] = c[y];
						}
						System.out.println("c2: "+new String(c).getBytes());
						out.write(c2);*/
					}
					c = new byte[100000];
					log.debug("x: "+x);
				}
//				out.write(new String(c).getBytes());
				log.debug("Linea: " + linea);
			}
		}
		catch (SocketException e)
		{
			if (Connect != null &&
					Connect.getInetAddress() != null)
				log.error("Connessione: "+Connect.getInetAddress().getHostAddress()+":"+Connect.getInetAddress().getHostName());
			if (Connect != null &&
					Connect.getLocalAddress() != null)
				log.error("Connessione: "+Connect.getLocalAddress().getHostAddress()+":"+Connect.getLocalPort());
			log.error(e);
			throw new ClientException("000", "Connessione  caduta",
					"Connessione caduta");
		}
		catch (Exception e)
		{
			if (Connect != null &&
					Connect.getInetAddress() != null)
				log.error("Connessione: "+Connect.getInetAddress().getHostAddress()+":"+Connect.getInetAddress().getHostName());
			if (Connect != null &&
					Connect.getLocalAddress() != null)
				log.error("Connessione: "+Connect.getLocalAddress().getHostAddress()+":"+Connect.getLocalPort());
			log.error(e);
		}
	}

	/**
   * Questo metodo viene utilizzato per leggerte le informazioni che arrivano in
   * risposta aspettando un carattere di controllo indicato dal termline
   * 
   * @param termline
   *          valore intero che indica il carattere in forma decimale da
   *          utilizzare come carattere di controllo finale della stringa
   * @return Messaggio ricevuto dal server
   */
	public String Recive(int termline) throws ClientException
	{
		// Questa funzione serve per leggere le informazioni che arrivano dal
		// Server.
		String Linea = "";
		boolean Ciclo = true;
		int carattere;

		try
		{
			if (!error)
			{
				log.debug("Recive (int)");
				// Inizia il Ciclo di Attesa per le informazioni del Server.
				while (Ciclo)
				{
					carattere = input.read();
					Linea += (char) carattere;
					if (carattere == termline)
					{
						Ciclo = false;
					}
				}
			}
			else
			{
				// Nel caso in cui ci sia stato un errore in precedenza non eseguo
				// l'operazione richiesta
				Linea = "";
			}
		}
		catch (SocketException e)
		{
			log.error(e);
			throw new ClientException("000", "Connessione  caduta",
					"Connessione caduta");
		}
		catch (Exception e)
		{
			log.error(e);
			Linea = "";
		}
		return Linea;
	}

	/**
   * Questo metodo viene utilizzato per leggerte le informazioni che arrivano in
   * risposta aspettando un carattere di controllo indicato dal termline e un
   * escapeline
   * 
   * @param termline
   *          valore intero che indica il carattere in forma decimale da
   *          utilizzare come carattere di controllo finale della stringa
   * @param escline
   *          valore intero che indica il carattere da utilizzare per verificare
   *          se il carattere termline trovato � finale o no
   * @return Messaggio ricevuto dal server
   */
	public String Recive(int termline, int escline) throws ClientException
	{
		// Questa funzione serve per leggere le informazioni che arrivano dal
		// Server.
		String Linea = "";
		boolean Ciclo = true;
		int carattere;
		int oldCar = 0;

		try
		{
			if (!error)
			{
				log.debug("Recive (int, int)");
//				System.out.println("Connect: "+Connect.getInetAddress().getHostAddress());
//				System.out.println("Recive ("+termline+", "+escline+")");
				// Inizia il Ciclo di Attesa per le informazioni del Server.
				while (Ciclo)
				{
//					System.out.println(Connect.isInputShutdown());
//					System.out.println("ReadLine");
				  carattere = input.read();
//					carattere = Inp.read();

//					System.out.print(carattere+"#"+termline+"@"+escline);
//					System.out.print("*");
					Linea += (char) carattere;
					if (carattere == termline && oldCar != escline)
					{
						Ciclo = false;
					}
					oldCar = carattere;
				}
//				System.out.println();
			}
			else
			{
				// Nel caso in cui ci sia stato un errore in precedenza non eseguo
				// l'operazione richiesta
				Linea = "";
			}
		}
		catch (SocketException e)
		{
			log.error(e);
			throw new ClientException("000", "Connessione  caduta",
					"Connessione caduta");
		}
		catch (Exception e)
		{
			log.error(e);
			Linea = "";
		}
		return Linea;
	}

	/**
   * Questo metodo viene utilizzato per leggerte le informazioni che arrivano in
   * risposta dal server, filtrando tutti i messaggi prendendo unicamente le
   * stringhe che iniziano con la parola inicata in code
   * 
   * @param code
   *          Singola parola utilizzata per il filtro nella risposta
   * @return Messaggio ricevuto dal server
   */
	public String Recive(String code) throws ClientException
	{
		// Questa funzione serve per leggere le informazioni che arrivano dal
		// Server.
		String Linea = "";
		String xCode = "";

		try
		{
			if (!error)
			{
				log.debug("Recive(String)");
				// Inizia il Ciclo di Attesa per le informazioni del Server.
				while (Linea.equals(""))
				{
					Linea = input.readLine();
					log.debug("Linea: "+Linea);
					if (Linea == null)
						Linea = "";
					else
					{
						if (Linea.indexOf(" ") > -1)
							xCode = Linea.substring(0, Linea.indexOf(" "));
						else
							xCode = Linea;
						if (!xCode.equals(code))
						{
							Linea = "";
						}
					}
				}
			}
			else
			{
				// Nel caso in cui ci sia stato un errore in precedenza non eseguo
				// l'operazione richiesta
				Linea = "";
			}
		}
		catch (SocketException e)
		{
			log.error(e);
			throw new ClientException("000", "Connessione  caduta",
					"Connessione caduta");
		}
		catch (Exception e)
		{
			log.error(e);
			Linea = "";
		}
		return Linea;
	}

	/**
   * Questo metodo viene utilizzato per leggerte le informazioni che arrivano in
   * risposta dal server, filtrando tutti i messaggi prendendo unicamente le
   * stringhe che iniziano con la parola inicata in code
   * 
   * @param codeOk
   * @param codeErr
   * @return Messaggio
   */
	public String Recive(String codeOk, Hashtable<String, String> codeErr) throws ClientException
	{
		// Questa funzione serve per leggere le informazioni che arrivano dal
		// Server.
		String Linea = "";
		String code = "";

		try
		{
			if (!error)
			{
				log.debug("Recive(String, Hashtable)");
				// Inizia il Ciclo di Attesa per le informazioni del Server.
				while (true)
				{
					Linea = input.readLine();
					log.debug("Linea: "+Linea);
					if (Linea == null)
					{
						throw new ClientException("Connessione caduta");
					}
					code = Linea.substring(0, Linea.indexOf(" "));
					log.debug("code: "+code);
					if (code.equals(codeOk))
					{
						log.debug("OK");
						break;
					}
					if (codeErr.containsKey(code))
					{
						log.debug("non OK");
						throw new ClientException(code, Linea, (String) codeErr.get(code));
					}
				}
			}
			else
			{
				// Nel caso in cui ci sia stato un errore in precedenza non eseguo
				// l'operazione richiesta
				Linea = "";
			}
		}
		catch (ClientException e)
		{
			log.error(e);
			throw e;
		}
		catch (SocketException e)
		{
			log.error(e);
			throw new ClientException("000", "Connessione  caduta",
					"Connessione caduta");
		}
		catch (Exception e)
		{
			log.error(e);
			Linea = "";
		}
		return Linea;
	}

	/**
   * Questo metodo viene utilizzato per leggerte le informazioni che arrivano in
   * risposta dal server, filtrando tutti i messaggi prendendo unicamente le
   * stringhe che iniziano con la parola inicata in code
   * 
   * @param codeOk
   * @param codeErr
   * @return Messaggio
   */
	public String Recive(Hashtable<String, String> codeOk, Hashtable<String, String> codeErr)
			throws ClientException
	{
		// Questa funzione serve per leggere le informazioni che arrivano dal
		// Server.
		String Linea = "";
		String code = "";

		try
		{
			if (!error)
			{
				log.debug("Recive(Hashtable, Hashtable)");
				// Inizia il Ciclo di Attesa per le informazioni del Server.
				while (true)
				{
					Linea = input.readLine();
					log.debug("Linea: " + Linea);
					if (Linea == null)
					{
						throw new ClientException("Connessione caduta");
					}
					code = Linea.substring(0, Linea.indexOf(" "));
					log.debug("code: " + code);
					if (codeOk.containsKey(code))
					{
						log.debug("OK");
						break;
					}
					if (codeErr.containsKey(code))
					{
						log.debug("non OK");
						throw new ClientException(code, Linea, (String) codeErr.get(code));
					}
				}
			}
			else
			{
				// Nel caso in cui ci sia stato un errore in precedenza non eseguo
				// l'operazione richiesta
				Linea = "";
			}
		}
		catch (ClientException e)
		{
			log.error(e);
			throw e;
		}
		catch (SocketException e)
		{
			log.error(e);
			throw new ClientException("000", "Connessione caduta",
					"Connessione caduta");
		}
		catch (Exception e)
		{
			log.error(e);
			Linea = "";
		}
		return Linea;
	}

	/**
   * Questo metodo serve per l'invio del messagio al server in un CTLF finale
   * 
   * @param msg
   *          Messaggio da inviare al server
   * @return risultato booleano che indica il risultato dell'invio
   */
	public boolean Send(String msg)
	{
		// Questa funzione serve per trasmettere le informazioni al server.

		try
		{
			log.debug("Send");
			log.debug("error: "+error);
			if (!error)
			{
				log.debug("Send(String)");
				log.debug("Msg: " + msg);
				output.println(msg);
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		return true;
	}

	/**
   * Questo metodo serve per l'invio del messagio al server nel quale �
   * possibile specificare se inserire il CTLF a fine messaggio
   * 
   * @param msg
   *          Messaggio da inviare al server
   * @param endline
   *          Valore booleano per la selezione del CTLF finale nel messaggio
   * @return risultato booleano che indica il risultato dell'invio
   */
	public boolean Send(String msg, boolean endline)
	{
		// Questa funzione serve per trasmettere le informazioni al server.

		try
		{
			if (!error)
			{
				log.debug("Send(String, boolean)");
				log.debug("Msg: " + msg);
				if (endline)
				{
					output.println(msg);
				}
				else
				{
					output.print(msg);
				}
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		return true;
	}

	/**
   * Questo metodo serve per eseguire la chiusura della connessione con il
   * server
   * 
   * @return risultato booleano che indica il risultato della chiusura
   */
	public boolean Close()
	{
		// Questa Funzione serve per Chiudere la Connesione con il Server

		try
		{
			if (!error)
			{
				if (output != null)
				{
					output.close();
					output = null;
				}

				if (os != null)
				{
					os.close();
					os = null;
				}

				if (input != null)
				{
					input.close();
					input = null;
				}

				if (isr != null)
				{
					isr.close();
					isr = null;
				}

				if (Inp != null)
				{
					Inp.close();
					Inp = null;
				}
				
				log.debug("Close");
				Connect.close();
				Connect= null;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			log.error(e);
			return false;
		}
		return true;
	}
}
