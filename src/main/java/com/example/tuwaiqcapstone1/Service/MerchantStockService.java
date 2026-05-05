package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Merchant;
import com.example.tuwaiqcapstone1.Model.MerchantStock;
import com.example.tuwaiqcapstone1.Model.Product;
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


    //EXTRA ENDPOINTS
    public int addStock(String productId, String merchantId, int amount){
        if(productService.findProductIndex(productId) == -1) return -1;
        if(merchantService.findMerchantIndex(merchantId) == -1) return -2;
        if (amount < 0) return -3;

        int index = findByProductAndMerchantId(productId, merchantId);
        if(index == -1)
            return -4;
        merchantStocks.get(index).setStock(merchantStocks.get(index).getStock() + amount);
        return 1;
    }

    public int clearMerchantStock(String merchantId){
        if(merchantService.findMerchantIndex(merchantId) == -1)
            return -1;
        boolean removed = merchantStocks.removeIf(m ->
                m.getMerchantId().equalsIgnoreCase(merchantId));
        return removed ? 1 : 0;
    }

    public ArrayList<Product> getMerchantProducts(String merchantId){
        if(merchantService.findMerchantIndex(merchantId) == -1) return null;

        ArrayList<Product> merchantProducts = new ArrayList<>();
        for(MerchantStock m: merchantStocks){
            if(m.getMerchantId().equalsIgnoreCase(merchantId)){
                int productId = productService.findProductIndex(m.getProductId());
                merchantProducts.add(productService.products.get(productId));
            }
        }
        return merchantProducts;
    }

    public ArrayList<Merchant> getProductMerchants(String productId){
        if(productService.findProductIndex(productId) == -1) return null;

        ArrayList<Merchant> productMerchants = new ArrayList<>();
        for(MerchantStock m: merchantStocks){
            if(m.getProductId().equalsIgnoreCase(productId)){
                int merchantId = merchantService.findMerchantIndex(m.getMerchantId());
                productMerchants.add(merchantService.merchants.get(merchantId));
            }
        }
        return productMerchants;
    }

    public ArrayList<Product> getOutOfStockProducts(){
        ArrayList<Product> outOfStockProducts = new ArrayList<>();
        for(MerchantStock m: merchantStocks){
            if(m.getStock() == 0){
                int productId = productService.findProductIndex(m.getProductId());
                outOfStockProducts.add(productService.products.get(productId));
            }
        }
        return outOfStockProducts;
    }

    public ArrayList<Product> getInStockProducts(){
        ArrayList<Product> inStockProducts = new ArrayList<>();
        for(MerchantStock m: merchantStocks){
            if(m.getStock() > 0){
                int productId = productService.findProductIndex(m.getProductId());
                inStockProducts.add(productService.products.get(productId));
            }
        }
        return inStockProducts;
    }


    //HELPER METHODS
    public int findMerchantStockIndex(String id){
        for(int i = 0; i < merchantStocks.size(); i++)
            if(merchantStocks.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }

    public int findByProductAndMerchantId(String productId, String merchantId){
        for(int i = 0; i < merchantStocks.size(); i++){
            if(merchantStocks.get(i).getProductId().equalsIgnoreCase(productId) && merchantStocks.get(i).getMerchantId().equalsIgnoreCase(merchantId))
                return i;
        }
        return -1;
    }
}
