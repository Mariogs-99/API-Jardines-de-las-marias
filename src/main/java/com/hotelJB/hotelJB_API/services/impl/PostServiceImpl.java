package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.PostDTO;
import com.hotelJB.hotelJB_API.models.entities.Post;
import com.hotelJB.hotelJB_API.models.entities.Category;
import com.hotelJB.hotelJB_API.models.responses.PostResponse;
import com.hotelJB.hotelJB_API.repositories.PostRepository;
import com.hotelJB.hotelJB_API.repositories.CategoryRepository;
import com.hotelJB.hotelJB_API.services.CategoryService;
import com.hotelJB.hotelJB_API.services.PostService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Value("${upload.base-url}")  // Definimos la URL base en application.properties
    private String uploadBaseUrl;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RequestErrorHandler errorHandler;

    @Override
    public void save(PostDTO data) throws Exception {
        try{
            Category category = categoryRepository.findById(data.getCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Category"));

            Post post = new Post(data.getTitleEs(),data.getTitleEn(),data.getDescriptionEs(),
                    data.getDescriptionEn(),data.getPathImage(), category);
            postRepository.save(post);
        }catch (Exception e){
            throw new Exception("Error save Post");
        }
    }

    @Override
    public void update(PostDTO data, int postId) throws Exception {
        try{

            Category category = categoryRepository.findById(data.getCategoryId())
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Category"));

            Post post = postRepository.findById(postId).orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Post"));

            post.setTitleEs(data.getTitleEs());
            post.setTitleEn(data.getTitleEn());
            post.setDescriptionEs(data.getDescriptionEs());
            post.setDescriptionEn(data.getDescriptionEn());
            post.setPathImage(data.getPathImage());
            post.setCategory(category);

            postRepository.save(post);
        }catch (Exception e){
            throw new Exception("Error save Post");
        }
    }

    @Override
    public void delete(int postId) throws Exception {
        try{
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Post"));

            postRepository.delete(post);
        }catch (Exception e){
            throw new Exception("Error delete post");
        }
    }

    @Override
    public List<Post> getAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<PostResponse> findById(int postId, String lang) {
        Optional<Post> post = postRepository.findById(postId);

        return post.map(value -> new PostResponse(
                value.getPostId(),
                lang.equals("es") ? value.getTitleEs() : value.getTitleEn(),
                lang.equals("es") ? value.getDescriptionEs() : value.getDescriptionEn(),
                uploadBaseUrl + value.getPathImage(),
                value.getCategory().getCategoryId()
        ));
    }

    @Override
    public List<PostResponse> findByCategory(int categoryId, String language) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Category"));
        List<Post> posts =  postRepository.findByCategory(category);

        return posts.stream().map(value -> new PostResponse(
                value.getPostId(),
                language.equals("es") ? value.getTitleEs() : value.getTitleEn(),
                language.equals("es") ? value.getDescriptionEs() : value.getDescriptionEn(),
                uploadBaseUrl + value.getPathImage(),
                value.getCategory().getCategoryId()
        )).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> findByLanguage(String language) {
        List<Post> posts = postRepository.findAll();

        return posts.stream().map(value -> new PostResponse(
                value.getPostId(),
                language.equals("es") ? value.getTitleEs() : value.getTitleEn(),
                language.equals("es") ? value.getDescriptionEs() : value.getDescriptionEn(),
                uploadBaseUrl + value.getPathImage(),
                value.getCategory().getCategoryId()
        )).collect(Collectors.toList());
    }
}
