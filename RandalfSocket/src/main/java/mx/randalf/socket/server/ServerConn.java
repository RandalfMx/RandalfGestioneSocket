/*
 * Created on 9-set-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mx.randalf.socket.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import org.apache.log4j.Logger;

import mx.randalf.socket.server.exception.ServerException;

/**
 * @author Randazzo
 *
 */
public class ServerConn
{

	private static Logger log = Logger.getLogger(ServerConn.class);
	private Socket incoming;
	private BufferedReader input;
	private PrintStream output;

	/**
	 * Costruttore
	 * @param myIncoming
	 */
	public ServerConn(Socket myIncoming)
	{
		super();
		
		try
		{
			incoming = myIncoming; 
			input = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
			output = new PrintStream(incoming.getOutputStream());
		}
		catch (IOException e)
		{
			log.error(e);
		}

	}
	
	/**
	 * Questa funzione serve per leggere le informazioni che arrivano dal client
	 * 
	 * @throws ServerException
	 */
	public String Recive() throws ServerException
	{
		String Linea = "";

		try
		{
			// Inizia il Ciclo di Attesa per le informazioni del Server.
			Linea = input.readLine();
			if (Linea == null)
			{
				throw new ServerException(ServerException.CONNDOWN, "Connessione caduta");
			}
		}
		catch (ServerException sExc)
		{
			throw sExc;
		}
		catch (IOException iExc)
		{
			throw new ServerException (ServerException.RECIVEERR, iExc.getMessage());
		}
		return Linea;
	}

	/**
	 * Questo etodo viene utilizzato per inviare un messaggio al client
	 * @param msg 
	 * 
	 * @throws ServerException
	 */
	public void Send(String msg) throws ServerException
	{

		try
		{
			output.println(msg);
		}
		catch (Exception e)
		{
			throw new ServerException(ServerException.CONNERROR, "Problemi durante la trasmissione del messaggio verso il client");
		}
	}

	/**
	 * Questo metodo viene utilizzato per chiudere la connessione con il Client
	 * 
	 */
	public boolean CloseConnection()
	{

		try
		{
			incoming.close();
		}
		catch(Exception e)
		{
			log.error("Problemi di Chiusura del Client");
			log.error(e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 */
	public Socket getIncoming()
	{
		return incoming;
	}

}
