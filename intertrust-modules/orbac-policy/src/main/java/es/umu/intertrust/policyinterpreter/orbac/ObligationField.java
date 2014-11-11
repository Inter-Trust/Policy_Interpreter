package es.umu.intertrust.policyinterpreter.orbac;

/**
 *
 * @author Juanma
 */
public enum ObligationField {

    SUBJECT(new Operator() {

        public String getValue(Obligation obligation) {
            return obligation.getSubject();
        }

        public void setValue(Obligation obligation, String value) {
            obligation.setSubject(value);
        }
    }),
    ACTION(new Operator() {

        public String getValue(Obligation obligation) {
            return obligation.getAction();
        }

        public void setValue(Obligation obligation, String value) {
            obligation.setAction(value);
        }
    }),
    OBJECT(new Operator() {

        public String getValue(Obligation obligation) {
            return obligation.getObject();
        }

        public void setValue(Obligation obligation, String value) {
            obligation.setObject(value);
        }
    }),
    PARAMETER(new Operator() {

        public String getValue(Obligation obligation) {
            return obligation.getParameter();
        }

        public void setValue(Obligation obligation, String value) {
            obligation.setParameter(value);
        }
    });

    Operator operator;

    private ObligationField(Operator operator) {
        this.operator = operator;
    }

    public String getValue(Obligation obligation) {
        return operator.getValue(obligation);
    }

    public void setValue(Obligation obligation, String value) {
        operator.setValue(obligation, value);
    }

    private interface Operator {

        public String getValue(Obligation obligation);

        public void setValue(Obligation obligation, String value);
    }
}
