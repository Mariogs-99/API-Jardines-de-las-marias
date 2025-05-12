package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
import com.hotelJB.hotelJB_API.models.entities.Img;
import com.hotelJB.hotelJB_API.repositories.ImgRepository;
import com.hotelJB.hotelJB_API.services.ImgService;
import com.hotelJB.hotelJB_API.utils.CustomException;
import com.hotelJB.hotelJB_API.utils.ErrorType;
import com.hotelJB.hotelJB_API.utils.RequestErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class ImgServiceImpl implements ImgService {
    @Autowired
    private ImgRepository imgRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    private final Path uploadDirPath;

    public ImgServiceImpl() {
        String envPath = "uploads"; // ✅ Ruta relativa
        this.uploadDirPath = Paths.get(envPath).toAbsolutePath().normalize();

        System.out.println("📂 Ruta de imágenes en ImgService: " + this.uploadDirPath);
    }



    @Override
    public void save(ImgDTO data) throws Exception {
        try{
            Img img = new Img(data.getNameImg(),data.getPath());
            imgRepository.save(img);
        }catch (Exception e){
            throw new Exception("Error save Image");
        }
    }

    @Override
    public void update(ImgDTO data, int imgId) throws Exception {
        try{
            Img img = imgRepository.findById(imgId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Image"));

            img.setNameImg(data.getNameImg());
            img.setPath(data.getPath());

            imgRepository.save(img);
        }catch (Exception e){
            throw new Exception("Error update image");
        }
    }

    @Override
    public void delete(int imgId) throws Exception {
        try{
            Img img = imgRepository.findById(imgId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Image"));

            imgRepository.delete(img);
        }catch (Exception e){
            throw new Exception("Error delete image");
        }
    }

    @Override
    public List<Img> getAll() {
        return imgRepository.findAll();
    }

    @Override
    public Optional<Img> findById(int imgId) {
        return imgRepository.findById(imgId);
    }

    @Override
    public Resource getFileAsResourceById(int id) {
        try {
            // Buscar la multimedia por ID
            Optional<Img> multimediaOptional = imgRepository.findById(id);
            if (multimediaOptional.isEmpty()) {
                throw new RuntimeException("No se encontró ninguna multimedia con el ID: " + id);
            }

            Img img = multimediaOptional.get();

            // Obtener la ruta del archivo desde la multimedia
            Path filePath = uploadDirPath.resolve(img.getPath()).normalize();

            // Cargar el archivo como recurso
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("Archivo no encontrado en la ruta: " + img.getPath());
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar la multimedia con ID: " + id, e);
        }
    }
}
