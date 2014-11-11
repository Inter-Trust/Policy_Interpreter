package es.umu.intertrust.policyinterpreter.sds;

import es.umu.intertrust.policyinterpreter.sds.xml.Sds;
import java.util.Properties;

/**
 *
 * @author Juanma
 */
public interface SDSSender {

    public void initialize(Properties deploymentProperties) throws SDSSenderException;

    public void send(Sds sds) throws SDSSenderException;

}
