package com.example.mealprep.data.model

enum class Aisle(val departmentName: String?, val value: Int) {
    FRUITS_AND_VEGETABLES("Fruits and Vegetables", 1),
    MEATS_AND_SEAFOOD("Meat and Seafood", 2),
    BREAD_AND_BAKERY("Bread and Bakery", 3),
    DAIRY_AND_EGGS("Dairy and Eggs", 4),
    HERBS_AND_SPICES("Herbs and Spices", 5),
    BAKING("Baking", 6),
    SNACKS("Snacks", 7),
    CANNED_FOODS("Canned food", 8),
    CONDIMENTS("Souces", 9),
    PASTA_RICE("Pasta And Rice", 10),
    DRINKS("Drinks", 11),
    OTHERS(null, 12),
}