package Objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class OutSourcedPart extends Part {

    private final StringProperty partCompanyName;

    public OutSourcedPart() {
        super();
        partCompanyName = new SimpleStringProperty();
    }

    public String getPartCompanyName() {
        return this.partCompanyName.get();
    }

    public void setPartCompanyName(String partCompanyName) {
        this.partCompanyName.set(partCompanyName);
    }
}