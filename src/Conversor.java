import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Scanner;

public class Conversor {
    public static void main(String[] args) {
        try {
            // Crear cliente HTTP y solicitud
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/aac43c89717ed91b0fe4ecd8/latest/USD"))
                    .build();

            // Enviar solicitud y obtener respuesta
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();

            // Convertir JSON a objeto Moneda
            Moneda rates = gson.fromJson(response.body(), Moneda.class);

            // Verificar si las tasas de conversión son válidas
            if (rates.getRates() != null) {
                System.out.println("Moneda base: " + rates.getBase());

                // Llamar al método para procesar la entrada del usuario
                interactWithUser(rates);
            } else {
                System.out.println("Error: No se pudieron obtener las tasas de conversión.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para interactuar con el usuario
    private static void interactWithUser(Moneda rates) {
        Scanner scanner = new Scanner(System.in);
        Map<String, Double> conversionRates = rates.getRates();

        System.out.println("Monedas disponibles: ");
        conversionRates.keySet().forEach(System.out::println);

        System.out.println("\nIngrese la moneda que desea convertir: ");
        String monedaSeleccionada = scanner.nextLine().toUpperCase();

        System.out.println("Ingrese el valor que desea convertir: ");
        double valor = scanner.nextDouble();

        // Realizar las conversiones a USD y EUR
        convertAndDisplay(monedaSeleccionada, valor, rates);

        scanner.close();
    }

    // Método para realizar las conversiones y mostrar los resultados
    private static void convertAndDisplay(String currency, double amount, Moneda rates) {
        Map<String, Double> conversionRates = rates.getRates();

        if (conversionRates.containsKey(currency)) {
            double toUSD = amount / conversionRates.get(currency);
            double toEUR = toUSD * conversionRates.get("EUR");

            System.out.printf("El valor de %.2f %s es %.2f USD%n", amount, currency, toUSD);
            System.out.printf("El valor de %.2f %s es %.2f EUR%n", amount, currency, toEUR);
        } else {
            System.out.println("Moneda no válida o tasa de conversión no disponible.");
        }
    }
}
