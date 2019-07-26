package com.wlminus.service;

import com.wlminus.config.Constants;
import com.wlminus.config.FileStorageProperties;
import com.wlminus.service.errors.FileStorageException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.UUID;

@Service
public class MediaUploadService {
    private final Logger log = LoggerFactory.getLogger(MediaUploadService.class);

    private final String rootLocationStr;
    private final Path rootLocation;
    private Path yearLocation;
    private Path monthLocation;

    private String currentYear;
    private String currentMonth;

    @Autowired
    public MediaUploadService(FileStorageProperties fileStorageProperties) {
        log.info("Root location upload directory" + fileStorageProperties.getUploadDir());
        this.rootLocationStr = fileStorageProperties.getUploadDir();
        this.rootLocation = Paths.get(this.rootLocationStr).toAbsolutePath().normalize();
        try {
            if (Files.notExists(this.rootLocation)) {
                Files.createDirectories(this.rootLocation);
            }
            TimeZone tz = TimeZone.getTimeZone(Constants.TIME_ZONE);
            Calendar cal = Calendar.getInstance(tz);

            this.currentYear = Integer.toString(cal.get(Calendar.YEAR));
            this.currentMonth = Integer.toString(cal.get(Calendar.MONTH) + 1);
            this.yearLocation = Paths.get(this.rootLocationStr + "/" + this.currentYear).toAbsolutePath().normalize();

            if (Files.notExists(this.yearLocation)) {
                Files.createDirectories(this.yearLocation);
            }

            this.monthLocation = Paths.get(this.rootLocationStr + "/" + this.currentYear + "/" + this.currentMonth).toAbsolutePath().normalize();
            if (Files.notExists(this.monthLocation)) {
                Files.createDirectories(this.monthLocation);
            }

        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String[] storeFile(MultipartFile file) {
//        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            String fileName = UUID.randomUUID().toString();
            Path targetLocation = this.monthLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            String fileSavedMeta[] = new String[3];
            fileSavedMeta[0] = fileName;
            fileSavedMeta[1] = this.currentYear;
            fileSavedMeta[2] = this.currentMonth;
            return fileSavedMeta;

//            --- Code to crop image: use for future
//            int dot = fileName.indexOf('.');
//            String basename = dot == -1 ? fileName : fileName.substring(0, dot);
//            String ext = fileName.substring(dot+1);
//
//            if (Files.exists(targetLocation)) {
//                throw new FileStorageException("File existed");
//            }
//
//            if (Arrays.stream(Constants.IMAGE_FILE_TYPE).anyMatch(file.getContentType()::equals)) {
//                BufferedImage originalImage = ImageIO.read(file.getInputStream());
//
//                int height = originalImage.getHeight();
//                int width = originalImage.getWidth();
//
//                if (height <= Constants.THUMB_SIZE && width <= Constants.THUMB_SIZE) {
//                    ImageIO.write(originalImage, ext, new File(targetLocation.toString()));
//                    return targetLocation.toString();
//                } else {
//                    int xc = (width <= Constants.THUMB_SIZE) ? 0 : (width - Constants.THUMB_SIZE)/2;
//                    int yc = (height <= Constants.THUMB_SIZE) ? 0 : (width - Constants.THUMB_SIZE)/2;
//                    int targetWidth = (width <= Constants.THUMB_SIZE) ? width : Constants.THUMB_SIZE;
//                    int targetHeight = (height <= Constants.THUMB_SIZE) ? height : Constants.THUMB_SIZE;
//
//                    BufferedImage cropImage = originalImage.getSubimage(
//                        xc,
//                        yc,
//                        targetWidth,
//                        targetHeight
//                    );
//                    ImageIO.write(originalImage, ext, new File(targetLocation.toString()));
//                    ImageIO.write(cropImage, ext, new File(this.monthLocation.toString() + "/" + basename + "_thumb." + ext));
//                    return targetLocation.toString();
//                }
//            }
//            else {
//                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//                return targetLocation.toString();
//            }
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file, Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName, String uploadYear, String uploadMonth) {
        try {
            Path filePath = Paths.get(this.rootLocationStr + "/" + uploadYear + "/" + uploadMonth + "/" + fileName).toAbsolutePath().normalize();
            log.debug(filePath.toString());
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + fileName, ex);
        }
    }

    public boolean deleteFile(String fileName, String uploadYear, String uploadMonth)
    {
        try
        {
            Path filePath = Paths.get(this.rootLocationStr + "/" + uploadYear + "/" + uploadMonth + "/" + fileName).toAbsolutePath().normalize();
            Files.delete(filePath);
            return true;
        }
        catch (IOException ex)
        {
            throw new FileStorageException("IO error " + fileName, ex);
        }
    }
}
