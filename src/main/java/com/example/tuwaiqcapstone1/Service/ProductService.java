package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final CategoryService categoryService;

    ArrayList<Product> products = new ArrayList<>();

    //BASIC CRUD ENDPOINTS
    public ArrayList<Product> getProducts(){
        return products;
    }

    public int addProduct(Product product){
        if(product.getTimesPurchased() != 0) return -2;

        if(findProductIndex(product.getId()) == -1) {//not found
            if(categoryService.findCategoryIndex(product.getCategoryId()) == -1) //not found
                return 0;
            products.add(product);
            return 1;
        }
        return -1;
    }

    public int updateProduct(String id, Product product){
        int index = findProductIndex(id);
        if(index == -1) return -1; //not found
        if (categoryService.findCategoryIndex(product.getCategoryId()) == -1) return 0;

        product.setId(id); //make sure the user doesn't change the id
        products.set(index, product);
        return 1;
    }

    public boolean deleteProduct(String id){
        int index = findProductIndex(id);
        if(index == -1) //not found
            return false;
        products.remove(index);
        return true;
    }


    //EXTRA ENDPOINTS
    public Product searchByName(String name){
        for(Product p: products){
            if(p.getName().equalsIgnoreCase(name))
                return p;
        }
        return null;
    }

    public ArrayList<Product> getProductsByCategory(String categoryId){
        if(categoryService.findCategoryIndex(categoryId) == -1)
            return null;

        ArrayList<Product> categoryProducts = new ArrayList<>();
        for(Product p: products){
            if(p.getCategoryId().equalsIgnoreCase(categoryId))
                categoryProducts.add(p);
        }
        return categoryProducts;
    }

    public ArrayList<Product> getProductsByPriceRange(double min, double max){
        if(min > max || min < 0) return null;
        ArrayList<Product> productsInRange = new ArrayList<>();
        for(Product p: products){
            if(p.getPrice() >= min && p.getPrice() <= max)
                productsInRange.add(p);
        }
        return productsInRange;
    }

    public ArrayList<Product> sortPriceLowToHigh(){
        ArrayList<Product> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        return sortedProducts;
    }

    public ArrayList<Product> sortPriceHighToLow(){
        ArrayList<Product> sortedProducts = new ArrayList<>(products);
        sortedProducts.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        return sortedProducts;
    }

    public ArrayList<Product> getBestSellers() {
        ArrayList<Product> bestSellers = new ArrayList<>();
        int topTimesPurchased = 0;
        for (Product p : products) {
            if (p.getTimesPurchased() > topTimesPurchased) {
                topTimesPurchased = p.getTimesPurchased();
                bestSellers.clear();
                bestSellers.add(p);
            } else if (p.getTimesPurchased() == topTimesPurchased)
                bestSellers.add(p);
        }
        return bestSellers;
    }

    public HashMap<Product, Double> getTopRatedProducts(){
        if(products.isEmpty()) return null;

        LinkedHashMap<Product, Double> rated = new LinkedHashMap<>();

        ArrayList<Product> sorted = new ArrayList<>(products);
        sorted.sort((a, b) -> Double.compare(getAverageRating(b), getAverageRating(a)));

        for(Product p: sorted)
            rated.put(p, getAverageRating(p));

        return rated;
    }


    //HELPER METHODS
    public int findProductIndex(String id){
        for(int i = 0; i < products.size(); i++)
            if(products.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }

    private double getAverageRating(Product product){
        if(product.getRatings().isEmpty()) return 0;
        int sum = 0;
        for(int r: product.getRatings()) sum += r;
        return (double) sum / product.getRatings().size();
    }

}
