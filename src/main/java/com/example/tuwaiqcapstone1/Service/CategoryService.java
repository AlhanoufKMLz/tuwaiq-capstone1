package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CategoryService {

    ArrayList<Category> categories = new ArrayList<>();

    //BASIC CRUD ENDPOINTS
    public ArrayList<Category> getCategories(){
        return categories;
    }

    public boolean addCategory(Category category){
        if(findCategoryIndex(category.getId()) == -1) {//not found
            categories.add(category);
            return true;
        }
        return false;
    }

    public boolean updateCategory(String id, Category category){
        int index = findCategoryIndex(id);
        if(index == -1) //not found
            return false;
        category.setId(id); //make sure the user doesn't change the id
        categories.set(index, category);
        return true;
    }

    public boolean deleteCategory(String id){
        int index = findCategoryIndex(id);
        if(index == -1) //not found
            return false;
        categories.remove(index);
        return true;
    }


    //HELPER METHODS
    public int findCategoryIndex(String id){
        for(int i = 0; i < categories.size(); i++)
            if(categories.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }
}
