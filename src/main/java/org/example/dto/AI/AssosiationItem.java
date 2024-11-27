package org.example.dto.AI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.example.entity.AI.RelationsNative;

import java.util.List;
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssosiationItem {
    private List<Integer> antecedents; //
    private Float confidence;
    private List<Integer> consequents;
    private Float lift;
    private Float support;


    public RelationsNative getNative(){
        RelationsNative relationsNative = new RelationsNative();
        List<Integer> all = this.antecedents;
        all.addAll(this.consequents);
        all.sort(Integer::compareTo);
        relationsNative.setAssosiatedItems(all);
//        relationsNative.setConfidence(confidence);
//        relationsNative.setLift(lift);
//        relationsNative.setSupport(support);
        return relationsNative;
    }
}
