package es.umu.intertrust.policyinterpreter.dummy;

import es.umu.intertrust.policyinterpreter.sds.SDSDocument;
import es.umu.intertrust.policyinterpreter.sds.SDSSender;

/**
 *
 * @author Juanma
 */
public class DummySDSSender implements SDSSender {

    public void send(SDSDocument sds) {
        System.out.println(sds);
    }
}
