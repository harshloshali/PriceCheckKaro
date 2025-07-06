package org.PriceCheckKaro;
import org.json.JSONObject;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public class FlightPriceChecker {

    static class FlightInfo {
        String airline;
        int price;

        FlightInfo(String airline, int price) {
            this.airline = airline;
            this.price = price;
        }

        @Override
        public String toString() {
            return airline + ": ₹" + price;
        }
    }

    public static void main(String[] args) throws Exception {
        String travelDate = "2025-07-10";
        String from = "DEL";
        String to = "BOM";

        List<FlightInfo> results = new ArrayList<>();

//        results.add(getFlightPrice("IndiGo", "https://mockapi.com/indigo", travelDate, from, to));
//        results.add(getFlightPrice("Vistara", "https://mockapi.com/vistara", travelDate, from, to));
//        results.add(getFlightPrice("Air India", "https://mockapi.com/airindia", travelDate, from, to));
        results.add(fetchFlight("IndiGo","https://run.mocky.io/v3/b1c7e0c4-12f9-4f83-b76b-6be3c8be8881", travelDate, from, to)); // Indigo
        results.add(fetchFlight("Vistara","https://run.mocky.io/v3/7fa682a5-3ee8-4c64-b00d-9f511be715fd", travelDate, from, to)); // Vistara
        results.add(fetchFlight("Air India","https://run.mocky.io/v3/202e7a1b-9465-4b59-9172-609c1ec99f80", travelDate, from, to)); // Air India

        // Sort by price
        results.sort(Comparator.comparingInt(f -> f.price));

        // Display sorted results
        System.out.println("📊 Flight Prices (Cheapest to Costliest):");
        results.forEach(System.out::println);
    }

    private static FlightInfo fetchFlight(String airline, String url, String date, String from, String to) throws Exception {
        // Construct JSON request body
        JSONObject body = new JSONObject();
        body.put("date", date);
        body.put("from", from);
        body.put("to", to);

        // Make HTTP POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Parse JSON response
        JSONObject json = new JSONObject(response.body());
        int price = json.getInt("price");

        return new FlightInfo(airline, price);
    }
}
