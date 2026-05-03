package com.example.tuwaiqcapstone1.Service;

import com.example.tuwaiqcapstone1.Model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

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


    //HELPER METHODS
    public int findUserIndex(String id){
        for(int i = 0; i < users.size(); i++)
            if(users.get(i).getId().equalsIgnoreCase(id))
                return i;
        return -1;
    }
}
