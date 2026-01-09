package pl.edu.agh.to.przychodnia.Doctor;

public enum Specialization {
    KARDIOLOG,
    DERMATOLOG,
    NEUROLOG,
    PEDIATRA,
    ORTOPEDA,
    GINEKOLOG,
    PSYCHIATRA;

    @Override
    public String toString() {
        switch (this) {
            case KARDIOLOG:
                return "Kardiolog";
            case DERMATOLOG:
                return "Dermatolog";
            case NEUROLOG:
                return "Neurolog";
            case PEDIATRA:
                return "Pediatra";
            case ORTOPEDA:
                return "Ortopeda";
            case GINEKOLOG:
                return "Ginekolog";
            case PSYCHIATRA:
                return "Psychiatra";
            default:
                return "Nieznana specjalizacja";
        }
    }

    public static Specialization fromString(String specializationStr) {
        switch (specializationStr.toLowerCase()) {
            case "kardiolog":
                return KARDIOLOG;
            case "dermatolog":
                return DERMATOLOG;
            case "neurolog":
                return NEUROLOG;
            case "pediatra":
                return PEDIATRA;
            case "ortopeda":
                return ORTOPEDA;
            case "ginekolog":
                return GINEKOLOG;
            case "psychiatra":
                return PSYCHIATRA;
            default:
                throw new IllegalArgumentException("Nieznana specjalizacja: " + specializationStr);
        }
    }
}
