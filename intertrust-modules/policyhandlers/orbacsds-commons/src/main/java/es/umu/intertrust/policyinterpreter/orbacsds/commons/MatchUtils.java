package es.umu.intertrust.policyinterpreter.orbacsds.commons;

import es.umu.intertrust.policyinterpreter.orbac.Obligation;
import es.umu.intertrust.policyinterpreter.policyhandlers.PolicyHandlerException;
import es.umu.intertrust.policyinterpreter.preconditions.Match;
import es.umu.intertrust.policyinterpreter.sds.SecurityFeature;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Juanma
 */
public class MatchUtils {

    public static Obligation getMatchingObligation(Match match) throws PolicyHandlerException {
        if (match.getMatchingValues().size() > 1) {
            throw new PolicyHandlerException("Invalid match");
        }
        Object matchingValue = match.getMatchingValues().iterator().next();
        return cast(matchingValue, Obligation.class);
    }

    public static List<Obligation> getMatchingObligations(Match match) throws PolicyHandlerException {
        List<Obligation> obligations = new ArrayList<Obligation>();
        for (Object matchingValue : match.getMatchingValues()) {
            obligations.add(cast(matchingValue, Obligation.class));
        }
        return obligations;
    }

    public static SecurityFeature getMatchingSecurityFeature(Match match) throws PolicyHandlerException {
        Collection<SecurityFeature> securityFeatures = match.getMatchingValues(SecurityFeature.class);
        if (securityFeatures.isEmpty()) {
            throw new PolicyHandlerException("No matching security feature found.");
        } else if (securityFeatures.size() > 1) {
            throw new PolicyHandlerException("More than one matching security feature found.");
        } else {
            return securityFeatures.iterator().next();
        }
    }

    public static <T> T cast(Object object, Class<T> targetClass) throws PolicyHandlerException {
        try {
            return (T) object;
        } catch (ClassCastException ex) {
            throw new PolicyHandlerException("Invalid matching element.");
        }
    }

}
