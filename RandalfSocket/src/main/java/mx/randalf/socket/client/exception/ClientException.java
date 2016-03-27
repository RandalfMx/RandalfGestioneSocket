/*
 * Created on 12-giu-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package mx.randalf.socket.client.exception;

import java.net.SocketException;

/**
 * Questa classe viene utilizzata per gestiore gli errori della classe cliente
 * @author Randazzo
 *
 */
public class ClientException extends SocketException
{

  /**
   * 
   */
  private static final long serialVersionUID = 6778612058820043620L;

  /**
	 * Inidica il codice del messaggio che ha generato l'errore
	 */
	private String code = "";
	
	/**
	 * Testo ricevuto in risposta dal server remoto
	 */
	private String testo = "";
	
	public ClientException()
	{
		super();
	}
	
	public ClientException(String Messaggio)
	{
		super(Messaggio);
	}
	
	/**
	 * Costruttore della classe 
	 * 
	 * @param Code Codice che ha generato l'errore
	 * @param Testo Testo della risposta del messaggio
	 * @param Messaggio Messaggio di Errore
	 */
	public ClientException(String Code, String Testo, String Messaggio)
	{
		super(Messaggio);
		code = Code;
		testo = Testo;
	}
	
	/**
	 * 
	 */
	public String getCode()
	{
		return code;
	}

	/**
	 * 
	 */
	public String getTesto()
	{
		return testo;
	}

}
