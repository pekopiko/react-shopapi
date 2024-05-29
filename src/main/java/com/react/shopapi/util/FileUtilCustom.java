package com.react.shopapi.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 파일 업로드 로직처리 클래스
@Component // 스프링빈으로 등록
@Slf4j
public class FileUtilCustom {

    @Value("${shop.upload.path}") // import 주의! springframework 꺼
    private String uploadPath;

    @PostConstruct
    public void init() {  // 실제 파일 저장할 폴더 생성
        // File 객체 생성
        File folder = new File(uploadPath);
        // 폴더 존재 여부 체크 후 생성
        if(!folder.exists()) { // 없으면
            folder.mkdir(); // 폴더 생성
        }
        log.info("********************* uploadPath : {}", folder.getAbsolutePath());
    }

    // 파일 저장 처리 메서드
    public List<String> saveFiles(List<MultipartFile> files) {
        // 파일 리스트가 null 이거나 데이터가 없으면
        if(files == null || files.isEmpty()) {
            return List.of(); // 빈 리스트 생성해 리턴
        }

        List<String> uploadNames = new ArrayList<>();

        for(MultipartFile mf : files) {
            // 파일이름 중복처리
            String uuid = UUID.randomUUID().toString();
            // 확장자명 추출
            String originalFilename = mf.getOriginalFilename();
            int idx = originalFilename.lastIndexOf(".");
            String ext = originalFilename.substring(idx);
            // 저장할 파일명 연결
            String savedName = uuid + ext;
            // 저장 경로 + 저장파일명
            Path savePath = Paths.get(uploadPath, savedName);// 파일경로/파일명 형태로 만들어 리턴
            log.info("savePath : {}", savePath);

            try {
                // 파일 복사 == 저장
                Files.copy(mf.getInputStream(), savePath);

                // 썸네일 생성 코드 추가 (원본파일 저장 후 처리)
                String contentType = mf.getContentType(); // 'image/png' 'image/jpeg'
                log.info("contentType : {}", contentType);
                if(contentType != null && contentType.startsWith("image")) {
                    Path thumbnailPath = Paths.get(uploadPath, "th_" + savedName);
                    Thumbnails.of(savePath.toFile()) // 원본파일지정
                            .size(200, 200) // 썸네일이미지 사이즈 지정
                            .toFile(thumbnailPath.toFile()); // 썸네일로 저장할 File 객체
                }
                uploadNames.add(savedName); // 저장된파일명 리스트에 추가
                log.info("savedName : {}", savedName);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }//try-catch

        }// for

        return uploadNames;
    }//saveFiles

    // 파일 브라우저에 보여주기 위한 메서드 : 저장된 파일명 매개값으로 받기
    public ResponseEntity<Resource> getFile(String fileName) {
        Resource resource = new FileSystemResource(uploadPath + File.separator + fileName);
        HttpHeaders headers = new HttpHeaders();
        try {
            headers.add("Content-Type", Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            //throw new RuntimeException(e);
            // 예외가 발생하면 상태코드만 응답으로 리턴 (서버에서 예외 터뜨리고 끝내는것이 아님)
            return ResponseEntity.internalServerError().build(); // HTTP 상태코드 500만 리턴
        }
        // 성공 + header정보 + resource 데이터 리턴
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    // 파일 삭제 기능
    public void deleteFiles(List<String> fileNames) {
        // 파일이 없으면 강제 종료
        if(fileNames == null || fileNames.isEmpty()) {
            return;
        }
        fileNames.forEach(fileName -> {
            // 썸네일+원본파일 존재여부 확인후 삭제
            String thumbnailFileName = "th_" + fileName;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);
            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

}//class
