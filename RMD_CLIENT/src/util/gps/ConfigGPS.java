package util.gps;

public class ConfigGPS {

	/**
	 * Este metodo especifica el intervalo de tiempo en milisegundos con el que se desea recibir la señal del gps.
	 */
	public Long intervaloAplicacionCheckeaGPSConfig;

	/**
	 * Especifica la distancia minima en metros que se quiere desde la posicion actual a la Anterior posicion.
	 */
	public Long intervaloMetersBusquedaGPSSignal;

	/**
	 * Especifica el tiempo desde que se activa el gps, que se va a pegar buscando puntos. No cada cuanto los busca, sino durante cuanto tiempo.
	 */
	public Long intervaloRecepcionSignal;

	public void setDefaultValues() {
		intervaloAplicacionCheckeaGPSConfig=new Long(20);
		intervaloRecepcionSignal=new Long(1 * 1000);
		intervaloAplicacionCheckeaGPSConfig=new Long(5 * 1000);
	}

	public ConfigGPS() {
		super();
		setDefaultValues();
	}

}