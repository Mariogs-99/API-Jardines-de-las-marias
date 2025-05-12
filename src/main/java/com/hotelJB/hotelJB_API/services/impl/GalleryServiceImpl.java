    package com.hotelJB.hotelJB_API.services.impl;

    import com.hotelJB.hotelJB_API.models.dtos.ImgDTO;
    import com.hotelJB.hotelJB_API.models.entities.Category;
    import com.hotelJB.hotelJB_API.models.entities.Gallery;
    import com.hotelJB.hotelJB_API.repositories.CategoryRepository;
    import com.hotelJB.hotelJB_API.repositories.GalleryRepository;
    import com.hotelJB.hotelJB_API.services.GalleryService;
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
    public class GalleryServiceImpl implements GalleryService {

        @Autowired
        private GalleryRepository galleryRepository;

        @Autowired
        private CategoryRepository categoryRepository;

        @Autowired
        private RequestErrorHandler errorHandler;

        private final Path uploadDirPath;

        public GalleryServiceImpl() {
            String envPath = System.getenv("MULTIMEDIA_STORAGE_PATH");

            if (envPath == null || envPath.isBlank()) {
                envPath = "C:/Users/meev2/Pictures/imagenes-ejemplo"; // Ruta de prueba
                System.out.println("⚠️ MULTIMEDIA_STORAGE_PATH no definida. Usando ruta de prueba: " + envPath);
            }

            this.uploadDirPath = Paths.get(envPath);
        }





        @Override
        public void save(ImgDTO data) throws Exception {
            try{
                Category category = categoryRepository.findById(data.getCategoryId())
                        .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Category"));

                Gallery gallery = new Gallery(data.getNameImg(),data.getPath(), category);
                galleryRepository.save(gallery);
            }catch (Exception e){
                throw new Exception("Error save Gallery");
            }
        }

        @Override
        public void update(ImgDTO data, int galleryId) throws Exception {
            try{
                Gallery gallery = galleryRepository.findById(galleryId)
                        .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Gallery"));

                gallery.setNameImg(data.getNameImg());
                gallery.setPath(data.getPath());

                galleryRepository.save(gallery);
            }catch (Exception e){
                throw new Exception("Error update gallery");
            }
        }

        @Override
        public void delete(int galleryId) throws Exception {
            try{
                Gallery gallery = galleryRepository.findById(galleryId)
                        .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Gallery"));

                galleryRepository.delete(gallery);
            }catch (Exception e){
                throw new Exception("Error delete gallery");
            }
        }

        @Override
        public List<Gallery> getAll() {
            return galleryRepository.findAll();
        }

        @Override
        public Optional<Gallery> findById(int galleryId) {
            return galleryRepository.findById(galleryId);
        }

        @Override
        public List<Gallery> findByCategory(int categoryId) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND,"Category"));
            return galleryRepository.findByCategory(category);
        }

        @Override
        public Resource getFileAsResourceById(int id) {
            try {
                // Buscar la multimedia por ID
                Optional<Gallery> multimediaOptional = galleryRepository.findById(id);
                if (multimediaOptional.isEmpty()) {
                    throw new RuntimeException("No se encontró ninguna multimedia con el ID: " + id);
                }

                Gallery gallery = multimediaOptional.get();

                // Obtener la ruta del archivo desde la multimedia
                Path filePath = uploadDirPath.resolve(gallery.getPath()).normalize();

                // Cargar el archivo como recurso
                Resource resource = new UrlResource(filePath.toUri());
                if (!resource.exists()) {
                    throw new RuntimeException("Archivo no encontrado en la ruta: " + gallery.getPath());
                }

                return resource;
            } catch (Exception e) {
                throw new RuntimeException("Error al descargar la multimedia con ID: " + id, e);
            }
        }
    }
