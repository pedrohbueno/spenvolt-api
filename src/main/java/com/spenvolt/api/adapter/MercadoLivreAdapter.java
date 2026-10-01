package com.spenvolt.api.adapter;

import com.spenvolt.api.model.Product;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// @Component
public class MercadoLivreAdapter implements ProductAdapter {

    private static final int MAX_RESULTS = 20;

    @Override
    public List<Product> search(String query) {
        List<Product> products = new ArrayList<>();

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String targetUrl = "https://lista.mercadolivre.com.br/" + encodedQuery;

            URL url = new URL(targetUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder html = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                html.append(line);
            }
            reader.close();

            // Regex para capturar Link, Imagem, Título e Preço no HTML do Mercado Livre
            Pattern cardPattern = Pattern.compile(
                    "<a[^>]+href=\"(https://[^\"]+)\"[^>]*class=\"[^\"]*ui-search-link[^\"]*\"[^>]*>.*?" +
                            "<img[^>]+src=\"(https://[^\"]+)\"[^>]*>.*?" +
                            "<h2[^>]*class=\"[^\"]*ui-search-item__title[^\"]*\"[^>]*>(.*?)</h2>.*?" +
                            "<span class=\"andes-money-amount__fraction\"[^>]*>(.*?)</span>",
                    Pattern.DOTALL
            );

            Matcher matcher = cardPattern.matcher(html.toString());
            int count = 0;

            while (matcher.find() && count < MAX_RESULTS) {
                String productUrl = matcher.group(1);
                String imageUrl = matcher.group(2);
                String name = matcher.group(3).trim();
                String priceStr = matcher.group(4).replace(".", "").replace(",", ".").trim();

                Product p = new Product();
                p.setName(name);
                p.setSource("mercadolivre");
                p.setPrice(Float.parseFloat(priceStr));
                p.setOriginalPrice(null); // Pode ser capturado se houver tag de desconto
                p.setPower(extractPower(name));
                p.setImageUrl(imageUrl);
                p.setProductUrl(productUrl);

                products.add(p);
                count++;
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar no Mercado Livre: " + e.getMessage());
        }

        return products;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    /**
     * Extrai padrões de potência e voltagem do título do produto (ex: 1000W, 1200 W, 220V, 110V)
     */
    private String extractPower(String text) {
        Pattern pattern = Pattern.compile("(\\d+\\s*(?:W|watts|kW|V|Volts))", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Não informada";
    }
}