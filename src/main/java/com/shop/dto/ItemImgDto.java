package com.shop.dto;


import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Setter
@Getter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ItemImgDto of(ItemImg itemImg){   //itemImg 엔티티 객체를  파라미터로 받아서 itemImg 객체의 자료형과 멤버변수의 이름이 같을때
                                                      //  ItemImgDto로 값을 복사해 반환함 static메소드를 선언하여 ItemImgDto 객체를 생성하지 않아도  호출할수 있도록
        return modelMapper.map(itemImg,ItemImgDto.class);
    }

}
