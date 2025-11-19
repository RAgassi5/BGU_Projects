package DTO.InventoryDTO;

public class CategoryDTO {
    private final String categoryName;
    private final int rank;

    public CategoryDTO(String categoryName, int rank) {
        this.categoryName = categoryName;
        this.rank = rank;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public int getRank() {
        return rank;
    }
}
