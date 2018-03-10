package it.unipr.scarpenti.ant.exception;

public class InvalidPathException extends AntGameException {

	private static final long serialVersionUID = -4287888472233168227L;

	public InvalidPathException(Throwable cause) {
		super("Verificare il path del file di output (parametro output_folder) sul file di configurazione config.properties", cause);
	}

}
