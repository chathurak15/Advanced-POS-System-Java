package org.example.entity.AI;

import java.util.List;

public class RelationsNative {

        private List<Integer> associatedItems;

        public List<Integer> getAssosiatedItems() {
            return associatedItems;
        }

        public void setAssosiatedItems(List<Integer> associatedItems) {
            this.associatedItems = associatedItems;
        }

        public RelationsNormal getNormal() {
            RelationsNormal normal = new RelationsNormal();
            normal.setRuleId(associatedItems.hashCode());
            return normal;
        }

}
