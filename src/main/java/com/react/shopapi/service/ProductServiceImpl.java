package com.react.shopapi.service;

import com.react.shopapi.domain.Product;
import com.react.shopapi.domain.ProductImage;
import com.react.shopapi.dto.PageRequestDTO;
import com.react.shopapi.dto.PageResponseDTO;
import com.react.shopapi.dto.ProductDTO;
import com.react.shopapi.repository.ProductRepository;
import com.react.shopapi.util.FileUtilCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final FileUtilCustom fileUtil;

    @Override
    public PageResponseDTO<ProductDTO> getList(PageRequestDTO pageRequestDTO) {
        log.info("***** ProductService getList - pageRequestDTO : {}", pageRequestDTO);
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending());

        Page<Object[]> result = productRepository.selectList(pageable);
        // Object [] = [ [Product, ProductImage], [Product, ProductImage],... ]
        //                  objArr                      objArr
        // * 리턴 타입에 맞게 데이터 변환 *
        // 1. 목록 변환
        List<ProductDTO> list = result.getContent().stream().map(objArr -> {
            Product product = (Product)objArr[0];
            ProductImage productImage = (ProductImage) objArr[1];
            // Product Entity -> ProductDTO 변환
            ProductDTO productDTO = entityToDTO(product); // 메서드로 따로 뺌
            // 이미지 꺼내서 productDTO안에 저장
            String imageFileName = productImage.getFileName();
            productDTO.setUploadedFileNames(List.of(imageFileName)); // 리스트로 넣어주기

            return productDTO; // map안에서는 항상 변경된 결과물을 return하는 형태로 지정
        }).collect(Collectors.toList()); // 리스트로 컬렉트 -> 리스트로 묶어서 리턴해줘

        // 2. 전체 상품 개수 꺼내기 (repository가 알아서 조회해 줌 -> result)
        long totalCount = result.getTotalElements();

        // PageResponseDTO 객체 생성(builder패턴으로 객체생성)해서 바로 리턴
        // 객체 생성시, list, totalCount, pageRequestDTO 넣어주면서 호출 (PageResponseDTO 생성자참고)
        return PageResponseDTO.<ProductDTO>withList()
                .list(list)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public Long add(ProductDTO productDTO) {
        // * 상품 이미지 처리 (Controller에 있는 부분을 이쪽으로 이동) *
        // 업로드 파일들 담은 list 꺼내기
        List<MultipartFile> files = productDTO.getFiles();
        // 실제 파일들 저장하고 저장된 파일명 리스트 리턴받기
        List<String> uploadFileNames = fileUtil.saveFiles(files);
        // 저장된 파일명을 productDTO에 저장
        productDTO.setUploadedFileNames(uploadFileNames);

        // * 상품 정보 처리 *
        // ProductDTO -> Product Entity 변환
        Product product = dtoToEntity(productDTO); // 메서드로 따로 뺌
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();// 저장된 이미지 파일명들 (UUID...)
        // 저장된 이미지 파일들이 있으면 Product 객체안 imageList 에 추가
        if(uploadedFileNames != null) {
            // 이미지 하나씩 꺼내서 product에 addImageString을 통해 -> ProductImage 객체로 변환해
            // Product imageList List에 추가
            uploadedFileNames.stream().forEach(name -> {
                product.addImageString(name);
            });
        }
        // 상품 정보 저장(이미지포함)
        Product saved = productRepository.save(product);
        return saved.getPno();
    }

    @Override
    public ProductDTO get(Long pno) {
        Product product = productRepository.selectOneById(pno).orElseThrow();
        // Entity -> ProductDTO 변환 후 리턴
        ProductDTO productDTO = entityToDTO(product); // 메서드로 따로 뺌
        List<ProductImage> imageList = product.getImageList();
        if(imageList == null || imageList.isEmpty()) {
            return productDTO;
        }
        // Product의 imageList는 ProductImage 객체 타입의 리스트다
        // 화면에서는 ord값 필요없이 fileName만 필요하기 때문에
        // ProductImage에서 getter 이용하여 파일명만 뽑아 리스트로 리턴 받기
        List<String> fileNameList = imageList.stream()
                .map(productImage -> productImage.getFileName()).toList();
        // ProductDTO에 이미지이름들만 모아놓은 리스트 추가
        productDTO.setUploadedFileNames(fileNameList);

        return productDTO;
    }

    @Override
    public ProductDTO modifyFiles(ProductDTO productDTO) {
        // 상품 파일만 처리
        // DB에서 조회해 ProductDTO타입으로 리턴
        ProductDTO oldDTO = get(productDTO.getPno()); // 위(ProductService)에 구현된 get() 호출
        // 기존에 저장된 파일 이름들
        List<String> oldFileNames = oldDTO.getUploadedFileNames();

        // 새로 업로드할 파일들
        List<MultipartFile> files = productDTO.getFiles();
        // 새 실제파일 저장후, 저장된 파일명 리턴받기
        List<String> newUploadFileNames = fileUtil.saveFiles(files);
        // 수정폼 화면에서 계속 유지되서 넘어온 이미지 파일명 (수정X, 삭제X == 계속 유지)
        List<String> remainFileNames = productDTO.getUploadedFileNames();
        // 유지되는 파일에 새로 추가된 파일명 추가 (합치기)
        if(newUploadFileNames != null && !newUploadFileNames.isEmpty()) { // 새로 추가된 이미지가 있으면
            remainFileNames.addAll(newUploadFileNames); // 기존리스트에 새 리스트 통으로 주고 합치기
        }
        // 기존에 저장한 파일들이 있으면
        if(oldFileNames != null && !oldFileNames.isEmpty()) {
            // 지워햐하는 파일명 목록 찾아
            List<String> removeFileNames = oldFileNames
                    .stream()
                    .filter(fileName -> !remainFileNames.contains(fileName)).collect(Collectors.toList());
            // 실제 파일 삭제
            fileUtil.deleteFiles(removeFileNames);
        }

        return productDTO;
    }

    @Override
    public void modify(ProductDTO productDTO) { // 유지된 파일명과, 새 파일명 담아서 넘어옴
        // 상품 정보 수정
        Product product = productRepository.selectOneById(productDTO.getPno()).orElseThrow();
        product.changePname(productDTO.getPname());
        product.changeDesc(productDTO.getPdesc());
        product.changePrice(productDTO.getPrice());

        // 상품 이미지 처리
        product.clearImageList(); // DB에서 불러온 이름은 전부 삭제
        List<String> uploadedFileNames = productDTO.getUploadedFileNames();
        if(uploadedFileNames != null && !uploadedFileNames.isEmpty()) { // 이미지가 하나라도 있으면
            uploadedFileNames.stream().forEach(name -> {
                product.addImageString(name); // 문자열주고 ProductImage 객체로 변환해서 Product 객체에 추가
            });
        }
        productRepository.save(product);
    }

    @Override
    public void remove(Long pno) {
        // 업로드 파일 삭제
        List<String> uploadedFileNames = get(pno).getUploadedFileNames();
        log.info("remove fileNames : {}", uploadedFileNames);
        fileUtil.deleteFiles(uploadedFileNames);
        // 상품 정보의 delFlag 수정
        productRepository.updateDelFlag(pno, true);
    }


    // 내부에서만 사용할 메서드 -> private 으로 지정
    // Entity -> ProductDTO
    private ProductDTO entityToDTO(Product product) {
        ProductDTO productDTO = ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .build();
        return productDTO;
    }
    // ProductDTO -> Entity
    private Product dtoToEntity(ProductDTO productDTO) {
        Product product = Product.builder()
                .pno(productDTO.getPno())
                .pname(productDTO.getPname())
                .pdesc(productDTO.getPdesc())
                .price(productDTO.getPrice())
                .build();
        return product;
    }



}
