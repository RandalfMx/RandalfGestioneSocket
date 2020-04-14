package mx.randalf.socket.server;

//import java.net.*;
//import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.randalf.socket.server.exception.ServerException;

public class Server
{
	// Questa classe serve per gestire un server.

	/**
   * Questa variabile viene utilizzata per loggare l'applicazione
   */
	private Logger log = LogManager.getLogger(Server.class);

	private ServerSocket Connect;
//	private boolean error;

	public void OpenServer(int port) throws ServerException
	{
		// Questa funzione serve per la Connesione con un server
		try
		{
			// Apro la Connesione Con il Server.
			Connect = new ServerSocket(port);
		}
		catch (SocketException sExc)
		{
			throw new ServerException(ServerException.OPENPORTERROR, sExc.getMessage());
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}

	public Socket OpenConnection() throws ServerException
	{
		Socket incoming = null;
		try
		{
			// Inizializzo le Variabili per il Colloquio con il Server.
			incoming = Connect.accept();
		}
		catch (SocketException sExc)
		{
			throw new ServerException(ServerException.CONNERROR, sExc.getMessage());
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
		}
		return incoming;
	}


	public boolean CloseServer()
	{
		//Questa Funzione serve per Chiudere la Connesione con il Server

		try
		{
			Connect.close();
		}
		catch(Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
		return true;
	}
}
