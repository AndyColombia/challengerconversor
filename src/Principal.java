import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Principal {
    // URL con tu clave de API personalizada
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/aac43c89717ed91b0fe4ecd8/latest/USD";

    public static void main(String[] args) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("¡Conversor de Monedas Iniciado!");
            parseJson(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseJson(String json) {
        try {
            // Parseamos el JSON usando JsonParser
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();

            // Verificamos que las claves "base_code" y "conversion_rates" existan
            if (jsonObject.has("base_code") && jsonObject.has("conversion_rates")) {
                String base = jsonObject.get("base_code").getAsString();
                JsonObject rates = jsonObject.getAsJsonObject("conversion_rates");

                System.out.println("Moneda base: " + base);

                // Filtramos y mostramos las tasas para USD y EUR
                filterAndDisplayRates(base, rates);
            } else {
                System.out.println("Error: Respuesta JSON incompleta.");
            }
        } catch (Exception e) {
            System.out.println("Error al analizar el JSON: " + e.getMessage());
        }
    }

    private static void filterAndDisplayRates(String base, JsonObject rates) {
        String[] selectedCurrencies = {"USD", "EUR"};

        for (String currency : selectedCurrencies) {
            if (rates.has(currency)) {
                double rate = rates.get(currency).getAsDouble();
                System.out.println("1 " + base + " = " + rate + " " + currency);
            } else {
                System.out.println("Tasa no disponible para: " + currency);
            }
        }
    }
}






