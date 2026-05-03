package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Merchant;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MerchantService {

    ArrayList<Merchant> merchants = new ArrayList<>();

    //BASIC CRUD ENDPOINTS
    public ArrayList<Merchant> getMerchants(){
        return merchants;
    }

    public boolean addMerchant(Merchant merchant){
        if(findMerchantIndex(merchant.getId()) == -1) {//not found
            merchants.add(merchant);
            return true;
        }
        return false;
    }

    public boolean updateMerchant(String id, Merchant merchant){
        int index = findMerchantIndex(id);
        if(index == -1) //not found
            return false;
        merchant.setId(id); //make sure the user doesn't change the id
        merchants.set(index, merchant);
        return true;
    }

    public boolean deleteMerchant(String id){
        int index = findMerchantIndex(id);
        if(index == -1) //not found
            return false;
        merchants.remove(index);
        return true;
    }


    //HELPER METHODS
    public int findMerchantIndex(String id){
        for(int i = 0; i < merchants.size(); i++)
            if(merchants.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }
}
