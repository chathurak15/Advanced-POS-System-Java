package org.example.dto.AI;

import lombok.Data;

import java.util.List;
@Data
public class AssosiationRules {
    private List<AssosiationItem> association_rules;

    public List<AssosiationItem> getAssociation_rules() {
        return association_rules;
    }

    public void setAssociation_rules(List<AssosiationItem> association_rules) {
        this.association_rules = association_rules;
    }

}