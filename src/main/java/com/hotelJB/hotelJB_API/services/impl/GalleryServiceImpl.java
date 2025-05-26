    package com.hotelJB.hotelJB_API.services.impl;

    import com.hotelJB.hotelJB_API.models.dtos.GalleryDTO;
    import com.hotelJB.hotelJB_API.models.entities.Gallery;
    import com.hotelJB.hotelJB_API.models.responses.GalleryResponse;
    import com.hotelJB.hotelJB_API.repositories.GalleryRepository;
    import com.hotelJB.hotelJB_API.services.GalleryService;
    import org.springframework.beans.BeanUtils;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.time.LocalDateTime;
    import java.util.List;

    @Service
    public class GalleryServiceImpl implements GalleryService {

        @Autowired
        private GalleryRepository galleryRepository;

        @Override
        public List<GalleryResponse> getAll() {
            return galleryRepository.findAll().stream().map(this::toResponse).toList();
        }

        @Override
        public List<GalleryResponse> getPublicGallery() {
            return galleryRepository.findAllByActiveTrueOrderByPositionAsc()
                    .stream().map(this::toResponse).toList();
        }

        @Override
        public GalleryResponse save(GalleryDTO dto, String imageUrl) {
            Gallery gallery = new Gallery();
            BeanUtils.copyProperties(dto, gallery);
            gallery.setImageUrl(imageUrl);
            Gallery saved = galleryRepository.save(gallery);
            return toResponse(saved);
        }


        @Override
        public GalleryResponse update(Long id, GalleryDTO dto) {
            Gallery gallery = galleryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Imagen no encontrada"));
            BeanUtils.copyProperties(dto, gallery, "galleryId", "createdAt");
            gallery.setUpdatedAt(LocalDateTime.now());
            return toResponse(galleryRepository.save(gallery));
        }

        @Override
        public void delete(Long id) {
            galleryRepository.deleteById(id);
        }

        private GalleryResponse toResponse(Gallery g) {
            GalleryResponse r = new GalleryResponse();
            BeanUtils.copyProperties(g, r);
            return r;
        }
    }

