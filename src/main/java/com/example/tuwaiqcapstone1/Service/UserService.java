package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.MerchantStock;
import com.example.tuwaiqcapstone1.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

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
        int productIndex = productService.findProductIndex(productId);
        int merchantIndex = merchantService.findMerchantIndex(merchantId);
        if(userIndex == -1) return -1;
        if(productIndex == -1) return 0;
        if(merchantIndex == -1) return 1;

        double userBalance = users.get(userIndex).getBalance();
        double productPrice = productService.products.get(productIndex).getPrice();
        if(userBalance < productPrice) return 2; //insufficient balance

        users.get(userIndex).setBalance(userBalance - productPrice);
        return 6;
    }

    //HELPER METHODS
    public int findUserIndex(String id){
        for(int i = 0; i < users.size(); i++)
            if(users.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }
}
