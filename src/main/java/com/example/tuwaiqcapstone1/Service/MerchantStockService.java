package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Category;
import com.example.tuwaiqcapstone1.Model.MerchantStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MerchantStockService {

    private final ProductService productService;
    private final MerchantService merchantService;

    ArrayList<MerchantStock> merchantStocks = new ArrayList<>();

    //BASIC CRUD ENDPOINTS
    public ArrayList<MerchantStock> getMerchantStocks(){
        return merchantStocks;
    }

    public int addMerchantStock(MerchantStock merchantStock){
        if(findMerchantStockIndex(merchantStock.getId()) == -1) {//not found
            if(productService.findProductIndex(merchantStock.getProductId()) == -1)
                return 0;
            if(merchantService.findMerchantIndex(merchantStock.getMerchantId()) == -1)
                return 1;
            if(merchantStock.getStock() < 11)
                return 2;
            merchantStocks.add(merchantStock);
            return 3;
        }
        return -1;
    }

    public int updateMerchantStock(String id, MerchantStock merchantStock){
        int index = findMerchantStockIndex(id);
        if(index == -1) //not found
            return -1;

        if(productService.findProductIndex(merchantStock.getProductId()) == -1)
            return 0;
        if(merchantService.findMerchantIndex(merchantStock.getMerchantId()) == -1)
            return 1;
        merchantStock.setId(id); //make sure the user doesn't change the id
        merchantStocks.set(index, merchantStock);
        return 2;
    }

    public boolean deleteMerchantStock(String id){
        int index = findMerchantStockIndex(id);
        if(index == -1) //not found
            return false;
        merchantStocks.remove(index);
        return true;
    }

    public boolean addStock(String productId, String merchantId, int amount){
        if (amount <= 0)
            return false;
        for(MerchantStock m: merchantStocks){
            if(m.getProductId().equalsIgnoreCase(productId) && m.getMerchantId().equalsIgnoreCase(merchantId)){
                m.setStock(m.getStock() + amount);
                return true;
            }
        }
        return false;
    }


    //HELPER METHODS
    public int findMerchantStockIndex(String id){
        for(int i = 0; i < merchantStocks.size(); i++)
            if(merchantStocks.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }
}
