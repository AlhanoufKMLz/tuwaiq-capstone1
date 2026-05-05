package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Product;
import com.example.tuwaiqcapstone1.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ProductService productService;
    private final MerchantService merchantService;
    private final MerchantStockService merchantStockService;

    ArrayList<User> users = new ArrayList<>();

    //BASIC CRUD ENDPOINTS
    public ArrayList<User> getUsers(){
        return users;
    }

    public boolean addUser(User user){
        if(findUserIndex(user.getId()) == -1) {//not found
            users.add(user);
            return true;
        }
        return false;
    }

    public boolean updateUser(String id, User user){
        int index = findUserIndex(id);
        if(index == -1) //not found
            return false;
        user.setId(id); //make sure the user doesn't change the id
        users.set(index, user);
        return true;
    }

    public boolean deleteUser(String id){
        int index = findUserIndex(id);
        if(index == -1) //not found
            return false;
        users.remove(index);
        return true;
    }


    //EXTRA ENDPOINTS
    public int buyProduct(String userId, String productId, String merchantId){
        int userIndex = findUserIndex(userId);
        if(userIndex == -1) return -1; //check user
        int productIndex = productService.findProductIndex(productId);
        if(productIndex == -1) return 0; //check product
        int merchantIndex = merchantService.findMerchantIndex(merchantId);
        if(merchantIndex == -1) return 1; //check merchant
        int merchantStockIndex = merchantStockService.findByProductAndMerchantId(productId, merchantId);
        if(merchantStockIndex == -1) return 2; //check merchant stock

        int stock = merchantStockService.merchantStocks.get(merchantStockIndex).getStock();
        if(stock < 1) return 3; //check stock

        double userBalance = users.get(userIndex).getBalance();
        double productPrice = productService.products.get(productIndex).getPrice();
        if(userBalance < productPrice) return 4; //check balance

        //update user balance
        users.get(userIndex).setBalance(userBalance - productPrice);
        //update user total spent
        double userTotalSpent = users.get(userIndex).getTotalSpent();
        users.get(userIndex).setTotalSpent(userTotalSpent + productPrice);
        //update stock
        merchantStockService.merchantStocks.get(merchantStockIndex).setStock(stock-1);
        //update product times purchased
        int timesPurchased = productService.products.get(productIndex).getTimesPurchased();
        productService.products.get(productIndex).setTimesPurchased(timesPurchased+1);

        return 6;//everything is good
    }

    public User searchByUsername(String username){
        for(User u: users){
            if(u.getUsername().equalsIgnoreCase(username))
                return u;
        }
        return null;
    }

    public ArrayList<Product> showCart(String userId){
        int userIndex = findUserIndex(userId);
        if(userIndex == -1) return null;

        ArrayList<Product> cartProducts = new ArrayList<>();
        User user = users.get(userIndex);
        for(String productId: user.getCart().keySet()){
            int productIndex = productService.findProductIndex(productId);
            Product product = productService.products.get(productIndex);
            cartProducts.add(product);
        }
        return cartProducts;
    }

    public int addToCart(String userId, String productId, String merchantId){
        int userIndex = findUserIndex(userId);
        if(userIndex == -1) return -1; //check user
        int productIndex = productService.findProductIndex(productId);
        if(productIndex == -1) return 0; //check product
        int merchantIndex = merchantService.findMerchantIndex(merchantId);
        if(merchantIndex == -1) return 1; //check merchant
        int merchantStockIndex = merchantStockService.findByProductAndMerchantId(productId, merchantId);
        if(merchantStockIndex == -1) return 2; //check merchant stock

        int stock = merchantStockService.merchantStocks.get(merchantStockIndex).getStock();
        if(stock < 1) return 3; //check stock

        users.get(userIndex).getCart().put(productId, merchantId);
        return 4;
    }

    public int removeFromCart(String userId, String productId) {
        int userIndex = findUserIndex(userId);
        if (userIndex == -1) return -1;

        User user = users.get(userIndex);
        if (user.getCart().isEmpty()) return 0;

        if (!user.getCart().containsKey(productId)) return 2;

        user.getCart().remove(productId);
        return 1;
    }

    public boolean clearCart(String userId){
        int userIndex = findUserIndex(userId);
        if(userIndex == -1)
            return false;

        users.get(userIndex).getCart().clear();
        return true;
    }

    public int claimReward(String userId){
        int userIndex = findUserIndex(userId);
        if(userIndex == -1) return -1;

        User user = users.get(userIndex);
        if(user.getTotalSpent() < 1000) return 0;

        user.setBalance(user.getBalance() + user.getTotalSpent() * 0.1);
        user.setTotalSpent(0);
        return 1;
    }

    public int checkout(String userId){
        int userIndex = findUserIndex(userId);
        if(userIndex == -1) return -1;

        User user = users.get(userIndex);
        if(user.getCart().isEmpty()) return 0;

        for(Map.Entry<String, String> entry: user.getCart().entrySet()){
            buyProduct(userId, entry.getKey(), entry.getValue());
        }
        user.getCart().clear();
        return 1;
    }

    public double calculateCartCost(String userId){
        int userIndex = findUserIndex(userId);
        if(userIndex == -1) return -1;

        User user = users.get(userIndex);
        if(user.getCart().isEmpty()) return -2;

        double totalCost = 0;
        for(Map.Entry<String, String> entry: user.getCart().entrySet()){
            int productIndex = productService.findProductIndex(entry.getKey());
            Product product = productService.products.get(productIndex);
            totalCost += product.getPrice();
        }
        return totalCost;
    }


    //HELPER METHODS
    public int findUserIndex(String id){
        for(int i = 0; i < users.size(); i++)
            if(users.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }
}
