package org.example.entity.AI;

import lombok.Data;
import org.example.entity.Product;

import java.util.List;
@Data
public class RelationsNormal {
    private Integer ruleId;
    private List<Product> relations;

    public Integer getRuleId() {
        return ruleId;
    }

    public void setRuleId(Integer ruleId) {
        this.ruleId = ruleId;
    }

    public List<Product> getRelations() {
        return relations;
    }

    public void setRelations(List<Product> relations) {
        this.relations = relations;
    }
}
