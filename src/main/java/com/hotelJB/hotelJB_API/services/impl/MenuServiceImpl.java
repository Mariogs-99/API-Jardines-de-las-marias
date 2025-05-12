package com.hotelJB.hotelJB_API.services.impl;

import com.hotelJB.hotelJB_API.models.dtos.MenuDTO;
import com.hotelJB.hotelJB_API.models.entities.Menu;
import com.hotelJB.hotelJB_API.repositories.MenuRepository;
import com.hotelJB.hotelJB_API.services.MenuService;
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
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private RequestErrorHandler errorHandler;

    private final Path uploadDirPath;

    public MenuServiceImpl() {
        String envPath = "uploads"; // ✅ Carpeta dentro del proyecto
        this.uploadDirPath = Paths.get(envPath).toAbsolutePath().normalize();

        System.out.println("📄 Ruta base para menús PDF: " + this.uploadDirPath);
    }


    @Override
    public void save(Menu data) throws Exception {
        try{
            Menu menu = new Menu(data.getTitle(),data.getPathPdf(),data.getSchedule());
            menuRepository.save(menu);
        }catch (Exception e){
            throw new Exception("Error save Image");
        }
    }

    @Override
    public void update(Menu data, int menuId) throws Exception {
        try{
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Menu"));

            menu.setTitle(data.getTitle());
            menu.setPathPdf(data.getPathPdf());
            menu.setSchedule(data.getSchedule());

            menuRepository.save(menu);
        }catch (Exception e){
            throw new Exception("Error update menu");
        }
    }

    @Override
    public void delete(int menuId) throws Exception {
        try{
            Menu menu = menuRepository.findById(menuId)
                    .orElseThrow(() -> new CustomException(ErrorType.ENTITY_NOT_FOUND, "Menu"));

            menuRepository.delete(menu);
        }catch (Exception e){
            throw new Exception("Error delete menu");
        }
    }

    @Override
    public List<Menu> getAll() {
        return menuRepository.findAll();
    }

    @Override
    public Optional<Menu> findById(int menuId) {
        return menuRepository.findById(menuId);
    }

    @Override
    public Resource getFileAsResourceById(int id) {
        try {
            // Buscar la multimedia por ID
            Optional<Menu> multimediaOptional = menuRepository.findById(id);
            if (multimediaOptional.isEmpty()) {
                throw new RuntimeException("No se encontró ninguna multimedia con el ID: " + id);
            }

            Menu menu = multimediaOptional.get();

            // Obtener la ruta del archivo desde la multimedia
            Path filePath = uploadDirPath.resolve(menu.getPathPdf()).normalize();

            // Cargar el archivo como recurso
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                throw new RuntimeException("Archivo no encontrado en la ruta: " + menu.getPathPdf());
            }

            return resource;
        } catch (Exception e) {
            throw new RuntimeException("Error al descargar la el archivo con ID: " + id, e);
        }
    }
}
