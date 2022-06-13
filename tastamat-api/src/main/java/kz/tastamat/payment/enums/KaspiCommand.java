package kz.tastamat.payment.enums;


public enum KaspiCommand {
    CHECK, PAY;

    public String key() {
        return this.name().toLowerCase();
    }
}
