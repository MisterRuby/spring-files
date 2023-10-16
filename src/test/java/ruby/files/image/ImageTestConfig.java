package ruby.files.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import ruby.files.common.file.FileService;
import ruby.files.common.file.local.LocalFileService;

@Configuration
public class ImageTestConfig {

    @Autowired
    ImageRepository imageRepository;
//    @Autowired
//    ImageService imageService;
//    @Autowired
//    LocalFileService fileService;

    @Bean
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }

    @Bean
    public FileService fileService() {
        return new LocalFileService(resourceLoader());
    }

    @Bean
    public ImageService imageService() {
        return new ImageService(imageRepository, fileService());
    }

//    @Bean
//    public ImageController imageController() {
//        return new ImageController(imageRepository, imageService, fileService);
//    }




//    @Bean
//    public ImageController imageController() {
//
//    }
}
