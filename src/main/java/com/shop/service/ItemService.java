package com.shop.service;


import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {


    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    /**
     * 상품 등록
     * @param itemFormDto
     * @param itemImgFileList
     * @return
     * @throws Exception
     */
    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception {

        //상품 등록
        Item item = itemFormDto.createItem();  //상품 등록 폼으로부터 입력받은 데이터를 이용하여 item 객체를 생성합니다
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemImgFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0)   //첫번째 이미지일 경우 대표 상품 이미지 여부값을 y로 세팅합니다 나머지 상품 이미지는 n으로 설정합니다
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));  //상품의 이미지 정보를 저장합니다

        }

        return item.getId();

    }

    /**
     * 상품 불러오기
     * @param itemId
     * @return
     */
    @Transactional(readOnly = true) //상품 데이터를 읽어오는 트랜잭션을 읽기전용으로 이럴경우 jpa가 더티체킹(변경감지)를 사용하지않아 성능을 향상 시킴
    public ItemFormDto getItemDtl(Long itemId) {

        List<ItemImg> itemImgList =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId); //해당 상품의 이미지를 조회 등록순으로 가져오기 위해 상품 이미지 아이디 오름차순으로 가져옴

        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) { //조회한 ItemImg 엔티티를 itemImgDto 객체로 만들어 리스트에 추가합니다
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);

        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new); //상품의 아이디를 통해 상품 엔티티를 조회합니다 존재하지 않을때는 EntityNotFoundException::new 을 발생

        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }


    /**
     * 상품 수정하기
     * @param itemFormDto
     * @param itemImgFileList
     * @return
     * @throws Exception
     */
    public Long updateItem(ItemFormDto itemFormDto,List<MultipartFile> itemImgFileList)throws Exception{

        //상품 수정
          Item item =  itemRepository.findById(itemFormDto.getId()).orElseThrow(EntityNotFoundException::new); //상품 등록 화면으로 부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회
          item.updateItem(itemFormDto); //상품 등록 화면으로 부터  전달 받은 ItemFormDto를 통해 상품 엔티티를 업데이트합니다
          
          List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트를 조회
          //이미지 등록
        for (int i = 0; i <itemImgFileList.size(); i++){ 
            itemImgService.updateItemImg(itemImgIds.get(i),itemImgFileList.get(i)); //상품 이미지를 업데이트하기 위해서 updateItemImg메소드에 상품이미지 아이디와 상품 이미지 파일 정보를
            //파라미터로 전달

        }

        return item.getId();
    }

    /**
     * 상품 조회 조건과 페이지 정보를 파라미터로 받아서 상품데이터 조회
     * @param itemSearchDto
     * @param pageable
     * @return
     */
    @Transactional(readOnly = true) //데이터수정이 일어나지않으므로 최적화하기위해 쓰는 어노테이션
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto ,Pageable pageable){
            return itemRepository.getAdminItemPage(itemSearchDto,pageable);
    }
}
