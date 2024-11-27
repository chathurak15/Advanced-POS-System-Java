package org.example.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHeaders;
import org.example.dto.AI.AssosiationRules;
import org.example.dto.AI.RelativeRequestDTO;
import org.example.entity.AI.Item;
import org.example.entity.AI.Relations;
import org.example.entity.AI.RelationsNative;
import org.example.entity.AI.RelationsNormal;
import org.example.repo.custom.impl.ProductRepoIMPL;
import org.example.service.custom.OrderItemService;
import org.example.util.RequestBody;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class AIService {
    private final OrderItemService orderItemService = new OrderItemService();
    private final ProductRepoIMPL productRepo = new ProductRepoIMPL();
    private static final String URL = "http://127.0.0.1:5000/find-patterns";


    public List<?> createDataset() throws Exception {
        List<List<Integer>> data = orderItemService.getAllOrderItems();
        return callToAI(data);
    }

    private List<?> callToAI(List<List<Integer>> data) {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody;

        try {
            jsonBody = objectMapper.writeValueAsString(new RequestBody(data));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing request body", e);
        }

        HttpURLConnection connection = null;
        try {
            // Open connection
            URL url = new URL(URL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // Success
                try (InputStream is = connection.getInputStream();
                     BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
//                    System.out.println(response);
                    AssosiationRules assosiationRules = objectMapper.readValue(response.toString(), AssosiationRules.class);
//                    System.out.println(assosiationRules.getAssociation_rules().toString());
                    return createNativeRelations(assosiationRules);

                }
            }else {
                throw new IOException("Request failed with code: " + responseCode);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error during HTTP request", e);
        }
    }

    public List<RelationsNormal> createNativeRelations(AssosiationRules assosiationRules) {
        List<RelationsNative> natives = assosiationRules.getAssociation_rules().stream()
                .map(rule -> rule.getNative())
                .toList();

        Set<Set<Integer>> seenItems = new HashSet<>();
        List<RelationsNative> uniqueNatives = natives.stream()
                .filter(relation -> {
                    Set<Integer> itemsSet = new HashSet<>(relation.getAssosiatedItems()); // Convert to Set for uniqueness
                    return seenItems.add(itemsSet); // Add to the Set, returns false if already exists
                })
                .collect(Collectors.toList());

        uniqueNatives = uniqueNatives.stream()
                .filter(r -> r.getAssosiatedItems().size() < 3)
                .collect(Collectors.toList());

//        System.out.println(uniqueNatives);
        return createNormalData(uniqueNatives);
    }

    private List<RelationsNormal> createNormalData(List<RelationsNative> natives) {
        List<RelationsNormal> normalRelations = natives.stream()
                .map(nativeItem -> {
                    RelationsNormal normal = nativeItem.getNormal();
                    normal.setRelations(
                            nativeItem.getAssosiatedItems().stream()
                                    .map(itemId -> {
                                        try {
                                            return productRepo.search(itemId);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    })
                                    .collect(Collectors.toList())
                    );
                    return normal;
                })
                .collect(Collectors.toList());

//        System.out.println(normalRelations);
        return normalRelations;
    }

//    public String saveActiveRelation(RelativeRequestDTO relativeRequestDTO) {
//        Item item1 = itemRepository.findById(relativeRequestDTO.getItem1()).orElseThrow(() -> new RuntimeException("Item not found"));
//        Item item2 = itemRepository.findById(relativeRequestDTO.getItem2()).orElseThrow(() -> new RuntimeException("Item not found"));
//
//        Relations relation = new Relations();
//        relation.setDependents(item1);
//        relation.setItem2(item2);
//        relation.setActive(true);
//
//        relationRepository.save(relation);
//
//        return "Relation saved successfully";
//    }
//
//    public String checkOffers(Integer id) {
//        Item item = null;
//        try {
//            productRepo.search(id);;
//        } catch (Exception e) {
//            throw new RuntimeException("Item not found");
//        }
//        Relations relations = relationRepository.findRelationsByDependentsAndActive(item, true);
//        if (relations != null) {
//            return "Offer available with Item: " + relations.getItem2();
//        } else {
//            return "No offer available";
//        }
//    }
//
//    public List<Relations> getActiveDataset() {
//        return relationRepository.findAll();
//    }
}
