/*
 * Created on 10-set-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mx.randalf.socket.server.exception;

import java.net.SocketException;

/**
 * @author Randazzo
 *
 */
public class ServerException extends SocketException
{
  
	/**
   * 
   */
  private static final long serialVersionUID = -9214780426876641627L;

  /**
	 * Indica che si � verificato un errore durante la procedura di connessione 
	 */
	public static int OPENPORTERROR = 1;
	
	/**
	 * Indica che si � verificato un errore nella procedura che rimane in ascolto sulla porta
	 */
	public static int CONNERROR = 2;

	/**
	 * Indica che si � verificato un errore durante la procedura di ricezione del messaggio
	 */
	public static int RECIVEERR = 3;
	
	/**
	 * Indica che la connessione tra il client e il server � caduta o � stata interrotta
	 */
	public static int CONNDOWN = 4;
	
	/**
	 * Codice ID dell'Errore
	 */
	private int idError = 0;
	
	/**
	 * Costruttore
	 * @param msg
	 */
	public ServerException(String msg)
	{
		super(msg);
	}

	/**
	 * Costruttore
	 * @param id
	 * @param msg
	 */
	public ServerException(int id, String msg)
	{
		super(msg);
		idError = id;
	}

	/**
	 * Costruttore 
	 */
	public ServerException()
	{
		super();
	}
	
	/**
	 * 
	 */
	public int getIdError()
	{
		return idError;
	}

}
