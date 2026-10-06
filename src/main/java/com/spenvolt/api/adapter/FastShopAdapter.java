package com.spenvolt.api.adapter;

import com.spenvolt.api.model.Device;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// @Component
public class FastShopAdapter implements DeviceAdapter {

    private static final int MAX_RESULTS = 20;
    private final RestTemplate restTemplate;

    public FastShopAdapter() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Device> search(String query) {
        List<Device> devices = new ArrayList<>();

        try {
            String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

            // Endpoint público VTEX da Fast Shop com paginação limitada a 20 itens (_from=0&_to=19)
            String targetUrl = "https://www.fastshop.com.br/api/catalog_system/pub/products/search?ft="
                    + encodedQuery + "&_from=0&_to=" + (MAX_RESULTS - 1);

            // O RestTemplate do Spring faz o Parse automático da resposta JSON da VTEX para List<Map>
            List<Map<String, Object>> response = restTemplate.getForObject(targetUrl, List.class);

            if (response == null || response.isEmpty()) {
                return devices;
            }

            for (Map<String, Object> item : response) {
                String name = (String) item.get("productName");
                String link = (String) item.get("link");

                // Mapeia o array "items" da VTEX (SKUs)
                List<Map<String, Object>> items = (List<Map<String, Object>>) item.get("items");
                if (items == null || items.isEmpty()) continue;

                Map<String, Object> firstItem = items.get(0);

                // Imagem do produto
                List<Map<String, Object>> images = (List<Map<String, Object>>) firstItem.get("images");
                String imageUrl = (images != null && !images.isEmpty())
                        ? (String) images.get(0).get("imageUrl")
                        : null;

                // Mapeia o preço atual no vendedor padrão
                Float price = null;
                List<Map<String, Object>> sellers = (List<Map<String, Object>>) firstItem.get("sellers");
                if (sellers != null && !sellers.isEmpty()) {
                    Map<String, Object> offer = (Map<String, Object>) sellers.get(0).get("commertialOffer");
                    if (offer != null && offer.get("Price") != null) {
                        price = ((Number) offer.get("Price")).floatValue();
                    }
                }

                if (name != null && price != null) {
                    Device p = new Device();
                    p.setName(name);
                    p.setSource("fastshop");
                    p.setPrice(price);
                    p.setOriginalPrice(null);
                    p.setPower(extractPower(name)); // Identifica 1000W, 220V, etc., no título
                    p.setImageUrl(imageUrl);
                    p.setDeviceUrl(link);

                    devices.add(p);
                }
            }

        } catch (Exception e) {
            System.err.println("Erro ao buscar no endpoint VTEX (FastShop): " + e.getMessage());
        }

        return devices;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    /**
     * Extrai padrões de potência e voltagem do título do produto
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