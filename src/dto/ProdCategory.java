package dto;

public enum ProdCategory {
    food,   // 음식
    alcohol; // 술

    public static int categoryToInt(String category) {
        switch (category){
            case "food" -> {
                return 1;
            }
            case "alcohol" -> {
                return 2;
            }
        }
        return 0; // 해당되는게 없다면 0 리턴
    }

    public static ProdCategory intToCategory(int num) {
        switch (num) {
            case 1 -> {
                return ProdCategory.food;
            }
            case 2 -> {
                return ProdCategory.alcohol;
            }
        }
        return null;
    }
}
