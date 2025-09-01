package korrekturmanagement;

import java.util.List;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class DropdownBean {

    private boolean hideNoSelectOption = false;

    public boolean isHideNoSelectOption() {
        return hideNoSelectOption;
    }

    public void setHideNoSelectOption(boolean hideNoSelectOption) {
        this.hideNoSelectOption = hideNoSelectOption;
    }

    public List<String> getPriorisierungen() {
        return List.of("niedrig", "mittel", "hoch", "sehr hoch");
    }
    
    public List<String> getKategorie() {
        return List.of("technischer Fehler", "inhaltlicher Fehler", "Verbesserungsvorschlag", " Ergänzung");
    }
}

