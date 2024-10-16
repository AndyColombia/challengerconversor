import java.util.Map;

public class Moneda {
    private String base_code;
    private Map<String, Double> conversion_rates;

    // Getter para la moneda base
    public String getBase() {
        return base_code;
    }

    // Getter para las tasas de conversión
    public Map<String, Double> getRates() {
        return conversion_rates;
    }
}
