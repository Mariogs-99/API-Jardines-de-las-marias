package com.hotelJB.hotelJB_API.services;

import com.hotelJB.hotelJB_API.models.dtos.PostDTO;
import com.hotelJB.hotelJB_API.models.entities.Post;
import com.hotelJB.hotelJB_API.models.responses.PostResponse;

import java.util.List;
import java.util.Optional;

public interface PostService {
    void save(PostDTO data) throws Exception;
    void update(PostDTO data, int postId)  throws Exception;
    void delete(int postId)  throws Exception;
    List<Post> getAll();
    Optional<PostResponse> findById(int postId, String language);
    List<PostResponse> findByCategory(int categoryId, String language);
    List<PostResponse> findByLanguage(String language);
}
