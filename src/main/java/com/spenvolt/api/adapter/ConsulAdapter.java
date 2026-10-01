package com.spenvolt.api.adapter;

import com.spenvolt.api.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ConsulAdapter implements ProductAdapter {

    private final RestTemplate restTemplate;
    private static final int MAX_RESULTS = 50;

    public ConsulAdapter() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public List<Product> search(String query) {
        System.out.println(">>> INICIANDO BUSCA NO ADAPTER CONSUL PARA: " + query);
        List<Product> products = new ArrayList<>();
        String url = "https://www.consul.com.br/api/catalog_system/pub/products/search?ft=" + query
                + "&_from=50&_to=" + (MAX_RESULTS - 1);

        try {
            List<?> response = restTemplate.getForObject(url, List.class);

            if (response == null || response.isEmpty()) {
                System.out.println(">>> Consul retornou resposta vazia.");
                return products;
            }

            for (Object itemObj : response) {
                Map<String, Object> item = asMap(itemObj);
                if (item == null) continue;

                String name = (String) item.get("productName");
                String link = (String) item.get("link");

                // 1. Validação de Nome
                if (name == null || isInvalidProductTitle(name)) {
                    continue;
                }

                // 2. Filtro por Categoria VTEX
                List<?> categoriesRaw = asList(item.get("categories"));
                if (categoriesRaw != null && isAccessoryOrPartCategory(categoriesRaw)) {
                    continue;
                }

                // 3. Validação de Variações
                List<?> itemsList = asList(item.get("items"));
                if (itemsList == null || itemsList.isEmpty()) {
                    continue;
                }

                Map<String, Object> firstItem = asMap(itemsList.get(0));
                if (firstItem == null) continue;

                // Extração de Imagem
                String imageUrl = null;
                List<?> imagesList = asList(firstItem.get("images"));
                if (imagesList != null && !imagesList.isEmpty()) {
                    Map<String, Object> firstImage = asMap(imagesList.get(0));
                    if (firstImage != null) {
                        imageUrl = (String) firstImage.get("imageUrl");
                    }
                }

                // Extração de Preço
                Float price = null;
                List<?> sellersList = asList(firstItem.get("sellers"));
                if (sellersList != null && !sellersList.isEmpty()) {
                    Map<String, Object> firstSeller = asMap(sellersList.get(0));
                    if (firstSeller != null) {
                        Map<String, Object> offer = asMap(firstSeller.get("commertialOffer"));
                        if (offer != null && offer.get("Price") != null) {
                            price = ((Number) offer.get("Price")).floatValue();
                        }
                    }
                }

                // Extração da Voltagem/Potência (Obrigatória)
                String power = extractPower(name);

                // Só adiciona se TIVER Preço E Power/Voltagem
                if (price != null && power != null) {
                    Product p = new Product();
                    p.setName(name);
                    p.setSource("consul");
                    p.setPrice(price);
                    p.setOriginalPrice(null);
                    p.setPower(power);
                    p.setImageUrl(imageUrl);
                    p.setProductUrl(link);

                    products.add(p);
                }
            }

            System.out.println(">>> Produtos válidos encontrados na Consul: " + products.size());

        } catch (Exception e) {
            System.err.println("Erro ao buscar produtos na API da Consul: " + e.getMessage());
        }

        return products;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    private String extractPower(String name) {
        if (name == null) return null;

        Pattern pattern = Pattern.compile("(?i)\\b(110\\s*v|127\\s*v|220\\s*v|110\\s*volts|127\\s*volts|220\\s*volts|bivolt)\\b");
        Matcher matcher = pattern.matcher(name);

        if (matcher.find()) {
            return matcher.group(1).toUpperCase();
        }

        return null;
    }

    private boolean isAccessoryOrPartCategory(List<?> categories) {
        StringBuilder categoriesText = new StringBuilder();
        for (Object cat : categories) {
            if (cat != null) {
                categoriesText.append(cat.toString()).append(" ");
            }
        }

        String text = categoriesText.toString().toLowerCase();
        String[] blacklist = {
                "peças", "pecas", "refil", "filtro", "acessórios", "acessorios",
                "limpeza", "kit", "capa", "suporte", "mangueira", "válvula", "valvula"
        };

        for (String keyword : blacklist) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private boolean isInvalidProductTitle(String name) {
        String nameLower = name.toLowerCase();

        String[] blacklist = {
                "refil", "filtro de água", "filtro para", "capa para", "kit de",
                "detergente", "limpadora", "gaveta", "prateleira", "reserva",
                "conector", "suporte", "mangueira", "placa eletrônica", "resistência"
        };

        for (String badWord : blacklist) {
            if (nameLower.contains(badWord)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> asMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        return null;
    }

    private List<?> asList(Object obj) {
        if (obj instanceof List) {
            return (List<?>) obj;
        }
        return null;
    }
}