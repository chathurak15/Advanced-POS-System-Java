package org.example.controllers.ai;

import org.example.dto.AI.OfferResponceDTO;
import org.example.dto.AI.RelativeRequestDTO;
import org.example.dto.AI.SingleLineResponceDTO;
import org.example.entity.AI.Relations;
import org.example.service.AIService;

import java.util.List;

public class AIController {

    private final AIService aiService = new AIService();

//    public List<Relations> getActiveDataset() {
//        return aiService.getActiveDataset();
//    }
//
//    public SingleLineResponceDTO saveActiveRelation(RelativeRequestDTO requestDTO) {
//        return aiService.saveActiveRelation(requestDTO);
//    }
////
//    public OfferResponceDTO checkOffers(Integer id) {
//        return aiService.checkOffers(id);
//    }

    public List<?> createDataset() throws Exception {
        return aiService.createDataset();
    }
}
