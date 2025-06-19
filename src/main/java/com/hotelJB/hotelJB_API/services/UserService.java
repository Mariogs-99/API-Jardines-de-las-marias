package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.LoginDTO;
import com.hotelJB.hotelJB_API.models.dtos.SingupDTO;
import com.hotelJB.hotelJB_API.models.dtos.UserDTO;
import com.hotelJB.hotelJB_API.models.entities.Token;
import com.hotelJB.hotelJB_API.models.entities.User_;
import com.hotelJB.hotelJB_API.models.responses.LoginResponse;
import com.hotelJB.hotelJB_API.models.responses.UserResponse;

import java.util.List;

public interface UserService {
    void save(SingupDTO data) throws Exception;
    void login(LoginDTO data) throws Exception;
    User_ findByUsername(String username);
//    void changePassword(ChangePasswordDTO data) throws Exception;
    List<User_> findAll();

    // Token
    Token registerToken(User_ user) throws Exception;
    Boolean isTokenValid(User_ user, String token);
    void cleanTokens(User_ user) throws Exception;
    User_ getUserFromToken (String info);
    Boolean comparePass(String toCompare, String current);
    void toggleToken(User_ user);
    User_ findUserAuthenticated();

    //?Nuevos metodos para USER

    List<UserResponse> getAllUsers();
    UserResponse getUserById(int id) throws Exception;
    void createUser(UserDTO dto) throws Exception;
    void updateUser(int id, UserDTO dto) throws Exception;
    void deleteUser(int id) throws Exception;

    LoginResponse loginWithToken(LoginDTO data) throws Exception;
}
